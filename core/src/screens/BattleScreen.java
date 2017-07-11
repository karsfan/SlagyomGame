package screens;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import battle.Battle;
import battle.CharacterBattle;
import battle.Enemy;
import character.Bomb;
import character.DynamicObjects.StateDynamicObject;
import gameManager.GameSlagyom;
import gameManager.LoadingImage;
import gameManager.ScreenManager.State;
import hud.BattleHud;
import multiplayer.NetworkCharacterBattle;

public class BattleScreen implements Screen {

	public OrthographicCamera gamecam;
	public Viewport gamePort;
	public GameSlagyom gameslagyom;
	public BattleHud hud;
	public Battle battle;
	boolean youWin = false;
	boolean youLose = false;
	Label potion;
	Label bomb;
	Label coin;

	public BattleScreen(GameSlagyom gameslagyom, Battle battle) {
		this.gameslagyom = gameslagyom;
		this.battle = battle;
		gamecam = new OrthographicCamera();
		gamePort = new ExtendViewport(854, 480, gamecam);
		gamePort.apply();
		gamecam.position.x = battle.character.getX();
		gamecam.position.y = battle.character.getY();
		System.out.println(battle.enemy.health);
		hud = new BattleHud(gameslagyom.batch, battle);
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

	private void draw() {

		gameslagyom.batch.draw(LoadingImage.getBattleBgImage(), 0, 0);

		CharacterBattle tmp = battle.character;
		gameslagyom.batch.draw(LoadingImage.getBattleFrame(tmp), tmp.getX(), tmp.getY(), tmp.getWidth(),
				tmp.getHeight());

		Enemy tmp1 = battle.enemy;
		gameslagyom.batch.draw(LoadingImage.getBattleFrame(tmp1), tmp1.getX(), tmp1.getY(), tmp1.getWidth(),
				tmp1.getHeight());

		Iterator<Bomb> bombIterator = battle.character.bag.bombe.iterator();
		while (bombIterator.hasNext()) {
			Bomb searching = (Bomb) bombIterator.next();
			if (searching.lanciata == true) {
				gameslagyom.batch.draw(LoadingImage.getTileImage(searching), searching.getMainX(), searching.getMainY(),
						searching.getWidth() + 10, searching.getHeight() + 10);
			}
		}
		Iterator<Bomb> bombIterator1 = battle.enemy.bombe.iterator();
		while (bombIterator1.hasNext()) {
			Bomb searching1 = (Bomb) bombIterator1.next();
			if (searching1.lanciata == true) {
				gameslagyom.batch.draw(LoadingImage.getTileImage(searching1), searching1.getMainX(),
						searching1.getMainY(), searching1.getWidth() + 10, searching1.getHeight() + 10);
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
				if (battle.character.getHealth() <= 0) {
					youLose = true;
					if (battle.enemy.win_bonus.getNumberOf("POTIONLEV3") > 0) {
						potion = new Label(
								"Potion lev3 x" + Integer.toString(battle.enemy.win_bonus.getNumberOf("POTIONLEV3")),
								MenuScreen.skin);
						potion.setSize(100, 100);
						potion.setPosition(gamePort.getWorldWidth() / 4, gamePort.getWorldHeight() / 2.5f);
						hud.stage.addActor(potion);
					}
				} else {
					youWin = true;
					if (battle.enemy.win_bonus.getNumberOf("POTIONLEV1") > 0) {
						potion = new Label(
								"Potion lev1 x" + Integer.toString(battle.enemy.win_bonus.getNumberOf("POTIONLEV1")),
								MenuScreen.skin);
						potion.setSize(100, 100);
						potion.setPosition(gamePort.getWorldWidth() / 4, gamePort.getWorldHeight() / 3);
						hud.stage.addActor(potion);
					}
				}
			}
		}
		if (youWin || youLose)
			if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
				if (battle.character instanceof NetworkCharacterBattle){
					gameslagyom.screenManager.swapScreen(State.MULTIPLAYERGAME);
				}
				else
					gameslagyom.screenManager.swapScreen(State.PLAYING);
			}

	}

	private void handleInput(float dt) {

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			gameslagyom.screenManager.swapScreen(State.PAUSE);
		}
		moveCharacter(dt);
	}

	private void moveCharacter(float dt) {

		if(Gdx.input.isKeyJustPressed(Keys.O)){
			System.out.println("SWap");
			battle.character.swapWeapon();
		}
		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			battle.character.caricaBomba(dt);
			battle.character.bomba = true;
		} else {
			if (battle.character.bomba) {
				battle.character.lancia();
				battle.character.bomba = false;
				battle.character.forza = 50;
			}

			if (Gdx.input.isKeyJustPressed(Keys.S))
				battle.character.setState(StateDynamicObject.DEFENDING, dt);

			if (Gdx.input.isKeyJustPressed(Keys.UP)) {
				battle.character.jump(dt);

			} else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
				if (Gdx.input.isKeyJustPressed(Keys.A))
					battle.character.fightLeft(dt);
				else
					battle.character.movesLeft(dt);
			} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
				if (Gdx.input.isKeyJustPressed(Keys.A))
					battle.character.fightRight(dt);
				else
					battle.character.movesRight(dt);
			} else {
				battle.character.stand();
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
