package it.slagyom;

import java.util.Iterator;
import java.util.ListIterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import it.slagyom.GameSlagyom.State;
import it.slagyom.src.Character.DynamicObjects;
import it.slagyom.src.Map.Item;
import it.slagyom.src.Map.StaticObject;
import it.slagyom.src.World.Game;
import it.slagyom.src.World.GameConfig;
import it.slagyom.src.World.Tile;

public class PlayScreen implements Screen, ControllerListener {

	public OrthographicCamera gamecam;
	public Viewport gamePort;
	public GameSlagyom game;
	public static Hud hud;
	private static Drawable noDialog = null;
	private static float textTimer;

	public static boolean drawAnimation = false;
	public int j = 0;

	private boolean stop = false;
	public int i = 0;
	PovDirection directionGamepad;
	public static Item obj;
	public static float drawx;
	public static float drawy;

	boolean movesGamePad = false;

	public PlayScreen(GameSlagyom game, String name) {

		this.game = game;
		new Game(name);
		new LoadingImage();
		gamecam = new OrthographicCamera();
		gamePort = new ExtendViewport(854, 480, gamecam);
		// gamePort = new ScreenViewport(gamecam);

		gamePort = new ExtendViewport(840, 480, gamecam);
		gamePort.apply();

		gamecam.position.x = Game.character.getX();
		gamecam.position.y = Game.character.getY();
		hud = new Hud(game.batch);

		Controllers.addListener(this);
		//LoadingImage.mainMusic.setVolume(0.5f);
		//LoadingImage.mainMusic.play();
		LoadingImage.backgroundSound.loop(1f);

	}

	public PlayScreen(GameSlagyom game, String path, String name) {
		new LoadingImage();
		new Game(path, name);
		this.game = game;
		hud = new Hud(game.batch);

		gamecam = new OrthographicCamera();
		gamePort = new ExtendViewport(854, 480, gamecam);
		//gamePort = new ScreenViewport(gamecam);
		gamePort = new ExtendViewport(840, 480, gamecam);

		gamecam.position.x = Game.character.getX();
		gamecam.position.y = Game.character.getY();
		stop = true;
		Controllers.addListener(this);
		//LoadingImage.mainMusic.setVolume(0.1f);

		//LoadingImage.mainMusic.play();
		LoadingImage.backgroundSound.loop(1f);


	}

	@SuppressWarnings({})
	@Override
	public void render(float delta) {
		update(delta);

		hud.update();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.setProjectionMatrix(gamecam.combined);

		game.batch.begin();
		draw();
		game.batch.end();

		hud.stage.draw();
		// TEXT TABLE RENDERING
		textTimer += delta;
		if (hud.showDialog) {
			if (textTimer > 0.08f) {
				textTimer = 0;
				if (i < hud.textDialog.length()) {
					if (i % 25 == 0)
						hud.textTable.row();
					if (i % 75 == 0) {
						hud.textTable.clear();
						LoadingImage.tickSound.play(0.8f);					
					}
					drawDialog(String.valueOf(hud.textDialog.charAt(i)));
					i++;
				}
			}
		}

		if (!hud.showDialog) {
			hideDialog();
			i = 0;
		}
		// END TEXT TABLE RENDERING

		if (drawAnimation)
			j += 2;
		else
			j = 0;

	}

	public static void pickAnimation(Item ob, float x, int y) {
		drawAnimation = true;
		obj = new Item();

		obj = ob;
	}

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

		// gamecam.position.x = Game.character.getX();
		// gamecam.position.y = Game.character.getY();
		//System.out.println(Game.character.getX() + " " + Gdx.graphics.getWidth() / 2);
		if ((Game.character.getX() - gamePort.getWorldWidth() / 2 > 0
				&& Game.character.getX() + gamePort.getWorldWidth() / 2 < GameConfig.WIDTH))
			gamecam.position.x = Game.character.getX();

		if (Game.character.getY() - gamePort.getWorldHeight() / 2 > 0
				&& Game.character.getY() + gamePort.getWorldHeight() / 2 < GameConfig.HEIGHT)
			gamecam.position.y = Game.character.getY();

		gamecam.update();

	}

	private void moveCharacter(float dt) {
		try {
			if (!stop) {
				if (movesGamePad) {
					if (directionGamepad == PovDirection.east)
						Game.character.movesRight(dt);
					else if (directionGamepad == PovDirection.north) {
						Game.character.movesUp(dt);
						if (Game.character.collideDoor) {
							game.swapScreen(State.SHOP);
							Game.world.semaphore.acquire();
							Game.character.collideDoor = false;
						}
					} else if (directionGamepad == PovDirection.west)
						Game.character.movesLeft(dt);
					else if (directionGamepad == PovDirection.south)
						Game.character.movesDown(dt);
					else if (directionGamepad == PovDirection.northEast)
						Game.character.movesNorthEast(dt);
					else if (directionGamepad == PovDirection.northWest)
						Game.character.movesNorthWest(dt);
					else if (directionGamepad == PovDirection.southEast)
						Game.character.movesSouthEast(dt);
					else if (directionGamepad == PovDirection.southWest)
						Game.character.movesSouthWest(dt);

				}

				else if (Gdx.input.isKeyPressed(Keys.Z)) {
					Game.character.setVelocity(150f);
					LoadingImage.setFrameDurationCharacter(0.1f);
				} else {
					Game.character.setVelocity(100);
					LoadingImage.setFrameDurationCharacter(0.2f);
				}
				if (Gdx.input.isKeyPressed(Keys.LEFT))
					Game.character.movesLeft(dt);
				else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
					Game.character.movesRight(dt);
				else if (Gdx.input.isKeyPressed(Keys.UP)) {
					Game.character.movesUp(dt);
					if (Game.character.collideDoor) {
						game.swapScreen(State.SHOP);
						Game.world.semaphore.acquire();
						Game.character.collideDoor = false;
					}

				} else if (Gdx.input.isKeyPressed(Keys.DOWN))
					Game.character.movesDown(dt);

				else if (Gdx.input.isKeyJustPressed(Keys.C)) {
					gamecam.zoom -= 0.2;
					gamecam.position.x = Game.character.getX();
					gamecam.position.y = Game.character.getY();

				} else if (Gdx.input.isKeyJustPressed(Keys.V)) {
					gamecam.zoom += 0.2;
					gamecam.position.x = Game.character.getX();
					gamecam.position.y = Game.character.getY();

				} else if (Gdx.input.isKeyJustPressed(Keys.ESCAPE))
					game.swapScreen(GameSlagyom.State.PAUSE);

				else if (Gdx.input.isKeyJustPressed(Keys.Y)) {
					Game.world.createBattle();
					Game.world.semaphore.acquire();
					game.swapScreen(it.slagyom.GameSlagyom.State.BATTLE);
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
				Game.world.semaphore.release();
				stop = false;
			}
		}
	}

	public synchronized void draw() {
		ListIterator<Tile> it = (ListIterator<Tile>) Game.world.getListTile().listIterator();

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
		}
		// game.batch.draw(LoadingImage.woman1Stand, 500, 500, 160,160);

		if (drawAnimation) {
			obj.setX((int) (obj.getX() + j));
			obj.setY((int) (obj.getY() + j));
			game.batch.draw(LoadingImage.getTileImage(obj), (float) ((StaticObject) obj).shape.getX(),
					(float) ((StaticObject) obj).shape.getY(), (float) ((StaticObject) obj).shape.getWidth(),
					(float) ((StaticObject) obj).shape.getHeight());
			if ((float) ((StaticObject) obj).shape.getX() == Gdx.graphics.getHeight()
					|| (float) ((StaticObject) obj).shape.getY() == Gdx.graphics.getWidth())
				drawAnimation = false;
		}
	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
		// gamePort.setScreenSize(width, height);
		// controlli per la posizione della camera
		if (gamePort.getWorldWidth() / 2 + Game.character.getX() - GameConfig.WIDTH > 0
				&& !(Game.character.getX() - gamePort.getWorldWidth() / 2 < 0))
			gamecam.position.x = GameConfig.WIDTH - gamePort.getWorldWidth() / 2;
		else if (Game.character.getX() - gamePort.getWorldWidth() / 2 < 0) {

			gamecam.position.x = gamePort.getWorldWidth() / 2;
		}
		if (gamePort.getWorldHeight() / 2 + Game.character.getY() - GameConfig.HEIGHT > 0)
			gamecam.position.y = GameConfig.HEIGHT - gamePort.getWorldHeight()/2;
		else if (Game.character.getY() - gamePort.getWorldHeight()/2 < 0) {
			gamecam.position.y = gamePort.getWorldHeight()/2;
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
		System.out.println("qui");

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