package multiplayer;

import java.util.Iterator;
import java.util.ListIterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;

import character.DynamicObjects;
import gameManager.GameSlagyom;
import hud.Hud;
import screens.BattleScreen;
import screens.PlayScreen;
import staticObjects.Item;
import staticObjects.StaticObject;
import world.GameConfig;

public class NetworkPlayScreen extends PlayScreen {

	public Client client;

	private boolean stop = false;

	PovDirection directionGamepad = null;
	boolean movesGamePad = false;

	@SuppressWarnings("static-access")
	public NetworkPlayScreen(GameSlagyom gameSlagyom, String name) {
		super(gameSlagyom);
		gameSlagyom.modalityMultiplayer = true;
		client = new Client(name, gameSlagyom);
		hud = new Hud(gameSlagyom);
		gamecam.position.x = client.networkWorld.player.getX();
		gamecam.position.y = client.networkWorld.player.getY();

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gameSlagyom.batch.setProjectionMatrix(gamecam.combined);

		gameSlagyom.batch.begin();
		if (client.go)
			draw();
		gameSlagyom.batch.end();
		hud.update();

		hud.stage.draw();
		if (client.go)
			update(delta);

		// TEXT TABLE RENDERING
		// textTimer += delta;
		// if (hud.showDialog) {
		// if (textTimer > 0.08f) {
		// textTimer = 0;
		// if (i < hud.textDialog.length()) {
		// if (i % 25 == 0)
		// hud.textTable.row();
		// if (i % 75 == 0) {
		// hud.textTable.clear();
		// LoadingMusic.tickSound.play(1.0f);
		//
		// }
		// drawDialog(String.valueOf(hud.textDialog.charAt(i)));
		// i++;
		// }
		// }
		// }

		// if (!hud.showDialog) {
		// hideDialog();
		// i = 0;
		// }
		// END TEXT TABLE RENDERING

	}

	// public static void drawDialog(final String text) {
	// Drawable dialog = new TextureRegionDrawable(new TextureRegion(new
	// Texture("res/dialogBox.png")));
	// // if (hud.showDialog) {
	// // Label dialogLabel = new Label(text, MenuScreen.skin);
	// // hud.textTable.setSize(236 * 3, 47 * 4);
	// // hud.textTable.setBackground(dialog);
	// // hud.textTable.add(dialogLabel).top();
	// // }
	// }

	public static void hideDialog() {
		// hud.textTable.clear();
		// hud.textTable.setBackground(noDialog);
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

		gamecam.update();
	}

	@SuppressWarnings("static-access")
	private void moveCharacter(float dt) {
		if (!stop) {

			if (Gdx.input.isKeyPressed(Keys.Z)) {
				client.networkWorld.player.setVelocity(150f);
				gameSlagyom.loadingImage.setFrameDurationCharacter(0.1f);
			} else {
				client.networkWorld.player.setVelocity(100);
				gameSlagyom.loadingImage.setFrameDurationCharacter(0.2f);
			}
			if (Gdx.input.isKeyPressed(Keys.LEFT))
				client.movesLeft(dt);
			else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
				client.movesRight(dt);
			else if (Gdx.input.isKeyPressed(Keys.UP) || (directionGamepad == PovDirection.north && movesGamePad)) {
				client.movesUp(dt);
				if (client.networkWorld.player.collideShop) {
					gameSlagyom.screenManager.swapScreen(gameManager.ScreenManager.State.SHOP);
					client.networkWorld.player.collideShop = false;
				}

				if (client.networkWorld.player.collideGym) {
					gameSlagyom.screenManager.battlescreen = new BattleScreen(gameSlagyom, client.networkWorld.battle);
					gameSlagyom.screenManager.swapScreen(gameManager.ScreenManager.State.BATTLE);
					client.networkWorld.player.collideGym = false;
				}

			} else if (Gdx.input.isKeyPressed(Keys.DOWN))
				client.movesDown(dt);
			else if (Gdx.input.isKeyJustPressed(Keys.C)) {
				gamecam.zoom -= 0.2;
				gamecam.position.x = client.networkWorld.player.getX();
				gamecam.position.y = client.networkWorld.player.getY();

			} else if (Gdx.input.isKeyJustPressed(Keys.V)) {
				gamecam.zoom += 0.2;
				gamecam.position.x = client.networkWorld.player.getX();
				gamecam.position.y = client.networkWorld.player.getY();

			} else if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
				gameSlagyom.loadingMusic.pause();
				gameSlagyom.screenManager.swapScreen(gameManager.ScreenManager.State.PAUSE);
			}

		}
		if (client.networkWorld.player.readyToFight && !client.networkWorld.player.isFighting) {
			// System.out.println("Sono " + client.networkWorld.player.ID +
			// "inizio la battaglia contro "
			// + client.networkWorld.player.IDOtherPlayer);
			client.networkWorld.createBattle(client.networkWorld.player.IDOtherPlayer);
			gameSlagyom.screenManager.battlescreen = new NetworkBattleScreen(gameSlagyom,
					client.networkWorld.battle, client);
			gameSlagyom.screenManager.swapScreen(gameManager.ScreenManager.State.BATTLE);
			client.networkWorld.player.readyToFight = false;
			client.networkWorld.player.isFighting = true;
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
			}
		}
		gameSlagyom.batch.draw(gameSlagyom.loadingImage.getFrame(client.networkWorld.player),
				client.networkWorld.player.getX(), client.networkWorld.player.getY(),
				client.networkWorld.player.getWidth(), client.networkWorld.player.getHeight());
		gameSlagyom.batch.draw(gameSlagyom.loadingImage.pointer, client.networkWorld.player.getX(),
				client.networkWorld.player.getY() + 30, 14, 13);
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

	@Override
	public void show() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
	}

	@Override
	public void connected(Controller controller) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disconnected(Controller controller) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {

		System.out.println("button" + buttonCode);
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		// TODO Auto-generated method stub

		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		// TODO Auto-generated method stub
		System.out.println(axisCode);
		return false;
	}

	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		// TODO Auto-generated method stub
		if (value == PovDirection.east) {
			movesGamePad = true;
			directionGamepad = value;
			return true;
		} else if (value == PovDirection.north) {
			movesGamePad = true;
			directionGamepad = value;
			return true;
		} else if (value == PovDirection.south) {
			movesGamePad = true;
			directionGamepad = value;
			return true;
		} else if (value == PovDirection.west) {
			movesGamePad = true;
			directionGamepad = value;
			return true;
		} else if (value == PovDirection.northEast || value == PovDirection.northWest || value == PovDirection.southWest
				|| value == PovDirection.southEast) {
			movesGamePad = true;
			directionGamepad = value;
			return true;
		}
		movesGamePad = false;
		return false;
	}

	@Override
	public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
		return false;
	}

	@Override
	public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
		return false;
	}

	@Override
	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
		return false;
	}

}