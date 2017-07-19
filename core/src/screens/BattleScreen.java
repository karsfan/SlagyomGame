package screens;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
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
import character.Weapon.Type;
import gameManager.GameSlagyom;
import gameManager.LoadingImage;
import gameManager.ScreenManager.State;
import hud.BattleHud;
import staticObjects.Item;
import staticObjects.Item.Level;
import staticObjects.StaticObject.Element;

public class BattleScreen implements Screen, ControllerListener {

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
	public PovDirection directionGamePad = null;
	public boolean movesGamePad = false;
	public int buttonCodePressed;
	public boolean buttonPressed = false;

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
		if (battle.character.soundBomb) {
			gameslagyom.loadingMusic.bombSound.play();
			battle.character.soundBomb = false;
		}
		Iterator<Bomb> bombIterator = player.bag.bombe.iterator();
		while (bombIterator.hasNext()) {
			Bomb searching = (Bomb) bombIterator.next();
			if (searching.lanciata == true) {
				gameslagyom.batch.draw(gameslagyom.loadingImage.bombImage, searching.getMainX(), searching.getMainY(),
						searching.getWidth() + 20, searching.getHeight() + 20);
			}
		}
		Iterator<Bomb> bombIterator1 = ((Enemy) enemy).getBombe().iterator();

		while (bombIterator1.hasNext()) {
			Bomb searching1 = (Bomb) bombIterator1.next();
			if (searching1.lanciata == true) {
				gameslagyom.batch.draw(gameslagyom.loadingImage.bombImage, searching1.getMainX(), searching1.getMainY(),
						searching1.getWidth() + 20, searching1.getHeight() + 20);
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
			if (Gdx.input.isKeyJustPressed(Keys.ENTER)
					|| (buttonPressed && (buttonCodePressed == 7 || buttonCodePressed == 0))) {
				gameslagyom.screenManager.swapScreen(State.PLAYING);
				buttonPressed = false;
			}

	}

	private void handleInput(float dt) {

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE) || (buttonPressed && buttonCodePressed == 7)) {
			gameslagyom.screenManager.swapScreen(State.PAUSE);
			buttonPressed = false;
		}
		if (this.battle.character.health < 300) {
			if ((Gdx.input.isKeyJustPressed(Keys.NUM_1) || (buttonPressed && buttonCodePressed == 1))
					&& battle.character.bag.getNumberOf(Element.POTION, Level.FIRST) > 0) {
				battle.character.useItem(new Item(Element.POTION, Level.FIRST));
				gameslagyom.loadingMusic.upgradeSound.play();
				buttonPressed = false;
			} else if ((Gdx.input.isKeyJustPressed(Keys.NUM_2) || (buttonPressed && buttonCodePressed == 2))
					&& battle.character.bag.getNumberOf(Element.POTION, Level.SECOND) > 0) {
				battle.character.useItem(new Item(Element.POTION, Level.SECOND));
				buttonPressed = false;
				gameslagyom.loadingMusic.upgradeSound.play();
			} else if ((Gdx.input.isKeyJustPressed(Keys.NUM_3) || (buttonPressed && buttonCodePressed == 3))
					&& battle.character.bag.getNumberOf(Element.POTION, Level.THIRD) > 0) {
				battle.character.useItem(new Item(Element.POTION, Level.THIRD));
				gameslagyom.loadingMusic.upgradeSound.play();
				buttonPressed = false;
			}
		}
		moveCharacter(dt);
	}

	private void moveCharacter(float dt) {

		if (Gdx.input.isKeyJustPressed(Keys.O) || (buttonPressed && buttonCodePressed == 4)) {
			battle.character.swapWeapon();
			buttonPressed = false;
		}
		if (Gdx.input.isKeyPressed(Keys.SPACE) || (buttonPressed && buttonCodePressed == 5)) {
			battle.character.caricaBomba(dt);
			battle.character.lanciaBomba = true;
		} else {
			if (battle.character.lanciaBomba) {
				battle.character.lancia();
				buttonPressed = false;
				battle.character.lanciaBomba = false;
				battle.character.forza = 50;
			}
			if (Gdx.input.isKeyJustPressed(Keys.UP) || (movesGamePad && directionGamePad == PovDirection.north)) {
				battle.character.jump(dt);
				movesGamePad = false;
			} else if (Gdx.input.isKeyPressed(Keys.LEFT) || (movesGamePad && directionGamePad == PovDirection.west)) {
				battle.character.movesLeft(dt);
			} else if (Gdx.input.isKeyPressed(Keys.RIGHT) || (movesGamePad && directionGamePad == PovDirection.east)) {
				battle.character.movesRight(dt);
			} else {
				battle.character.stand();
			}
			if (Gdx.input.isKeyJustPressed(Keys.A) || (buttonPressed && buttonCodePressed == 0)) {
				buttonPressed = false;
				if (battle.character.primary_weapon.getType() == Type.Sword)
					gameslagyom.loadingMusic.swordSound.play();
				else if (battle.character.primary_weapon.getType() == Type.Bow) {
					gameslagyom.loadingMusic.arrowSound.play();
				}
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
		if (buttonCode == 5) {
			buttonPressed = true;
			buttonCodePressed = buttonCode;
			return true;
		}
		buttonPressed = false;
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		if (buttonCode == 0 || buttonCode == 1 || buttonCode == 2 || buttonCode == 3 || buttonCode == 4
				|| buttonCode == 7) {
			buttonPressed = true;
			buttonCodePressed = buttonCode;
			return true;
		}
		buttonPressed = false;
		buttonCodePressed = 111111;
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		if (value == PovDirection.east) {
			movesGamePad = true;
			directionGamePad = value;
			return true;
		} else if (value == PovDirection.north || value == PovDirection.northEast || value == PovDirection.northWest) {
			movesGamePad = true;
			directionGamePad = PovDirection.north;
			return true;
		} else if (value == PovDirection.west) {
			movesGamePad = true;
			directionGamePad = value;
			return true;
		}
		movesGamePad = false;
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
