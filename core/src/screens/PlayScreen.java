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
import hud.Hud;
import staticObjects.Item;
import staticObjects.StaticObject;
import staticObjects.StaticObject.Element;
import world.Game;
import world.GameConfig;

public class PlayScreen implements Screen, ControllerListener {

	public OrthographicCamera gamecam;
	public Viewport gamePort;
	public GameSlagyom gameSlagyom;
	public static Hud hud;
	private static Drawable noDialog = null;
	private static float textTimer;
	public int textIndex = 0;
	private boolean stop = false;

	PovDirection directiongamepad = null;
	boolean movesgamePad = false;
	LoadingImage loadingImage;
	Game game;

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
	public PlayScreen(GameSlagyom gameslagyom, String name, boolean male) {

		this.loadingImage = gameslagyom.loadingImage;
		game = new Game(name, male);
		this.gameSlagyom = gameslagyom;

		gamecam = new OrthographicCamera();
		gamePort = new ExtendViewport(854, 480, gamecam);

		gamePort.apply();

		gamecam.position.x = game.world.player.getX();
		gamecam.position.y = game.world.player.getY();
		hud = new Hud(gameslagyom);

		Controllers.addListener(this);
		gameslagyom.modalityMultiplayer = false;
		gameslagyom.loadingMusic.backgroundSound.loop(100);

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
	public PlayScreen(GameSlagyom gameSlagyom, String path, String name, boolean male) {

		game = new Game(path, name, male);
		this.gameSlagyom = gameSlagyom;
		this.gameSlagyom.modalityMultiplayer = false;
		gamecam = new OrthographicCamera();
		gamePort = new ExtendViewport(854, 480, gamecam);
		this.loadingImage = gameSlagyom.loadingImage;
		gamecam.position.x = game.world.player.getX();
		gamecam.position.y = game.world.player.getY();
		hud = new Hud(gameSlagyom);

		stop = true;
		Controllers.addListener(this);
		gameSlagyom.loadingMusic.backgroundSound.loop(100);
	}

	@SuppressWarnings("static-access")
	public PlayScreen(String text, GameSlagyom gameSlagyom, String charName, boolean male) {

		game = new Game(text, gameSlagyom, charName, male);
		this.gameSlagyom = gameSlagyom;
		this.loadingImage = gameSlagyom.loadingImage;
		gamecam = new OrthographicCamera();
		gamePort = new ExtendViewport(854, 480, gamecam);
		gamePort.apply();
		gamecam.position.x = game.world.player.getX();
		gamecam.position.y = game.world.player.getY();
		hud = new Hud(gameSlagyom);
		this.gameSlagyom.modalityMultiplayer = false;
		gameSlagyom.loadingMusic.backgroundSound.loop(100);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gameSlagyom.batch.setProjectionMatrix(gamecam.combined);

		gameSlagyom.batch.begin();
		draw();
		drawMiniMap();
//		drawLights();
		gameSlagyom.batch.end();
		
		hud.update();
		hud.stage.draw();
		hud.updateNight(delta);

		gameSlagyom.batch.setProjectionMatrix(gamecam.combined);
		gameSlagyom.batch.begin();
		drawLights();
		gameSlagyom.batch.end();
		
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
		// END TEXT TABLE RENDERING
		update(delta);
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

	@SuppressWarnings("static-access")
	public void update(float dt) {
		moveCharacter(dt);
		if (game.world.player.collideCoin)
			gameSlagyom.loadingMusic.coinSound.play();
		if (game.world.player.collideItem)
			gameSlagyom.loadingMusic.itemSound.play();
		if ((game.world.player.getX() - gamePort.getWorldWidth() / 2 > 0
				&& game.world.player.getX() + gamePort.getWorldWidth() / 2 < GameConfig.WIDTH))
			gamecam.position.x = game.world.player.getX();

		if (game.world.player.getY() - gamePort.getWorldHeight() / 2 > 0
				&& game.world.player.getY() + gamePort.getWorldHeight() / 2 < GameConfig.HEIGHT)
			gamecam.position.y = game.world.player.getY();

		gamecam.update();
		game.world.update(dt);
	}

	@SuppressWarnings("static-access")
	private void moveCharacter(float dt) {
		try {
			if (!stop) {
				/*
				 * if (movesgamePad) { if (directiongamepad ==
				 * PovDirection.east) game.world.player.movesRight(dt); else if
				 * (directiongamepad == PovDirection.north) {
				 * game.world.player.movesUp(dt); if
				 * (game.world.player.collideShop) {
				 * game.screenManager.swapScreen(it.slagyom.ScreenManager.State.
				 * SHOP); game.world.semaphore.acquire();
				 * game.world.player.collideShop = false; } } else if
				 * (directiongamepad == PovDirection.west)
				 * game.world.player.movesLeft(dt); else if (directiongamepad ==
				 * PovDirection.south) game.world.player.movesDown(dt); else if
				 * (directiongamepad == PovDirection.northEast)
				 * game.world.player.movesNorthEast(dt); else if
				 * (directiongamepad == PovDirection.northWest)
				 * game.world.player.movesNorthWest(dt); else if
				 * (directiongamepad == PovDirection.southEast)
				 * game.world.player.movesSouthEast(dt); else if
				 * (directiongamepad == PovDirection.southWest)
				 * game.world.player.movesSouthWest(dt);
				 * 
				 * }
				 */

				if (Gdx.input.isKeyPressed(Keys.Z)) {
					game.world.player.setVelocity(150f);
					loadingImage.setFrameDurationCharacter(0.1f);
				} else {
					game.world.player.setVelocity(100);
					loadingImage.setFrameDurationCharacter(0.2f);
				}
				if (Gdx.input.isKeyPressed(Keys.LEFT))
					game.world.player.movesLeft(dt);
				else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
					game.world.player.movesRight(dt);
				else if (Gdx.input.isKeyPressed(Keys.UP) || (directiongamepad == PovDirection.north && movesgamePad)) {
					game.world.player.movesUp(dt);
					if (game.world.player.collideShop) {
						// game.world.semaphore.acquire();
						gameSlagyom.screenManager.swapScreen(gameManager.ScreenManager.State.SHOP);
						game.world.player.collideShop = false;
					}
					if (game.world.player.collideGym) {
						gameSlagyom.screenManager.battlescreen = new BattleScreen(gameSlagyom, game.world.battle);
						gameSlagyom.screenManager.swapScreen(gameManager.ScreenManager.State.BATTLE);
						// game.world.semaphore.acquire();
						game.world.player.collideGym = false;
					}

				} else if (Gdx.input.isKeyPressed(Keys.DOWN))
					game.world.player.movesDown(dt);

				else if (Gdx.input.isKeyJustPressed(Keys.C)) {
					gamecam.zoom -= 0.2;
					gamecam.position.x = game.world.player.getX();
					gamecam.position.y = game.world.player.getY();

				} else if (Gdx.input.isKeyJustPressed(Keys.V)) {
					gamecam.zoom += 0.2;
					gamecam.position.x = game.world.player.getX();
					gamecam.position.y = game.world.player.getY();

				} else if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
					gameSlagyom.loadingMusic.pause();
					// game.world.semaphore.acquire();
					gameSlagyom.screenManager.swapScreen(gameManager.ScreenManager.State.PAUSE);
				}

				else if (Gdx.input.isKeyJustPressed(Keys.B)) {
					game.world.nextLevel();
				} // else
					// game.character.setState(StateDynamicObject.STANDING);
			}
		} catch (InterruptedException e) {
		}

		if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
			hud.showDialog = false;
			hideDialog();
			if (stop) {
				stop = false;
			}
		}
	}

	public float miniMapScale = 7;
	public float miniMapRadius = (float) 63.5;

	@SuppressWarnings("static-access")
	public synchronized void draw() {
		ListIterator<StaticObject> it = (ListIterator<StaticObject>) game.world.getListTile().listIterator();

		while (it.hasNext()) {
			Object ob = (Object) it.next();
			if (ob instanceof StaticObject) {
				gameSlagyom.batch.draw(loadingImage.getTileImage(ob), (float) ((StaticObject) ob).shape.getX(),
						(float) ((StaticObject) ob).shape.getY(), (float) ((StaticObject) ob).shape.getWidth(),
						(float) ((StaticObject) ob).shape.getHeight());
				if (((StaticObject) ob).getElement() == Element.LAMP ||((StaticObject) ob).getElement() == Element.LIGHTLAMP )
					if (hud.timer >= (hud.dayTime * 3) / 5 && !hud.isNight) {
						((StaticObject) ob).setElement(Element.LIGHTLAMP);
					}
					else
						((StaticObject) ob).setElement(Element.LAMP);
				// if (((StaticObject) ob).getElement() == Element.LIGHTLAMP)
				// gameSlagyom.batch.draw(loadingImage.lightImage, (float)
				// ((StaticObject) ob).shape.getX(),
				// (float) ((StaticObject) ob).shape.getY(), 192, 192);
			}
		}
		synchronized (game.world.getListItems()) {
			ListIterator<Item> it2 = game.world.getListItems().listIterator();
			while (it2.hasNext()) {
				Object ob = (Object) it2.next();
				if (ob instanceof StaticObject)
					gameSlagyom.batch.draw(loadingImage.getTileImage(ob), (float) ((StaticObject) ob).shape.getX(),
							(float) ((StaticObject) ob).shape.getY(), (float) ((StaticObject) ob).shape.getWidth(),
							(float) ((StaticObject) ob).shape.getHeight());
			}
		}
		Iterator<DynamicObjects> it1 = game.world.getListDynamicObjects().iterator();
		while (it1.hasNext()) {
			Object ob = (Object) it1.next();
			if (ob instanceof DynamicObjects) {
				gameSlagyom.batch.draw(loadingImage.getFrame(ob), ((DynamicObjects) ob).getX(),
						((DynamicObjects) ob).getY(), ((DynamicObjects) ob).getWidth(),
						((DynamicObjects) ob).getHeight());
				if (ob instanceof Man)
					if (((Man) ob).collisionWithCharacter) {
						hud.showDialog = true;
						hud.setDialogText(((Man) ob).name + ": " + ((Man) ob).info);
					}
				if (ob instanceof Woman)
					if (((Woman) ob).collisionWithCharacter) {
						hud.showDialog = true;
						hud.setDialogText(((Woman) ob).name + ": " + ((Woman) ob).info);
					}
			}
			if (ob instanceof Player)
				gameSlagyom.batch.draw(LoadingImage.pointer, ((DynamicObjects) ob).getX(),
						((DynamicObjects) ob).getY() + 30, 14, 13);

		}

	}

	@SuppressWarnings("static-access")
	public void drawMiniMap() {
		gameSlagyom.batch.draw(LoadingImage.miniMap, (float) gamecam.position.x + 260, (float) gamecam.position.y - 225,
				127, 127);

		ListIterator<StaticObject> itMiniMapStatic = (ListIterator<StaticObject>) game.world.getListTile()
				.listIterator();
		Iterator<DynamicObjects> itMiniMapDynamic = game.world.getListDynamicObjects().iterator();

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
						gameSlagyom.batch.draw(loadingImage.getTileImage(ob), gamecam.position.x + 260 + miniX,
								gamecam.position.y - 225 + miniY,
								(float) ((StaticObject) ob).shape.getWidth() / miniMapScale,
								(float) ((StaticObject) ob).shape.getHeight() / miniMapScale);
					}
			}
		}

		while (itMiniMapDynamic.hasNext()) {
			Object ob = (Object) itMiniMapDynamic.next();
			if (ob instanceof Player) {
				// MINI-MAP PLAYER
				float miniX = (float) (((DynamicObjects) ob).getX() * 127 / 1440);
				float miniY = (float) (((DynamicObjects) ob).getY() * 127 / 960);
				if ((miniX - miniMapRadius) * (miniX - miniMapRadius)
						+ (miniY - miniMapRadius) * (miniY - miniMapRadius) < miniMapRadius * miniMapRadius)
					gameSlagyom.batch.draw(LoadingImage.miniMapPointer, gamecam.position.x + 260 + miniX,
							gamecam.position.y - 225 + miniY, (float) ((DynamicObjects) ob).getWidth() / 4,
							(float) ((DynamicObjects) ob).getHeight() / 4);
			}
		}
	}

	public void drawLights() {
		ListIterator<StaticObject> it = (ListIterator<StaticObject>) game.world.getListTile().listIterator();
		while (it.hasNext()) {
			Object ob = (Object) it.next();
			if (((StaticObject) ob).getElement() == Element.LIGHTLAMP && hud.timer >= (hud.dayTime * 3) / 5) {
				gameSlagyom.batch.draw(loadingImage.lightImage, ((StaticObject) ob).getX() - 96,
						((StaticObject) ob).getY() - 54, 192, 192);		
			}
		}
	}
	
	

	@SuppressWarnings("static-access")
	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);

		// controlli per la posizione della camera
		if (gamePort.getWorldWidth() / 2 + game.world.player.getX() - GameConfig.WIDTH > 0
				&& !(game.world.player.getX() - gamePort.getWorldWidth() / 2 < 0))
			gamecam.position.x = GameConfig.WIDTH - gamePort.getWorldWidth() / 2;
		else if (game.world.player.getX() - gamePort.getWorldWidth() / 2 < 0) {
			gamecam.position.x = gamePort.getWorldWidth() / 2;
		}

		if (gamePort.getWorldHeight() / 2 + game.world.player.getY() - GameConfig.HEIGHT > 0)
			gamecam.position.y = GameConfig.HEIGHT - gamePort.getWorldHeight() / 2;
		else if (game.world.player.getY() - gamePort.getWorldHeight() / 2 < 0) {
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
			movesgamePad = true;
			directiongamepad = value;
			return true;
		} else if (value == PovDirection.north) {
			movesgamePad = true;
			directiongamepad = value;
			return true;
		} else if (value == PovDirection.south) {
			movesgamePad = true;
			directiongamepad = value;
			return true;
		} else if (value == PovDirection.west) {
			movesgamePad = true;
			directiongamepad = value;
			return true;
		} else if (value == PovDirection.northEast || value == PovDirection.northWest || value == PovDirection.southWest
				|| value == PovDirection.southEast) {
			movesgamePad = true;
			directiongamepad = value;
			return true;
		}
		movesgamePad = false;
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