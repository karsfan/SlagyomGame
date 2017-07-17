package screens;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import battle.Battle;
import battle.CharacterBattle;
import battle.Enemy;
import battle.Fighting;
import battle.Pack;
import character.Arrow;
import character.Bomb;
import character.Weapon;
import character.DynamicObjects.StateDynamicObject;
import gameManager.GameSlagyom;
import gameManager.LoadingImage;
import gameManager.ScreenManager.State;
import hud.BattleHud;
import multiplayer.NetworkCharacterBattle;
import staticObjects.Item;
import staticObjects.Item.Level;
import staticObjects.StaticObject.Element;

public class BattleScreen implements Screen {

	public OrthographicCamera gamecam;
	public Viewport gamePort;
	public GameSlagyom gameslagyom;
	public BattleHud hud;
	public Battle battle;
	public boolean youWin = false;
	public boolean youLose = false;

	public Table packTable;
	public Label bluePotion;
	public Label redPotion;
	public Label greenPotion;
	public Label parchmentLev1;
	public Label parchmentLev2;
	public Label bomb;
	public Label coin;

	public BattleScreen(GameSlagyom gameslagyom, Battle battle) {
		this.gameslagyom = gameslagyom;
		this.battle = battle;
		gamecam = new OrthographicCamera();
		gamePort = new ExtendViewport(1440, 960, gamecam);
		gamePort.apply();
		gamecam.position.x = battle.character.getX();
		gamecam.position.y = battle.character.getY();
		hud = new BattleHud(gameslagyom, battle);
		packTable = new Table();
		packTable.setLayoutEnabled(false);
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		update(delta);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		gameslagyom.batch.begin();
		draw();
		gameslagyom.batch.end();
		hud.stage.draw();
	}

	@SuppressWarnings("static-access")
	private void draw() {

		gameslagyom.batch.draw(LoadingImage.getBattleBgImage(), 0, 0);

		Fighting enemy = battle.enemy;
		gameslagyom.batch.draw(gameslagyom.loadingImage.getBattleFrame(enemy), enemy.getX(), enemy.getY(),
				enemy.getWidth(), enemy.getHeight());
		CharacterBattle player = battle.character;
		gameslagyom.batch.draw(gameslagyom.loadingImage.getBattleFrame(player), player.getX(), player.getY(),
				player.getWidth(), player.getHeight());

		Iterator<Bomb> bombIterator = player.bag.bombe.iterator();
		while (bombIterator.hasNext()) {
			Bomb searching = (Bomb) bombIterator.next();
			if (searching.lanciata == true) {
				gameslagyom.batch.draw(gameslagyom.loadingImage.getTileImage(searching), searching.getMainX(),
						searching.getMainY(), searching.getWidth() + 10, searching.getHeight() + 10);
			}
		}
		Iterator<Bomb> bombIterator1 = ((Enemy) enemy).getBombe().iterator();

		while (bombIterator1.hasNext()) {
			Bomb searching1 = (Bomb) bombIterator1.next();
			if (searching1.lanciata == true) {
				gameslagyom.batch.draw(gameslagyom.loadingImage.getTileImage(searching1), searching1.getMainX(),
						searching1.getMainY(), searching1.getWidth() + 10, searching1.getHeight() + 10);
			}
		}
		if (!player.arrowsShooted.isEmpty()) {
			Iterator<Weapon> it = player.arrowsShooted.iterator();
			while (it.hasNext()) {
				Arrow ob = (Arrow) it.next();
				gameslagyom.batch.draw(gameslagyom.loadingImage.getArrowImage(ob.left), ob.x, ob.y, ob.getWidth() + 100,
						ob.getHeight() + 30);
			}
		}
		if (!((Enemy) enemy).arrowsShooted.isEmpty()) {
			Iterator<Weapon> it2 = ((Enemy) enemy).arrowsShooted.iterator();
			while (it2.hasNext()) {
				Arrow ob = (Arrow) it2.next();
				gameslagyom.batch.draw(gameslagyom.loadingImage.getArrowImage(ob.left), ob.x, ob.y, ob.getWidth() + 100,
						ob.getHeight() + 30);
			}
		}
		if (youWin)
			gameslagyom.batch.draw(LoadingImage.getYouWinImage(), 0, 0);
		else if (youLose)
			gameslagyom.batch.draw(LoadingImage.getYouLoseImage(), 0, 0);
	}

	public void update(float dt) {

		if (!youWin && !youLose) {
			handleInput(dt);
			hud.update(dt);
			if (battle.update(dt)) {
				if (battle.character.getHealth() == 10) {
					youLose = true;
				} else {
					youWin = true;
					battle.character.bag.addPack((Pack) ((Enemy) battle.enemy).getWin_bonus());
					gameslagyom.loadingMusic.cashSound.play(1.5f);
					if (((Pack) ((Enemy) battle.enemy).getWin_bonus()).getNumberOf("POTIONLEV1") > 0) {
						bluePotion = new Label(
								"Blue potion x" + Integer.toString(
										((Pack) ((Enemy) battle.enemy).getWin_bonus()).getNumberOf("POTIONLEV1")),
								MenuScreen.skin);
						bluePotion.setPosition(440, 410);
						packTable.add(bluePotion);
					}
					if (((Pack) ((Enemy) battle.enemy).getWin_bonus()).getNumberOf("POTIONLEV2") > 0) {
						redPotion = new Label(
								"Red potion x" + Integer.toString(
										((Pack) ((Enemy) battle.enemy).getWin_bonus()).getNumberOf("POTIONLEV2")),
								MenuScreen.skin);
						redPotion.setPosition(440, 346);
						packTable.add(redPotion);
					}
					if (((Pack) ((Enemy) battle.enemy).getWin_bonus()).getNumberOf("POTIONLEV3") > 0) {
						greenPotion = new Label(
								"Green potion x" + Integer.toString(
										((Pack) ((Enemy) battle.enemy).getWin_bonus()).getNumberOf("POTIONLEV3")),
								MenuScreen.skin);
						greenPotion.setPosition(440, 282);
						packTable.add(greenPotion);
					}
					if (((Pack) ((Enemy) battle.enemy).getWin_bonus()).getNumberOf("COIN") > 0) {
						coin = new Label(
								"Coins x" + Integer
										.toString(((Pack) ((Enemy) battle.enemy).getWin_bonus()).getNumberOf("COIN")),
								MenuScreen.skin);
						coin.setPosition(800, 410);
						packTable.add(coin);
					}
					if (((Pack) ((Enemy) battle.enemy).getWin_bonus()).getNumberOf("PARCHLEV1") > 0) {
						parchmentLev1 = new Label(
								"Parch. lev1 x" + Integer.toString(
										((Pack) ((Enemy) battle.enemy).getWin_bonus()).getNumberOf("PARCHLEV1")),
								MenuScreen.skin);
						parchmentLev1.setPosition(800, 346);
						packTable.add(parchmentLev1);
					}
					if (((Pack) ((Enemy) battle.enemy).getWin_bonus()).getNumberOf("PARCHLEV2") > 0) {
						parchmentLev2 = new Label(
								"Parch. lev2 x" + Integer.toString(
										((Pack) ((Enemy) battle.enemy).getWin_bonus()).getNumberOf("PARCHLEV2")),
								MenuScreen.skin);
						parchmentLev2.setPosition(800, 282);
						packTable.add(parchmentLev2);
					}
				}
			}
			hud.stage.addActor(packTable);
		}
		if (youWin || youLose)
			if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
				if (battle.character instanceof NetworkCharacterBattle) {
					gameslagyom.screenManager.swapScreen(State.PLAYING);
				} else
					gameslagyom.screenManager.swapScreen(State.PLAYING);
			}

	}

	private void handleInput(float dt) {

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			gameslagyom.screenManager.swapScreen(State.PAUSE);
		}
		if (this.battle.character.health < 300) {
			if (Gdx.input.isKeyJustPressed(Keys.NUM_1)
					&& battle.character.bag.getNumberOf(Element.POTION, Level.FIRST) > 0) {
				battle.character.useItem(new Item(Element.POTION, Level.FIRST));
				gameslagyom.loadingMusic.upgradeSound.play();
			}
			if (Gdx.input.isKeyJustPressed(Keys.NUM_2)
					&& battle.character.bag.getNumberOf(Element.POTION, Level.SECOND) > 0) {
				battle.character.useItem(new Item(Element.POTION, Level.SECOND));
				gameslagyom.loadingMusic.upgradeSound.play();
			}
			if (Gdx.input.isKeyJustPressed(Keys.NUM_3)
					&& battle.character.bag.getNumberOf(Element.POTION, Level.THIRD) > 0) {
				battle.character.useItem(new Item(Element.POTION, Level.THIRD));
				gameslagyom.loadingMusic.upgradeSound.play();
			}
		}
		moveCharacter(dt);
	}

	private void moveCharacter(float dt) {

		if (Gdx.input.isKeyJustPressed(Keys.O)) {
			// System.out.println("Swap");
			battle.character.swapWeapon();
		}
		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			battle.character.caricaBomba(dt);
			battle.character.lanciaBomba = true;
		} else {
			if (battle.character.lanciaBomba) {
				battle.character.lancia();
				// System.out.println("lancia");
				battle.character.lanciaBomba = false;
				battle.character.forza = 50;
			}

			if (Gdx.input.isKeyJustPressed(Keys.S))
				battle.character.setState(StateDynamicObject.DEFENDING, dt);

			if (Gdx.input.isKeyJustPressed(Keys.UP)) {
				battle.character.jump(dt);

			} else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
				battle.character.movesLeft(dt);
			} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
				battle.character.movesRight(dt);
			} else {
				battle.character.stand();
			}
			if (Gdx.input.isKeyJustPressed(Keys.A)) {
				gameslagyom.loadingMusic.swordSound.play();
				if (battle.character.left)
					battle.character.fightLeft(dt);
				else
					battle.character.fightRight(dt);
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
		gamecam.update();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
