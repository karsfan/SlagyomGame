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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import character.DynamicObjects;
import character.Man;
import character.Player;
import character.Woman;
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
	LoadingImage loadingImage;
	/**
	 * Constructor of the screen where you play the game
	 * 
	 * @param game
	 *            that are you playing
	 * @param name
	 *            of the player
	 * @param male
	 *            true if the player's gender is male
	 * 
	 */
	@SuppressWarnings("static-access")
	public PlayScreen(GameSlagyom game, String name, boolean male) {
		
		this.loadingImage = game.loadingImage;
		new Game(name, male);
		this.game = game;

		gamecam = new OrthographicCamera();
		gamePort = new ExtendViewport(854, 480, gamecam);

		gamePort.apply();

		gamecam.position.x = Game.world.player.getX();
		gamecam.position.y = Game.world.player.getY();
		hud = new Hud(game);

		Controllers.addListener(this);
		this.game.modalityMultiplayer = false;
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
	 * @param male
	 *            true if the player's gender is male
	 */
	@SuppressWarnings("static-access")
	public PlayScreen(GameSlagyom game, String path, String name, boolean male) {
		

		new Game(path, name, male);
		this.game = game;
		this.game.modalityMultiplayer = false;
		gamecam = new OrthographicCamera();
		gamePort = new ExtendViewport(854, 480, gamecam);

		gamecam.position.x = Game.world.player.getX();
		gamecam.position.y = Game.world.player.getY();
		hud = new Hud(game);

		stop = true;
		Controllers.addListener(this);
		System.out.println();

	}
	
	@SuppressWarnings("static-access")
	public PlayScreen(String text, GameSlagyom game, String charName, boolean male) {

		
		new Game(text, game, charName, male);
		this.game = game;
		gamecam = new OrthographicCamera();
		gamePort = new ExtendViewport(854, 480, gamecam);
		gamePort.apply();
		gamecam.position.x = Game.world.player.getX();
		gamecam.position.y = Game.world.player.getY();
		hud = new Hud(game);
		this.game.modalityMultiplayer = false;
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
		if (hud.showDialog) {
			Label dialogLabel = new Label(text, MenuScreen.skin);
			hud.textTable.setSize(236 * 3, 47 * 4);
			hud.textTable.setBackground(LoadingImage.dialog);
			hud.textTable.add(dialogLabel).top();
		}
	}

	public static void hideDialog() {
		hud.textTable.clear();
		hud.textTable.setBackground(noDialog);
	}

	public void update(float dt) {
		moveCharacter(dt);
		if ((Game.world.player.getX() - gamePort.getWorldWidth() / 2 > 0
				&& Game.world.player.getX() + gamePort.getWorldWidth() / 2 < GameConfig.WIDTH))
			gamecam.position.x = Game.world.player.getX();

		if (Game.world.player.getY() - gamePort.getWorldHeight() / 2 > 0
				&& Game.world.player.getY() + gamePort.getWorldHeight() / 2 < GameConfig.HEIGHT)
			gamecam.position.y = Game.world.player.getY();

		gamecam.update();

	}

	private void moveCharacter(float dt) {
		try {
			if (!stop) {
				/*
				 * if (movesGamePad) { if (directionGamepad ==
				 * PovDirection.east) Game.world.player.movesRight(dt); else if
				 * (directionGamepad == PovDirection.north) {
				 * Game.world.player.movesUp(dt); if
				 * (Game.world.player.collideShop) {
				 * game.screenManager.swapScreen(it.slagyom.ScreenManager.State.
				 * SHOP); Game.world.semaphore.acquire();
				 * Game.world.player.collideShop = false; } } else if
				 * (directionGamepad == PovDirection.west)
				 * Game.world.player.movesLeft(dt); else if (directionGamepad ==
				 * PovDirection.south) Game.world.player.movesDown(dt); else if
				 * (directionGamepad == PovDirection.northEast)
				 * Game.world.player.movesNorthEast(dt); else if
				 * (directionGamepad == PovDirection.northWest)
				 * Game.world.player.movesNorthWest(dt); else if
				 * (directionGamepad == PovDirection.southEast)
				 * Game.world.player.movesSouthEast(dt); else if
				 * (directionGamepad == PovDirection.southWest)
				 * Game.world.player.movesSouthWest(dt);
				 * 
				 * }
				 */

				if (Gdx.input.isKeyPressed(Keys.Z)) {
					Game.world.player.setVelocity(150f);
					loadingImage.setFrameDurationCharacter(0.1f);
				} else {
					Game.world.player.setVelocity(100);
					loadingImage.setFrameDurationCharacter(0.2f);
				}
				if (Gdx.input.isKeyPressed(Keys.LEFT))
					Game.world.player.movesLeft(dt);
				else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
					Game.world.player.movesRight(dt);
				else if (Gdx.input.isKeyPressed(Keys.UP) || (directionGamepad == PovDirection.north && movesGamePad)) {
					Game.world.player.movesUp(dt);
					if (Game.world.player.collideShop) {
						Game.world.semaphore.acquire();
						game.screenManager.swapScreen(gameManager.ScreenManager.State.SHOP);
						Game.world.player.collideShop = false;
					}
					if (Game.world.player.collideGym) {
						game.screenManager.battlescreen = new BattleScreen(game, world.Game.world.battle);
						game.screenManager.swapScreen(gameManager.ScreenManager.State.BATTLE);
						Game.world.semaphore.acquire();
						Game.world.player.collideGym = false;
					}

				} else if (Gdx.input.isKeyPressed(Keys.DOWN))
					Game.world.player.movesDown(dt);

				else if (Gdx.input.isKeyJustPressed(Keys.C)) {
					gamecam.zoom -= 0.2;
					gamecam.position.x = Game.world.player.getX();
					gamecam.position.y = Game.world.player.getY();

				} else if (Gdx.input.isKeyJustPressed(Keys.V)) {
					gamecam.zoom += 0.2;
					gamecam.position.x = Game.world.player.getX();
					gamecam.position.y = Game.world.player.getY();

				} else if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
					LoadingMusic.pause();
					Game.world.semaphore.acquire();
					game.screenManager.swapScreen(gameManager.ScreenManager.State.PAUSE);
				}

				 else if (Gdx.input.isKeyJustPressed(Keys.B)) {
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
				game.batch.draw(loadingImage.getTileImage(ob), (float) ((StaticObject) ob).shape.getX(),
						(float) ((StaticObject) ob).shape.getY(), (float) ((StaticObject) ob).shape.getWidth(),
						(float) ((StaticObject) ob).shape.getHeight());
		}
		
		ListIterator<Item> it2 = Game.world.getListItems().listIterator();
		while (it2.hasNext()) {
			Object ob = (Object) it2.next();
			if (ob instanceof StaticObject)
				game.batch.draw(loadingImage.getTileImage(ob), (float) ((StaticObject) ob).shape.getX(),
						(float) ((StaticObject) ob).shape.getY(), (float) ((StaticObject) ob).shape.getWidth(),
						(float) ((StaticObject) ob).shape.getHeight());
		}
		
		Iterator<DynamicObjects> it1 = Game.world.getListDynamicObjects().iterator();
		while (it1.hasNext()) {
			Object ob = (Object) it1.next();
			if (ob instanceof DynamicObjects) {
				game.batch.draw(loadingImage.getFrame(ob), ((DynamicObjects) ob).getX(), ((DynamicObjects) ob).getY(),
						((DynamicObjects) ob).getWidth(), ((DynamicObjects) ob).getHeight());
				if (ob instanceof Man)
					if (((Man) ob).collisionWithCharacter) {
						hud.showDialog = true;
						hud.setDialogText(((Man) ob).name + ": " + ((Man) ob).info);
						// try {
						// Game.world.semaphore.acquire();
						// stop = true;
						// } catch (InterruptedException e) {
						// e.printStackTrace();
						// }
					}
				if (ob instanceof Woman)
					if (((Woman) ob).collisionWithCharacter) {
						hud.showDialog = true;
						hud.setDialogText(((Woman) ob).name + ": " + ((Woman) ob).info);
						// try {
						// Game.world.semaphore.acquire();
						// stop = true;
						// } catch (InterruptedException e) {
						// e.printStackTrace();
						// }
					}
			}
			if (ob instanceof Player)
				game.batch.draw(game.loadingImage.pointer, ((DynamicObjects) ob).getX(), ((DynamicObjects) ob).getY() + 30,
						14, 13);
		}

	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
		// gamePort.setScreenSize(width, height);
		// controlli per la posizione della camera
		if (gamePort.getWorldWidth() / 2 + Game.world.player.getX() - GameConfig.WIDTH > 0
				&& !(Game.world.player.getX() - gamePort.getWorldWidth() / 2 < 0))
			gamecam.position.x = GameConfig.WIDTH - gamePort.getWorldWidth() / 2;
		else if (Game.world.player.getX() - gamePort.getWorldWidth() / 2 < 0) {

			gamecam.position.x = gamePort.getWorldWidth() / 2;
		}
		if (gamePort.getWorldHeight() / 2 + Game.world.player.getY() - GameConfig.HEIGHT > 0)
			gamecam.position.y = GameConfig.HEIGHT - gamePort.getWorldHeight() / 2;
		else if (Game.world.player.getY() - gamePort.getWorldHeight() / 2 < 0) {
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