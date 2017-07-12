package multiplayer;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;

import battle.Battle;
import character.Bomb;
import character.DynamicObjects.StateDynamicObject;
import gameManager.GameSlagyom;
import gameManager.LoadingImage;
import gameManager.ScreenManager.State;
import screens.BattleScreen;

public class NetworkBattleScreen extends BattleScreen {
	Client client;

	public NetworkBattleScreen(GameSlagyom gameslagyom, Battle battle, Client client) {
		super(gameslagyom, battle);
		this.client = client;
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

	private void draw() {
		gameslagyom.batch.draw(LoadingImage.getBattleBgImage(), 0, 0);

		NetworkCharacterBattle tmp = (NetworkCharacterBattle) battle.character;
		gameslagyom.batch.draw(LoadingImage.getBattleFrame(tmp), tmp.getX(), tmp.getY(), tmp.getWidth(),
				tmp.getHeight());

		NetworkCharacterBattle tmp1 = (NetworkCharacterBattle) battle.enemy;
		gameslagyom.batch.draw(LoadingImage.getBattleFrame(tmp1), tmp1.getX(), tmp1.getY(), tmp1.getWidth(),
				tmp1.getHeight());

		Iterator<Bomb> bombIterator = tmp.bag.bombe.iterator();
		while (bombIterator.hasNext()) {
			Bomb searching = (Bomb) bombIterator.next();
			if (searching.lanciata == true) {
				gameslagyom.batch.draw(LoadingImage.getTileImage(searching), searching.getMainX(), searching.getMainY(),
						searching.getWidth() + 10, searching.getHeight() + 10);
			}
		}

		Iterator<Bomb> bombIterator1 = tmp1.bag.bombe.iterator();

		while (bombIterator1.hasNext()) {
			Bomb searching1 = (Bomb) bombIterator1.next();
			if (searching1.lanciata == true) {
				gameslagyom.batch.draw(LoadingImage.getTileImage(searching1), searching1.getMainX(),
						searching1.getMainY(), searching1.getWidth() + 10, searching1.getHeight() + 10);
			}
		}
		// if (youWin)
		// gameslagyom.batch.draw(LoadingImage.getYouWinImage(), 0, 0);
		// else if (youLose)
		// gameslagyom.batch.draw(LoadingImage.getYouLoseImage(), 0, 0);
	}

	public void update(float dt) {
		battle.update(dt);
		hud.update(dt);
		//System.out.println("update netBattleScreen");
		handleInput(dt);

	}

	private void handleInput(float dt) {

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			gameslagyom.screenManager.swapScreen(State.PAUSE);
		}
		moveCharacter(dt);
	}

	private void moveCharacter(float dt) {

		if (Gdx.input.isKeyJustPressed(Keys.O)) {
			System.out.println("SWap");
			battle.character.swapWeapon();
		}
		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			battle.character.caricaBomba(dt);
			battle.character.bomba = true;
		} else {
			if (battle.character.bomba) {
				battle.character.lancia();
				System.out.println("lancia");
				battle.character.bomba = false;
				battle.character.forza = 50;
			}

			if (Gdx.input.isKeyJustPressed(Keys.S))
				battle.character.setState(StateDynamicObject.DEFENDING, dt);
			
			if (Gdx.input.isKeyJustPressed(Keys.UP)) {
				battle.character.jump(dt);
				client.writer.println(2 + " " + ((NetworkCharacterBattle) battle.character).ID + " "
						+ dt + " " + battle.character.getY() + " " + character.DynamicObjects.StateDynamicObject.JUMPING + ";"
						+ ((NetworkCharacterBattle) battle.character).IDOtherPlayer + ";");
				client.writer.flush();
			} else if (Gdx.input.isKeyPressed(Keys.LEFT) && !battle.character.fighting) {
				if (Gdx.input.isKeyJustPressed(Keys.A)){
					battle.character.fightLeft(dt);
					client.writer.println(2 + " " + ((NetworkCharacterBattle) battle.character).ID + " "
							+ dt + " " + battle.character.getY() + " " + character.DynamicObjects.StateDynamicObject.FIGHTINGRIGHT+ ";"
							+ ((NetworkCharacterBattle) battle.character).IDOtherPlayer + ";");
					client.writer.flush();
				}
				else{
					battle.character.movesLeft(dt);
					client.writer.println(2 + " " + ((NetworkCharacterBattle) battle.character).ID + " "
							+ dt + " " + battle.character.getY() + " " + character.DynamicObjects.StateDynamicObject.RUNNINGRIGHT + ";"
							+ ((NetworkCharacterBattle) battle.character).IDOtherPlayer + ";");
					client.writer.flush();
				}
			} else if (Gdx.input.isKeyPressed(Keys.RIGHT) && !battle.character.fighting) {
				if (Gdx.input.isKeyJustPressed(Keys.A)){
					battle.character.fightRight(dt);
					client.writer.println(2 + " " + ((NetworkCharacterBattle) battle.character).ID + " "
							+ dt + " " + battle.character.getY() + " " + character.DynamicObjects.StateDynamicObject.FIGHTINGLEFT + ";"
							+ ((NetworkCharacterBattle) battle.character).IDOtherPlayer + ";");
					client.writer.flush();
				}
				else {
					battle.character.movesRight(dt);
					client.writer.println(2 + " " + ((NetworkCharacterBattle) battle.character).ID + " "
							+ dt + " " + battle.character.getY() + " " + character.DynamicObjects.StateDynamicObject.RUNNINGLEFT + ";"
							+ ((NetworkCharacterBattle) battle.character).IDOtherPlayer + ";");
					client.writer.flush();
					
				}
			} else {
				battle.character.stand();
			}
		}
	}

}
