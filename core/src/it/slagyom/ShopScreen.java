package it.slagyom;

import com.badlogic.gdx.Screen;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import hud.BattleHud;
import it.slagyom.ScreenManager.State;
import it.slagyom.src.World.Game;
import staticObjects.Item;
import staticObjects.Item.Level;
import staticObjects.StaticObject.Element;

public class ShopScreen implements Screen {
	private enum Category {
		POTIONS, WEAPONS, PARCHMENTS
	}

	public Category currentCategory;

	private GameSlagyom game;
	protected Stage stage;
	private Viewport viewport;
	private OrthographicCamera camera;

	private Sprite backgroundSprite;
	private Sprite selectionBackgroundSprite;
	private Sprite buyBackgroundSprite;

	private Table weaponsTable;
	private Table potionsTable;
	private Table parchmentsTable;
	private Table buyingTable;
	private Table optionsTable;

	private TextButton selectButton;
	private TextButton returnButton;
	private Label coins;

	boolean selection;
	boolean buying;


	// Variables for cash-scaling animation
	int refreshedCoins;
	float coinsTimer = 0;
	boolean scaling = false;

	public Item itemSelected;

	public ShopScreen(final GameSlagyom game) {
		this.game = game;
		// SCREEN INITIALIZING
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(854, 480, camera);
		viewport.apply();
		stage = new Stage(viewport, game.batch);

		currentCategory = Category.POTIONS;
		selection = false;
		buying = false;
		itemSelected = new Item();

		backgroundSprite = new Sprite(new Texture("res/shop/shopBackground.png"));
		selectionBackgroundSprite = new Sprite(new Texture("res/shop/shopSelectionBG.png"));
		buyBackgroundSprite = new Sprite(new Texture("res/shop/shopBuyBG.png"));

		// OPTIONS TABLE
		optionsTable = new Table();
		optionsTable.setLayoutEnabled(false);

		selectButton = new TextButton("Select", MenuScreen.skin);
		returnButton = new TextButton("Return", MenuScreen.skin);
		coins = new Label("" + Game.player.coins, MenuScreen.skin);

		selectButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				hideInfo();
				buying = true;
			}
		});

		returnButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.emptyShopIcon);
				hideInfo();
				LoadingImage.emptyShopIcon.setVisible(true);
			}
		});

		LoadingImage.rightArrow.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (currentCategory == Category.POTIONS) {
					currentCategory = Category.WEAPONS;
				} else if (currentCategory == Category.WEAPONS)
					currentCategory = Category.PARCHMENTS;
				else if (currentCategory == Category.PARCHMENTS)
					currentCategory = Category.POTIONS;
			}
		});

		LoadingImage.leftArrow.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (currentCategory == Category.POTIONS)
					currentCategory = Category.PARCHMENTS;
				else if (currentCategory == Category.WEAPONS)
					currentCategory = Category.POTIONS;
				else if (currentCategory == Category.PARCHMENTS)
					currentCategory = Category.WEAPONS;
			}
		});

		// SETTING POSITION AND VISIBILITY OF TABLE'S PARTS
		LoadingImage.emptyShopIcon.setPosition(141, 43);
		LoadingImage.rightArrow.setPosition(283, 274);
		LoadingImage.leftArrow.setPosition(115, 274);
		coins.setPosition(199, 274);
		selectButton.setPosition(573, 90);
		returnButton.setPosition(573, 50);
		LoadingImage.emptyShopIcon.setVisible(true);
		selectButton.setVisible(false);
		returnButton.setVisible(false);

		optionsTable.add(coins);
		optionsTable.add(selectButton);
		optionsTable.add(returnButton);
		optionsTable.add(LoadingImage.rightArrow);
		optionsTable.add(LoadingImage.leftArrow);
		optionsTable.add(LoadingImage.emptyShopIcon);
		// END OPTIONS TABLE

		// POTIONS TABLE
		potionsTable = new Table();
		potionsTable.setLayoutEnabled(false);
		Label potionsLabel;
		TextButton[] potions;

		potionsLabel = new Label("Potions", MenuScreen.skin);
		potions = new TextButton[3];
		potions[0] = new TextButton(
				"Blue potion    $10"/*
									 * + Game.character.bag.getNumberOf(Element.
									 * POTION, Level.FIRST)
									 */, MenuScreen.skin);
		potions[1] = new TextButton(
				"Red potion    $20" /*
									 * + Game.character.bag.getNumberOf(Element.
									 * POTION, Level.SECOND)
									 */, MenuScreen.skin);
		potions[2] = new TextButton(
				"Green potion  $30" /*
									 * + Game.character.bag.getNumberOf(Element.
									 * POTION, Level.THIRD)
									 */ , MenuScreen.skin);

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
				showInfo(LoadingImage.redPotion);
				itemSelected.setElement(Element.POTION);
				itemSelected.setLevel(Level.SECOND);
			}
		});

		potions[2].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.greenPotion);
				itemSelected.setElement(Element.POTION);
				itemSelected.setLevel(Level.THIRD);
			}
		});

		potionsLabel.setPosition(149, 425);
		potions[0].setPosition(350, 420);
		potions[1].setPosition(350, 370);
		potions[2].setPosition(350, 320);
		potionsTable.add(potionsLabel);
		potionsTable.add(potions[0]);
		potionsTable.add(potions[1]);
		potionsTable.add(potions[2]);
		// END POTIONS TABLE

		// WEAPON TABLE
		weaponsTable = new Table();
		weaponsTable.setLayoutEnabled(false);
		weaponsTable.setVisible(false);
		Label weaponsLabel;
		TextButton[] weapons;

		weaponsLabel = new Label("Weapons", MenuScreen.skin);
		weapons = new TextButton[3];
		weapons[0] = new TextButton("Ascia", MenuScreen.skin);
		weapons[1] = new TextButton("Spada", MenuScreen.skin);
		weapons[2] = new TextButton("Mazza", MenuScreen.skin);

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
		weapons[0].setPosition(350, 420);
		weapons[1].setPosition(350, 370);
		weapons[2].setPosition(350, 320);
		weaponsTable.add(weaponsLabel);
		weaponsTable.add(weapons[0]);
		weaponsTable.add(weapons[1]);
		weaponsTable.add(weapons[2]);
		// END WEAPONS TABLE

		// PARCHMENTS TABLE
		parchmentsTable = new Table();
		parchmentsTable.setLayoutEnabled(false);

		Label parchmentsLabel;
		parchmentsTable.setVisible(false);
		TextButton[] parchments;

		parchmentsLabel = new Label("Parchments", MenuScreen.skin);
		parchments = new TextButton[3];
		parchments[0] = new TextButton("Parchment1", MenuScreen.skin);
		parchments[1] = new TextButton("Parchment2", MenuScreen.skin);
		parchments[2] = new TextButton("Parchment3", MenuScreen.skin);

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

		parchments[2].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.spear);
			}
		});

		parchmentsLabel.setPosition(120, 425);
		parchments[0].setPosition(350, 420);
		parchments[1].setPosition(350, 370);
		parchments[2].setPosition(350, 320);
		parchmentsTable.add(parchmentsLabel);
		parchmentsTable.add(parchments[0]);
		parchmentsTable.add(parchments[1]);
		parchmentsTable.add(parchments[2]);
		// END PARCHMENTS TABLE

		// BUYING TABLE
		buyingTable = new Table();
		buyingTable.setLayoutEnabled(false);
		buyingTable.setVisible(false);
		TextButton[] buyingLevels;

		buyingLevels = new TextButton[3];
		buyingLevels[0] = new TextButton("Level 1", MenuScreen.skin);
		buyingLevels[1] = new TextButton("Level 2", MenuScreen.skin);
		buyingLevels[2] = new TextButton("Level 3", MenuScreen.skin);

		buyingLevels[0].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
			}
		});

		buyingLevels[1].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
			}
		});

		buyingLevels[2].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
			}
		});
		
		buyingLevels[0].setPosition(302, 245);
		buyingLevels[1].setPosition(302, 202);
		buyingLevels[2].setPosition(302, 159);
		buyingTable.add(buyingLevels[0]);
		buyingTable.add(buyingLevels[1]);
		buyingTable.add(buyingLevels[2]);

		TextButton buyButton = new TextButton("Buy", MenuScreen.skin);
		buyButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				refreshedCoins = Game.player.coins - 30;
				MusicManager.play("CASH");
				scaling = true;
			}
		});

		TextButton returnBuyButton = new TextButton("Return", MenuScreen.skin);
		returnBuyButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				buyingTable.setVisible(false);
				buying = false;
				optionsTable.add(LoadingImage.rightArrow);
				optionsTable.add(LoadingImage.leftArrow);
				showInfo(LoadingImage.emptyShopIcon);

			}
		});

		final TextField level1n = new TextField("", MenuScreen.skin);
		final TextField level2n = new TextField("", MenuScreen.skin);
		final TextField level3n = new TextField("", MenuScreen.skin);
		level1n.setMessageText("0");
		level1n.setFocusTraversal(true);
		level1n.setWidth(30);
		level2n.setMessageText("0");
		level2n.setFocusTraversal(true);
		level2n.setWidth(30);
		level3n.setMessageText("0");
		level3n.setFocusTraversal(true);
		level3n.setWidth(30);

		buyButton.setPosition(573, 90);
		returnBuyButton.setPosition(573, 50);
		level1n.setPosition(482, 240);
		level2n.setPosition(482, 198);
		level3n.setPosition(482, 154);
		buyingTable.add(buyButton);
		buyingTable.add(returnBuyButton);
		buyingTable.add(level1n);
		buyingTable.add(level2n);
		buyingTable.add(level3n);
		// END BUYING TABLE

		stage.addActor(potionsTable);
		stage.addActor(weaponsTable);
		stage.addActor(parchmentsTable);
		stage.addActor(optionsTable);
		stage.addActor(buyingTable);
	}

	private void showInfo(ImageButton icon) {
		icon.setPosition(149, 43);
		optionsTable.removeActor(icon);
		optionsTable.add(icon);
		LoadingImage.emptyShopIcon.setVisible(false);

		selection = true;
		selectButton.setVisible(true);
		returnButton.setVisible(true);
	}

	private void hideInfo() {
		selection = false;
		selectButton.setVisible(false);
		returnButton.setVisible(false);
	}

	void buyingMode() {
		optionsTable.removeActor(LoadingImage.rightArrow);
		optionsTable.removeActor(LoadingImage.leftArrow);
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
		else if (buying) {
			buyingMode();
			selectionBackgroundSprite.draw(game.batch);
			buyBackgroundSprite.draw(game.batch);
		}

		else
			backgroundSprite.draw(game.batch);

		game.batch.end();

		stage.act();
		stage.draw();

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			game.screenManager.swapScreen(State.PLAYING);
			// Game.world.semaphore.release();
		}

		if (currentCategory == Category.POTIONS) {
			potionsTable.setVisible(true);
			weaponsTable.setVisible(false);
			parchmentsTable.setVisible(false);
			buyingTable.setVisible(false);
		} else if (currentCategory == Category.WEAPONS) {
			weaponsTable.setVisible(true);
			potionsTable.setVisible(false);
			parchmentsTable.setVisible(false);
			buyingTable.setVisible(false);

		} else if (currentCategory == Category.PARCHMENTS) {
			parchmentsTable.setVisible(true);
			potionsTable.setVisible(false);
			weaponsTable.setVisible(false);
			buyingTable.setVisible(false);

		}
		if (buying)
			buyingTable.setVisible(true);

		coinsTimer += delta;
		if (scaling) {
			if (coinsTimer > 0.008f) {
				coinsTimer = 0;
				if (Game.player.coins > refreshedCoins) {
					coins.setText((String.valueOf(Game.player.coins -= 1)));
				} else
					scaling = false;
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		// viewport.update(width, height);
		stage.getViewport().setScreenSize(width, height);
		// camera.position.set(camera.viewportWidth / 2, camera.viewportHeight /
		// 2, 0);
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
