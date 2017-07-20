package multiplayer;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import battle.Battle;
import battle.Enemy;
import character.Arrow;
import character.Bomb;
import character.DynamicObjects;
import character.Weapon;
import character.Weapon.Type;
import gameManager.GameSlagyom;
import gameManager.LoadingImage;
import gameManager.ScreenManager.State;
import screens.BattleScreen;
import screens.MenuScreen;
import staticObjects.Item;
import staticObjects.Item.Level;
import staticObjects.StaticObject.Element;
import world.GameConfig;

public class NetworkBattleScreen extends BattleScreen {
	public Client client;
	public boolean weapon_primary_comunicated = false;

	public NetworkBattleScreen(GameSlagyom gameslagyom, Battle battle, Client client) {
		super(gameslagyom, battle);
		this.client = client;
	}

	@Override
	public void render(float delta) {
		if (!weapon_primary_comunicated && battle.enemy instanceof NetworkCharacterBattle)
			sendPrimaryWeapon();
		update(delta);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gameslagyom.batch.begin();
		draw();
		gameslagyom.batch.end();
		hud.stage.draw();
		if (client.serverDisconnected)
			gameslagyom.screenManager.swapScreen(State.MENU);
		if (client.soundPotionBattle) {
			client.soundPotionBattle = false;
			gameslagyom.loadingMusic.upgradeSound.play(GameConfig.soundVolume);
		}
	}

	private void sendPrimaryWeapon() {
		client.writer.println(5 + " " + ((NetworkCharacterBattle) battle.character).ID + " "
				+ battle.character.primary_weapon.getType() + " " + battle.character.primary_weapon.getLevel() + " " + 0
				+ ";" + ((NetworkCharacterBattle) battle.character).IDOtherPlayer + ";");
		client.writer.flush();
		weapon_primary_comunicated = true;
	}

	@SuppressWarnings("static-access")
	private void draw() {
		gameslagyom.batch.draw(LoadingImage.getBattleBgImage(), 0, 0);

		NetworkCharacterBattle player = (NetworkCharacterBattle) battle.character;
		gameslagyom.batch.draw(gameslagyom.loadingImage.getBattleFrame(player), player.getX(), player.getY(),
				player.getWidth(), player.getHeight());
		DynamicObjects enemy;
		if (battle.enemy instanceof NetworkCharacterBattle)
			enemy = (NetworkCharacterBattle) battle.enemy;
		else
			enemy = (NetworkEnemy) battle.enemy;
		gameslagyom.batch.draw(gameslagyom.loadingImage.getBattleFrame(enemy), enemy.getX(), enemy.getY(),
				enemy.getWidth(), enemy.getHeight());

		Iterator<Bomb> bombIterator = player.bag.bombe.iterator();
		while (bombIterator.hasNext()) {
			Bomb searching = (Bomb) bombIterator.next();
			if (searching.launched == true) {
				gameslagyom.batch.draw(gameslagyom.loadingImage.bombImage, searching.getMainX(), searching.getMainY(),
						searching.getWidth() + 20, searching.getHeight() + 20);
			}
		}
		Iterator<Bomb> bombIterator1 = null;
		if (battle.enemy instanceof NetworkCharacterBattle)
			bombIterator1 = ((NetworkCharacterBattle) enemy).bag.bombe.iterator();
		else
			bombIterator1 = ((NetworkEnemy) enemy).getBombe().iterator();

		while (bombIterator1.hasNext()) {
			Bomb searching1 = (Bomb) bombIterator1.next();
			if (searching1.launched == true) {
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
		if (battle.enemy instanceof NetworkCharacterBattle) {
			if (!((NetworkCharacterBattle) enemy).arrowsShooted.isEmpty()) {
				Iterator<Weapon> it2 = ((NetworkCharacterBattle) enemy).arrowsShooted.iterator();
				while (it2.hasNext()) {
					Arrow ob = (Arrow) it2.next();
					gameslagyom.batch.draw(gameslagyom.loadingImage.getArrowImage(ob.left), ob.x, ob.y,
							ob.getWidth() + 100, ob.getHeight() + 30);
				}
			}
		} else {
			if (!((Enemy) enemy).arrowsShooted.isEmpty()) {
				Iterator<Weapon> it2 = ((Enemy) enemy).arrowsShooted.iterator();
				while (it2.hasNext()) {
					Arrow ob = (Arrow) it2.next();
					gameslagyom.batch.draw(gameslagyom.loadingImage.getArrowImage(ob.left), ob.x, ob.y,
							ob.getWidth() + 100, ob.getHeight() + 30);
				}
			}
		}
		if (youWin)
			gameslagyom.batch.draw(LoadingImage.getYouWinImage(), 0, 0);
		else if (youLose)
			gameslagyom.batch.draw(LoadingImage.getYouLoseImage(), 0, 0);
	}

	@SuppressWarnings("static-access")
	@Override
	public void update(float dt) {
		if (!youWin && !youLose) {
			handleInput(dt);
			hud.update(dt);
			if (battle.update(dt)) {
				if (battle.character.getHealth() <= 0) {
					youLose = true;
				} else {
					youWin = true;
					Client.networkWorld.player.bag.addPack(((NetworkBattle) battle).win_bonus);
					gameslagyom.loadingMusic.cashSound.play(1.5f);
					if (((NetworkBattle) battle).win_bonus.getNumberOf("POTIONLEV1") > 0) {
						bluePotion = new Label(
								"Blue potion x" + Integer
										.toString(((NetworkBattle) battle).win_bonus.getNumberOf("POTIONLEV1")),
								MenuScreen.skin);
						bluePotion.setPosition(440, 410);
						packTable.add(bluePotion);
					}
					if (((NetworkBattle) battle).win_bonus.getNumberOf("POTIONLEV2") > 0) {
						redPotion = new Label(
								"Red potion x" + Integer
										.toString(((NetworkBattle) battle).win_bonus.getNumberOf("POTIONLEV2")),
								MenuScreen.skin);
						redPotion.setPosition(440, 346);
						packTable.add(redPotion);
					}
					if (((NetworkBattle) battle).win_bonus.getNumberOf("POTIONLEV3") > 0) {
						greenPotion = new Label(
								"Green potion x" + Integer
										.toString(((NetworkBattle) battle).win_bonus.getNumberOf("POTIONLEV3")),
								MenuScreen.skin);
						greenPotion.setPosition(440, 282);
						packTable.add(greenPotion);
					}
					if (((NetworkBattle) battle).win_bonus.getNumberOf("COIN") > 0) {
						coin = new Label(
								"Coins x" + Integer.toString(((NetworkBattle) battle).win_bonus.getNumberOf("COIN")),
								MenuScreen.skin);
						coin.setPosition(800, 410);
						packTable.add(coin);
					}
					if (((NetworkBattle) battle).win_bonus.getNumberOf("PARCHLEV1") > 0) {
						parchmentLev1 = new Label(
								"Parch. lev1 x"
										+ Integer.toString(((NetworkBattle) battle).win_bonus.getNumberOf("PARCHLEV1")),
								MenuScreen.skin);
						parchmentLev1.setPosition(800, 346);
						packTable.add(parchmentLev1);
					}
					if (((NetworkBattle) battle).win_bonus.getNumberOf("PARCHLEV2") > 0) {
						parchmentLev2 = new Label(
								"Parch. lev2 x"
										+ Integer.toString(((NetworkBattle) battle).win_bonus.getNumberOf("PARCHLEV2")),
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
				if (battle.enemy instanceof NetworkCharacterBattle) {
					if (youLose) {
						((NetworkPlayScreen) gameslagyom.screenManager.playScreen).youLose = true;
					}
					((NetworkPlayScreen) gameslagyom.screenManager.playScreen).sendUpdate();
					client.networkWorld.player.isFighting = false;
					gameslagyom.screenManager.swapScreen(State.PLAYING);

				} else {
					client.networkWorld.player.isFighting = false;
					((NetworkPlayScreen) gameslagyom.screenManager.playScreen).sendUpdate();
					gameslagyom.screenManager.swapScreen(State.PLAYING);
				}
				buttonPressed = false;
			}
	}

	private void handleInput(float dt) {
		moveCharacter(dt);
		if (this.battle.character.health < 300) {
			Item item = null;
			if ((Gdx.input.isKeyJustPressed(Keys.NUM_1) || (buttonPressed && buttonCodePressed == 1))
					&& battle.character.bag.getNumberOf(Element.POTION, Level.FIRST) > 0) {
				buttonPressed = false;
				item = new Item(Element.POTION, Level.FIRST);
				battle.character.useItem(item);
				battle.character.bag.removeItem(Element.POTION, Level.FIRST);
				gameslagyom.loadingMusic.upgradeSound.play(GameConfig.soundVolume);
				if (battle.enemy instanceof NetworkCharacterBattle) {
					client.writer.println(6 + " " + ((NetworkCharacterBattle) battle.character).ID + " "
							+ Element.POTION.toString() + " " + Level.FIRST.toString() + " " + 0 + ";"
							+ ((NetworkCharacterBattle) battle.character).IDOtherPlayer + ";");
					client.writer.flush();
				}
			}
			if ((Gdx.input.isKeyJustPressed(Keys.NUM_2) || (buttonPressed && buttonCodePressed == 2))
					&& battle.character.bag.getNumberOf(Element.POTION, Level.SECOND) > 0) {
				buttonPressed = false;
				item = new Item(Element.POTION, Level.SECOND);
				battle.character.useItem(item);
				battle.character.bag.removeItem(Element.POTION, Level.SECOND);
				gameslagyom.loadingMusic.upgradeSound.play(GameConfig.soundVolume);
				if (battle.enemy instanceof NetworkCharacterBattle) {
					client.writer.println(6 + " " + ((NetworkCharacterBattle) battle.character).ID + " "
							+ Element.POTION.toString() + " " + Level.SECOND.toString() + " " + 0 + ";"
							+ ((NetworkCharacterBattle) battle.character).IDOtherPlayer + ";");
					client.writer.flush();
				}
			}
			if ((Gdx.input.isKeyJustPressed(Keys.NUM_3) || (buttonPressed && buttonCodePressed == 3))
					&& battle.character.bag.getNumberOf(Element.POTION, Level.THIRD) > 0) {
				buttonPressed = false;
				item = new Item(Element.POTION, Level.THIRD);
				battle.character.useItem(item);
				battle.character.bag.removeItem(Element.POTION, Level.THIRD);
				gameslagyom.loadingMusic.upgradeSound.play(GameConfig.soundVolume);
				if (battle.enemy instanceof NetworkCharacterBattle) {
					client.writer.println(6 + " " + ((NetworkCharacterBattle) battle.character).ID + " "
							+ Element.POTION.toString() + " " + Level.THIRD.toString() + " " + 0 + ";"
							+ ((NetworkCharacterBattle) battle.character).IDOtherPlayer + ";");
					client.writer.flush();
				}
			}
		}
	}

	private void moveCharacter(float dt) {

		if (Gdx.input.isKeyJustPressed(Keys.O) || (buttonPressed && buttonCodePressed == 4)) {
			battle.character.swapWeapon();
			buttonPressed = false;
			if (battle.enemy instanceof NetworkCharacterBattle) {
				if (((NetworkCharacterBattle) battle.character).weaponChanged) {
					client.writer.println(5 + " " + ((NetworkCharacterBattle) battle.character).ID + " "
							+ battle.character.primary_weapon.getType() + " "
							+ battle.character.primary_weapon.getLevel() + " " + 0 + ";"
							+ ((NetworkCharacterBattle) battle.character).IDOtherPlayer + ";");
					client.writer.flush();
				}
			}
		}
		if (Gdx.input.isKeyPressed(Keys.SPACE) || (buttonPressed && buttonCodePressed == 5)) {
			battle.character.caricaBomba(dt);
			battle.character.lanciaBomba = true;
		} else {
			if (battle.character.lanciaBomba) {
				battle.character.launch();
				buttonPressed = false;
				if (battle.enemy instanceof NetworkCharacterBattle) {
					if (((NetworkCharacterBattle) battle.character).bombaLanciata) {
						client.writer.println(3 + " " + ((NetworkCharacterBattle) battle.character).ID + " "
								+ battle.character.getX() + " " + battle.character.power + " "
								+ ((NetworkCharacterBattle) battle.character).bomb.level + ";"
								+ ((NetworkCharacterBattle) battle.character).IDOtherPlayer + ";");
						client.writer.flush();
					}
				}
				battle.character.lanciaBomba = false;
				battle.character.power = 50;
				((NetworkCharacterBattle) battle.character).bombaLanciata = false;
			} else if (Gdx.input.isKeyJustPressed(Keys.UP)
					|| (movesGamePad && directionGamePad == PovDirection.north)) {
				battle.character.jump(dt);
				movesGamePad = false;
				if (battle.enemy instanceof NetworkCharacterBattle) {
					client.writer.println(2 + " " + ((NetworkCharacterBattle) battle.character).ID + " " + dt + " "
							+ battle.character.getY() + " " + character.DynamicObjects.StateDynamicObject.JUMPING + ";"
							+ ((NetworkCharacterBattle) battle.character).IDOtherPlayer + ";");
					client.writer.flush();
				}
			} else if ((Gdx.input.isKeyPressed(Keys.LEFT) || (movesGamePad && directionGamePad == PovDirection.west))
					&& !battle.character.fighting) {
				battle.character.movesLeft(dt);
				if (battle.enemy instanceof NetworkCharacterBattle) {
					client.writer.println(2 + " " + ((NetworkCharacterBattle) battle.character).ID + " " + dt + " "
							+ battle.character.getY() + " " + character.DynamicObjects.StateDynamicObject.RUNNINGRIGHT
							+ ";" + ((NetworkCharacterBattle) battle.character).IDOtherPlayer + ";");
					client.writer.flush();
				}
			} else if ((Gdx.input.isKeyPressed(Keys.RIGHT) || (movesGamePad && directionGamePad == PovDirection.east))
					&& !battle.character.fighting) {
				battle.character.movesRight(dt);
				if (battle.enemy instanceof NetworkCharacterBattle) {
					client.writer.println(2 + " " + ((NetworkCharacterBattle) battle.character).ID + " " + dt + " "
							+ battle.character.getY() + " " + character.DynamicObjects.StateDynamicObject.RUNNINGLEFT
							+ ";" + ((NetworkCharacterBattle) battle.character).IDOtherPlayer + ";");
					client.writer.flush();
				}
			} else {
				battle.character.stand();
			}
			if (Gdx.input.isKeyJustPressed(Keys.A) || (buttonPressed && buttonCodePressed == 0)) {
				buttonPressed = false;
				if (battle.character.primary_weapon.getType() == Type.Sword)
					gameslagyom.loadingMusic.swordSound.play(GameConfig.soundVolume);
				else if (battle.character.primary_weapon.getType() == Type.Bow) {
					gameslagyom.loadingMusic.arrowSound.play(GameConfig.soundVolume);
				}
				if (battle.character.left) {
					battle.character.fightLeft(dt);
					if (battle.enemy instanceof NetworkCharacterBattle) {
						client.writer.println(2 + " " + ((NetworkCharacterBattle) battle.character).ID + " " + dt + " "
								+ battle.character.getY() + " "
								+ character.DynamicObjects.StateDynamicObject.FIGHTINGRIGHT + ";"
								+ ((NetworkCharacterBattle) battle.character).IDOtherPlayer + ";");
						client.writer.flush();
					}
				} else if (!battle.character.left) {
					battle.character.fightRight(dt);
					if (battle.enemy instanceof NetworkCharacterBattle) {
						client.writer.println(2 + " " + ((NetworkCharacterBattle) battle.character).ID + " " + dt + " "
								+ battle.character.getY() + " "
								+ character.DynamicObjects.StateDynamicObject.FIGHTINGLEFT + ";"
								+ ((NetworkCharacterBattle) battle.character).IDOtherPlayer + ";");
						client.writer.flush();
					}
				}
			}
		}
	}

}
