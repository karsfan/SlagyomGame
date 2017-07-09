package screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import gameManager.GameSlagyom;
import gameManager.ScreenManager;
import gameManager.ScreenManager.State;


public class MenuScreen implements Screen {
	GameSlagyom game;

	public Stage stage;
	private Viewport viewport;
	private OrthographicCamera camera;

	private Texture background;
	private Sprite backgroundSprite;

	static TextureAtlas atlas;
	public static Skin skin;
	public Table mainTable;

	public TextButton musicButton;
	public TextButton returnButton;
	public Music menuMusic;

	public MenuScreen(final GameSlagyom game) {
		this.game = game;
		atlas = new TextureAtlas("menu/vhs/vhs-ui.atlas");
		skin = new Skin(Gdx.files.internal("menu/vhs/vhs-ui.json"), atlas);
		menuMusic = Gdx.audio.newMusic(Gdx.files.internal("res/audio/mainMusic.mp3"));

		musicButton = new TextButton("Music", skin);
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
		final TextButton playButton = new TextButton("New Game", skin);
		final TextButton continueButton = new TextButton("Continue game", skin);
		final TextButton multiplayerButton = new TextButton("Multiplayer", skin);
		final TextButton optionsButton = new TextButton("Options", skin);
		final TextButton exitButton = new TextButton("Exit", skin);

		// Add listeners to buttons
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.screenManager.swapScreen(gameManager.ScreenManager.State.NEWGAME);
			}
		});
		continueButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
			
				final TextField nameLoad = new TextField("", MenuScreen.skin);
				nameLoad.setMessageText("Saved game");
				nameLoad.setFocusTraversal(true);
				mainTable.clear();
				mainTable.add(playButton).pad(5).padTop(viewport.getWorldHeight() / 3);
				mainTable.row();
				mainTable.add(nameLoad).pad(5);
				mainTable.row();
				TextButton cont = new TextButton("Continue", MenuScreen.skin);
				cont.addListener(new ClickListener(){
					public void clicked(InputEvent event, float x, float y) {
						game.loadGame(nameLoad.getText());
						
					}
				});
				mainTable.add(cont).pad(5);
				mainTable.row();
				mainTable.add(multiplayerButton).pad(5);
				mainTable.row();
				mainTable.add(optionsButton).pad(5);
				mainTable.row();
				mainTable.add(exitButton).pad(5);
				//game.loadGame(path);
				menuMusic.stop();
				//game.screenManager.swapScreen(State.PLAYING);
				//PlayScreen.hud.textTable.clear();
				//PlayScreen.hud.textDialog = "Game loaded!";
			}
		});
		multiplayerButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.screenManager.swapScreen(gameManager.ScreenManager.State.MULTIPLAYER);
			}
		});
		
		optionsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.screenManager.swapScreen(gameManager.ScreenManager.State.OPTIONMENU);

			}
		});
		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});

		// Add buttons to table
		mainTable.add(playButton).pad(5).padTop(viewport.getWorldHeight() / 3);
		mainTable.row();
		mainTable.add(continueButton).pad(5);
		mainTable.row();
		mainTable.add(multiplayerButton).pad(5);
		mainTable.row();
		mainTable.add(optionsButton).pad(5);
		mainTable.row();
		mainTable.add(exitButton).pad(5);

		// Add table to stage
		stage.addActor(mainTable);
		//Controllers.addListener(new MenuControllerListener(mainTable));
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

		stage.act(delta);
		stage.draw();

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

}