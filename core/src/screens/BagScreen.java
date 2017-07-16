package screens;

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

import character.Weapon;
import gameManager.GameSlagyom;
import gameManager.LoadingImage;
import gameManager.MenuControllerListener;
import gameManager.ScreenConfig;
import gameManager.ScreenManager.State;
import multiplayer.Client;
import staticObjects.Item;
import staticObjects.Item.Level;
import staticObjects.StaticObject.Element;
import world.Game;

public class BagScreen implements Screen {
	private enum Pocket {
		POTIONS, WEAPONS, PARCHMENTS, BOMBS
	}

	public Pocket currentPocket;

	private GameSlagyom game;
	public Stage stage;
	private Viewport viewport;
	private OrthographicCamera camera;

	private Texture background;
	private Sprite backgroundSprite;

	private Texture selectionBackground;
	private Sprite selectionBackgroundSprite;
	boolean selection;

	private Table weaponsTable;
	private Table bombsTable;
	private Table potionsTable;
	private Table parchmentsTable;

	private Table optionsTable;
	private TextButton use;
	private TextButton delete;
	private TextButton exit;

	public Item itemSelected;
	public Weapon weaponSelected;

	TextButton[] potions;
	TextButton[] weapons;
	TextButton[] bombs;
	TextButton[] parchments;

	@SuppressWarnings("static-access")
	public BagScreen(final GameSlagyom game) {
		this.game = game;
		itemSelected = new Item(null, null);
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(854, 480, camera);
		selection = false;
		viewport.apply();
		background = new Texture("res/bag/bagBackground.png");
		backgroundSprite = new Sprite(background);

		selectionBackground = new Texture("res/bag/bagSelectionBG.png");
		selectionBackgroundSprite = new Sprite(selectionBackground);

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
		weapons = new TextButton[2];
		weaponsTable = new Table();
		use.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.loadingMusic.selectionSound.play();
				if (game.screenManager.getPreviousState() == State.BATTLE)
					if (potionsTable.isVisible()) {
						if (!game.modalityMultiplayer)
							Game.world.player.bag.useItem(itemSelected);
						setTextPotions();
					}
				if (weaponsTable.isVisible()) {
					if (Game.world.player.primary_weapon.upgrade(Game.world.player.bag))
						weapons[0].setText(Game.world.player.primary_weapon.getType().toString() + " "
								+ Game.world.player.primary_weapon.getLevel());
				}
			}

		});
		delete.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (potionsTable.isVisible()) {
					if (!game.modalityMultiplayer)
						Game.world.player.bag.removeItem(itemSelected.getElement(), itemSelected.getLevel());
					else
						Client.networkWorld.player.bag.removeItem(itemSelected.getElement(), itemSelected.getLevel());
					setTextPotions();
				}
				if (parchmentsTable.isVisible()) {
					if (!game.modalityMultiplayer)
						Game.world.player.bag.removeItem(itemSelected.getElement(), itemSelected.getLevel());
					else
						Client.networkWorld.player.bag.removeItem(itemSelected.getElement(), itemSelected.getLevel());
					setTextParchment();
				}
				if (bombsTable.isVisible()) {
					if (!game.modalityMultiplayer)
						Game.world.player.bag.removeBomb(weaponSelected.getLevel());
					else
						Client.networkWorld.player.bag.removeBomb(weaponSelected.getLevel());
					setTextBomb();
				}

				if (weaponsTable.isVisible()) {
					if (!game.modalityMultiplayer) {
						if (Game.world.player.bag.secondary_weapon == weaponSelected)
							Game.world.player.bag.secondary_weapon = null;
					}
					setTextWeapon();
					// weapons.setText("");
				}
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
		LoadingImage.close.setPosition(101, 450);
		LoadingImage.emptyBagIcon.setVisible(true);

		use.setPosition(ScreenConfig.optionTableX, ScreenConfig.optionTableFirstY);
		use.setVisible(false);
		delete.setPosition(ScreenConfig.optionTableX, ScreenConfig.optionTableSecondY);
		delete.setVisible(false);
		exit.setPosition(ScreenConfig.optionTableX, ScreenConfig.optionTableThirdY);
		exit.setVisible(false);

		optionsTable.add(LoadingImage.emptyBagIcon);
		optionsTable.add(LoadingImage.rightArrow);
		optionsTable.add(LoadingImage.leftArrow);
		optionsTable.add(LoadingImage.close);
		optionsTable.add(LoadingImage.emptyBagIcon);

		optionsTable.add(use);
		optionsTable.add(delete);
		optionsTable.add(exit);

		LoadingImage.rightArrow.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (currentPocket == Pocket.POTIONS) {
					currentPocket = Pocket.WEAPONS;
				} else if (currentPocket == Pocket.WEAPONS)
					currentPocket = Pocket.PARCHMENTS;
				else if (currentPocket == Pocket.PARCHMENTS)
					currentPocket = Pocket.BOMBS;
				else if (currentPocket == Pocket.BOMBS)
					currentPocket = Pocket.POTIONS;
			}
		});

		LoadingImage.leftArrow.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (currentPocket == Pocket.POTIONS)
					currentPocket = Pocket.BOMBS;
				else if (currentPocket == Pocket.BOMBS)
					currentPocket = Pocket.PARCHMENTS;
				else if (currentPocket == Pocket.PARCHMENTS)
					currentPocket = Pocket.WEAPONS;
				else if (currentPocket == Pocket.WEAPONS)
					currentPocket = Pocket.POTIONS;
			}
		});

		LoadingImage.close.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!game.modalityMultiplayer) {
					if (game.screenManager.previousState == State.BATTLE)
						game.screenManager.swapScreen(State.BATTLE);
					else
						game.screenManager.swapScreen(State.PLAYING);
				} else
					game.screenManager.swapScreen(State.PLAYING);
			}
		});

		// END OPTIONS TABLE

		// POTIONS TABLE
		potionsTable = new Table();
		Label potionsLabel = new Label("Potions", MenuScreen.skin);

		potionsTable.setLayoutEnabled(false);
		potions = new TextButton[3];

		potions[0] = new TextButton("Blue potion  x", MenuScreen.skin);
		potions[1] = new TextButton("Red potion  x", MenuScreen.skin);
		potions[2] = new TextButton("Green potion  x", MenuScreen.skin);

		setTextPotions();

		potions[0].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.bluePotion);
				itemSelected = new Item(Element.POTION, Level.FIRST);
			}
		});

		potions[1].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.greenPotion);
				itemSelected = new Item(Element.POTION, Level.SECOND);
			}
		});

		potions[2].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.redPotion);
				itemSelected = new Item(Element.POTION, Level.THIRD);
			}
		});

		potionsLabel.setPosition(149, 425);
		potionsTable.add(potionsLabel);

		// setPositionPotion();
		potionsTable.add(potions[0]);
		potionsTable.add(potions[1]);
		potionsTable.add(potions[2]);
		// END POTIONS TABLE

		Label bombsLabel = new Label("Bombs", MenuScreen.skin);
		bombsTable = new Table();
		bombsTable.setLayoutEnabled(false);

		bombs = new TextButton[3];
		bombs[0] = new TextButton("Bomb lev1  x", MenuScreen.skin);
		bombs[1] = new TextButton("Bomb lev2  x", MenuScreen.skin);
		bombs[2] = new TextButton("Bomb lev3  x", MenuScreen.skin);
		setTextBomb();

		bombs[0].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.bomb);
				weaponSelected = new Weapon(character.Weapon.Level.lev1, character.Weapon.Type.Bomba);
			}
		});

		bombs[1].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.bomb);
				weaponSelected = new Weapon(character.Weapon.Level.lev2, character.Weapon.Type.Bomba);
			}
		});

		bombs[2].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.bomb);
				weaponSelected = new Weapon(character.Weapon.Level.lev3, character.Weapon.Type.Bomba);
			}
		});

		bombsLabel.setPosition(149, 425);
		bombsTable.add(bombsLabel);

		bombsTable.add(bombs[0]);
		bombsTable.add(bombs[1]);
		bombsTable.add(bombs[2]);
		// END BOMBS TABLE

		// WEAPON TABLE

		Label weaponsLabel;

		weaponsTable.setVisible(false);
		weaponsTable.setLayoutEnabled(false);
		weapons[0] = new TextButton("Weapon ", MenuScreen.skin);
		weapons[1] = new TextButton("Weapon ", MenuScreen.skin);
		weaponsLabel = new Label("Weapons", MenuScreen.skin);
		if (!game.modalityMultiplayer) {
			if (Game.world.player.bag.secondary_weapon != null) {
				weapons[1] = new TextButton(Game.world.player.bag.secondary_weapon.getType().toString() + " "
						+ Game.world.player.bag.secondary_weapon.getLevel().toString(), MenuScreen.skin);
			} else
				weapons[1] = new TextButton("", MenuScreen.skin);
			if (Game.world.player.primary_weapon != null) {
				weapons[0] = new TextButton(Game.world.player.primary_weapon.getType().toString() + " "
						+ Game.world.player.primary_weapon.getLevel().toString(), MenuScreen.skin);
			} else
				weapons[0] = new TextButton("", MenuScreen.skin);

		} else {
			if (Client.networkWorld.player.bag.secondary_weapon != null)
				weapons[1] = new TextButton(
						Client.networkWorld.player.bag.secondary_weapon.getType().toString() + " "
								+ Client.networkWorld.player.bag.secondary_weapon.getLevel().toString(),
						MenuScreen.skin);
			else
				weapons[1] = new TextButton("", MenuScreen.skin);
			if (Client.networkWorld.player.primary_weapon != null)
				weapons[0] = new TextButton(Client.networkWorld.player.primary_weapon.getType().toString() + " "
						+ Client.networkWorld.player.primary_weapon.getLevel().toString(), MenuScreen.skin);
			else
				weapons[0] = new TextButton("", MenuScreen.skin);
		}
		weapons[0].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.spear);
				if (!GameSlagyom.modalityMultiplayer)
					weaponSelected = new Weapon(Game.world.player.primary_weapon.getLevel(),
							Game.world.player.primary_weapon.getType());
				else
					weaponSelected = new Weapon(Client.networkWorld.player.primary_weapon.getLevel(),
							Client.networkWorld.player.primary_weapon.getType());
			}
		});
		weapons[1].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.spear);
				if (!GameSlagyom.modalityMultiplayer)
					weaponSelected = new Weapon(Game.world.player.bag.secondary_weapon.getLevel(),
							Game.world.player.bag.secondary_weapon.getType());
				else
					weaponSelected = new Weapon(Client.networkWorld.player.bag.secondary_weapon.getLevel(),
							Client.networkWorld.player.bag.secondary_weapon.getType());
			}
		});

		weaponsLabel.setPosition(140, 425);
		weaponsTable.add(weaponsLabel);

		weapons[0].setPosition(ScreenConfig.tableBagX, ScreenConfig.tableBagFirstY);
		weapons[1].setPosition(ScreenConfig.tableBagX, ScreenConfig.tableBagSecondY);
		weaponsTable.add(weapons);

		// END WEAPONS TABLE

		// PARCHMENTS TABLE
		parchmentsTable = new Table();
		Label parchmentsLabel;

		parchmentsTable.setVisible(false);
		parchmentsTable.setLayoutEnabled(false);

		parchmentsLabel = new Label("Parchments", MenuScreen.skin);
		parchments = new TextButton[2];
		parchments[0] = new TextButton("Parchment lev1  x", MenuScreen.skin);
		parchments[1] = new TextButton("Parchment lev2  x", MenuScreen.skin);
		setTextParchment();

		parchments[0].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.parchment);
				itemSelected = new Item(Element.PARCHMENT, Level.FIRST);
			}
		});

		parchments[1].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.parchment2);
				itemSelected = new Item(Element.PARCHMENT, Level.SECOND);
			}
		});

		parchmentsLabel.setPosition(120, 425);
		parchmentsTable.add(parchmentsLabel);

		parchmentsTable.add(parchments[0]);
		parchmentsTable.add(parchments[1]);

		// END PARCHMENTS TABLE

		stage.addActor(potionsTable);
		stage.addActor(bombsTable);
		stage.addActor(weaponsTable);
		stage.addActor(parchmentsTable);
		stage.addActor(optionsTable);
				
		Controllers.addListener(new MenuControllerListener(potionsTable));
		Controllers.addListener(new MenuControllerListener(weaponsTable));
		Controllers.addListener(new MenuControllerListener(parchmentsTable));
		Controllers.addListener(new MenuControllerListener(optionsTable));
	}

	@SuppressWarnings("static-access")
	protected void setTextWeapon() {
		if (!game.modalityMultiplayer) {
			weapons[0].setText(Game.world.player.primary_weapon.getType().toString() + " "
					+ Game.world.player.primary_weapon.getLevel().toString());
			if (Game.world.player.bag.secondary_weapon != null)
				weapons[1].setText(Game.world.player.bag.secondary_weapon.getType().toString() + " "
						+ Game.world.player.bag.secondary_weapon.getLevel().toString());
		} else {
			weapons[0].setText(Client.networkWorld.player.primary_weapon.getType().toString() + " "
					+ Client.networkWorld.player.primary_weapon.getLevel().toString());
			if (Client.networkWorld.player.bag.secondary_weapon != null)
				weapons[1].setText(Client.networkWorld.player.bag.secondary_weapon.getType().toString() + " "
						+ Client.networkWorld.player.bag.secondary_weapon.getLevel().toString());
		}
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

	@SuppressWarnings("static-access")
	@Override
	public void render(float delta) {

		if (Gdx.input.justTouched())
			game.loadingMusic.selectionSound.play();

		Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.begin();
		if (selection)
			selectionBackgroundSprite.draw(game.batch);
		else
			backgroundSprite.draw(game.batch);

		game.batch.end();

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			if (!game.modalityMultiplayer) {
				if (game.screenManager.previousState == State.BATTLE)
					game.screenManager.swapScreen(State.BATTLE);
				else
					game.screenManager.swapScreen(State.PLAYING);
			} else
				game.screenManager.swapScreen(State.PLAYING);
		}

		if (currentPocket == Pocket.POTIONS) {
			potionsTable.setVisible(true);
			setPositionPotion();
			bombsTable.setVisible(false);
			weaponsTable.setVisible(false);
			parchmentsTable.setVisible(false);
		} else if (currentPocket == Pocket.WEAPONS) {
			weaponsTable.setVisible(true);
			bombsTable.setVisible(false);
			if (!game.modalityMultiplayer)
				if (Game.world.player.bag.secondary_weapon != null)
					weapons[1].setVisible(true);
				else
					weapons[1].setVisible(false);
			else if (game.modalityMultiplayer)
				if (Client.networkWorld.player.bag.secondary_weapon != null && game.modalityMultiplayer)
					weapons[1].setVisible(true);
				else
					weapons[1].setVisible(false);
			potionsTable.setVisible(false);
			parchmentsTable.setVisible(false);
		} else if (currentPocket == Pocket.PARCHMENTS) {
			parchmentsTable.setVisible(true);
			setPositionParchement();
			bombsTable.setVisible(false);
			potionsTable.setVisible(false);
			weaponsTable.setVisible(false);
		} else if (currentPocket == Pocket.BOMBS) {
			parchmentsTable.setVisible(false);
			bombsTable.setVisible(true);
			setPositionBomb();
			potionsTable.setVisible(false);
			weaponsTable.setVisible(false);
		}
		stage.act();
		stage.draw();
	}

	@SuppressWarnings("static-access")
	public void setTextParchment() {
		if (!game.modalityMultiplayer) {
			parchments[0]
					.setText("Parchment lev1  x" + Game.world.player.bag.getNumberOf(Element.PARCHMENT, Level.FIRST));
			parchments[1]
					.setText("Parchment lev2  x" + Game.world.player.bag.getNumberOf(Element.PARCHMENT, Level.SECOND));
		} else {
			parchments[0].setText(
					"Parchment lev1  x" + Client.networkWorld.player.bag.getNumberOf(Element.PARCHMENT, Level.FIRST));
			parchments[1].setText(
					"Parchment lev2  x" + Client.networkWorld.player.bag.getNumberOf(Element.PARCHMENT, Level.SECOND));
		}
	}

	@SuppressWarnings("static-access")
	public void setTextPotions() {
		if (!game.modalityMultiplayer) {
			potions[0].setText("Blue potion  x" + Game.world.player.bag.getNumberOf(Element.POTION, Level.FIRST));
			potions[1].setText("Red potion  x" + Game.world.player.bag.getNumberOf(Element.POTION, Level.SECOND));
			potions[2].setText("Green potion  x" + Game.world.player.bag.getNumberOf(Element.POTION, Level.THIRD));
		} else {
			potions[0].setText(
					"Blue potion  x" + Client.networkWorld.player.bag.getNumberOf(Element.POTION, Level.FIRST));
			potions[1].setText(
					"Red potion  x" + Client.networkWorld.player.bag.getNumberOf(Element.POTION, Level.SECOND));
			potions[2].setText(
					"Green potion  x" + Client.networkWorld.player.bag.getNumberOf(Element.POTION, Level.THIRD));
		}
	}

	@SuppressWarnings("static-access")
	public void setTextBomb() {
		if (!game.modalityMultiplayer) {
			bombs[0].setText("Bomb lev1   x" + Game.world.player.bag.getNumberOfBomb(character.Weapon.Level.lev1));
			bombs[1].setText("Bomb lev2   x" + Game.world.player.bag.getNumberOfBomb(character.Weapon.Level.lev2));
			bombs[2].setText("Bomb lev3   x" + Game.world.player.bag.getNumberOfBomb(character.Weapon.Level.lev3));
		} else {
			bombs[0].setText(
					"Bomb lev1   x" + Client.networkWorld.player.bag.getNumberOfBomb(character.Weapon.Level.lev1));
			bombs[1].setText(
					"Bomb lev2   x" + Client.networkWorld.player.bag.getNumberOfBomb(character.Weapon.Level.lev2));
			bombs[2].setText(
					"Bomb lev3   x" + Client.networkWorld.player.bag.getNumberOfBomb(character.Weapon.Level.lev3));
		}
	}

	@SuppressWarnings("static-access")
	public void setPositionBomb() {
		if (!game.modalityMultiplayer) {
			if (Game.world.player.bag.getNumberOfBomb(character.Weapon.Level.lev1) <= 0) {
				bombs[0].setVisible(false);
				bombs[1].setPosition(ScreenConfig.tableBagX, ScreenConfig.tableBagFirstY);
				bombs[2].setPosition(ScreenConfig.tableBagX, ScreenConfig.tableBagSecondY);
			} else {
				bombs[0].setVisible(true);
				bombs[0].setPosition(ScreenConfig.tableBagX, ScreenConfig.tableBagFirstY);
				bombs[1].setPosition(ScreenConfig.tableBagX, ScreenConfig.tableBagSecondY);
				bombs[2].setPosition(ScreenConfig.tableBagX, ScreenConfig.tableBagThirdY);
			}
			if (Game.world.player.bag.getNumberOfBomb(character.Weapon.Level.lev2) <= 0) {
				bombs[1].setVisible(false);
				bombs[2].setPosition(ScreenConfig.tableBagX, bombs[1].getY());
			} else
				bombs[1].setVisible(true);
			if (Game.world.player.bag.getNumberOfBomb(character.Weapon.Level.lev3) <= 0)
				bombs[2].setVisible(false);
			else
				bombs[2].setVisible(true);
		} else {
			if (Client.networkWorld.player.bag.getNumberOfBomb(character.Weapon.Level.lev1) <= 0) {
				bombs[0].setVisible(false);
				bombs[1].setPosition(ScreenConfig.tableBagX, ScreenConfig.tableBagFirstY);
				bombs[2].setPosition(ScreenConfig.tableBagX, ScreenConfig.tableBagSecondY);
			} else {
				bombs[0].setVisible(true);
				bombs[0].setPosition(ScreenConfig.tableBagX, ScreenConfig.tableBagFirstY);
				bombs[1].setPosition(ScreenConfig.tableBagX, ScreenConfig.tableBagSecondY);
				bombs[2].setPosition(ScreenConfig.tableBagX, ScreenConfig.tableBagThirdY);
			}
			if (Client.networkWorld.player.bag.getNumberOfBomb(character.Weapon.Level.lev2) <= 0) {
				bombs[1].setVisible(false);
				bombs[2].setPosition(ScreenConfig.tableBagX, bombs[1].getY());
			} else
				bombs[1].setVisible(true);
			if (Client.networkWorld.player.bag.getNumberOfBomb(character.Weapon.Level.lev3) <= 0)
				bombs[2].setVisible(false);
			else
				bombs[2].setVisible(true);
		}
	}

	@SuppressWarnings("static-access")
	public void setPositionPotion() {
		if (!game.modalityMultiplayer) {
			if (Game.world.player.bag.getNumberOf(Element.POTION, Level.FIRST) <= 0) {
				potions[0].setVisible(false);
				potions[0].setPosition(ScreenConfig.tableBagX, ScreenConfig.tableBagFirstY);
				potions[1].setPosition(ScreenConfig.tableBagX, ScreenConfig.tableBagFirstY);
				potions[2].setPosition(ScreenConfig.tableBagX, ScreenConfig.tableBagSecondY);
			} else {
				potions[0].setVisible(true);
				potions[0].setPosition(ScreenConfig.tableBagX, ScreenConfig.tableBagFirstY);
				potions[1].setPosition(ScreenConfig.tableBagX, ScreenConfig.tableBagSecondY);
				potions[2].setPosition(ScreenConfig.tableBagX, ScreenConfig.tableBagThirdY);
			}
			if (Game.world.player.bag.getNumberOf(Element.POTION, Level.SECOND) <= 0) {
				potions[1].setVisible(false);
				potions[2].setPosition(ScreenConfig.tableBagX, potions[1].getY());
			} else
				potions[1].setVisible(true);
			if (Game.world.player.bag.getNumberOf(Element.POTION, Level.THIRD) <= 0)
				potions[2].setVisible(false);
			else
				potions[2].setVisible(true);
		} else {
			if (Client.networkWorld.player.bag.getNumberOf(Element.POTION, Level.FIRST) <= 0) {
				potions[0].setVisible(false);
				potions[1].setPosition(ScreenConfig.tableBagX, ScreenConfig.tableBagFirstY);
				potions[2].setPosition(ScreenConfig.tableBagX, ScreenConfig.tableBagSecondY);
			} else {
				potions[0].setVisible(true);
				potions[0].setPosition(ScreenConfig.tableBagX, ScreenConfig.tableBagFirstY);
				potions[1].setPosition(ScreenConfig.tableBagX, ScreenConfig.tableBagSecondY);
				potions[2].setPosition(ScreenConfig.tableBagX, ScreenConfig.tableBagThirdY);
			}
			if (Client.networkWorld.player.bag.getNumberOf(Element.POTION, Level.SECOND) <= 0) {
				potions[1].setVisible(false);
				potions[2].setPosition(ScreenConfig.tableBagX, potions[1].getY());
			} else
				potions[1].setVisible(true);
			if (Client.networkWorld.player.bag.getNumberOf(Element.POTION, Level.THIRD) <= 0)
				potions[2].setVisible(false);
			else
				potions[2].setVisible(true);
		}
	}

	@SuppressWarnings("static-access")
	public void setPositionParchement() {
		if (!game.modalityMultiplayer) {
			if (Game.world.player.bag.getNumberOf(Element.PARCHMENT, Level.FIRST) <= 0) {
				parchments[0].setVisible(false);
				parchments[1].setPosition(ScreenConfig.tableBagX, ScreenConfig.tableBagFirstY);
			} else {
				parchments[0].setVisible(true);
				parchments[0].setPosition(ScreenConfig.tableBagX, ScreenConfig.tableBagFirstY);
				parchments[1].setPosition(ScreenConfig.tableBagX, ScreenConfig.tableBagSecondY);
			}
			if (Game.world.player.bag.getNumberOf(Element.PARCHMENT, Level.SECOND) <= 0)
				parchments[1].setVisible(false);
			else
				parchments[1].setVisible(true);
		} else {
			if (Client.networkWorld.player.bag.getNumberOf(Element.PARCHMENT, Level.FIRST) <= 0) {
				parchments[0].setVisible(false);
				parchments[1].setPosition(ScreenConfig.tableBagX, ScreenConfig.tableBagFirstY);
			} else {
				parchments[0].setVisible(true);
				parchments[0].setPosition(ScreenConfig.tableBagX, ScreenConfig.tableBagFirstY);
				parchments[1].setPosition(ScreenConfig.tableBagX, ScreenConfig.tableBagSecondY);
			}
			if (Client.networkWorld.player.bag.getNumberOf(Element.PARCHMENT, Level.SECOND) <= 0)
				parchments[1].setVisible(false);
			else
				parchments[1].setVisible(true);
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
