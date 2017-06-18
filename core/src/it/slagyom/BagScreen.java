package it.slagyom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import it.slagyom.src.Map.Item;
import it.slagyom.src.Map.Item.Level;
import it.slagyom.src.Map.StaticObject.Element;
import it.slagyom.src.World.Game;

public class BagScreen implements Screen {
	private enum Pocket {
		POTIONS, WEAPONS, PARCHMENTS
	}
	
	public Pocket currentPocket;

	private GameSlagyom game;
	protected Stage stage;
	private Viewport viewport;
	private OrthographicCamera camera;

	private Texture background;
	private Sprite backgroundSprite;

	private Texture selectionBackground;
	private Sprite selectionBackgroundSprite;
	boolean selection;

	private Table weaponsTable;
	private Table potionsTable;
	private Table parchmentsTable;

	private Table optionsTable;
	private TextButton use;
	private TextButton delete;
	private TextButton exit;
	public Item itemSelected;
	TextButton[] potions;

	public BagScreen(final GameSlagyom game) {
		this.game = game;
		itemSelected = new Item();
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(854, 480, camera);
		selection = false;
		viewport.apply();
		background = new Texture("res/bag/bagBackground.png");
		backgroundSprite = new Sprite(background);

		selectionBackground = new Texture("res/bag/bagSelectionBG.png");
		selectionBackgroundSprite = new Sprite(selectionBackground);

		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		camera.update();

		stage = new Stage(viewport, game.batch);

		currentPocket = Pocket.POTIONS;

		// OPTIONS TABLE
		optionsTable = new Table();
		optionsTable.setLayoutEnabled(false);
		optionsTable.setFillParent(true);
		optionsTable.top();
		use = new TextButton("Use", MenuScreen.skin);
		delete = new TextButton("Delete", MenuScreen.skin);
		exit = new TextButton("Return", MenuScreen.skin);

		delete.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Game.character.bag.removeItem(itemSelected.getElement(), itemSelected.getLevel());
				potions[0].setText("Blue potion  x" + Game.character.bag.getNumberOf(Element.POTION, Level.FIRST));
				potions[1].setText("Green potion  x" + Game.character.bag.getNumberOf(Element.POTION, Level.SECOND));
				potions[2].setText("Red potion  x" + Game.character.bag.getNumberOf(Element.POTION, Level.THIRD));
			}
		});

		exit.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.emptyBagIcon);
				hideInfo();
			}
		});

		LoadingImage.emptyBagIcon.setPosition(141, 43);
		LoadingImage.rightArrow.setPosition(283, 254);
		LoadingImage.leftArrow.setPosition(115, 254);
		LoadingImage.emptyBagIcon.setVisible(true);

		use.setPosition(573, 110);
		use.setVisible(false);
		delete.setPosition(573, 70);
		delete.setVisible(false);
		exit.setPosition(573, 30);
		exit.setVisible(false);

		optionsTable.add(LoadingImage.emptyBagIcon);
		optionsTable.add(LoadingImage.rightArrow);
		optionsTable.add(LoadingImage.leftArrow);
		optionsTable.add(LoadingImage.emptyBagIcon);

		optionsTable.add(use);
		optionsTable.add(delete);
		optionsTable.add(exit);

		LoadingImage.rightArrow.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (currentPocket == Pocket.POTIONS)
					currentPocket = Pocket.WEAPONS;
				else if (currentPocket == Pocket.WEAPONS)
					currentPocket = Pocket.PARCHMENTS;
				else if (currentPocket == Pocket.PARCHMENTS)
					currentPocket = Pocket.POTIONS;
			}
		});

		LoadingImage.leftArrow.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (currentPocket == Pocket.POTIONS)
					currentPocket = Pocket.PARCHMENTS;
				else if (currentPocket == Pocket.WEAPONS)
					currentPocket = Pocket.POTIONS;
				else if (currentPocket == Pocket.PARCHMENTS)
					currentPocket = Pocket.WEAPONS;
			}
		});

		// END OPTIONS TABLE

		// POTIONS TABLE
		potionsTable = new Table();
		Label potionsLabel;

		potionsTable.setLayoutEnabled(false);

		potionsLabel = new Label("Potions", MenuScreen.skin);
		potions = new TextButton[3];
		potions[0] = new TextButton("Blue potion    x" + Game.character.bag.getNumberOf(Element.POTION, Level.FIRST),
				MenuScreen.skin);
		potions[1] = new TextButton("Green potion    x" + Game.character.bag.getNumberOf(Element.POTION, Level.SECOND),
				MenuScreen.skin);
		potions[2] = new TextButton("Red potion  x" + Game.character.bag.getNumberOf(Element.POTION, Level.THIRD),
				MenuScreen.skin);

		potions[0].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.bluePotion);
				itemSelected.setElement(Element.POTION);
				itemSelected.setLevel(Level.FIRST);
			}
		});

		potions[1].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.greenPotion);
				itemSelected.setElement(Element.POTION);
				itemSelected.setLevel(Level.SECOND);
			}
		});

		potions[2].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.redPotion);
				itemSelected.setElement(Element.POTION);
				itemSelected.setLevel(Level.THIRD);
			}
		});

		potionsLabel.setPosition(149, 425);
		potionsTable.add(potionsLabel);

		potions[0].setPosition(350, 420);
		potionsTable.add(potions[0]);

		potions[1].setPosition(350, 370);
		potionsTable.add(potions[1]);

		potions[2].setPosition(350, 320);
		potionsTable.add(potions[2]);
		// END POTIONS TABLE

		// WEAPON TABLE
		weaponsTable = new Table();
		Label weaponsLabel;
		TextButton[] weapons;

		weaponsTable.setVisible(false);
		weaponsTable.setLayoutEnabled(false);

		weaponsLabel = new Label("Weapons", MenuScreen.skin);
		weapons = new TextButton[3];

		weapons[0] = new TextButton(Game.character.primary_weapon.getType().toString() + " "
				+ Game.character.primary_weapon.getLevel().toString(), MenuScreen.skin);
		weapons[1] = new TextButton("Sword  lev", MenuScreen.skin);
		weapons[2] = new TextButton("Bow  lev", MenuScreen.skin);

		weapons[0].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.spear);
			}
		});

		weapons[1].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.sword);
			}
		});

		weapons[2].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.spear);
			}
		});

		weaponsLabel.setPosition(140, 425);
		weaponsTable.add(weaponsLabel);

		weapons[0].setPosition(350, 420);
		weaponsTable.add(weapons[0]);

		weapons[1].setPosition(350, 370);
		weaponsTable.add(weapons[1]);

		weapons[2].setPosition(350, 320);
		weaponsTable.add(weapons[2]);
		// END WEAPONS TABLE

		// PARCHMENTS TABLE
		parchmentsTable = new Table();
		Label parchmentsLabel;
		TextButton[] parchments;

		parchmentsTable.setVisible(false);
		parchmentsTable.setLayoutEnabled(false);

		parchmentsLabel = new Label("Parchments", MenuScreen.skin);
		parchments = new TextButton[2];
		parchments[0] = new TextButton("Parchment1", MenuScreen.skin);
		parchments[1] = new TextButton("Parchment2", MenuScreen.skin);

		parchments[0].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.bluePotion);
			}
		});

		parchments[1].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.sword);
			}
		});

		parchmentsLabel.setPosition(120, 425);
		parchmentsTable.add(parchmentsLabel);

		parchments[0].setPosition(350, 420);
		parchmentsTable.add(parchments[0]);

		parchments[1].setPosition(350, 370);
		parchmentsTable.add(parchments[1]);

		// END PARCHMENTS TABLE

		stage.addActor(potionsTable);
		stage.addActor(weaponsTable);
		stage.addActor(parchmentsTable);
		stage.addActor(optionsTable);
		
		Controllers.addListener(new MenuControllerListener(potionsTable));
		Controllers.addListener(new MenuControllerListener(weaponsTable));
		Controllers.addListener(new MenuControllerListener(parchmentsTable));
		Controllers.addListener(new MenuControllerListener(optionsTable));
		
		
	}

	private void showInfo(ImageButton icon) {
		icon.setPosition(149, 43);
		// optionsTable.add(LoadingImage.emptyBagIcon);
		optionsTable.removeActor(icon);
		optionsTable.add(icon);
		LoadingImage.emptyBagIcon.setVisible(false);

		selection = true;
		use.setVisible(true);
		delete.setVisible(true);
		exit.setVisible(true);
	}

	private void hideInfo() {
		// optionsTable.removeActor(LoadingImage.emptyBagIcon);
		// optionsTable.add(LoadingImage.emptyBagIcon);
		LoadingImage.emptyBagIcon.setVisible(true);

		selection = false;
		use.setVisible(false);
		delete.setVisible(false);
		exit.setVisible(false);
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.begin();
		if (selection)
			selectionBackgroundSprite.draw(game.batch);
		else
			backgroundSprite.draw(game.batch);

		game.batch.end();

		stage.act();
		stage.draw();

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			game.swapScreen(GameSlagyom.State.PLAYING);
			Game.world.semaphore.release();
		}

		if (currentPocket == Pocket.POTIONS) {
			potionsTable.setVisible(true);
			weaponsTable.setVisible(false);
			parchmentsTable.setVisible(false);
		} else if (currentPocket == Pocket.WEAPONS) {
			weaponsTable.setVisible(true);
			potionsTable.setVisible(false);
			parchmentsTable.setVisible(false);
		} else if (currentPocket == Pocket.PARCHMENTS) {
			parchmentsTable.setVisible(true);
			potionsTable.setVisible(false);
			weaponsTable.setVisible(false);
		}

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
