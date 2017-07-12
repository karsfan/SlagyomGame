package screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import gameManager.GameSlagyom;
import gameManager.LoadingMusic;
import gameManager.ScreenManager.State;

public class InitializerScreen implements Screen {

	public GameSlagyom game;
	public Stage stage;
	private Viewport viewport;
	private OrthographicCamera camera;

	private Texture background;
	private Sprite backgroundSprite;
//	private BitmapFont bf_loadProgress;
//	private long progress = 0;
//	private long startTime = 0;
//	private ShapeRenderer mShapeRenderer;
	final Label name;
	final TextButton defaultLevelButton = new TextButton("Default level", MenuScreen.skin);
	final TextButton chooseLevelButton = new TextButton("Choose level...", MenuScreen.skin);
	final TextButton returnButton = new TextButton("Return", MenuScreen.skin);

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
		final Table mainTable = new Table();
		mainTable.setFillParent(true);
		mainTable.top();

		// Create buttons
		// Add listeners to buttons
		defaultLevelButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				LoadingMusic.pause();
				LoadingMusic.backgroundSound.loop(10.0f);
				game.screenManager.menuScreen.menuMusic.stop();
				game.screenManager.setPlayScreen(new PlayScreen(game, game.screenManager.newCharacterScreen.charName, game.screenManager.newCharacterScreen.maleSelected));
				game.setScreen(game.screenManager.playScreen);
				game.screenManager.currentState = State.PLAYING;
				//Gdx.input.setInputProcessor(null);
			}
		});

		chooseLevelButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				/*JFrame f = new JFrame();
				JFileChooser fc = new JFileChooser();
				String path = null;
				f.setVisible(true);
				f.toFront();
				f.setVisible(false);
				int res = fc.showSaveDialog(f);
				f.dispose();
				if (res == JFileChooser.APPROVE_OPTION) {
					path = (fc.getSelectedFile().getAbsolutePath());
				}*/
				final TextField nameAI = new TextField("", MenuScreen.skin);
				nameAI.setMessageText("Name");
				nameAI.setFocusTraversal(true);
				
				mainTable.clear();
				mainTable.add(name).pad(30);
				mainTable.row();
				mainTable.add(defaultLevelButton).pad(5).padTop(camera.viewportHeight / 2 - camera.viewportHeight / 3);
				mainTable.row();
				mainTable.add(nameAI);
				mainTable.row();
				TextButton a = new TextButton("Continue", MenuScreen.skin);
				a.addListener(new ClickListener(){
					public void clicked(InputEvent event, float x, float y) {
						game.screenManager.setPlayScreen(new PlayScreen(nameAI.getText(),game, game.screenManager.newCharacterScreen.charName, game.screenManager.newCharacterScreen.maleSelected));
						game.screenManager.swapScreen(gameManager.ScreenManager.State.PLAYING);
					}
				});
				mainTable.add(a).pad(5);
				mainTable.row();
				mainTable.add(returnButton).pad(5);
				mainTable.row();
				//game.screenManager.setPlayScreen(new PlayScreen(game, path, NewCharacterScreen.charName));
				//game.screenManager.swapScreen(it.slagyom.ScreenManager.State.PLAYING);
			}
		});

		returnButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.screenManager.swapScreen(State.NEWGAME);
			}
		});
		// Add buttons to table

		mainTable.add(name).pad(30);
		mainTable.row();
		mainTable.add(defaultLevelButton).pad(5).padTop(camera.viewportHeight / 5);
		mainTable.row();
		mainTable.add(chooseLevelButton).pad(5);
		mainTable.row();
		mainTable.add(returnButton).pad(5);
		mainTable.row();

		stage.addActor(mainTable);
//		bf_loadProgress = new BitmapFont();
//
//		mShapeRenderer = new ShapeRenderer();
//		startTime = TimeUtils.nanoTime();

	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batch.begin();
		//showLoadProgress();
		backgroundSprite.draw(game.batch);
		game.batch.end();

		stage.act();
		stage.draw();
		
		if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
			LoadingMusic.pause();
			LoadingMusic.backgroundSound.loop(10.0f);
			game.screenManager.menuScreen.menuMusic.stop();
			game.screenManager.setPlayScreen(new PlayScreen(game, game.screenManager.newCharacterScreen.charName, game.screenManager.newCharacterScreen.maleSelected));
			game.setScreen(game.screenManager.playScreen);
			game.screenManager.currentState = State.PLAYING;
		}
	}
//	private void showLoadProgress() {
//		long currentTimeStamp = TimeUtils.nanoTime();
//		if (currentTimeStamp - startTime > TimeUtils.millisToNanos(500)) {
//			startTime = currentTimeStamp;
//			progress = progress + 10;
//		}
//		// Width of progress bar on screen relevant to Screen width
//		float progressBarWidth = ((viewport.getWorldWidth()/2) / 100) * progress;
//
//		//game.batch.begin();
//		bf_loadProgress.draw(game.batch, "Loading " + progress + " / " + 100, 10, 40);
//		//game.batch.end();
//
//		mShapeRenderer.setProjectionMatrix(camera.combined);
//		mShapeRenderer.begin(ShapeType.Filled);
//		mShapeRenderer.setColor(Color.YELLOW);
//		mShapeRenderer.rect(0, 10, progressBarWidth, 10);
//		mShapeRenderer.end();
//
//	}

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
