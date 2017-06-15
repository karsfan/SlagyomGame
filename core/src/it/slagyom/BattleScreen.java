package it.slagyom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import it.slagyom.GameSlagyom.State;
import it.slagyom.src.Character.CharacterBattle;
import it.slagyom.src.Character.DynamicObjects.StateDynamicObject;
import it.slagyom.src.World.Battle;
import it.slagyom.src.World.Enemy;
import it.slagyom.src.World.Game;

public class BattleScreen implements Screen {

	public OrthographicCamera gamecam;
	public Viewport gamePort;
	public GameSlagyom gameslagyom;
	public BattleHud hud;
	public Battle battle;

	public BattleScreen(GameSlagyom gameslagyom, Battle battle) {
		this.gameslagyom = gameslagyom;
		this.battle = battle;
		gamecam = new OrthographicCamera();
		gamePort = new ExtendViewport(500, 500, gamecam);
		gamePort.apply();
		gamecam.position.x = battle.character.getX();
		gamecam.position.y = battle.character.getY();
		hud = new BattleHud(gameslagyom.batch);
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
		gamePort.apply();
	}

	private void draw() {

		gameslagyom.batch.draw(LoadingImage.getBattleBgImage(), 0, 0);
		CharacterBattle tmp = battle.character;
		gameslagyom.batch.draw(LoadingImage.getBattleFrame(tmp), tmp.getX(), tmp.getY(), tmp.getWidth(),
				tmp.getHeight());
		Enemy tmp1 = battle.enemy;
		gameslagyom.batch.draw(LoadingImage.getBattleFrame(tmp1), tmp1.getX(), tmp1.getY(), tmp1.getWidth(),
				tmp1.getHeight());

	}

	public void update(float dt) {

		handleInput(dt);
		hud.update(dt);
		if (battle.update(dt)) {
			gameslagyom.swapScreen(State.PLAYING);
			Game.world.semaphore.release();
		}

	}

	private void handleInput(float dt) {

		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			// va messo in pausa e poi in caso bisogna ritornare nel playscreen
			gameslagyom.swapScreen(State.PLAYING);
			Game.world.semaphore.release();
		}
		moveCharacter(dt);
	}

	private void moveCharacter(float dt) {

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

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
		gamecam.position.set((float) gamecam.viewportWidth / 2, (float) gamecam.viewportHeight / 2, 0);
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
