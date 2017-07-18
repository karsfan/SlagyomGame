package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import gameManager.GameSlagyom;

public class MenuScreen implements Screen, ControllerListener {
	GameSlagyom game;

	public Stage stage;
	private Viewport viewport;
	private OrthographicCamera camera;

	private Texture background;
	private Texture titleBackground = new Texture("res/title.png");
	private Sprite backgroundSprite;

	static TextureAtlas atlas;
	public static Skin skin;
	public Table mainTable;

	public TextButton playButton;
	public TextButton continueButton;
	public TextButton multiplayerButton;
	public TextButton optionsButton;
	public TextButton exitButton;
	public TextButton returnButton;
	public TextButton checkContinueGame;
	public TextField nameLoad;
	public Music menuMusic;
	TextButton buttonSelected;

	public MenuScreen(final GameSlagyom game) {
		this.game = game;
		atlas = new TextureAtlas("menu/vhs/vhs-ui.atlas");
		skin = new Skin(Gdx.files.internal("menu/vhs/vhs-ui.json"), atlas);
		menuMusic = Gdx.audio.newMusic(Gdx.files.internal("res/audio/mainMusic.mp3"));

		returnButton = new TextButton("Return", skin);

		camera = new OrthographicCamera();
		viewport = new ExtendViewport(854, 480, camera);
		viewport.apply();

		background = new Texture("res/background.png");
		backgroundSprite = new Sprite(background);

		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		camera.update();

		stage = new Stage(viewport, game.batch);
		Gdx.input.setInputProcessor(stage);

		mainTable = new Table();
		mainTable.setFillParent(true);
		mainTable.top();

		// Create buttons
		continueButton = new TextButton("Continue game", skin);
		multiplayerButton = new TextButton("Multiplayer", skin);
		optionsButton = new TextButton("Options", skin);
		exitButton = new TextButton("Exit", skin);
		playButton = new TextButton("New Game", skin);
		checkContinueGame = new TextButton("Continue", MenuScreen.skin);
		// Add listeners to buttons
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				clickPlayButton();
			}

		});
		continueButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				clickContinueButton();
			}
		});
		multiplayerButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				clickMultiplayerButton();
			}
		});

		optionsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				clickOptionButton();
			}
		});
		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});

		// Add buttons to table
		mainTable.add(playButton).pad(5).padTop(viewport.getWorldHeight() / 3 + 30);
		mainTable.row();
		mainTable.add(continueButton).pad(5);
		mainTable.row();
		mainTable.add(multiplayerButton).pad(5);
		mainTable.row();
		mainTable.add(optionsButton).pad(5);
		mainTable.row();
		mainTable.add(exitButton).pad(5);

		Image title = new Image(titleBackground);
		title.setPosition(0, 150); //310

		// Add table to stage
		stage.addActor(mainTable);
		
		stage.addActor(title);
		title.addAction(Actions.alpha(0f));
//		title.addAction(Actions.fadeIn(2f));
		title.addAction(Actions.sequence(Actions.fadeIn(1.5f), (Actions.moveTo(0, 310, 1f))));
//		title.addAction(Actions.moveTo(0, 310, 2f));
		mainTable.addAction(Actions.delay(2f, Actions.fadeIn(1.5f)));
		mainTable.addAction(Actions.alpha(0f));
//		stage.addAction(Actions.alpha(0f, 0f));
//		stage.addAction(Actions.fadeIn(2f));
//		mainTable.addAction(Actions.fadeIn(2f));

		
		buttonSelected = playButton;
	}

	protected void clickOptionButton() {
		game.screenManager.swapScreen(gameManager.ScreenManager.State.OPTIONMENU);
	}

	protected void clickMultiplayerButton() {
		game.screenManager.swapScreen(gameManager.ScreenManager.State.MULTIPLAYERMENU);

	}

	protected void clickContinueButton() {
		nameLoad = new TextField("", MenuScreen.skin);
		nameLoad.setMessageText("Saved game");
		nameLoad.setFocusTraversal(true);
		mainTable.clear();
		mainTable.add(playButton).pad(5).padTop(viewport.getWorldHeight() / 3);
		mainTable.row();
		mainTable.add(nameLoad).pad(5);
		mainTable.row();

		checkContinueGame.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				clickCheckContinueGame();
			}
		});
		continueButton.setVisible(false);
		mainTable.add(checkContinueGame).pad(5);
		mainTable.row();
		mainTable.add(multiplayerButton).pad(5);
		mainTable.row();
		mainTable.add(optionsButton).pad(5);
		mainTable.row();
		mainTable.add(exitButton).pad(5);
		menuMusic.stop();

	}

	protected void clickCheckContinueGame() {
		if (!nameLoad.getText().equals(""))
			game.loadGame(nameLoad.getText());
	}

	@Override
	public void show() {
	}

	public void clickPlayButton() {
		game.screenManager.swapScreen(gameManager.ScreenManager.State.NEWGAME);
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

//		game.batch.draw(titleBackground, 0, 310);
		game.batch.end();

		stage.act(delta);
		stage.draw();
		if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
			game.loadGame(nameLoad.getText());
		}

	}

	private void mouseMoved() {
		if (playButton.isOver())
			buttonSelected = playButton;
		else if (continueButton.isOver())
			buttonSelected = continueButton;
		else if (multiplayerButton.isOver())
			buttonSelected = multiplayerButton;
		else if (optionsButton.isOver())
			buttonSelected = optionsButton;
		else if (exitButton.isOver())
			buttonSelected = exitButton;
		else if (checkContinueGame.isOver())
			buttonSelected = checkContinueGame;
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().setScreenSize(width, height);
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
		skin.dispose();
		atlas.dispose();

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
		if (buttonCode == 0) {
			if (buttonSelected == playButton)
				clickPlayButton();
			else if (buttonSelected == continueButton)
				clickContinueButton();
			else if (buttonSelected == checkContinueGame)
				clickCheckContinueGame();
			else if (buttonSelected == multiplayerButton)
				clickMultiplayerButton();
			else if (buttonSelected == optionsButton)
				clickOptionButton();
			else if (buttonSelected == exitButton)
				Gdx.app.exit();
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
		// TODO Auto-generated method stub
		if (value == PovDirection.north) {
			buttonSelected.getLabel().setFontScale(1.0f);
			if (buttonSelected == playButton)
				buttonSelected = exitButton;
			else if (buttonSelected == continueButton)
				buttonSelected = playButton;
			else if (buttonSelected == checkContinueGame)
				buttonSelected = playButton;
			else if (buttonSelected == multiplayerButton) {
				if (continueButton.isVisible())
					buttonSelected = continueButton;
				else
					buttonSelected = checkContinueGame;
			} else if (buttonSelected == optionsButton)
				buttonSelected = multiplayerButton;
			else if (buttonSelected == exitButton)
				buttonSelected = optionsButton;
			return true;
		} else if (value == PovDirection.south) {
			buttonSelected.getLabel().setFontScale(1.0f);
			if (buttonSelected == playButton) {
				if (continueButton.isVisible())
					buttonSelected = continueButton;
				else
					buttonSelected = checkContinueGame;
			} else if (buttonSelected == continueButton)
				buttonSelected = multiplayerButton;
			else if (buttonSelected == checkContinueGame)
				buttonSelected = multiplayerButton;
			else if (buttonSelected == multiplayerButton)
				buttonSelected = optionsButton;
			else if (buttonSelected == optionsButton)
				buttonSelected = exitButton;
			else if (buttonSelected == exitButton)
				buttonSelected = playButton;
			return true;
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

}