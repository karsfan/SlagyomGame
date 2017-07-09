
package screens;

import java.net.Socket;


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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import gameManager.GameSlagyom;
import multiplayer.NetworkPlayScreen;
import multiplayer.Server;
import multiplayer.ServerThread;

public class MultiplayerScreen implements Screen {

	private Socket socket; 
	
	private GameSlagyom game;
	public Stage stage;
	private Viewport viewport;
	private OrthographicCamera camera;

	private Texture background;
	private Sprite backgroundSprite;

	public static String multiplayerCharName;
	public static String multiplayerAddress;
	public static int multiplayerPort;
	Server server;
	public MultiplayerScreen(final GameSlagyom game) {
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

		// Create buttons
		final TextField name = new TextField("", MenuScreen.skin);
		name.setMessageText("Name");
		name.setFocusTraversal(true);

		final TextField address = new TextField("", MenuScreen.skin);
		address.setMessageText("Address");
		address.setFocusTraversal(true);

		final TextField port = new TextField("", MenuScreen.skin);
		port.setMessageText("Port");
		port.setFocusTraversal(true);

		TextButton startServerButton = new TextButton("Start server", MenuScreen.skin);
		TextButton playButton = new TextButton("Play", MenuScreen.skin);
		TextButton returnButton = new TextButton("Return", MenuScreen.skin);
		// Add listeners to buttons
		startServerButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				multiplayerAddress = address.getText();
				multiplayerPort = Integer.parseInt(port.getText());
				server = new Server(multiplayerPort, 3);
				new ServerThread(server);
				game.setScreen(new NetworkPlayScreen(game, name.getText()));
			}
		});
		
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// game.swapScreen(State.WELCOME);
				multiplayerCharName = name.getText();
				multiplayerAddress = address.getText();
				multiplayerPort = Integer.parseInt(port.getText());
				System.out.println("clic");
				game.setScreen(new NetworkPlayScreen(game, name.getText()));
			}
		});
		
		returnButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.screenManager.swapScreen(gameManager.ScreenManager.State.MENU);
			}
		});
		
		// Add buttons to table
		mainTable.add(name).pad(5).padTop(Gdx.graphics.getHeight() / 2 - Gdx.graphics.getHeight() / 5);
		mainTable.row();
		mainTable.add(address).pad(5);
		mainTable.row();
		mainTable.add(port).pad(5);
		mainTable.row();
		mainTable.add(startServerButton).pad(5);
		mainTable.row();
		mainTable.add(playButton).pad(5);
		mainTable.row();
		mainTable.add(returnButton).pad(35);
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