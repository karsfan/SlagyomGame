package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import gameManager.GameSlagyom;
import gameManager.ScreenManager.State;

public class InitializerScreen implements Screen, ControllerListener {

	public GameSlagyom game;
	public Stage stage;
	private Viewport viewport;
	private OrthographicCamera camera;

	private Texture background;
	private Sprite backgroundSprite;
	final Label name;
	TextButton defaultAiButton = new TextButton("Default enemies' A.I.", MenuScreen.skin);
	TextButton chooseAiButton = new TextButton("Choose enemies' A.I...", MenuScreen.skin);
	TextButton returnButton = new TextButton("Return", MenuScreen.skin);
	TextButton continueButton;
	TextButton buttonSelected;
	TextField nameAI;
	public Table mainTable = new Table();

	public InitializerScreen(final GameSlagyom game) {
		this.game = game;
		name = new Label("Welcome " + game.screenManager.newCharacterScreen.charName, MenuScreen.skin);
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

		// Create buttons
		// Add listeners to buttons
		defaultAiButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				clickDefaultLevelButton();
			}
		});

		chooseAiButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				clickChooseLevelButton();
			}
		});

		returnButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.screenManager.swapScreen(State.NEWGAME);
			}
		});
		continueButton = new TextButton("Continue", MenuScreen.skin);
		continueButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				clickContinueButton();
			}
		});
		continueButton.setVisible(false);
		// Add buttons to table

		mainTable.add(name).pad(30);
		mainTable.row();
		mainTable.add(defaultAiButton).pad(5).padTop(camera.viewportHeight / 5);
		mainTable.row();
		mainTable.add(chooseAiButton).pad(5);
		mainTable.row();
		mainTable.add(returnButton).pad(5);
		mainTable.row();

		stage.addActor(mainTable);
		buttonSelected = defaultAiButton;

	}

	protected void clickChooseLevelButton() {
		nameAI = new TextField("", MenuScreen.skin);
		nameAI.setMessageText("Name");
		nameAI.setFocusTraversal(true);

		mainTable.clear();
		mainTable.add(name).pad(30);
		mainTable.row();
		mainTable.add(defaultAiButton).pad(5).padTop(camera.viewportHeight / 2 - camera.viewportHeight / 3);
		mainTable.row();
		mainTable.add(nameAI);
		mainTable.row();
		continueButton.setVisible(true);
		
		mainTable.add(continueButton).pad(5);
		mainTable.row();
		mainTable.add(returnButton).pad(5);
		mainTable.row();
	}

	protected void clickContinueButton() {
		if (!nameAI.getText().isEmpty()) {
			game.screenManager.setPlayScreen(
					new PlayScreen(nameAI.getText(), game, game.screenManager.newCharacterScreen.charName,
							game.screenManager.newCharacterScreen.maleSelected));
			game.screenManager.swapScreen(gameManager.ScreenManager.State.PLAYING);
		}
	}

	protected void clickDefaultLevelButton() {
		game.loadingMusic.pause();
		game.screenManager.menuScreen.menuMusic.stop();
		
		game.screenManager.playScreen = new PlayScreen(game, game.screenManager.newCharacterScreen.charName,
				game.screenManager.newCharacterScreen.maleSelected);
		game.screenManager.setPlayScreen(game.screenManager.playScreen);
		game.setScreen(game.screenManager.playScreen);
		game.screenManager.currentState = State.PLAYING;
		Gdx.input.setInputProcessor(null);
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
		buttonSelected.getLabel().setFontScale(1.0f);
		mouseMoved();
		buttonSelected.getLabel().setFontScale(1.2f);
		stage.act();
		stage.draw();

		if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
			game.loadingMusic.pause();
			game.screenManager.menuScreen.menuMusic.stop();
			game.screenManager.setPlayScreen(new PlayScreen(game, game.screenManager.newCharacterScreen.charName,
					game.screenManager.newCharacterScreen.maleSelected));
			game.setScreen(game.screenManager.playScreen);
			game.screenManager.currentState = State.PLAYING;
		}
	}

	private void mouseMoved() {
		if (continueButton.isOver())
			buttonSelected = continueButton;
		else if (returnButton.isOver())
			buttonSelected = returnButton;
		else if (defaultAiButton.isOver())
			buttonSelected = defaultAiButton;
		else if (chooseAiButton.isOver())
			buttonSelected = chooseAiButton;
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
		MenuScreen.skin.dispose();
		MenuScreen.atlas.dispose();
	}

	@Override
	public void connected(Controller controller) {
	}

	@Override
	public void disconnected(Controller controller) {
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		if (buttonCode == 0) {
			if (buttonSelected == defaultAiButton)
				clickDefaultLevelButton();
			else if (buttonSelected == chooseAiButton)
				clickChooseLevelButton();
			else if (buttonSelected == returnButton)
				game.screenManager.swapScreen(State.NEWGAME);
			else if (buttonSelected == continueButton)
				clickContinueButton();
		} else if (buttonCode == 1)
			game.screenManager.swapScreen(State.NEWGAME);
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		return false;
	}

	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		if (value == PovDirection.north) {
			buttonSelected.getLabel().setFontScale(1.0f);
			if (buttonSelected == defaultAiButton)
				buttonSelected = returnButton;
			else if (buttonSelected == chooseAiButton)
				buttonSelected = defaultAiButton;
			else if (buttonSelected == returnButton) {
				if (!continueButton.isVisible())
					buttonSelected = chooseAiButton;
				else
					buttonSelected = continueButton;
			} else if (buttonSelected == continueButton)
				buttonSelected = defaultAiButton;

		} else if (value == PovDirection.south) {
			buttonSelected.getLabel().setFontScale(1.0f);
			if (buttonSelected == defaultAiButton) {
				if (!continueButton.isVisible())
					buttonSelected = chooseAiButton;
				else
					buttonSelected = continueButton;
			} else if (buttonSelected == chooseAiButton)
				buttonSelected = returnButton;
			else if (buttonSelected == returnButton)
				buttonSelected = defaultAiButton;
			else if(buttonSelected == continueButton)
				buttonSelected = returnButton;
		}
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
