package screens;

import java.util.Iterator;
import java.util.ListIterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import character.DynamicObjects;
import character.Player;
import gameManager.GameSlagyom;
import gameManager.LoadingImage;
import gameManager.LoadingMusic;
import hud.Hud;
import staticObjects.Item;
import staticObjects.StaticObject;
import world.Game;
import world.GameConfig;

public class PlayScreen implements Screen, ControllerListener {

	public OrthographicCamera gamecam;
	public Viewport gamePort;
	public GameSlagyom game;
	public static Hud hud;
	private static Drawable noDialog = null;

	private static float textTimer;
	public int textIndex = 0;
	private boolean stop = false;

	PovDirection directionGamepad = null;
	boolean movesGamePad = false;

	/**
	 * Constructor of the screen where you play the game
	 * 
	 * @param game
	 *            that are you playing
	 * @param name
	 *            of the player
	 */
	public PlayScreen(GameSlagyom game, String name) {
		new LoadingImage();

		new Game(name);
		this.game = game;

		gamecam = new OrthographicCamera();
		gamePort = new ExtendViewport(854, 480, gamecam);

		gamePort.apply();

		gamecam.position.x = Game.player.getX();
		gamecam.position.y = Game.player.getY();
		hud = new Hud(game.batch);

		Controllers.addListener(this);

	}

	/**
	 * Constructor of the screen where you play the game with map path
	 * 
	 * @param game
	 *            that are you playing
	 * @param game's
	 *            map's path
	 * @param name
	 *            of the player
	 */
	public PlayScreen(GameSlagyom game, String path, String name) {
		new LoadingImage();

		new Game(path, name);
		this.game = game;

		gamecam = new OrthographicCamera();
		gamePort = new ExtendViewport(854, 480, gamecam);

		gamecam.position.x = Game.player.getX();
		gamecam.position.y = Game.player.getY();
		hud = new Hud(game.batch);

		stop = true;
		Controllers.addListener(this);

	}

	public PlayScreen(String text, GameSlagyom game, String charName) {

		new LoadingImage();
		new Game(text, game, charName);
		this.game = game;
		gamecam = new OrthographicCamera();
		gamePort = new ExtendViewport(854, 480, gamecam);
		gamePort.apply();
		gamecam.position.x = Game.player.getX();
		gamecam.position.y = Game.player.getY();
		hud = new Hud(game.batch);

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
		hud.update();

		hud.stage.draw();
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
						LoadingMusic.tickSound.play(1.0f);

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
		// END TEXT TABLE RENDERING

	}

	/**
	 * Draws the dialog on the screen
	 * 
	 * @param text
	 *            that you want to show
	 */
	public static void drawDialog(final String text) {
		Drawable dialog = new TextureRegionDrawable(new TextureRegion(new Texture("res/dialogBox.png")));
		if (hud.showDialog) {
			Label dialogLabel = new Label(text, MenuScreen.skin);
			hud.textTable.setSize(236 * 3, 47 * 4);
			hud.textTable.setBackground(dialog);
			hud.textTable.add(dialogLabel).top();
		}
	}

	public static void hideDialog() {
		hud.textTable.clear();
		hud.textTable.setBackground(noDialog);
	}

	public void update(float dt) {
		moveCharacter(dt);
		if ((Game.player.getX() - gamePort.getWorldWidth() / 2 > 0
				&& Game.player.getX() + gamePort.getWorldWidth() / 2 < GameConfig.WIDTH))
			gamecam.position.x = Game.player.getX();

		if (Game.player.getY() - gamePort.getWorldHeight() / 2 > 0
				&& Game.player.getY() + gamePort.getWorldHeight() / 2 < GameConfig.HEIGHT)
			gamecam.position.y = Game.player.getY();

		gamecam.update();

	}

	private void moveCharacter(float dt) {
		try {
			if (!stop) {
				/*
				 * if (movesGamePad) { if (directionGamepad ==
				 * PovDirection.east) Game.player.movesRight(dt); else if
				 * (directionGamepad == PovDirection.north) {
				 * Game.player.movesUp(dt); if (Game.player.collideShop) {
				 * game.screenManager.swapScreen(it.slagyom.ScreenManager.State.
				 * SHOP); Game.world.semaphore.acquire();
				 * Game.player.collideShop = false; } } else if
				 * (directionGamepad == PovDirection.west)
				 * Game.player.movesLeft(dt); else if (directionGamepad ==
				 * PovDirection.south) Game.player.movesDown(dt); else if
				 * (directionGamepad == PovDirection.northEast)
				 * Game.player.movesNorthEast(dt); else if (directionGamepad ==
				 * PovDirection.northWest) Game.player.movesNorthWest(dt); else
				 * if (directionGamepad == PovDirection.southEast)
				 * Game.player.movesSouthEast(dt); else if (directionGamepad ==
				 * PovDirection.southWest) Game.player.movesSouthWest(dt);
				 * 
				 * }
				 */

				if (Gdx.input.isKeyPressed(Keys.Z)) {
					Game.player.setVelocity(150f);
					LoadingImage.setFrameDurationCharacter(0.1f);
				} else {
					Game.player.setVelocity(100);
					LoadingImage.setFrameDurationCharacter(0.2f);
				}
				if (Gdx.input.isKeyPressed(Keys.LEFT))
					Game.player.movesLeft(dt);
				else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
					Game.player.movesRight(dt);
				else if (Gdx.input.isKeyPressed(Keys.UP) || (directionGamepad == PovDirection.north && movesGamePad)) {
					Game.player.movesUp(dt);
					if (Game.player.collideShop) {
						Game.world.semaphore.acquire();
						game.screenManager.swapScreen(gameManager.ScreenManager.State.SHOP);
						Game.player.collideShop = false;
					}

					if (Game.player.collideGym) {
						game.screenManager.battlescreen = new BattleScreen(game, world.Game.world.battle);
						game.screenManager.swapScreen(gameManager.ScreenManager.State.BATTLE);
						Game.world.semaphore.acquire();
						Game.player.collideGym = false;
					}

				} else if (Gdx.input.isKeyPressed(Keys.DOWN))
					Game.player.movesDown(dt);

				else if (Gdx.input.isKeyJustPressed(Keys.C)) {
					gamecam.zoom -= 0.2;
					gamecam.position.x = Game.player.getX();
					gamecam.position.y = Game.player.getY();

				} else if (Gdx.input.isKeyJustPressed(Keys.V)) {
					gamecam.zoom += 0.2;
					gamecam.position.x = Game.player.getX();
					gamecam.position.y = Game.player.getY();

				} else if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
					LoadingMusic.pause();
					Game.world.semaphore.acquire();
					game.screenManager.swapScreen(gameManager.ScreenManager.State.PAUSE);
				}

				else if (Gdx.input.isKeyJustPressed(Keys.Y)) {

					// Game.world.createBattle();
					Game.world.semaphore.acquire();
					game.screenManager.swapScreen(gameManager.ScreenManager.State.BATTLE);
				} else if (Gdx.input.isKeyJustPressed(Keys.B)) {
					Game.world.nextLevel();
				} // else
					// Game.character.setState(StateDynamicObject.STANDING);
			}
		} catch (InterruptedException e) {

		}
		if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
			hud.showDialog = false;
			hideDialog();
			if (stop) {
				// Game.world.semaphore.release();
				Game.world.getThread().start();
				stop = false;
			}
		}
	}

	public synchronized void draw() {
		ListIterator<StaticObject> it = (ListIterator<StaticObject>) Game.world.getListTile().listIterator();

		while (it.hasNext()) {
			Object ob = (Object) it.next();
			if (ob instanceof StaticObject)
				game.batch.draw(LoadingImage.getTileImage(ob), (float) ((StaticObject) ob).shape.getX(),
						(float) ((StaticObject) ob).shape.getY(), (float) ((StaticObject) ob).shape.getWidth(),
						(float) ((StaticObject) ob).shape.getHeight());
		}
		ListIterator<Item> it2 = Game.world.getListItems().listIterator();
		while (it2.hasNext()) {
			Object ob = (Object) it2.next();
			if (ob instanceof StaticObject)
				game.batch.draw(LoadingImage.getTileImage(ob), (float) ((StaticObject) ob).shape.getX(),
						(float) ((StaticObject) ob).shape.getY(), (float) ((StaticObject) ob).shape.getWidth(),
						(float) ((StaticObject) ob).shape.getHeight());
		}
		Iterator<DynamicObjects> it1 = Game.world.getListDynamicObjects().iterator();
		while (it1.hasNext()) {
			Object ob = (Object) it1.next();
			if (ob instanceof DynamicObjects)
				game.batch.draw(LoadingImage.getFrame(ob), ((DynamicObjects) ob).getX(), ((DynamicObjects) ob).getY(),
						((DynamicObjects) ob).getWidth(), ((DynamicObjects) ob).getHeight());
			if (ob instanceof Player)
				game.batch.draw(LoadingImage.pointer, ((DynamicObjects) ob).getX(), ((DynamicObjects) ob).getY() + 30,
						14, 13);
		}

	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
		// gamePort.setScreenSize(width, height);
		// controlli per la posizione della camera
		if (gamePort.getWorldWidth() / 2 + Game.player.getX() - GameConfig.WIDTH > 0
				&& !(Game.player.getX() - gamePort.getWorldWidth() / 2 < 0))
			gamecam.position.x = GameConfig.WIDTH - gamePort.getWorldWidth() / 2;
		else if (Game.player.getX() - gamePort.getWorldWidth() / 2 < 0) {

			gamecam.position.x = gamePort.getWorldWidth() / 2;
		}
		if (gamePort.getWorldHeight() / 2 + Game.player.getY() - GameConfig.HEIGHT > 0)
			gamecam.position.y = GameConfig.HEIGHT - gamePort.getWorldHeight() / 2;
		else if (Game.player.getY() - gamePort.getWorldHeight() / 2 < 0) {
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