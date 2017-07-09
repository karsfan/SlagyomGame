package multiplayer;

import java.util.Iterator;
import java.util.ListIterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import character.DynamicObjects;
import gameManager.GameSlagyom;
import gameManager.LoadingImage;
import gameManager.LoadingMusic;
import hud.Hud;
import screens.BattleScreen;
import staticObjects.Item;
import staticObjects.StaticObject;
import world.Game;
import world.GameConfig;

public class NetworkPlayScreen implements Screen, ControllerListener {

	public OrthographicCamera gamecam;
	public Viewport gamePort;
	public GameSlagyom game;
	public static Hud hud;
	private static Drawable noDialog = null;
	private static float textTimer;
	public Client client;
	public int j = 0;

	private boolean stop = false;
	public int i = 0;

	PovDirection directionGamepad = null;
	boolean movesGamePad = false;

	public NetworkPlayScreen(GameSlagyom game, String name) {
		new LoadingImage();
		this.game = game;

		gamecam = new OrthographicCamera();
		gamePort = new ExtendViewport(854, 480, gamecam);

		gamePort.apply();

		// hud = new Hud(game.batch);

		client = new Client(name);
		gamecam.position.x = client.networkWorld.player.getX();
		gamecam.position.y = client.networkWorld.player.getY();
		// Controllers.addListener(this);

	}

	@Override
	public void render(float delta) {
		update(delta);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.setProjectionMatrix(gamecam.combined);

		game.batch.begin();
		draw();
		game.batch.end();
		// hud.update();

		// hud.stage.draw();

		// TEXT TABLE RENDERING
		textTimer += delta;
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

	public static void drawDialog(final String text) {
		Drawable dialog = new TextureRegionDrawable(new TextureRegion(new Texture("res/dialogBox.png")));
		// if (hud.showDialog) {
		// Label dialogLabel = new Label(text, MenuScreen.skin);
		// hud.textTable.setSize(236 * 3, 47 * 4);
		// hud.textTable.setBackground(dialog);
		// hud.textTable.add(dialogLabel).top();
		// }
	}

	public static void hideDialog() {
		// hud.textTable.clear();
		// hud.textTable.setBackground(noDialog);
	}

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

	private void moveCharacter(float dt) {
		if (!stop) {

			if (Gdx.input.isKeyPressed(Keys.Z)) {
				client.networkWorld.player.setVelocity(150f);
				LoadingImage.setFrameDurationCharacter(0.1f);
			} else {
				client.networkWorld.player.setVelocity(100);
				LoadingImage.setFrameDurationCharacter(0.2f);
			}
			if (Gdx.input.isKeyPressed(Keys.LEFT))
				client.movesLeft(dt);
			else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
				client.movesRight(dt);
			else if (Gdx.input.isKeyPressed(Keys.UP) || (directionGamepad == PovDirection.north && movesGamePad)) {
				client.movesUp(dt);
				if (client.networkWorld.player.collideShop) {
					// client.networkWorld.world.semaphore.acquire();
					game.screenManager.swapScreen(gameManager.ScreenManager.State.SHOP);
					client.networkWorld.player.collideShop = false;
				}

				if (client.networkWorld.player.collideGym) {
					// game.screenManager.battlescreen = new
					// BattleScreen(game, world.Game.world.battle);
					// game.screenManager.swapScreen(gameManager.ScreenManager.State.BATTLE);
					// client.networkWorld.world.semaphore.acquire();
					// client.networkWorld.player.collideGym = false;
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
				LoadingMusic.pause();
				// Game.world.semaphore.acquire();
				game.screenManager.swapScreen(gameManager.ScreenManager.State.PAUSE);
			}

		}
	
	}

	public synchronized void draw() {
		ListIterator<StaticObject> it = (ListIterator<StaticObject>) client.networkWorld.getListTile().listIterator();

		while (it.hasNext()) {
			Object ob = (Object) it.next();
			if (ob instanceof StaticObject)
				game.batch.draw(LoadingImage.getTileImage(ob), (float) ((StaticObject) ob).shape.getX(),
						(float) ((StaticObject) ob).shape.getY(), (float) ((StaticObject) ob).shape.getWidth(),
						(float) ((StaticObject) ob).shape.getHeight());
		}
		ListIterator<Item> it2 = client.networkWorld.getListItems().listIterator();
		while (it2.hasNext()) {
			Object ob = (Object) it2.next();
			if (ob instanceof StaticObject)
				game.batch.draw(LoadingImage.getTileImage(ob), (float) ((StaticObject) ob).shape.getX(),
						(float) ((StaticObject) ob).shape.getY(), (float) ((StaticObject) ob).shape.getWidth(),
						(float) ((StaticObject) ob).shape.getHeight());
		}
		Iterator<NetworkPlayer> it1 = client.networkWorld.getOtherPlayersList().iterator();
		while (it1.hasNext()) {
			// System.out.println("disegno");
			Object ob = (Object) it1.next();
			if (ob instanceof NetworkPlayer) {

				game.batch.draw(LoadingImage.getFrame(ob), ((DynamicObjects) ob).getX(), ((DynamicObjects) ob).getY(),
						((DynamicObjects) ob).getWidth(), ((DynamicObjects) ob).getHeight());
			}
			/*
			 * if (ob instanceof Player) game.batch.draw(LoadingImage.pointer,
			 * ((DynamicObjects) ob).getX(), ((DynamicObjects) ob).getY() + 30,
			 * 14, 13);
			 */
		}
		game.batch.draw(LoadingImage.getFrame(client.networkWorld.player), client.networkWorld.player.getX(),
				client.networkWorld.player.getY(), client.networkWorld.player.getWidth(),
				client.networkWorld.player.getHeight());
		game.batch.draw(LoadingImage.pointer, client.networkWorld.player.getX(), client.networkWorld.player.getY() + 30,
				14, 13);
	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
		// gamePort.setScreenSize(width, height);
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