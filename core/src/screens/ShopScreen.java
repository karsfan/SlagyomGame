package screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import character.Bomb;
import character.Weapon;
import character.Weapon.Type;
import gameManager.GameSlagyom;
import gameManager.LoadingImage;
import gameManager.ScreenConfig;
import gameManager.ScreenManager.State;
import multiplayer.Client;
import staticObjects.Item;
import staticObjects.Item.Level;
import staticObjects.StaticObject.Element;
import world.Game;
import world.GameConfig;

public class ShopScreen implements Screen, ControllerListener {
	private enum Category {
		POTIONS, WEAPONS, PARCHMENTS, BOMBS
	}

	public Category currentCategory;

	private GameSlagyom game;
	public Stage stage;
	private Viewport viewport;
	private OrthographicCamera camera;

	private Sprite backgroundSprite;
	private Sprite selectionBackgroundSprite;
	private Sprite buyBackgroundSprite;

	private Table weaponsTable;
	private Table bombsTable;
	private Table potionsTable;
	private Table parchmentsTable;
	private Table buyingTable;

	private Table optionsTable;

	private TextButton returnButton;
	private Label coins;
	Label textTable;
	boolean buying;
	boolean selectionWeapon = false;
	Weapon weaponSelected;
	TextButton[] parchments;
	TextButton buyButton = new TextButton("Buy", MenuScreen.skin);
	final TextField level1n = new TextField("", MenuScreen.skin);
	final TextField level2n = new TextField("", MenuScreen.skin);
	final TextField level3n = new TextField("", MenuScreen.skin);
	final TextButton lev1 = new TextButton("Lev1  $15", MenuScreen.skin);
	final TextButton lev2 = new TextButton("Lev2  $60", MenuScreen.skin);
	final TextButton lev3 = new TextButton("Lev3  $100", MenuScreen.skin);
	// Variables for cash-scaling animation
	int refreshedCoins;
	float coinsTimer = 0;
	boolean scaling = false;
	public Item itemSelected;
	Label potionsLabel;
	TextButton[] potions;
	Label bombsLabel;
	TextButton[] bombs;
	Label weaponsLabel;
	TextButton[] weapons;

	TextButton buttonSelected;
	TextButton optionButtonSelected;
	TextButton buttonLevelSelected;
	Label parchmentsLabel;

	@SuppressWarnings("static-access")
	public ShopScreen(final GameSlagyom game) {
		this.game = game;

		// SCREEN INITIALIZING
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(854, 480, camera);
		viewport.apply();
		stage = new Stage(viewport, game.batch);

		currentCategory = Category.POTIONS;
		// selection = false;
		buying = false;
		itemSelected = new Item(null, null);

		buyingTable = new Table();
		buyingTable.setLayoutEnabled(false);
		buyingTable.setVisible(false);
		level1n.setMessageText("0");
		level1n.setFocusTraversal(true);
		level1n.setWidth(30);
		level2n.setMessageText("0");
		level2n.setFocusTraversal(true);
		level2n.setWidth(30);
		level3n.setMessageText("0");
		level3n.setFocusTraversal(true);
		level3n.setWidth(30);

		backgroundSprite = new Sprite(new Texture("res/shop/shopBackground.png"));
		selectionBackgroundSprite = new Sprite(new Texture("res/shop/shopSelectionBG.png"));
		buyBackgroundSprite = new Sprite(new Texture("res/shop/shopBuyBG.png"));

		// OPTIONS TABLE
		optionsTable = new Table();
		optionsTable.setLayoutEnabled(false);

		returnButton = new TextButton("Return", MenuScreen.skin);
		if (!game.modalityMultiplayer)
			coins = new Label("" + Game.world.player.coins, MenuScreen.skin);
		else
			coins = new Label("" + Client.networkWorld.player.coins, MenuScreen.skin);
		buyButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				clickBuyButton();
			}
		});

		returnButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				clickReturnButton();
			}
		});

		LoadingImage.rightArrow.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				clickRightArrow();
			}
		});

		LoadingImage.leftArrow.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				clickLeftArrow();
			}
		});

		LoadingImage.close.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.screenManager.swapScreen(State.PLAYING);
			}
		});

		buyButton.setPosition(573, 90);
		level3n.setMaxLength(3);
		level1n.setMaxLength(3);
		level2n.setMaxLength(3);
		level1n.setPosition(482, 240);
		level2n.setPosition(482, 198);
		level3n.setPosition(482, 154);
		LoadingImage.howMuch.setPosition(0, 0);
		// optionsTable.add(buyButton);
		// optionsTable.add(returnButton);
		buyingTable.add(level1n);
		buyingTable.add(level2n);
		buyingTable.add(level3n);

		// SETTING POSITION AND VISIBILITY OF TABLE'S PARTS
		LoadingImage.emptyShopIcon.setPosition(141, 43);
		LoadingImage.rightArrow.setPosition(283, 274);
		LoadingImage.leftArrow.setPosition(115, 274);
		LoadingImage.close.setPosition(101, 450);
		coins.setPosition(199, 274);
		returnButton.setPosition(573, 50);
		LoadingImage.emptyShopIcon.setVisible(true);
		returnButton.setVisible(false);
		buyButton.setVisible(false);
		coins.setVisible(true);
		optionsTable.setVisible(true);
		optionsTable.add(LoadingImage.emptyShopIcon);
		// END OPTIONS TABLE

		// POTIONS TABLE
		potionsTable = new Table();
		potionsTable.setLayoutEnabled(false);

		potionsLabel = new Label("Potions", MenuScreen.skin);
		potions = new TextButton[3];
		potions[0] = new TextButton("Blue potion   $10", MenuScreen.skin);
		potions[1] = new TextButton("Red potion    $20", MenuScreen.skin);
		potions[2] = new TextButton("Green potion  $30", MenuScreen.skin);

		potions[0].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				clickPotionFirst();
			}
		});

		potions[1].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				clickPotionSecond();
			}
		});

		potions[2].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				clickPotionThird();
			}
		});

		potionsLabel.setPosition(149, 425);
		potions[0].setPosition(ScreenConfig.tableBagX, ScreenConfig.tableBagFirstY);
		potions[1].setPosition(ScreenConfig.tableBagX, ScreenConfig.tableBagSecondY);
		potions[2].setPosition(ScreenConfig.tableBagX, ScreenConfig.tableBagThirdY);
		potionsTable.add(potionsLabel);
		potionsTable.add(potions[0]);
		potionsTable.add(potions[1]);
		potionsTable.add(potions[2]);
		// END POTIONS TABLE
		bombsTable = new Table();
		bombsTable.setLayoutEnabled(false);
		bombsLabel = new Label("Bombs", MenuScreen.skin);
		bombs = new TextButton[3];

		bombs[0] = new TextButton("Bomb   lev1   $3", MenuScreen.skin);
		bombs[1] = new TextButton("Bomb   lev2   $5", MenuScreen.skin);
		bombs[2] = new TextButton("Bomb   lev3   $15", MenuScreen.skin);

		bombs[0].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				clickBombFirst();
			}
		});

		bombs[1].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				clickBombSecond();
			}
		});

		bombs[2].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				clickBombThird();
			}
		});

		bombsLabel.setPosition(155, 425);
		bombs[0].setPosition(350, 420);
		bombs[1].setPosition(350, 370);
		bombs[2].setPosition(350, 320);
		bombsTable.add(bombsLabel);
		bombsTable.add(bombs[0]);
		bombsTable.add(bombs[1]);
		bombsTable.add(bombs[2]);
		// END bombs TABLE

		// WEAPON TABLE
		weaponsTable = new Table();
		weaponsTable.setLayoutEnabled(false);
		weaponsTable.setVisible(false);
		weapons = new TextButton[3];

		lev1.setPosition(302, 250);
		lev2.setPosition(302, 206);
		lev3.setPosition(302, 163);
		// leggi nota prima del metdo
		weaponsLabel = new Label("Weapons", MenuScreen.skin);
		weapons[0] = new TextButton("Sword", MenuScreen.skin);
		weapons[1] = new TextButton("Spear", MenuScreen.skin);
		weapons[2] = new TextButton("Bow", MenuScreen.skin);
		weapons[0].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				clickWeaponSword();
			}
		});

		weapons[1].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				clickWeaponSpear();
			}
		});

		weapons[2].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				clickWeaponBow();
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

		parchmentsLabel = new Label("Parchments", MenuScreen.skin);
		parchments = new TextButton[2];
		parchments[0] = new TextButton("Parchment lev1 $4", MenuScreen.skin);
		parchments[1] = new TextButton("Parchment lev2 $9", MenuScreen.skin);

		parchments[0].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				clickParchmentFirst();
			}
		});

		parchments[1].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				clickParchmentSecond();
			}
		});

		parchmentsLabel.setPosition(120, 425);
		parchments[0].setPosition(350, 420);
		parchments[1].setPosition(350, 370);
		parchmentsTable.add(parchmentsLabel);
		parchmentsTable.add(parchments[0]);
		parchmentsTable.add(parchments[1]);
		// END PARCHMENTS TABLE

		stage.addActor(bombsTable);
		stage.addActor(potionsTable);
		stage.addActor(weaponsTable);
		stage.addActor(parchmentsTable);
		stage.addActor(optionsTable);
		stage.addActor(buyingTable);
		stage.addActor(LoadingImage.leftArrow);
		stage.addActor(LoadingImage.rightArrow);
		stage.addActor(LoadingImage.close);
		stage.addActor(coins);
		buttonLevelSelected = lev1;
		optionButtonSelected = buyButton;
		buttonSelected = potions[0];
	}

	protected void clickParchmentSecond() {
		showInfo(LoadingImage.parchment2, LoadingImage.parchmentDescription);
		itemSelected = new Item(Element.PARCHMENT, Level.SECOND);
		setBuyingTable();
	}

	protected void clickParchmentFirst() {
		showInfo(LoadingImage.parchment, LoadingImage.parchmentDescription);
		itemSelected = new Item(Element.PARCHMENT, Level.FIRST);
		setBuyingTable();
	}

	protected void clickWeaponBow() {
		showInfo(LoadingImage.bow, LoadingImage.weaponDescription);
		buying = true;
		buyingTable.clear();
		buyingTable.setVisible(true);
		lev1.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				weaponSelected = new Weapon(character.Weapon.Level.lev1, Type.Bow);
				clickBuyButton();
			}
		});
		lev2.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				weaponSelected = new Weapon(character.Weapon.Level.lev2, Type.Bow);
				clickBuyButton();
			}
		});
		lev3.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				weaponSelected = new Weapon(character.Weapon.Level.lev3, Type.Bow);
				clickBuyButton();
			}
		});
		setBuyingTableWeapon();
	}

	protected void clickWeaponSpear() {
		showInfo(LoadingImage.sword, LoadingImage.weaponDescription);
		buying = true;
		buyingTable.clear();
		buyingTable.setVisible(true);
		lev1.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				weaponSelected = new Weapon(character.Weapon.Level.lev1, Type.Spear);
				clickBuyButton();
			}
		});
		lev2.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				weaponSelected = new Weapon(character.Weapon.Level.lev2, Type.Spear);
				clickBuyButton();
			}
		});
		lev3.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				weaponSelected = new Weapon(character.Weapon.Level.lev3, Type.Spear);
				clickBuyButton();
			}
		});
		setBuyingTableWeapon();

	}

	protected void clickWeaponSword() {
		showInfo(LoadingImage.spear, LoadingImage.weaponDescription);
		buying = true;
		buyingTable.clear();
		buyingTable.setVisible(true);

		lev1.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				weaponSelected = new Weapon(character.Weapon.Level.lev1, Type.Sword);
				clickBuyButton();
			}
		});
		lev2.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				weaponSelected = new Weapon(character.Weapon.Level.lev2, Type.Sword);
				clickBuyButton();
			}
		});
		lev3.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				weaponSelected = new Weapon(character.Weapon.Level.lev3, Type.Sword);
				clickBuyButton();
			}
		});
		setBuyingTableWeapon();
	}

	protected void clickBombThird() {
		game.loadingMusic.selectionSound.play(GameConfig.musicVolume);
		showInfo(LoadingImage.bomb, LoadingImage.bombDescription);
		setBuyingTable();
		weaponSelected = new Bomb(character.Weapon.Level.lev3, character.Weapon.Type.Bomba);
	}

	protected void clickBombSecond() {
		game.loadingMusic.selectionSound.play(GameConfig.musicVolume);
		showInfo(LoadingImage.bomb, LoadingImage.bombDescription);
		setBuyingTable();
		weaponSelected = new Bomb(character.Weapon.Level.lev2, character.Weapon.Type.Bomba);
	}

	protected void clickBombFirst() {
		game.loadingMusic.selectionSound.play(GameConfig.musicVolume);
		showInfo(LoadingImage.bomb, LoadingImage.bombDescription);
		weaponSelected = new Bomb(character.Weapon.Level.lev1, character.Weapon.Type.Bomba);
		setBuyingTable();
	}

	protected void clickPotionThird() {
		game.loadingMusic.selectionSound.play(GameConfig.musicVolume);
		showInfo(LoadingImage.greenPotion, LoadingImage.potionDescription);
		setBuyingTable();
		itemSelected = new Item(Element.POTION, Level.THIRD);
	}

	protected void clickPotionSecond() {
		game.loadingMusic.selectionSound.play(GameConfig.musicVolume);
		showInfo(LoadingImage.redPotion, LoadingImage.potionDescription);
		setBuyingTable();
		itemSelected = new Item(Element.POTION, Level.SECOND);
	}

	protected void clickPotionFirst() {
		game.loadingMusic.selectionSound.play(GameConfig.musicVolume);
		showInfo(LoadingImage.bluePotion, LoadingImage.potionDescription);
		itemSelected = new Item(Element.POTION, Level.FIRST);
		setBuyingTable();
	}

	protected void clickLeftArrow() {
		game.loadingMusic.selectionSound.play(GameConfig.musicVolume);
		if (currentCategory == Category.POTIONS) {
			currentCategory = Category.BOMBS;
			buttonSelected = bombs[0];
		} else if (currentCategory == Category.BOMBS) {
			currentCategory = Category.PARCHMENTS;
			buttonSelected = parchments[0];
		} else if (currentCategory == Category.PARCHMENTS) {
			currentCategory = Category.WEAPONS;
			buttonSelected = weapons[0];
		} else if (currentCategory == Category.WEAPONS) {
			currentCategory = Category.POTIONS;
			buttonSelected = potions[0];
		}
		buying = false;
	}

	protected void clickRightArrow() {
		game.loadingMusic.selectionSound.play(GameConfig.musicVolume);
		if (currentCategory == Category.POTIONS) {
			currentCategory = Category.WEAPONS;
			buttonSelected = weapons[0];
		} else if (currentCategory == Category.WEAPONS) {
			currentCategory = Category.PARCHMENTS;
			buttonSelected = parchments[0];
		} else if (currentCategory == Category.PARCHMENTS) {
			currentCategory = Category.BOMBS;
			buttonSelected = bombs[0];
		} else if (currentCategory == Category.BOMBS) {
			currentCategory = Category.POTIONS;
			buttonSelected = potions[0];
		}
		buying = false;
	}

	protected void clickReturnButton() {
		game.loadingMusic.selectionSound.play(GameConfig.musicVolume);
		hideInfo();
		LoadingImage.emptyShopIcon.setVisible(true);
		buyingTable.setVisible(false);
		buying = false;
		for (Actor actor : stage.getActors()) {
			if (actor == LoadingImage.rightArrow)
				actor.setVisible(true);
			else if (actor == LoadingImage.leftArrow)
				actor.setVisible(true);
		}
		showInfo(LoadingImage.emptyShopIcon, LoadingImage.emptyDescription);
		optionsTable.removeActor(LoadingImage.bombDescription);
		optionsTable.removeActor(LoadingImage.potionDescription);
		optionsTable.removeActor(LoadingImage.weaponDescription);
		optionsTable.removeActor(LoadingImage.parchmentDescription);
	}

	@SuppressWarnings("static-access")
	protected void clickBuyButton() {
		boolean buy = false;
		game.loadingMusic.selectionSound.play(GameConfig.musicVolume);
		if ((potionsTable.isVisible() || parchmentsTable.isVisible()) && !level2n.getText().equals("")) {
			if (!game.modalityMultiplayer) {
				int tmp = (int) (Game.world.player.coins - (Integer.parseInt(level2n.getText())) * itemSelected.price);
				if (tmp >= 0) {
					buy = true;
					refreshedCoins = tmp;
					for (int i = 0; i < Integer.parseInt(level2n.getText()); i++)
						Game.world.player.bag.add(itemSelected);
				}
			} else {
				int tmp = (int) (Client.networkWorld.player.coins
						- (Integer.parseInt(level2n.getText())) * itemSelected.price);
				if (tmp >= 0) {
					buy = true;
					refreshedCoins = tmp;
					for (int i = 0; i < Integer.parseInt(level2n.getText()); i++)
						Client.networkWorld.player.bag.add(itemSelected);
				}
			}
		} else if (weaponsTable.isVisible()) {
			if (!game.modalityMultiplayer) {
				int tmp = (int) (Game.world.player.coins - weaponSelected.price);
				if (tmp >= 0) {
					buy = true;
					refreshedCoins = tmp;
					Game.world.player.bag.add(weaponSelected);
				}
			} else {
				int tmp = (int) (Client.networkWorld.player.coins - weaponSelected.price);
				if (tmp >= 0) {
					buy = true;
					refreshedCoins = tmp;
					Client.networkWorld.player.bag.add(weaponSelected);
				}
			}
		} else if (bombsTable.isVisible() && level2n.getText() != null) {
			if (!game.modalityMultiplayer) {
				int tmp = (int) (Game.world.player.coins
						- (Integer.parseInt(level2n.getText())) * weaponSelected.price);
				if (tmp >= 0) {
					buy = true;
					refreshedCoins = tmp;

					for (int i = 0; i < Integer.parseInt(level2n.getText()); i++)
						Game.world.player.bag.add(weaponSelected);
				}
			} else {
				int tmp = (int) (Client.networkWorld.player.coins
						- (Integer.parseInt(level2n.getText())) * weaponSelected.price);
				if (tmp >= 0) {
					buy = true;
					refreshedCoins = tmp;

					for (int i = 0; i < Integer.parseInt(level2n.getText()); i++)
						Client.networkWorld.player.bag.add(weaponSelected);
				}
			}
		}
		if (buy)
			game.loadingMusic.cashSound.play(1.5f);
		else {
			if (!game.modalityMultiplayer)
				refreshedCoins = Game.world.player.coins;
			else
				refreshedCoins = Client.networkWorld.player.coins;
		}
		scaling = true;
	}

	private void showInfo(ImageButton icon, ImageButton description) {
		icon.setPosition(149, 43);
		description.setPosition(230, 16);
		optionsTable.removeActor(icon);
		optionsTable.removeActor(description);
		LoadingImage.emptyShopIcon.setVisible(false);
		icon.setVisible(true);
		optionsTable.add(icon);
		optionsTable.add(description);
		buyButton.setVisible(true);
		returnButton.setVisible(true);

	}

	private void hideInfo() {
		returnButton.setVisible(false);
		buyButton.setVisible(false);
	}

	void buyingMode() {
		for (Actor actor : stage.getActors()) {
			if (actor == LoadingImage.rightArrow)
				actor.setVisible(false);
			else if (actor == LoadingImage.leftArrow)
				actor.setVisible(false);
		}
	}

	@Override
	public void show() {

	}

	@SuppressWarnings("static-access")
	@Override
	public void render(float delta) {
		buttonSelected.getLabel().setFontScale(1.0f);
		if (buttonLevelSelected != null)
			buttonLevelSelected.getLabel().setFontScale(1.0f);
		optionButtonSelected.getLabel().setFontScale(1.0f);

		mouseMoved();

		buttonSelected.getLabel().setFontScale(1.1f);
		if (buttonLevelSelected != null)
			buttonLevelSelected.getLabel().setFontScale(1.1f);
		optionButtonSelected.getLabel().setFontScale(1.1f);

		Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batch.begin();
		if (buying) {
			buyingMode();
			selectionBackgroundSprite.draw(game.batch);
			buyBackgroundSprite.draw(game.batch);
		}

		else
			backgroundSprite.draw(game.batch);

		game.batch.end();

		stage.act();
		stage.draw();

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE))
			if (!game.modalityMultiplayer)
				game.screenManager.swapScreen(State.PLAYING);
			else
				game.screenManager.swapScreen(State.PLAYING);
		if (currentCategory == Category.POTIONS) {
			potionsTable.setVisible(true);
			weaponsTable.setVisible(false);
			parchmentsTable.setVisible(false);
			buyingTable.setVisible(false);
			bombsTable.setVisible(false);
		} else if (currentCategory == Category.WEAPONS) {
			weaponsTable.setVisible(true);
			potionsTable.setVisible(false);
			parchmentsTable.setVisible(false);
			buyingTable.setVisible(false);
			bombsTable.setVisible(false);
		} else if (currentCategory == Category.PARCHMENTS) {
			parchmentsTable.setVisible(true);
			potionsTable.setVisible(false);
			weaponsTable.setVisible(false);
			buyingTable.setVisible(false);
			bombsTable.setVisible(false);
		} else if (currentCategory == Category.BOMBS) {
			parchmentsTable.setVisible(false);
			bombsTable.setVisible(true);
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
				if (!game.modalityMultiplayer) {
					if (Game.world.player.coins > refreshedCoins) {
						coins.setText((String.valueOf(Game.world.player.coins -= 1)));
					} else
						scaling = false;
				} else {
					if (Client.networkWorld.player.coins > refreshedCoins) {
						coins.setText((String.valueOf(Client.networkWorld.player.coins -= 1)));
					} else
						scaling = false;
				}
			}
		}
	}

	private void mouseMoved() {
		if (!buyingTable.isVisible()) {
			if (potionsTable.isVisible()) {
				if (potions[0].isOver()) {
					if (buttonSelected != potions[0])
						game.loadingMusic.overMenuSound.play(GameConfig.musicVolume);
					buttonSelected = potions[0];
				} else if (potions[1].isOver()) {
					if (buttonSelected != potions[1])
						game.loadingMusic.overMenuSound.play(GameConfig.musicVolume);
					buttonSelected = potions[1];
				} else if (potions[2].isOver()) {
					if (buttonSelected != potions[2])
						game.loadingMusic.overMenuSound.play(GameConfig.musicVolume);
					buttonSelected = potions[2];
				}
			} else if (weaponsTable.isVisible()) {
				if (weapons[0].isOver()) {
					if (buttonSelected != weapons[0])
						game.loadingMusic.overMenuSound.play(GameConfig.musicVolume);
					buttonSelected = weapons[0];
				} else if (weapons[1].isOver()) {
					if (buttonSelected != weapons[1])
						game.loadingMusic.overMenuSound.play(GameConfig.musicVolume);
					buttonSelected = weapons[1];
				}
			} else if (bombsTable.isVisible()) {
				if (bombs[0].isOver()) {
					if (buttonSelected != bombs[0])
						game.loadingMusic.overMenuSound.play(GameConfig.musicVolume);
					buttonSelected = bombs[0];
				} else if (bombs[1].isOver()) {
					if (buttonSelected != bombs[1])
						game.loadingMusic.overMenuSound.play(GameConfig.musicVolume);
					buttonSelected = bombs[1];
				} else if (bombs[2].isOver()) {
					if (buttonSelected != bombs[2])
						game.loadingMusic.overMenuSound.play(GameConfig.musicVolume);
					buttonSelected = bombs[2];
				}
			} else if (parchmentsTable.isVisible()) {
				if (parchments[0].isOver()) {
					if (buttonSelected != parchments[0])
						game.loadingMusic.overMenuSound.play(GameConfig.musicVolume);
					buttonSelected = parchments[0];
				} else if (parchments[1].isOver()) {
					if (buttonSelected != parchments[1])
						game.loadingMusic.overMenuSound.play(GameConfig.musicVolume);
					buttonSelected = parchments[1];
				}
			}
		}
		if (buyingTable.isVisible()) {
			if (lev1.isOver()){
				if(buttonLevelSelected != lev1)
					game.loadingMusic.overMenuSound.play(GameConfig.musicVolume);
				buttonLevelSelected = lev1;
			}
			else if (lev2.isOver()){
				if(buttonLevelSelected != lev2)
					game.loadingMusic.overMenuSound.play(GameConfig.musicVolume);
				buttonLevelSelected = lev2;
			}
			else if (lev3.isOver()){
				if(buttonLevelSelected != lev3)
					game.loadingMusic.overMenuSound.play(GameConfig.musicVolume);
				buttonLevelSelected = lev3;
			}
			if (buyButton.isOver()){
				if(optionButtonSelected != buyButton)
					game.loadingMusic.overMenuSound.play(GameConfig.musicVolume);
				optionButtonSelected = buyButton;
			}
			else if (returnButton.isOver()){
				if(optionButtonSelected != returnButton)
					game.loadingMusic.overMenuSound.play(GameConfig.musicVolume);
				optionButtonSelected = returnButton;
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().setScreenSize(width, height);
		camera.update();
	}

	public void setBuyingTableWeapon() {
		buyingTable.add(lev1);
		buyingTable.add(lev2);
		buyingTable.add(lev3);
	}

	public void setBuyingTable() {
		LoadingImage.howMuch.setVisible(true);
		buying = true;
		buyingTable.clear();
		buyingTable.setVisible(true);
		buyingTable.add(textTable);
		buyingTable.add(LoadingImage.howMuch);
		buyingTable.add(level2n);
		buyingTable.add(buyButton);
		buyingTable.add(returnButton);
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
		if (buttonCode == 1) {
			
			if (buyingTable.isVisible())
				clickReturnButton();
			else{
				game.loadingMusic.selectionSound.play(GameConfig.musicVolume);
				game.screenManager.swapScreen(State.PLAYING);
			}
		} else if (buttonCode == 0) {
			if (!buyingTable.isVisible()) {
				if (potionsTable.isVisible()) {
					if (buttonSelected == potions[0])
						clickPotionFirst();
					else if (buttonSelected == potions[1])
						clickPotionSecond();
					else if (buttonSelected == potions[2])
						clickPotionThird();
				} else if (parchmentsTable.isVisible()) {
					if (buttonSelected == parchments[0])
						clickParchmentFirst();
					else if (buttonSelected == parchments[1])
						clickParchmentSecond();
				} else if (bombsTable.isVisible()) {
					if (buttonSelected == bombs[0])
						clickBombFirst();
					else if (buttonSelected == bombs[1])
						clickBombSecond();
					else if (buttonSelected == bombs[2])
						clickBombThird();
				} else if (weaponsTable.isVisible()) {
					if (buttonSelected == weapons[0])
						clickWeaponSword();
					else if (buttonSelected == weapons[1])
						clickWeaponSpear();
					else if (buttonLevelSelected == weapons[2])
						clickWeaponBow();
				}
			} else if (buyingTable.isVisible()) {
				if (optionButtonSelected == buyButton)
					clickBuyButton();
				else if (optionButtonSelected == returnButton)
					clickReturnButton();
				if (buttonSelected == weapons[0]) {
					if (buttonLevelSelected == lev1)
						weaponSelected = new Weapon(character.Weapon.Level.lev1, Type.Sword);
					else if (buttonLevelSelected == lev2)
						weaponSelected = new Weapon(character.Weapon.Level.lev2, Type.Sword);
					else if (buttonLevelSelected == lev3)
						weaponSelected = new Weapon(character.Weapon.Level.lev3, Type.Sword);
					clickBuyButton();
				} else if (buttonSelected == weapons[1]) {
					if (buttonLevelSelected == lev1)
						weaponSelected = new Weapon(character.Weapon.Level.lev1, Type.Spear);
					else if (buttonLevelSelected == lev2)
						weaponSelected = new Weapon(character.Weapon.Level.lev2, Type.Spear);
					else if (buttonLevelSelected == lev3)
						weaponSelected = new Weapon(character.Weapon.Level.lev3, Type.Spear);
					clickBuyButton();
				} else if (buttonSelected == weapons[2]) {
					if (buttonLevelSelected == lev1)
						weaponSelected = new Weapon(character.Weapon.Level.lev1, Type.Bow);
					else if (buttonLevelSelected == lev2)
						weaponSelected = new Weapon(character.Weapon.Level.lev2, Type.Bow);
					else if (buttonLevelSelected == lev3)
						weaponSelected = new Weapon(character.Weapon.Level.lev3, Type.Bow);
					clickBuyButton();
				}
			}
		} else if (buttonCode == 4) {
			clickLeftArrow();
		} else if (buttonCode == 5)
			clickRightArrow();
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		if (value == PovDirection.north) {
			game.loadingMusic.overMenuSound.play(GameConfig.musicVolume);
			buttonSelected.getLabel().setFontScale(1.0f);
			if (!buyingTable.isVisible()) {
				if (potionsTable.isVisible()) {
					if (buttonSelected == potions[0])
						buttonSelected = potions[2];
					else if (buttonSelected == potions[1])
						buttonSelected = potions[0];
					else if (buttonSelected == potions[2])
						buttonSelected = potions[1];
					buttonLevelSelected = null;
				} else if (weaponsTable.isVisible()) {
					if (buttonSelected == weapons[0])
						buttonSelected = weapons[1];
					else if (buttonSelected == weapons[1])
						buttonSelected = weapons[0];
					buttonLevelSelected = lev1;
				} else if (bombsTable.isVisible()) {
					if (buttonSelected == bombs[0])
						buttonSelected = bombs[2];
					else if (buttonSelected == bombs[1])
						buttonSelected = bombs[0];
					else if (buttonSelected == bombs[2])
						buttonSelected = bombs[1];
					buttonLevelSelected = null;
				} else if (parchmentsTable.isVisible()) {
					if (buttonSelected == parchments[0])
						buttonSelected = parchments[1];
					else if (buttonSelected == parchments[1])
						buttonSelected = parchments[0];
					buttonLevelSelected = null;
				}
			} else {
				optionButtonSelected.getLabel().setFontScale(1.0f);
				if (buttonLevelSelected != null)
					buttonLevelSelected.getLabel().setFontScale(1.0f);
				if (buttonLevelSelected == null) {
					System.out.println("alkhkjh");
					if (optionButtonSelected == buyButton)
						optionButtonSelected = returnButton;
					else if (optionButtonSelected == returnButton)
						optionButtonSelected = buyButton;
				}
				if (buttonLevelSelected == lev1)
					buttonLevelSelected = lev3;
				else if (buttonLevelSelected == lev2)
					buttonLevelSelected = lev1;
				else if (buttonLevelSelected == lev3)
					buttonLevelSelected = lev2;
			}
		} else if (value == PovDirection.south) {
			game.loadingMusic.overMenuSound.play(GameConfig.musicVolume);
			buttonSelected.getLabel().setFontScale(1.0f);
			if (!buyingTable.isVisible()) {
				if (potionsTable.isVisible()) {
					if (buttonSelected == potions[0])
						buttonSelected = potions[1];
					else if (buttonSelected == potions[1])
						buttonSelected = potions[2];
					else if (buttonSelected == potions[2])
						buttonSelected = potions[0];
					buttonLevelSelected = null;
				} else if (weaponsTable.isVisible()) {
					if (buttonSelected == weapons[0])
						buttonSelected = weapons[1];
					else if (buttonSelected == weapons[1])
						buttonSelected = weapons[0];
					buttonLevelSelected = lev1;
				} else if (bombsTable.isVisible()) {
					if (buttonSelected == bombs[0])
						buttonSelected = bombs[1];
					else if (buttonSelected == bombs[1])
						buttonSelected = bombs[2];
					else if (buttonSelected == bombs[2])
						buttonSelected = bombs[0];
					buttonLevelSelected = null;
				} else if (parchmentsTable.isVisible()) {
					if (buttonSelected == parchments[0])
						buttonSelected = parchments[1];
					else if (buttonSelected == parchments[1])
						buttonSelected = parchments[0];
					buttonLevelSelected = null;
				}
			} else {
				optionButtonSelected.getLabel().setFontScale(1.0f);
				if (buttonLevelSelected != null)
					buttonLevelSelected.getLabel().setFontScale(1.0f);
				if (buttonLevelSelected == null) {
					if (optionButtonSelected == buyButton)
						optionButtonSelected = returnButton;
					else if (optionButtonSelected == returnButton)
						optionButtonSelected = buyButton;
				}
				if (buttonLevelSelected == lev1)
					buttonLevelSelected = lev2;
				else if (buttonLevelSelected == lev2)
					buttonLevelSelected = lev3;
				else if (buttonLevelSelected == lev3)
					buttonLevelSelected = lev1;
			}
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
