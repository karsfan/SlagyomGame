package multiplayer;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import battle.Battle;
import character.Bomb;
import character.DynamicObjects;
import gameManager.GameSlagyom;
import gameManager.LoadingImage;
import gameManager.ScreenManager.State;
import screens.BattleScreen;
import screens.MenuScreen;

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
	}

	private void sendPrimaryWeapon() {
		client.writer.println(5 + " " + ((NetworkCharacterBattle) battle.character).ID + " "
				+ battle.character.primary_weapon.getType() + " " + battle.character.primary_weapon.getLevel() + " " + 0
				+ ";" + ((NetworkCharacterBattle) battle.character).IDOtherPlayer + ";");
		client.writer.flush();
		weapon_primary_comunicated = true;
	}

	private void draw() {
		gameslagyom.batch.draw(LoadingImage.getBattleBgImage(), 0, 0);

		NetworkCharacterBattle tmp = (NetworkCharacterBattle) battle.character;
		gameslagyom.batch.draw(gameslagyom.loadingImage.getBattleFrame(tmp), tmp.getX(), tmp.getY(), tmp.getWidth(),
				tmp.getHeight());
		DynamicObjects tmp1;
		if (battle.enemy instanceof NetworkCharacterBattle)
			tmp1 = (NetworkCharacterBattle) battle.enemy;
		else
			tmp1 = (NetworkEnemy) battle.enemy;
		gameslagyom.batch.draw(gameslagyom.loadingImage.getBattleFrame(tmp1), tmp1.getX(), tmp1.getY(), tmp1.getWidth(),
				tmp1.getHeight());

		Iterator<Bomb> bombIterator = tmp.bag.bombe.iterator();
		while (bombIterator.hasNext()) {
			Bomb searching = (Bomb) bombIterator.next();
			if (searching.lanciata == true) {
				gameslagyom.batch.draw(gameslagyom.loadingImage.getTileImage(searching), searching.getMainX(),
						searching.getMainY(), searching.getWidth() + 10, searching.getHeight() + 10);
			}
		}
		Iterator<Bomb> bombIterator1 = null;
		if (battle.enemy instanceof NetworkCharacterBattle)
			bombIterator1 = ((NetworkCharacterBattle) tmp1).bag.bombe.iterator();
		else
			bombIterator1 = ((NetworkEnemy) tmp1).getBombe().iterator();

		while (bombIterator1.hasNext()) {
			Bomb searching1 = (Bomb) bombIterator1.next();
			if (searching1.lanciata == true) {
				gameslagyom.batch.draw(gameslagyom.loadingImage.getTileImage(searching1), searching1.getMainX(),
						searching1.getMainY(), searching1.getWidth() + 10, searching1.getHeight() + 10);
			}
		}
		if (youWin)
			gameslagyom.batch.draw(LoadingImage.getYouWinImage(), 0, 0);
		else if (youLose)
			gameslagyom.batch.draw(LoadingImage.getYouLoseImage(), 0, 0);
	}

	@Override
	public void update(float dt) {
		// System.out.println("Update NetBattleScreen");
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
			if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
				if (battle.character instanceof NetworkCharacterBattle) {
					if (youLose) {
						((NetworkPlayScreen) gameslagyom.screenManager.playScreen).youLose = true;
					}
					gameslagyom.screenManager.swapScreen(State.PLAYING);
					client.networkWorld.player.isFighting = false;
					((NetworkPlayScreen) gameslagyom.screenManager.playScreen).sendUpdate();
				} else
					gameslagyom.screenManager.swapScreen(State.PLAYING);
			}

	}

	private void handleInput(float dt) {
		moveCharacter(dt);
	}

	private void moveCharacter(float dt) {

		if (Gdx.input.isKeyJustPressed(Keys.O)) {
			System.out.println("SWap");
			battle.character.swapWeapon();
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
		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			battle.character.caricaBomba(dt);
			battle.character.lanciaBomba = true;
		} else {
			if (battle.character.lanciaBomba) {
				battle.character.lancia();
				if (battle.enemy instanceof NetworkCharacterBattle) {
					if (((NetworkCharacterBattle) battle.character).bombaLanciata) {
						client.writer.println(3 + " " + ((NetworkCharacterBattle) battle.character).ID + " "
								+ battle.character.getX() + " " + battle.character.forza + " "
								+ ((NetworkCharacterBattle) battle.character).bomb.level + ";"
								+ ((NetworkCharacterBattle) battle.character).IDOtherPlayer + ";");
						client.writer.flush();
					}
				}
				battle.character.lanciaBomba = false;
				battle.character.forza = 50;
				((NetworkCharacterBattle) battle.character).bombaLanciata = false;
			} else if (Gdx.input.isKeyJustPressed(Keys.UP)) {
				battle.character.jump(dt);
				if (battle.enemy instanceof NetworkCharacterBattle) {
					client.writer.println(2 + " " + ((NetworkCharacterBattle) battle.character).ID + " " + dt + " "
							+ battle.character.getY() + " " + character.DynamicObjects.StateDynamicObject.JUMPING + ";"
							+ ((NetworkCharacterBattle) battle.character).IDOtherPlayer + ";");
					client.writer.flush();
				}
			} else if (Gdx.input.isKeyPressed(Keys.LEFT) && !battle.character.fighting) {
				if (Gdx.input.isKeyJustPressed(Keys.A)) {
					battle.character.fightLeft(dt);
					if (battle.enemy instanceof NetworkCharacterBattle) {
						client.writer.println(2 + " " + ((NetworkCharacterBattle) battle.character).ID + " " + dt + " "
								+ battle.character.getY() + " "
								+ character.DynamicObjects.StateDynamicObject.FIGHTINGRIGHT + ";"
								+ ((NetworkCharacterBattle) battle.character).IDOtherPlayer + ";");
						client.writer.flush();
					}
				} else {
					battle.character.movesLeft(dt);
					if (battle.enemy instanceof NetworkCharacterBattle) {
						client.writer.println(2 + " " + ((NetworkCharacterBattle) battle.character).ID + " " + dt + " "
								+ battle.character.getY() + " "
								+ character.DynamicObjects.StateDynamicObject.RUNNINGRIGHT + ";"
								+ ((NetworkCharacterBattle) battle.character).IDOtherPlayer + ";");
						client.writer.flush();
					}
				}
			} else if (Gdx.input.isKeyPressed(Keys.RIGHT) && !battle.character.fighting) {
				if (Gdx.input.isKeyJustPressed(Keys.A)) {
					battle.character.fightRight(dt);
					if (battle.enemy instanceof NetworkCharacterBattle) {
						client.writer.println(2 + " " + ((NetworkCharacterBattle) battle.character).ID + " " + dt + " "
								+ battle.character.getY() + " "
								+ character.DynamicObjects.StateDynamicObject.FIGHTINGLEFT + ";"
								+ ((NetworkCharacterBattle) battle.character).IDOtherPlayer + ";");
						client.writer.flush();
					}
				} else {
					battle.character.movesRight(dt);
					if (battle.enemy instanceof NetworkCharacterBattle) {
						client.writer.println(2 + " " + ((NetworkCharacterBattle) battle.character).ID + " " + dt + " "
								+ battle.character.getY() + " "
								+ character.DynamicObjects.StateDynamicObject.RUNNINGLEFT + ";"
								+ ((NetworkCharacterBattle) battle.character).IDOtherPlayer + ";");
						client.writer.flush();
					}
				}
			} else {
				battle.character.stand();
			}
		}
	}

}
