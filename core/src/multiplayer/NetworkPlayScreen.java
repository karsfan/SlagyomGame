package multiplayer;

import java.util.Iterator;
import java.util.ListIterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.GL20;

import character.DynamicObjects;
import gameManager.GameSlagyom;
import gameManager.LoadingImage;
import gameManager.ScreenManager.State;
import hud.Hud;
import screens.PlayScreen;
import staticObjects.Item;
import staticObjects.StaticObject;
import staticObjects.StaticObject.Element;
import world.GameConfig;

public class NetworkPlayScreen extends PlayScreen {

	public Client client;
	boolean youLose = false;

	@SuppressWarnings("static-access")
	public NetworkPlayScreen(GameSlagyom gameSlagyom, String name) {
		super(gameSlagyom);
		gameSlagyom.modalityMultiplayer = true;
		client = new Client(name, gameSlagyom);
		gamecam.position.x = client.networkWorld.player.getX();
		gamecam.position.y = client.networkWorld.player.getY();
		hud = new Hud(gameSlagyom);
	}

	@SuppressWarnings("static-access")
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gameSlagyom.batch.setProjectionMatrix(gamecam.combined);

		gameSlagyom.batch.begin();
		if (!client.go) {
			gameSlagyom.batch.draw(gameSlagyom.loadingImage.blackBg, gamecam.position.x - gamePort.getWorldWidth() / 2,
					gamecam.position.y - gamePort.getWorldHeight() / 2);
			gameSlagyom.batch.draw(gameSlagyom.loadingImage.loadingAnimation.getKeyFrame(loadingTimer, true),
					gamecam.position.x - gamePort.getWorldWidth() / 2,
					gamecam.position.y - gamePort.getWorldHeight() / 2);
		}
		if (client.go) {
			if (loading) {
				gameSlagyom.loadingMusic.backgroundSound.loop(10.0f);
				loading = false;
			}
			draw();
		}
		gameSlagyom.batch.end();
		if (client.go) {
			hud.update();
			hud.stage.draw();
			hud.updateNight(delta);
			gameSlagyom.batch.setProjectionMatrix(gamecam.combined);
			gameSlagyom.batch.begin();
			drawLights();
			drawMiniMap();
			gameSlagyom.batch.end();
			update(delta);
		}
		// TEXT TABLE RENDERING
		textTimer += delta;
		if (hud.showDialog) {
			if (textTimer > 0.08f) {
				textTimer = 0;
				if (textIndex < hud.textDialog.length()) {
					if (textIndex % 25 == 0)
						hud.textTable.row();
					if (textIndex % 75 == 0) {
						hud.textTable.clear();
						gameSlagyom.loadingMusic.tickSound.play(1.0f);
					}
					drawDialog(String.valueOf(hud.textDialog.charAt(textIndex)));
					textIndex++;
				}
			}
		}

		if (!hud.showDialog) {
			hideDialog();
			textIndex = 0;
		}
		if (client.text)
			hud.setDialogText(client.textDiaglog);

		if (youLose)
			client.networkWorld.player.invisible = true;
		if (client.serverDisconnected)
			gameSlagyom.screenManager.swapScreen(State.MENU);
	}

	@SuppressWarnings("static-access")
	public void update(float dt) {
		moveCharacter(dt);
		if ((client.networkWorld.player.getX() - gamePort.getWorldWidth() / 2 > 0
				&& client.networkWorld.player.getX() + gamePort.getWorldWidth() / 2 < GameConfig.WIDTH))
			gamecam.position.x = client.networkWorld.player.getX();

		if (client.networkWorld.player.getY() - gamePort.getWorldHeight() / 2 > 0
				&& client.networkWorld.player.getY() + gamePort.getWorldHeight() / 2 < GameConfig.HEIGHT)
			gamecam.position.y = client.networkWorld.player.getY();
		if (client.text)
			if (Gdx.input.isKeyPressed(Keys.ENTER) || (buttonPressed && buttonCodePressed == 0)) {
				hud.showDialog = false;
				buttonPressed = false;
				hideDialog();
				client.text = false;
			}
		gamecam.update();
	}

	@SuppressWarnings("static-access")
	private void moveCharacter(float dt) {
		if (!stop) {
			if (Gdx.input.isKeyPressed(Keys.Z) || (buttonPressed && buttonCodePressed == 5)) {
				client.networkWorld.player.setVelocity(150f);
				gameSlagyom.loadingImage.setFrameDurationCharacter(0.1f);
			} else {
				client.networkWorld.player.setVelocity(100);
				gameSlagyom.loadingImage.setFrameDurationCharacter(0.2f);
			}
			if (Gdx.input.isKeyPressed(Keys.LEFT) || (directionGamePad == PovDirection.west && movesGamePad))
				client.movesLeft(dt);
			else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)
					|| (directionGamePad == PovDirection.east && movesGamePad))
				client.movesRight(dt);
			else if (Gdx.input.isKeyPressed(Keys.UP) || (directionGamePad == PovDirection.north && movesGamePad)) {
				client.movesUp(dt);
				if (client.networkWorld.player.collideShop) {
					gameSlagyom.screenManager.swapScreen(gameManager.ScreenManager.State.SHOP);
					client.networkWorld.player.collideShop = false;
					movesGamePad = false;
				}
				if (client.networkWorld.player.collideGym) {
					gameSlagyom.screenManager.battlescreen = new NetworkBattleScreen(gameSlagyom, client.networkWorld.battle, client);
					gameSlagyom.screenManager.swapScreen(gameManager.ScreenManager.State.BATTLE);
					client.networkWorld.player.collideGym = false;
					client.networkWorld.player.isFighting = true;
					movesGamePad = false;
				}if(!client.networkWorld.player.textDialog.equals("")){
					hud.setDialogText(client.networkWorld.player.textDialog);
					client.networkWorld.player.textDialog = "";
				}
			} else if (Gdx.input.isKeyPressed(Keys.DOWN) || (directionGamePad == PovDirection.south && movesGamePad))
				client.movesDown(dt);
			else if (Gdx.input.isKeyJustPressed(Keys.ESCAPE) || (buttonPressed && buttonCodePressed == 7)) {
				buttonPressed = false;
				gameSlagyom.screenManager.swapScreen(gameManager.ScreenManager.State.PAUSE);
			}
		}
		if (client.networkWorld.player.readyToFight && !client.networkWorld.player.isFighting) {
			client.networkWorld.createBattle(client.networkWorld.player.IDOtherPlayer);
			gameSlagyom.screenManager.battlescreen = new NetworkBattleScreen(gameSlagyom, client.networkWorld.battle,
					client);
			gameSlagyom.screenManager.swapScreen(gameManager.ScreenManager.State.BATTLE);
			client.networkWorld.player.readyToFight = false;
			client.networkWorld.player.isFighting = true;
		}
		if (client.sound) {
			if (client.networkWorld.player.itemPicked.getElement() == Element.COIN)
				gameSlagyom.loadingMusic.coinSound.play();
			else
				gameSlagyom.loadingMusic.itemSound.play();
			client.sound = false;
		}
	}

	public void sendUpdate() {
		System.out.println("qui");
		client.update();
	}

	@SuppressWarnings("static-access")
	public void drawLights() {
		ListIterator<StaticObject> it = (ListIterator<StaticObject>) client.networkWorld.getListLightLamps()
				.listIterator();
		while (it.hasNext()) {
			Object ob = (Object) it.next();
			if (((StaticObject) ob).getElement() == Element.LIGHTLAMP && hud.timer >= (hud.dayTime * 3) / 5) {
				gameSlagyom.batch.draw(gameSlagyom.loadingImage.lightImage, ((StaticObject) ob).getX() - 96,
						((StaticObject) ob).getY() - 54, 192, 192);
			}
		}
	}

	@SuppressWarnings("static-access")
	public synchronized void draw() {
		ListIterator<StaticObject> it = (ListIterator<StaticObject>) client.networkWorld.getListTile().listIterator();
		while (it.hasNext()) {
			Object ob = (Object) it.next();
			if (ob instanceof StaticObject)
				gameSlagyom.batch.draw(gameSlagyom.loadingImage.getTileImage(ob),
						(float) ((StaticObject) ob).shape.getX(), (float) ((StaticObject) ob).shape.getY(),
						(float) ((StaticObject) ob).shape.getWidth(), (float) ((StaticObject) ob).shape.getHeight());
			if (((StaticObject) ob).getElement() == Element.LAMP
					|| ((StaticObject) ob).getElement() == Element.LIGHTLAMP)
				if (hud.timer >= (hud.dayTime * 3) / 5 && !hud.isNight) {
					((StaticObject) ob).setElement(Element.LIGHTLAMP);
				} else
					((StaticObject) ob).setElement(Element.LAMP);
		}

		client.canModify = false;
		ListIterator<Item> it2 = client.networkWorld.getListItems().listIterator();
		while (it2.hasNext()) {
			Object ob = (Object) it2.next();
			if (ob instanceof StaticObject)
				gameSlagyom.batch.draw(gameSlagyom.loadingImage.getTileImage(ob),
						(float) ((StaticObject) ob).shape.getX(), (float) ((StaticObject) ob).shape.getY(),
						(float) ((StaticObject) ob).shape.getWidth(), (float) ((StaticObject) ob).shape.getHeight());
		}
		client.canModify = true;
		Iterator<NetworkPlayer> it1 = client.networkWorld.getOtherPlayersList().iterator();
		while (it1.hasNext()) {
			Object ob = (Object) it1.next();
			if (ob instanceof NetworkPlayer) {
				gameSlagyom.batch.draw(gameSlagyom.loadingImage.getFrame(ob), ((DynamicObjects) ob).getX(),
						((DynamicObjects) ob).getY(), ((DynamicObjects) ob).getWidth(),
						((DynamicObjects) ob).getHeight());
				if (((NetworkPlayer) ob).isFighting)
					gameSlagyom.batch.draw(gameSlagyom.loadingImage.areFighting,
							((DynamicObjects) ob).getX() - ((DynamicObjects) ob).getWidth() / 3,
							((DynamicObjects) ob).getY(), ((DynamicObjects) ob).getWidth(),
							((DynamicObjects) ob).getHeight());
			}

		}
		if (!youLose) {
			gameSlagyom.batch.draw(gameSlagyom.loadingImage.getFrame(client.networkWorld.player),
					client.networkWorld.player.getX(), client.networkWorld.player.getY(),
					client.networkWorld.player.getWidth(), client.networkWorld.player.getHeight());
			gameSlagyom.batch.draw(gameSlagyom.loadingImage.pointer, client.networkWorld.player.getX(),
					client.networkWorld.player.getY() + 30, 14, 13);
		}
	}

	@SuppressWarnings("static-access")
	public void drawMiniMap() {
		gameSlagyom.batch.draw(LoadingImage.miniMap, (float) gamecam.position.x + 260, (float) gamecam.position.y - 225,
				127, 127);

		Iterator<StaticObject> itMiniMapStatic = client.networkWorld.getListObjectsMiniMap().listIterator();
		while (itMiniMapStatic.hasNext()) {
			Object ob = (Object) itMiniMapStatic.next();
			if (ob instanceof StaticObject) {
				// MINI-MAP
				float miniX = (float) (((StaticObject) ob).shape.getX() * 127 / 1440);
				float miniY = (float) (((StaticObject) ob).shape.getY() * 127 / 960);

				if ((miniX - miniMapRadius) * (miniX - miniMapRadius)
						+ (miniY - miniMapRadius) * (miniY - miniMapRadius) < miniMapRadius * miniMapRadius)
					if (((StaticObject) ob).getElement() != Element.GROUND
							&& ((StaticObject) ob).getElement() != Element.THREE) {
						gameSlagyom.batch.draw(gameSlagyom.loadingImage.getTileImage(ob),
								gamecam.position.x + 260 + miniX, gamecam.position.y - 225 + miniY,
								(float) ((StaticObject) ob).shape.getWidth() / miniMapScale,
								(float) ((StaticObject) ob).shape.getHeight() / miniMapScale);
					}
			}
		}

		float miniX = (float) client.networkWorld.player.getX() * 127 / 1440;
		float miniY = (float) client.networkWorld.player.getY() * 127 / 960;
		if ((miniX - miniMapRadius) * (miniX - miniMapRadius)
				+ (miniY - miniMapRadius) * (miniY - miniMapRadius) < miniMapRadius * miniMapRadius)
			gameSlagyom.batch.draw(LoadingImage.miniMapPlayerPointer, gamecam.position.x + 260 + miniX,
					gamecam.position.y - 225 + miniY, (float) client.networkWorld.player.getWidth() / 4,
					(float) client.networkWorld.player.getHeight() / 4);

		Iterator<NetworkPlayer> otherPlayer = client.networkWorld.otherPlayers.iterator();
		while (otherPlayer.hasNext()) {
			NetworkPlayer ob = otherPlayer.next();
			miniX = (float) ob.getX() * 127 / 1440;
			miniY = (float) ob.getY() * 127 / 960;
			if ((miniX - miniMapRadius) * (miniX - miniMapRadius)
					+ (miniY - miniMapRadius) * (miniY - miniMapRadius) < miniMapRadius * miniMapRadius)
				gameSlagyom.batch.draw(LoadingImage.miniMapPointer, gamecam.position.x + 260 + miniX,
						gamecam.position.y - 225 + miniY, (float) ob.getWidth() / 4, (float) ob.getHeight() / 4);
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
		// controlli per la posizione della camera
		if (gamePort.getWorldWidth() / 2 + client.networkWorld.player.getX() - GameConfig.WIDTH > 0
				&& !(client.networkWorld.player.getX() - gamePort.getWorldWidth() / 2 < 0))
			gamecam.position.x = GameConfig.WIDTH - gamePort.getWorldWidth() / 2;
		else if (client.networkWorld.player.getX() - gamePort.getWorldWidth() / 2 < 0) {

			gamecam.position.x = gamePort.getWorldWidth() / 2;
		}
		if (gamePort.getWorldHeight() / 2 + client.networkWorld.player.getY() - GameConfig.HEIGHT > 0)
			gamecam.position.y = GameConfig.HEIGHT - gamePort.getWorldHeight() / 2;
		else if (client.networkWorld.player.getY() - gamePort.getWorldHeight() / 2 < 0) {
			gamecam.position.y = gamePort.getWorldHeight() / 2;
		}

	}

}