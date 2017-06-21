package it.slagyom;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import it.slagyom.GameSlagyom.State;

public class InitializerScreen implements Screen {

	private GameSlagyom game;
	protected Stage stage;
	private Viewport viewport;
	private OrthographicCamera camera;

	private Texture background;
	private Sprite backgroundSprite;

	public InitializerScreen(final GameSlagyom game) {
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
		Table mainTable = new Table();
		mainTable.setFillParent(true);
		mainTable.top();

		Label name = new Label("Welcome " + NewCharacterScreen.charName, MenuScreen.skin);
		// Create buttons
		TextButton defaultLevelButton = new TextButton("Default level", MenuScreen.skin);
		TextButton chooseLevelButton = new TextButton("Choose level...", MenuScreen.skin);
		TextButton returnButton = new TextButton("Return", MenuScreen.skin);
		// Add listeners to buttons
		defaultLevelButton.addListener(new ClickListener() {
			@SuppressWarnings("static-access")
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.playScreen = new PlayScreen(game, NewCharacterScreen.charName);
				game.swapScreen(State.PLAYING);
			}
		});

		chooseLevelButton.addListener(new ClickListener() {
			@SuppressWarnings("static-access")
			@Override
			public void clicked(InputEvent event, float x, float y) {
				JFrame f = new JFrame();
				JFileChooser fc = new JFileChooser();
				String path = null;
				f.setVisible(true);
				f.toFront();
				f.setVisible(false);
				int res = fc.showSaveDialog(f);
				f.dispose();
				if (res == JFileChooser.APPROVE_OPTION) {
					path = (fc.getSelectedFile().getAbsolutePath());
				}
				game.playScreen = new PlayScreen(game, path, NewCharacterScreen.charName);
				game.swapScreen(State.PLAYING);
			}
		});

		returnButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.swapScreen(State.NEWGAME);
			}
		});
		// Add buttons to table

		mainTable.add(name).pad(30);
		mainTable.row();
		mainTable.add(defaultLevelButton).pad(5).padTop(Gdx.graphics.getHeight() / 2 - Gdx.graphics.getHeight() / 3);
		mainTable.row();
		mainTable.add(chooseLevelButton).pad(5);
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
		// viewport.update(width, height);
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

}
