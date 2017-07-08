package it.slagyom;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import it.slagyom.src.World.Game;

public class PauseScreen implements Screen {

	private GameSlagyom game;
	protected Stage stage;
	private Viewport viewport;
	private OrthographicCamera camera;

	private Texture background;
	private Sprite backgroundSprite;
	// private boolean movesGamePad= false;
	// private PovDirection directionGamepad;
	Table mainTable = new Table();

	public PauseScreen(final GameSlagyom game) {

		this.game = game;
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(854, 480, camera);
		viewport.apply();

		background = new Texture("res/background.png");
		backgroundSprite = new Sprite(background);
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		camera.update();

		stage = new Stage(viewport, game.batch);

		// Create Table
		mainTable.setFillParent(true);
		mainTable.top();
		Label pauseLabel = new Label("PAUSE", MenuScreen.skin);
		// Create buttons
		TextButton bagButton = new TextButton("Bag", MenuScreen.skin);
		TextButton returnButton = new TextButton("Return", MenuScreen.skin);
		TextButton saveGame = new TextButton("Save game", MenuScreen.skin);
		TextButton optionsButton = new TextButton("Options", MenuScreen.skin);
		TextButton exitButton = new TextButton("Exit", MenuScreen.skin);
		TextButton menuButton = new TextButton("Menu", MenuScreen.skin);

		// Add listeners to buttons
		saveGame.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.saveGame();
				game.screenManager.swapScreen(it.slagyom.ScreenManager.State.PLAYING);
				PlayScreen.hud.setDialogText("Game saved!");
			}
		});
	//	saveGame.addListener(new InputListener());
		optionsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.screenManager.swapScreen(it.slagyom.ScreenManager.State.OPTIONMENU);

			}
		});

		returnButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.screenManager.swapScreen(game.screenManager.getPreviousState());
			}
		});
		menuButton.addListener(new ClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Game.world.getThread().stop();
				game.screenManager.playScreen.dispose();
				game.screenManager.menuScreen = new MenuScreen(game);
				game.screenManager.swapScreen(it.slagyom.ScreenManager.State.MENU);
			}
		});

		bagButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.screenManager.swapScreen(it.slagyom.ScreenManager.State.BAG);
			}
		});

		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});

		// Add buttons to table
		mainTable.add(pauseLabel).pad(30);
		mainTable.row();
		mainTable.add(bagButton).padTop(Gdx.graphics.getHeight() / 3 - Gdx.graphics.getHeight() / 5);
		mainTable.row();
		mainTable.add(saveGame).pad(5);
		mainTable.row().pad(15);

		mainTable.add(optionsButton).pad(5);
		mainTable.row();
		mainTable.add(returnButton).pad(5);
		mainTable.row();
		mainTable.add(menuButton).pad(5);
		mainTable.row();
		mainTable.add(exitButton).pad(20);
		mainTable.row();
		stage.addActor(mainTable);

		Table helpTable = new Table();
		helpTable.setFillParent(true);
		helpTable.bottom();

		Drawable arrowDraw = new TextureRegionDrawable(new TextureRegion(new Texture("res/help/arrow.png")));
		final ImageButton arrow = new ImageButton(arrowDraw);

		Drawable runDraw = new TextureRegionDrawable(new TextureRegion(new Texture("res/help/run.png")));
		final ImageButton run = new ImageButton(runDraw);

		Drawable actionDraw = new TextureRegionDrawable(new TextureRegion(new Texture("res/help/action.png")));
		final ImageButton action = new ImageButton(actionDraw);

		helpTable.add(arrow);// .padLeft(Gdx.graphics.getWidth()/2);
		helpTable.add(run);
		helpTable.add(action);
		stage.addActor(helpTable);
		Controllers.addListener(new MenuControllerListener(mainTable));
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.begin();
		backgroundSprite.draw(game.batch);
		game.batch.end();

		stage.act();
		stage.draw();

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			game.screenManager.swapScreen(game.screenManager.getPreviousState());
		}

	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().setScreenSize(width, height);
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		camera.update();
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
		game.screenManager.playScreen.dispose();
	}

	/*
	 * @Override public void connected(Controller controller) { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 * 
	 * @Override public void disconnected(Controller controller) { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 * 
	 * @Override public boolean buttonDown(Controller controller, int
	 * buttonCode) { // TODO Auto-generated method stub return false; }
	 * 
	 * @Override public boolean buttonUp(Controller controller, int buttonCode)
	 * { // TODO Auto-generated method stub return false; }
	 * 
	 * @Override public boolean axisMoved(Controller controller, int axisCode,
	 * float value) { // TODO Auto-generated method stub return false; }
	 * 
	 * @Override public boolean povMoved(Controller controller, int povCode,
	 * PovDirection value) { if (value == PovDirection.east) { movesGamePad =
	 * true; directionGamepad = value; return true; } else if (value ==
	 * PovDirection.north) { movesGamePad = true; directionGamepad = value;
	 * return true; } else if (value == PovDirection.south) { movesGamePad =
	 * true; directionGamepad = value; return true; } else if (value ==
	 * PovDirection.west) { movesGamePad = true; directionGamepad = value;
	 * return true; } else if (value == PovDirection.northEast || value ==
	 * PovDirection.northWest || value == PovDirection.southWest || value ==
	 * PovDirection.southEast) { movesGamePad = true; directionGamepad = value;
	 * return true; } //stage.keyDown(povCode); movesGamePad = false; return
	 * false; }
	 * 
	 * @Override public boolean xSliderMoved(Controller controller, int
	 * sliderCode, boolean value) { // TODO Auto-generated method stub return
	 * false; }
	 * 
	 * @Override public boolean ySliderMoved(Controller controller, int
	 * sliderCode, boolean value) { // TODO Auto-generated method stub return
	 * false; }
	 * 
	 * @Override public boolean accelerometerMoved(Controller controller, int
	 * accelerometerCode, Vector3 value) { // TODO Auto-generated method stub
	 * return false; }
	 */

}