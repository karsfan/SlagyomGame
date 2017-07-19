package screens;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import gameManager.GameSlagyom;
import world.GameConfig;

public class OptionScreen implements Screen, ControllerListener {

	private GameSlagyom game;
	public Stage stage;
	private Viewport viewport;
	private OrthographicCamera camera;

	private Texture background;
	private Sprite backgroundSprite;

	public boolean activeMusic;
	public Table mainTable = new Table();
	public TextButton buttonSelected;
	TextButton musicButton;
	TextButton fullscreenButton;

	TextButton returnButton;

	public OptionScreen(final GameSlagyom game) {
		this.game = game;

		camera = new OrthographicCamera();
		viewport = new ExtendViewport(854, 480, camera);
		viewport.apply();

		activeMusic = true;
		background = new Texture("res/background.png");
		backgroundSprite = new Sprite(background);

		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		camera.update();

		stage = new Stage(viewport, game.batch);
		// Create Table
		mainTable.setFillParent(true);
		mainTable.top();

		// Create buttons
		musicButton = new TextButton("Music ON", MenuScreen.skin);
		fullscreenButton = new TextButton("Fullscreen OFF", MenuScreen.skin);

		 returnButton = new TextButton("Return", MenuScreen.skin);

		// Add listeners to buttons
		musicButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				clickMusicButton();
			}
		});

		fullscreenButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				clickFullScreenButton();
			}
		});

		returnButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				clickReturnButton();
			}
		});
		// Add buttons to table
		mainTable.add(musicButton).pad(5).padTop(viewport.getWorldHeight() / 2 - viewport.getWorldHeight() / 5);
		mainTable.row();
		mainTable.add(fullscreenButton).pad(5);
		mainTable.row();
		mainTable.add(returnButton).pad(5);
		mainTable.row();

		stage.addActor(mainTable);
		buttonSelected = musicButton;
	}

	protected void clickMusicButton() {
		activeMusic = !activeMusic;
		if (!activeMusic) {
			GameConfig.musicVolume = 0;
			GameConfig.soundVolume = 0;
			musicButton.setText("Music OFF");
		} else {
			GameConfig.musicVolume = 1.0f;
			GameConfig.soundVolume = 1.2f;
			musicButton.setText("Music ON");
		}
	}

	protected void clickFullScreenButton() {
		GameConfig.fullscreen = !GameConfig.fullscreen;
		if (!GameConfig.fullscreen) {
			Gdx.graphics.setWindowedMode(854, 480);
			fullscreenButton.setText("Fullscreen OFF");
		} else {
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
			fullscreenButton.setText("Fullscreen ON");
		}
	}

	private void mouseMoved() {
		if (musicButton.isOver())
			buttonSelected = musicButton;
		else if (fullscreenButton.isOver())
			buttonSelected = fullscreenButton;
		else if (returnButton.isOver())
			buttonSelected = returnButton;
	}

	protected void clickReturnButton() {
		if (game.screenManager.playScreen != null)
			game.screenManager.swapScreen(gameManager.ScreenManager.State.PLAYING);
		else
			game.screenManager.swapScreen(gameManager.ScreenManager.State.MENU);
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
			if (buttonSelected == musicButton)
				clickMusicButton();
			else if (buttonSelected == fullscreenButton)
				clickFullScreenButton();
			else if (buttonSelected == returnButton)
				clickReturnButton();
		}
		else if(buttonCode == 1)
			clickReturnButton();
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		if (value == PovDirection.south) {
			buttonSelected.getLabel().setFontScale(1.0f);
			if (buttonSelected == musicButton)
				buttonSelected = fullscreenButton;
			else if (buttonSelected == fullscreenButton)
				buttonSelected = returnButton;
			else if (buttonSelected == returnButton)
				buttonSelected = musicButton;
		} else if (value == PovDirection.north) {
			buttonSelected.getLabel().setFontScale(1.0f);
			if (buttonSelected == musicButton)
				buttonSelected = returnButton;
			else if (buttonSelected == fullscreenButton)
				buttonSelected = musicButton;
			else if (buttonSelected == returnButton)
				buttonSelected = fullscreenButton;
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