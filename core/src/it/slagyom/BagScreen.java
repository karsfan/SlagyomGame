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

import it.slagyom.src.World.Game;
import it.slagyom.src.World.Weapon;
import staticObjects.Item;
import staticObjects.Item.Level;
import staticObjects.StaticObject.Element;

public class BagScreen implements Screen {
	private enum Pocket {
		POTIONS, WEAPONS, PARCHMENTS, BOMBS
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
	private Table bombsTable;
	private Table potionsTable;
	private Table parchmentsTable;

	private Table optionsTable;
	private TextButton use;
	private TextButton delete;
	private TextButton exit;
	public Item itemSelected;
	private int firstX;
	private int firstY;
	private int secondX;
	private int secondY;
	private int thirdX;
	private int thirdY;
	TextButton[] potions;
	TextButton weapons;
	TextButton[] bombs;
	public Weapon weaponSelected;
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
				Game.player.bag.removeItem(itemSelected.getElement(), itemSelected.getLevel());
				potions[0].setText("Blue potion  x" + Game.player.bag.getNumberOf(Element.POTION, Level.FIRST));
				potions[1].setText("Green potion  x" + Game.player.bag.getNumberOf(Element.POTION, Level.SECOND));
				potions[2].setText("Red potion  x" + Game.player.bag.getNumberOf(Element.POTION, Level.THIRD));
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

		// END OPTIONS TABLE

		// POTIONS TABLE
		potionsTable = new Table();
		Label potionsLabel;

		potionsTable.setLayoutEnabled(false);

		potionsLabel = new Label("Potions", MenuScreen.skin);
		potions = new TextButton[3];
		potions[0] = new TextButton("Blue potion    x" + Game.player.bag.getNumberOf(Element.POTION, Level.FIRST),
				MenuScreen.skin);
		potions[1] = new TextButton("Red potion    x" + Game.player.bag.getNumberOf(Element.POTION, Level.SECOND),
				MenuScreen.skin);
		potions[2] = new TextButton("Green potion  x" + Game.player.bag.getNumberOf(Element.POTION, Level.THIRD),
				MenuScreen.skin);

		potions[0].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.bluePotion);
				itemSelected = new Item(Element.POTION,Level.FIRST);
			}
		});

		potions[1].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.greenPotion);
				itemSelected = new Item(Element.POTION,Level.SECOND);
			}
		});

		potions[2].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.redPotion);
				itemSelected = new Item(Element.POTION,Level.THIRD);
			}
		});

		potionsLabel.setPosition(149, 425);
		potionsTable.add(potionsLabel);
		firstX = 350;
		firstY = 420;
		secondY = 370;
		thirdY = 320;
		potions[0].setPosition(firstX, firstY);
		potionsTable.add(potions[0]);

		potions[1].setPosition(firstX, secondY);
		potionsTable.add(potions[1]);

		potions[2].setPosition(firstX, thirdY);
		potionsTable.add(potions[2]);
		// END POTIONS TABLE
		Label bombsLabel;
		bombsTable = new Table();
		bombsTable.setLayoutEnabled(false);

		bombsLabel = new Label("Bombs", MenuScreen.skin);
		bombs = new TextButton[3];
		bombs[0] = new TextButton("Bomb lev1   x" + Game.player.bag.getNumberOfBomb(it.slagyom.src.World.Weapon.Level.lev1),
				MenuScreen.skin);
		bombs[1] = new TextButton("Bomb lev2   x" + Game.player.bag.getNumberOfBomb(it.slagyom.src.World.Weapon.Level.lev2),
				MenuScreen.skin);
		bombs[2] = new TextButton("Bomb lev3   x" + Game.player.bag.getNumberOfBomb(it.slagyom.src.World.Weapon.Level.lev3),
				MenuScreen.skin);

		bombs[0].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.bluePotion);
				weaponSelected = new Weapon(it.slagyom.src.World.Weapon.Level.lev1, it.slagyom.src.World.Weapon.Type.Bomba);
			}
		});

		bombs[1].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.greenPotion);
				weaponSelected = new Weapon(it.slagyom.src.World.Weapon.Level.lev2, it.slagyom.src.World.Weapon.Type.Bomba);
			}
		});

		bombs[2].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.redPotion);
				weaponSelected = new Weapon(it.slagyom.src.World.Weapon.Level.lev3, it.slagyom.src.World.Weapon.Type.Bomba);
			}
		});

		bombsLabel.setPosition(149, 425);
		bombsTable.add(bombsLabel);
		firstX = 350;
		firstY = 420;
		secondY = 370;
		thirdY = 320;
		bombs[0].setPosition(firstX, firstY);
		bombsTable.add(bombs[0]);

		bombs[1].setPosition(firstX, secondY);
		bombsTable.add(bombs[1]);

		bombs[2].setPosition(firstX, thirdY);
		bombsTable.add(bombs[2]);
		// END bombs TABLE
		// WEAPON TABLE
		weaponsTable = new Table();
		Label weaponsLabel;
		

		weaponsTable.setVisible(false);
		weaponsTable.setLayoutEnabled(false);

		weaponsLabel = new Label("Weapons", MenuScreen.skin);

		weapons = new TextButton(Game.player.primary_weapon.getType().toString() + " "
				+ Game.player.primary_weapon.getLevel().toString(), MenuScreen.skin);
		
		weapons.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showInfo(LoadingImage.spear);
			}
		});


		weaponsLabel.setPosition(140, 425);
		weaponsTable.add(weaponsLabel);

		weapons.setPosition(350, 420);
		weaponsTable.add(weapons);

		
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
		stage.addActor(bombsTable);
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

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			game.screenManager.swapScreen(ScreenManager.State.PLAYING);
			Game.world.semaphore.release();
		}

		if (currentPocket == Pocket.POTIONS) {
			potionsTable.setVisible(true);
			bombsTable.setVisible(false);
			if (Game.player.bag.getNumberOf(Element.POTION, Level.FIRST) <= 0) {
				potions[0].setVisible(false);
				potions[1].setPosition(firstX, firstY);
				potions[2].setPosition(firstX, secondY);
			} else {
				potions[0].setVisible(true);
				potions[0].setPosition(firstX, firstY);
				potions[1].setPosition(firstX, secondY);
				potions[2].setPosition(firstX, thirdY);
			}
			if (Game.player.bag.getNumberOf(Element.POTION, Level.SECOND) <= 0) {
				potions[1].setVisible(false);
				potions[2].setPosition(firstX, secondY);
			} else
				potions[1].setVisible(true);
			if (Game.player.bag.getNumberOf(Element.POTION, Level.THIRD) <= 0)
				potions[2].setVisible(false);
			else
				potions[2].setVisible(true);

			weaponsTable.setVisible(false);
			parchmentsTable.setVisible(false);
		} else if (currentPocket == Pocket.WEAPONS) {
			weaponsTable.setVisible(true);
			bombsTable.setVisible(false);
			if(Game.player.bag.secondary_weapon != null)
				weapons.setVisible(true);
			else
				weapons.setVisible(false);
			potionsTable.setVisible(false);
			parchmentsTable.setVisible(false);
		} else if (currentPocket == Pocket.PARCHMENTS) {
			parchmentsTable.setVisible(true);
			bombsTable.setVisible(false);
			potionsTable.setVisible(false);
			weaponsTable.setVisible(false);
		}else if (currentPocket == Pocket.BOMBS) {
			parchmentsTable.setVisible(false);
			bombsTable.setVisible(true);
			if (Game.player.bag.getNumberOfBomb(it.slagyom.src.World.Weapon.Level.lev1) <= 0) {
				bombs[0].setVisible(false);
				bombs[1].setPosition(firstX, firstY);
				bombs[2].setPosition(firstX, secondY);
			} else {
				bombs[0].setVisible(true);
				bombs[0].setPosition(firstX, firstY);
				bombs[1].setPosition(firstX, secondY);
				bombs[2].setPosition(firstX, thirdY);
			}
			if (Game.player.bag.getNumberOfBomb(it.slagyom.src.World.Weapon.Level.lev2)<= 0) {
				bombs[1].setVisible(false);
				bombs[2].setPosition(firstX, secondY);
			} else
				bombs[1].setVisible(true);
			if (Game.player.bag.getNumberOfBomb(it.slagyom.src.World.Weapon.Level.lev3) <= 0)
				bombs[2].setVisible(false);
			else
				bombs[2].setVisible(true);
			potionsTable.setVisible(false);
			weaponsTable.setVisible(false);
		}
		stage.act();
		stage.draw();
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
