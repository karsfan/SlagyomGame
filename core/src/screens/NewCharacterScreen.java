package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import gameManager.GameSlagyom;
import gameManager.MenuControllerListener;
import gameManager.ScreenManager.State;

public class NewCharacterScreen implements Screen {

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
	// Drawable up, Drawable down, Drawable checked, Drawable imageUp, Drawable
	// imageDown,
	// Drawable imageChecked
	ImageButton male = new ImageButton(new ImageButtonStyle(null, null, maleokDraw, maleDraw, null, maleokDraw));
	ImageButton female = new ImageButton(
			new ImageButtonStyle(null, null, femaleokDraw, femaleDraw, null, femaleokDraw));

	Table gender = new Table();

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
		Table mainTable = new Table();
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
				System.out.println("MALE");
				male.setChecked(true);
				female.setChecked(false);
				maleSelected = true;
			}
		});

		female.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("FEMALE");
				female.setChecked(true);
				male.setChecked(false);
				maleSelected = false;
			}
		});

		TextButton continueButton = new TextButton("Continue", MenuScreen.skin);
		continueButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				charName = name.getText().toUpperCase();
				if (!charName.isEmpty())
					game.screenManager.swapScreen(gameManager.ScreenManager.State.WELCOME);
			}
		});

		TextButton returnButton = new TextButton("Return", MenuScreen.skin);
		returnButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
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

		stage.act();
		stage.draw();

		if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
			charName = name.getText().toUpperCase();
			if (!charName.isEmpty())
				game.screenManager.swapScreen(gameManager.ScreenManager.State.WELCOME);
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

}