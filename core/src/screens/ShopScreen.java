package screens;

import com.badlogic.gdx.Screen;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import character.Bomb;
import character.Weapon;
import character.Weapon.Type;
import gameManager.GameSlagyom;
import gameManager.LoadingImage;
import gameManager.LoadingMusic;
import gameManager.ScreenConfig;
import gameManager.ScreenManager.State;
import multiplayer.Client;
import staticObjects.Item;
import staticObjects.Item.Level;
import staticObjects.StaticObject.Element;
import world.Game;

public class ShopScreen implements Screen {
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
				boolean buy = false;
				if ((potionsTable.isVisible() || parchmentsTable.isVisible()) && level2n.getText() != null) {
					if (!game.modalityMultiplayer) {
						int tmp = (int) (Game.world.player.coins
								- (Integer.parseInt(level2n.getText())) * itemSelected.price);
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
					LoadingMusic.cashSound.play(1.5f);
				else {
					if (!game.modalityMultiplayer)
						refreshedCoins = Game.world.player.coins;
					else
						refreshedCoins = Client.networkWorld.player.coins;
				}
				scaling = true;
			}
		});

		returnButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.emptyShopIcon);
				hideInfo();
				LoadingImage.emptyShopIcon.setVisible(true);
				buyingTable.setVisible(false);
				buying = false;
				optionsTable.add(LoadingImage.rightArrow);
				optionsTable.add(LoadingImage.leftArrow);
				optionsTable.add(LoadingImage.close);

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
					currentCategory = Category.BOMBS;
				else if (currentCategory == Category.BOMBS)
					currentCategory = Category.POTIONS;
				buying = false;
			}
		});

		LoadingImage.leftArrow.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (currentCategory == Category.POTIONS)
					currentCategory = Category.BOMBS;
				else if (currentCategory == Category.BOMBS)
					currentCategory = Category.PARCHMENTS;
				else if (currentCategory == Category.PARCHMENTS)
					currentCategory = Category.WEAPONS;
				else if (currentCategory == Category.WEAPONS)
					currentCategory = Category.POTIONS;
				buying = false;
			}
		});
		
		LoadingImage.close.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!game.modalityMultiplayer)
					game.screenManager.swapScreen(State.PLAYING);
				else
					game.screenManager.swapScreen(State.MULTIPLAYERGAME);
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
		optionsTable.add(buyButton);
		optionsTable.add(returnButton);
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
		optionsTable.add(coins);
		optionsTable.add(returnButton);
		optionsTable.add(LoadingImage.rightArrow);
		optionsTable.add(LoadingImage.leftArrow);
		optionsTable.add(LoadingImage.close);
		optionsTable.add(LoadingImage.emptyShopIcon);
		// END OPTIONS TABLE

		// POTIONS TABLE
		potionsTable = new Table();
		potionsTable.setLayoutEnabled(false);
		Label potionsLabel;
		TextButton[] potions;

		potionsLabel = new Label("Potions", MenuScreen.skin);
		potions = new TextButton[3];
		potions[0] = new TextButton("Blue potion   $10", MenuScreen.skin);
		potions[1] = new TextButton("Red potion    $20", MenuScreen.skin);
		potions[2] = new TextButton("Green potion  $30", MenuScreen.skin);

		potions[0].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.bluePotion);
				itemSelected = new Item(Element.POTION, Level.FIRST);
				setBuyingTable();
			}
		});

		potions[1].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.redPotion);
				setBuyingTable();
				itemSelected = new Item(Element.POTION, Level.SECOND);

			}
		});

		potions[2].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.greenPotion);

				setBuyingTable();
				itemSelected = new Item(Element.POTION, Level.THIRD);

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
		Label bombsLabel = new Label("Bombs", MenuScreen.skin);
		TextButton[] bombs = new TextButton[3];

		bombs[0] = new TextButton("Bomb   lev1   $3", MenuScreen.skin);
		bombs[1] = new TextButton("Bomb   lev2   $5", MenuScreen.skin);
		bombs[2] = new TextButton("Bomb   lev3   $15", MenuScreen.skin);

		bombs[0].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.bomb);
				weaponSelected = new Bomb(character.Weapon.Level.lev1, character.Weapon.Type.Bomba);
				setBuyingTable();
			}
		});

		bombs[1].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.bomb);
				setBuyingTable();
				weaponSelected = new Bomb(character.Weapon.Level.lev2, character.Weapon.Type.Bomba);
			}
		});

		bombs[2].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.bomb);
				setBuyingTable();
				weaponSelected = new Bomb(character.Weapon.Level.lev3, character.Weapon.Type.Bomba);
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
		Label weaponsLabel;
		TextButton[] weapons = new TextButton[3];

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
				showInfo(LoadingImage.spear);
				buying = true;
				buyingTable.clear();
				buyingTable.setVisible(true);

				lev1.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						weaponSelected = new Weapon(character.Weapon.Level.lev1, Type.Sword);
					}
				});
				lev2.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						weaponSelected = new Weapon(character.Weapon.Level.lev2, Type.Sword);
					}
				});
				lev3.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						weaponSelected = new Weapon(character.Weapon.Level.lev3, Type.Sword);
					}
				});
				setBuyingTableWeapon();
			}
		});

		weapons[1].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.sword);
				buying = true;
				buyingTable.clear();
				buyingTable.setVisible(true);
				lev1.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						weaponSelected = new Weapon(character.Weapon.Level.lev1, Type.Spear);
					}
				});
				lev2.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						weaponSelected = new Weapon(character.Weapon.Level.lev2, Type.Spear);
					}
				});
				lev3.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						weaponSelected = new Weapon(character.Weapon.Level.lev3, Type.Spear);
					}
				});
				setBuyingTableWeapon();
			}
		});

		weapons[2].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.bow);
				buying = true;
				buyingTable.clear();
				buyingTable.setVisible(true);
				lev1.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						weaponSelected = new Weapon(character.Weapon.Level.lev1, Type.Bow);
					}
				});
				lev2.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						weaponSelected = new Weapon(character.Weapon.Level.lev2, Type.Bow);
					}
				});
				lev3.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						weaponSelected = new Weapon(character.Weapon.Level.lev3, Type.Bow);
					}
				});
				setBuyingTableWeapon();
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

		Label parchmentsLabel = new Label("Parchments", MenuScreen.skin);
		parchments = new TextButton[2];
		parchments[0] = new TextButton("Parchment1 $4", MenuScreen.skin);
		parchments[1] = new TextButton("Parchment2 $9", MenuScreen.skin);

		parchments[0].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.parchment);
				itemSelected = new Item(Element.PARCHMENT, Level.FIRST);
				setBuyingTable();
			}
		});

		parchments[1].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.parchment);
				itemSelected = new Item(Element.PARCHMENT, Level.SECOND);
				setBuyingTable();
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
	}

	private void showInfo(ImageButton icon) {
		icon.setPosition(149, 43);
		optionsTable.removeActor(icon);
		optionsTable.add(icon);
		LoadingImage.emptyShopIcon.setVisible(false);

		buyButton.setVisible(true);
		returnButton.setVisible(true);

	}

	private void hideInfo() {
		returnButton.setVisible(false);
		buyButton.setVisible(false);
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
				game.screenManager.swapScreen(State.MULTIPLAYERGAME);
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

	@Override
	public void resize(int width, int height) {
		stage.getViewport().setScreenSize(width, height);
		camera.update();
	}

	public void setBuyingTableWeapon() {

		buyingTable.add(lev1);
		buyingTable.add(lev2);
		buyingTable.add(lev3);
		buyingTable.add(buyButton);
		buyingTable.add(returnButton);
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

}
