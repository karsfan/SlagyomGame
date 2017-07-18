package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
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

import gameManager.GameSlagyom;
import gameManager.ScreenManager.State;
import multiplayer.NetworkPlayScreen;

public class PauseScreen implements Screen, ControllerListener {

	private GameSlagyom game;
	public Stage stage;
	private Viewport viewport;
	private OrthographicCamera camera;

	private Texture background;
	private Sprite backgroundSprite;

	Table mainTable = new Table();
	TextButton buttonSelected;
	TextButton bagButton;
	TextButton returnButton;
	TextButton saveGame;
	TextButton optionsButton;
	TextButton exitButton;
	TextButton menuButton;

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
		bagButton = new TextButton("Bag", MenuScreen.skin);
		returnButton = new TextButton("Return", MenuScreen.skin);
		saveGame = new TextButton("Save game", MenuScreen.skin);
		optionsButton = new TextButton("Options", MenuScreen.skin);
		exitButton = new TextButton("Exit", MenuScreen.skin);
		menuButton = new TextButton("Menu", MenuScreen.skin);

		// Add listeners to buttons
		saveGame.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				clickSaveButton();
			}
		});
		
		optionsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.screenManager.swapScreen(gameManager.ScreenManager.State.OPTIONMENU);
			}
		});

		returnButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.screenManager.swapScreen(game.screenManager.getPreviousState());
			}
		});
		menuButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				clickMenuButton();
			}
		});

		bagButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.screenManager.swapScreen(gameManager.ScreenManager.State.BAG);
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
		mainTable.add(bagButton).padTop(viewport.getWorldHeight() / 3 - viewport.getWorldHeight() / 5);
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

		helpTable.add(arrow);
		helpTable.add(run);
		helpTable.add(action);
		stage.addActor(helpTable);
		buttonSelected = bagButton;
	}

	@SuppressWarnings("static-access")
	protected void clickMenuButton() {
		if (!game.modalityMultiplayer) {
			game.screenManager.playScreen.dispose();
		} else {
			int id = ((NetworkPlayScreen) game.screenManager.playScreen).client.networkWorld.player.ID;
			((NetworkPlayScreen) game.screenManager.playScreen).client.writer
					.println(10 + " " + id + " " + 0 + " " + 0 + " " + 0 + ";" + "111111" + ";");
			((NetworkPlayScreen) game.screenManager.playScreen).client.writer.flush();
		}
		game.screenManager.menuScreen = new MenuScreen(game);
		game.screenManager.swapScreen(gameManager.ScreenManager.State.MENU);
	}

	protected void clickSaveButton() {
		game.saveGame();
		game.screenManager.swapScreen(gameManager.ScreenManager.State.PLAYING);
		PlayScreen.hud.setDialogText("Game saved!");
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		buttonSelected.getLabel().setFontScale(1.0f);
		mouseMoved();
		buttonSelected.getLabel().setFontScale(1.2f);
		
		game.batch.begin();
		backgroundSprite.draw(game.batch);
		game.batch.end();

		stage.act();
		stage.draw();

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			game.screenManager.swapScreen(game.screenManager.getPreviousState());
		}
	}
	private void mouseMoved() {
		if (bagButton.isOver())
			buttonSelected = bagButton;
		else if (returnButton.isOver())
			buttonSelected = returnButton;
		else if (saveGame.isOver())
			buttonSelected = saveGame;
		else if (menuButton.isOver())
			buttonSelected = menuButton;
		else if(exitButton.isOver())
			buttonSelected = exitButton;
		else if(optionsButton.isOver())
			buttonSelected = optionsButton;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		if(buttonCode == 1)
			game.screenManager.swapScreen(game.screenManager.getPreviousState());
		else if(buttonCode == 0){
			if(buttonSelected == saveGame)
				clickSaveButton();
			else if(buttonSelected == bagButton)
				game.screenManager.swapScreen(gameManager.ScreenManager.State.BAG);
			else if(buttonSelected == returnButton)
				game.screenManager.swapScreen(game.screenManager.getPreviousState());
			else if(buttonSelected == menuButton)
				game.screenManager.swapScreen(State.MENU);
			else if(buttonSelected == exitButton)
				Gdx.app.exit();
			else if(buttonSelected == optionsButton)
				game.screenManager.swapScreen(gameManager.ScreenManager.State.OPTIONMENU);
		}
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		if(value == PovDirection.north){
			buttonSelected.getLabel().setFontScale(1.0f);
			if(buttonSelected == bagButton)
				buttonSelected = exitButton;
			else if(buttonSelected == saveGame)
				buttonSelected = bagButton;
			else if(buttonSelected == optionsButton)
				buttonSelected = saveGame;
			else if(buttonSelected == returnButton)
				buttonSelected = optionsButton;
			else if(buttonSelected == menuButton)
				buttonSelected = returnButton;
			else if(buttonSelected == exitButton)
				buttonSelected = menuButton;
		}
		if(value == PovDirection.south){
			buttonSelected.getLabel().setFontScale(1.0f);
			if(buttonSelected == bagButton)
				buttonSelected = saveGame;
			else if(buttonSelected == saveGame)
				buttonSelected = optionsButton;
			else if(buttonSelected == optionsButton)
				buttonSelected = returnButton;
			else if(buttonSelected == returnButton)
				buttonSelected = menuButton;
			else if(buttonSelected == menuButton)
				buttonSelected = exitButton;
			else if(buttonSelected == exitButton)
				buttonSelected = bagButton;
		}
		return false;
	}

	@Override
	public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
		// TODO Auto-generated method stub
		return false;
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