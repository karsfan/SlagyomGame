package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import gameManager.GameSlagyom;
import gameManager.ScreenManager.State;
import world.GameConfig;

public class NewCharacterScreen implements Screen, ControllerListener {

	private GameSlagyom game;
	public Stage stage;
	private Viewport viewport;
	private OrthographicCamera camera;

	private Texture background;
	private Sprite backgroundSprite;

	public String charName;
	final TextField name;
	public boolean maleSelected;

	Drawable maleDraw = new TextureRegionDrawable(new TextureRegion(new Texture("res/male.png")));
	Drawable maleokDraw = new TextureRegionDrawable(new TextureRegion(new Texture("res/maleok.png")));

	Drawable femaleDraw = new TextureRegionDrawable(new TextureRegion(new Texture("res/female.png")));
	Drawable femaleokDraw = new TextureRegionDrawable(new TextureRegion(new Texture("res/femaleok.png")));

	ImageButton male = new ImageButton(new ImageButtonStyle(null, null, maleokDraw, maleDraw, null, maleokDraw));
	ImageButton female = new ImageButton(
			new ImageButtonStyle(null, null, femaleokDraw, femaleDraw, null, femaleokDraw));

	Table gender = new Table();
	public Table mainTable = new Table();
	TextButton continueButton;
	TextButton returnButton;
	TextButton buttonSelected;

	public NewCharacterScreen(final GameSlagyom game) {
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

		charName = "";
		maleSelected = true;
		// Create buttons

		name = new TextField("", MenuScreen.skin);
		name.setMessageText("Name");
		name.setFocusTraversal(true);

		male.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.loadingMusic.selectionSound.play(GameConfig.musicVolume);
				male.setChecked(true);
				female.setChecked(false);
				maleSelected = true;
			}
		});

		female.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.loadingMusic.selectionSound.play(GameConfig.musicVolume);
				female.setChecked(true);
				male.setChecked(false);
				maleSelected = false;
			}
		});

		continueButton = new TextButton("Continue", MenuScreen.skin);
		continueButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				clickContinueButton();
			}
		});

		returnButton = new TextButton("Return", MenuScreen.skin);
		returnButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.loadingMusic.selectionSound.play(GameConfig.musicVolume);
				game.screenManager.swapScreen(State.MENU);
			}
		});

		// Add buttons to table
		mainTable.add(name).pad(5).padTop(viewport.getWorldHeight() / 4);
		mainTable.row();
		male.setPosition(mainTable.getPadX(), mainTable.getPadY());
		gender.pad(15);
		gender.add(male);
		gender.add(female).padLeft(20);
		mainTable.add(gender);
		mainTable.row();
		mainTable.add(continueButton).pad(20).center();
		mainTable.row();
		mainTable.add(returnButton).pad(20).center();

		stage.addActor(mainTable);
		buttonSelected = continueButton;
		// Controllers.addListener(new MenuControllerListener(mainTable));
	}

	protected void clickContinueButton() {
		game.loadingMusic.selectionSound.play(GameConfig.musicVolume);
		charName = name.getText().toUpperCase();
		if (!charName.isEmpty())
			game.screenManager.swapScreen(gameManager.ScreenManager.State.WELCOME);
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
		buttonSelected.getLabel().setFontScale(1.0f);
		mouseMoved();
		buttonSelected.getLabel().setFontScale(1.2f);
		stage.act();
		stage.draw();

		if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
			charName = name.getText().toUpperCase();
			if (!charName.isEmpty())
				game.screenManager.swapScreen(gameManager.ScreenManager.State.WELCOME);
		}

	}

	private void mouseMoved() {
		if (continueButton.isOver()){
			if(buttonSelected != continueButton)
				game.loadingMusic.overMenuSound.play(GameConfig.musicVolume);
			buttonSelected = continueButton;
		}
		else if (returnButton.isOver())
		{
			if(buttonSelected != returnButton)
				game.loadingMusic.overMenuSound.play(GameConfig.musicVolume);
			buttonSelected = returnButton;
		}
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
	}

	@Override
	public void disconnected(Controller controller) {
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		if (buttonCode == 1)
			game.screenManager.swapScreen(State.MENU);
		else if (buttonCode == 0) {
			if (buttonSelected == continueButton)
				clickContinueButton();
			else if (buttonSelected == returnButton){
				game.screenManager.swapScreen(State.MENU);
				game.loadingMusic.selectionSound.play(GameConfig.musicVolume);
			}
		}
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		return false;
	}

	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		if (value == PovDirection.north) {
			game.loadingMusic.overMenuSound.play(GameConfig.musicVolume);
			buttonSelected.getLabel().setFontScale(1.0f);
			if (buttonSelected == continueButton)
				buttonSelected = returnButton;
			else if (buttonSelected == returnButton)
				buttonSelected = continueButton;
		} else if (value == PovDirection.south) {
			game.loadingMusic.overMenuSound.play(GameConfig.musicVolume);
			buttonSelected.getLabel().setFontScale(1.0f);
			if (buttonSelected == continueButton)
				buttonSelected = returnButton;
			else if (buttonSelected == returnButton)
				buttonSelected = continueButton;
		}
		return false;
	}

	@Override
	public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
		return false;
	}

	@Override
	public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
		return false;
	}

	@Override
	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
		return false;
	}

}