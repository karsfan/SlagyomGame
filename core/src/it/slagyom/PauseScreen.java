package it.slagyom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import it.slagyom.GameSlagyom.State;
import it.slagyom.src.World.Game;

public class PauseScreen implements Screen {

	private GameSlagyom game;
	protected Stage stage;
	private Viewport viewport;
	private OrthographicCamera camera;

	private Texture background;
	private Sprite backgroundSprite;
	
	public PauseScreen(final GameSlagyom game) {
		
		this.game = game;
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(500, 500, camera);
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
		Label pauseLabel = new Label("PAUSE", MenuScreen.skin);
		// Create buttons
		TextButton bagButton = new TextButton("Bag", MenuScreen.skin);
		TextButton returnButton = new TextButton("Return", MenuScreen.skin);
		TextButton saveGame = new TextButton("Save game", MenuScreen.skin);
		TextButton loadGame = new TextButton("Load game", MenuScreen.skin);
		TextButton exitButton = new TextButton("Exit", MenuScreen.skin);
		TextButton menuButton = new TextButton("Menu", MenuScreen.skin);
		
		
		//final Drawable noDialog = null;

		// Add listeners to buttons
		saveGame.addListener(new ClickListener() {
			@SuppressWarnings("static-access")
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//game.setScreen(game.playScreen);
				game.saveGame();
				game.swapScreen(State.PLAYING);
				Game.world.semaphore.release();
				PlayScreen.hud.setDialogText("Game saved!");
			}
		});

		loadGame.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//System.out.println("CARICATO");
				game.loadGame();
				game.swapScreen(State.CONTINUEGAME);
				PlayScreen.hud.setDialogText("Game loaded!");

			}
		});

		returnButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.swapScreen(State.PLAYING);
				Game.world.semaphore.release();
			}
		});
		menuButton.addListener(new ClickListener() {
			@SuppressWarnings({ "deprecation", "static-access" })
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Game.world.getThread().stop();
				game.playScreen.dispose();
				game.swapScreen(State.MENU);
			}
		});
		
		bagButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.swapScreen(State.BAG);
			//	game.swapScreen(State.SHOP);

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
		
		mainTable.add(loadGame).pad(5);
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
		
		
		helpTable.add(arrow);//.padLeft(Gdx.graphics.getWidth()/2); 
		helpTable.add(run);
		helpTable.add(action);

		
		
		stage.addActor(helpTable);
	}

	@Override
	public void show() {

	}


	@Override
	public void render(float delta) {
		//System.out.println(Game.world.semaphore.getQueueLength()+"pausa");
		Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batch.begin();
		backgroundSprite.draw(game.batch);
		game.batch.end();

		stage.act();
		stage.draw();
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			Game.world.semaphore.release();
			game.swapScreen(State.PLAYING);
		}
		

	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
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