
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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import gameManager.GameSlagyom;
import gameManager.ScreenManager.State;
import multiplayer.NetworkPlayScreen;
import multiplayer.Server;
import multiplayer.ServerThread;

public class MultiplayerScreen implements Screen, ControllerListener {

	private GameSlagyom game;
	public Stage stage;
	private Viewport viewport;
	private OrthographicCamera camera;

	private Texture background;
	private Sprite backgroundSprite;

	public static String multiplayerCharName;
	public static String multiplayerAddress;
	public static int multiplayerPort;
	TextButton startServerButton;
	TextButton playButton;
	TextButton returnButton;
	TextButton buttonSelected;
	Server server;
	public Table mainTable = new Table();
	TextField name;
	TextField address;
	TextField port;

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
		mainTable.setFillParent(true);
		mainTable.top();

		// Create buttons
		name = new TextField("", MenuScreen.skin);
		name.setMessageText("Name");
		name.setFocusTraversal(true);

		address = new TextField("", MenuScreen.skin);
		address.setMessageText("Address");
		address.setFocusTraversal(true);

		port = new TextField("", MenuScreen.skin);
		port.setMessageText("Port");
		port.setFocusTraversal(true);

		startServerButton = new TextButton("Start server", MenuScreen.skin);
		playButton = new TextButton("Play", MenuScreen.skin);
		returnButton = new TextButton("Return", MenuScreen.skin);
		// Add listeners to buttons
		startServerButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				clickServerButton();
			}
		});

		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				clickPlayButton();
			}
		});

		returnButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.screenManager.swapScreen(gameManager.ScreenManager.State.MENU);
			}
		});

		// Add buttons to table
		mainTable.add(name).pad(5).padTop(viewport.getWorldHeight() / 2 - viewport.getWorldHeight() / 5);
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
		buttonSelected = startServerButton;
	}

	protected void clickPlayButton() {
		if (!name.getText().isEmpty() && !port.getText().isEmpty()) {
			multiplayerCharName = name.getText();
			multiplayerAddress = address.getText();
			multiplayerPort = Integer.parseInt(port.getText());
			game.screenManager.playScreen = new NetworkPlayScreen(game, name.getText());
			game.screenManager.swapScreen(State.PLAYING);
		}
	}

	protected void clickServerButton() {
		if (!port.getText().isEmpty() && !name.getText().isEmpty()) {
			multiplayerAddress = address.getText();
			multiplayerPort = Integer.parseInt(port.getText());
			server = new Server(multiplayerPort, 3);
			new ServerThread(server);
			game.screenManager.playScreen = new NetworkPlayScreen(game, name.getText());
			game.screenManager.swapScreen(State.PLAYING);
		}
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
	private void mouseMoved() {
		if (startServerButton.isOver())
			buttonSelected = startServerButton;
		else if (playButton.isOver())
			buttonSelected = playButton;
		else if (returnButton.isOver())
			buttonSelected = returnButton;
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
		if(buttonCode == 1)
			game.screenManager.swapScreen(gameManager.ScreenManager.State.MENU);
		else if(buttonCode == 0){
			if(buttonSelected == startServerButton)
				clickServerButton();
			else if(buttonSelected == playButton)
				clickPlayButton();
			else if(buttonSelected == returnButton)
				game.screenManager.swapScreen(gameManager.ScreenManager.State.MENU);
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
		if(value == PovDirection.north){
			buttonSelected.getLabel().setFontScale(1.0f);
			if(buttonSelected == startServerButton)
				buttonSelected = returnButton;
			else if(buttonSelected == playButton)
				buttonSelected = startServerButton;
			else if(buttonSelected == returnButton)
				buttonSelected = playButton;
		}
		else if(value == PovDirection.south){
			buttonSelected.getLabel().setFontScale(1.0f);
			if(buttonSelected == startServerButton)
				buttonSelected = playButton;
			else if(buttonSelected == playButton)
				buttonSelected = returnButton;
			else if(buttonSelected == returnButton)
				buttonSelected = startServerButton;
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