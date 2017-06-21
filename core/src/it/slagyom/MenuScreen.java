package it.slagyom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.controllers.Controllers;
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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.levels.editor.Editor;
import it.slagyom.GameSlagyom.State;

public class MenuScreen implements Screen {
	GameSlagyom game;

	protected Stage stage;
	private Viewport viewport;
	private OrthographicCamera camera;

	private Texture background;
	private Sprite backgroundSprite;

	static TextureAtlas atlas;
	protected static Skin skin;
	public Table mainTable;

	public TextButton musicButton;
	public TextButton returnButton;
	Music menuMusic;

	public MenuScreen(final GameSlagyom game) {
		this.game = game;
		atlas = new TextureAtlas("menu/vhs/vhs-ui.atlas");
		skin = new Skin(Gdx.files.internal("menu/vhs/vhs-ui.json"), atlas);
		menuMusic = Gdx.audio.newMusic(Gdx.files.internal("res/audio/mainMusic.mp3"));
		// menuMusic.play();

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
		TextButton playButton = new TextButton("New Game", skin);
		TextButton continueButton = new TextButton("Continue game", skin);
		TextButton multiplayerButton = new TextButton("Multiplayer", skin);
		TextButton editorButton = new TextButton("Level editor", skin);
		TextButton optionsButton = new TextButton("Options", skin);
		TextButton exitButton = new TextButton("Exit", skin);

		// Add listeners to buttons
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.swapScreen(State.NEWGAME);
			}
		});
		continueButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.loadGame();
				menuMusic.stop();
				game.swapScreen(State.CONTINUEGAME);
				PlayScreen.hud.textTable.clear();
				PlayScreen.hud.textDialog = "Game loaded!";
			}
		});
		multiplayerButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.swapScreen(State.MULTIPLAYER);
			}
		});
		editorButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				new Editor();
			}
		});
		optionsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.swapScreen(State.OPTIONMENU);

			}
		});
		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});

		// Add buttons to table
		mainTable.add(playButton).pad(5).padTop(Gdx.graphics.getHeight() / 2 - Gdx.graphics.getHeight() / 5);
		mainTable.row();
		mainTable.add(continueButton).pad(5);
		mainTable.row();
		mainTable.add(multiplayerButton).pad(5);
		mainTable.row();
		mainTable.add(editorButton).pad(5);
		mainTable.row();
		mainTable.add(optionsButton).pad(5);
		mainTable.row();
		mainTable.add(exitButton).pad(5);

		// Add table to stage
		stage.addActor(mainTable);
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