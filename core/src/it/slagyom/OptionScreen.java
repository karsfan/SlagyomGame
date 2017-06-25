package it.slagyom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import it.slagyom.src.World.Game;
import it.slagyom.src.World.GameConfig;

public class OptionScreen implements Screen {

	private GameSlagyom game;
	protected Stage stage;
	private Viewport viewport;
	private OrthographicCamera camera;
	public static boolean fromPause;

	private Texture background;
	private Sprite backgroundSprite;

	public boolean activeMusic;

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
		fromPause = false;
		// Create Table
		Table mainTable = new Table();
		mainTable.setFillParent(true);
		mainTable.top();

		// Create buttons
		final TextButton musicButton = new TextButton("Music ON", MenuScreen.skin);
		final TextButton fullscreenButton = new TextButton("Fullscreen OFF", MenuScreen.skin);

		TextButton returnButton = new TextButton("Return", MenuScreen.skin);

		// Add listeners to buttons
		musicButton.addListener(new ClickListener() {
			@SuppressWarnings("static-access")
			@Override
			public void clicked(InputEvent event, float x, float y) {
				activeMusic = !activeMusic;
				if (!activeMusic) {
					game.screenManager.menuScreen.menuMusic.pause();
					musicButton.setText("Music OFF");
				} else {
					game.screenManager.menuScreen.menuMusic.play();
					musicButton.setText("Music ON");
				}
			}
		});

		fullscreenButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				GameConfig.fullscreen = !GameConfig.fullscreen;
				if (!GameConfig.fullscreen) {
					Gdx.graphics.setWindowedMode(854, 480);
					fullscreenButton.setText("Fullscreen OFF");
				} else {
					Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
					fullscreenButton.setText("Fullscreen ON");
				}
			}
		});

		returnButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (fromPause) {
					Game.world.semaphore.release();					
					game.screenManager.swapScreen(it.slagyom.ScreenManager.State.PLAYING);
				}
				else
					game.screenManager.swapScreen(it.slagyom.ScreenManager.State.MENU);
			}
		});
		// Add buttons to table
		mainTable.add(musicButton).pad(5).padTop(Gdx.graphics.getHeight() / 2 - Gdx.graphics.getHeight() / 5);
		mainTable.row();
		mainTable.add(fullscreenButton).pad(5);
		mainTable.row();
		mainTable.add(returnButton).pad(5);
		mainTable.row();

		stage.addActor(mainTable);
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
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().setScreenSize(width, height);
		//viewport.update(width, height);
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

}