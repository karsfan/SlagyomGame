package multiplayer;

import java.util.Iterator;

import battle.Enemy;
import character.Bomb;
import world.GameConfig;

public class NetworkEnemy extends Enemy {

	public NetworkEnemy(Enemy enemy) {
		super(enemy);
	}

	public NetworkEnemy(Level level) {
		super(level);
	}

	@Override
	public void update(float dt) {

		if (!fighting && !jumping && !doubleJumping) {

			switch (level) {
			case EASY:
				updateEnemyEasy(dt);
				break;
			case MEDIUM:
				updateEnemyMedium(dt);
				break;
			case HARD:
				updateEnemyHard(dt);
				break;
			default:
				break;
			}
		}
		if (fighting && fightingTimeCurrent < fightingTime) {
			fightingTimeCurrent += dt;
			setState(getCurrentState());

		} else if (fighting && fightingTimeCurrent > fightingTime) {
			fighting = false;
			fightingTimeCurrent = 0;
		}
		dt = 0.35f;
		if ((jumping || doubleJumping) && y + velocityY * dt > GameConfig.mainY_Battle) {
			y += velocityY * dt;
			updateVelocityY(dt);
			setState(StateDynamicObject.JUMPING);
			if (collide() && x < Client.networkWorld.battle.character.getX())
				x = Client.networkWorld.battle.character.getX() - getWidth() / 2;
			else if (collide() && x > Client.networkWorld.battle.character.getX())
				x = Client.networkWorld.battle.character.getX() + Client.networkWorld.battle.character.getWidth() / 2;
		} else {
			jumping = false;
			doubleJumping = false;
			y = GameConfig.mainY_Battle;
			velocityY = 0;
		}
		Iterator<Bomb> it1 = bombe.iterator();
		while (it1.hasNext()) {
			Bomb ob = (Bomb) it1.next();
			if (ob.lanciata == true) {
				((Bomb) ob).update(dt);
				if (ob.morta) {
					it1.remove();
					System.out.println("Bomba enemy eliminata");
					continue;
				}
			}
		}

	}

	@Override
	public void updateEnemyHard(float dt) {
		int rand = (int) (Math.random() * 100);
		if ((x - Client.networkWorld.battle.character.getX() < 100
				&& x - Client.networkWorld.battle.character.getX() > 0)
				|| (Client.networkWorld.battle.character.getX() - x < 100
						&& Client.networkWorld.battle.character.getX() - x > 0)) {

			if (rand < 15 && Client.networkWorld.battle.character.fighting)
				jump(dt);
			if (rand > 15 && rand < 30 && Client.networkWorld.battle.character.fighting) {
				setState(StateDynamicObject.DEFENDING);
			} else if (x - Client.networkWorld.battle.character.getX() < 100
					&& x - Client.networkWorld.battle.character.getX() > 0 && rand < 90)
				fightLeft();
			else if (Client.networkWorld.battle.character.getX() - x < 100
					&& Client.networkWorld.battle.character.getX() - x > 0 && rand < 90)
				fightRight();
		} else if (Client.networkWorld.battle.character.getX() > x && rand < 90)
			movesRight(dt);
		else if (x > Client.networkWorld.battle.character.getX() && rand < 90) {
			if (rand > 75 && rand < 80)
				lanciaBomb(dt);
			movesLeft(dt);
		}
	}

	@Override
	public void updateEnemyMedium(float dt) {
		int rand = (int) (Math.random() * 100);
		if ((x - Client.networkWorld.battle.character.getX() < 100
				&& x - Client.networkWorld.battle.character.getX() > 0)
				|| (Client.networkWorld.battle.character.getX() - x < 100
						&& Client.networkWorld.battle.character.getX() - x > 0)) {

			if (rand < 10 && Client.networkWorld.battle.character.fighting)
				jump(dt);
			else if (rand > 10 && rand < 25 && Client.networkWorld.battle.character.fighting)
				setState(StateDynamicObject.DEFENDING);
			else if (x - Client.networkWorld.battle.character.getX() < 100
					&& x - Client.networkWorld.battle.character.getX() > 0 && rand < 55)
				fightLeft();
			else if (Client.networkWorld.battle.character.getX() - x < 100
					&& Client.networkWorld.battle.character.getX() - x > 0 && rand < 55)
				fightRight();

		} else if (Client.networkWorld.battle.character.getX() > x && rand < 70)
			movesRight(dt);

		else if (x > Client.networkWorld.battle.character.getX() && rand < 70)
			movesLeft(dt);
	}

	@Override
	public void updateEnemyEasy(float dt) {
		int rand = (int) (Math.random() * 100);
		if ((x - Client.networkWorld.battle.character.getX() < 100
				&& x - Client.networkWorld.battle.character.getX() > 0)
				|| (Client.networkWorld.battle.character.getX() - x < 100
						&& Client.networkWorld.battle.character.getX() - x > 0)) {
			if (rand < 5 && Client.networkWorld.battle.character.fighting)
				jump(dt);
			else if (rand > 5 && rand < 20 && Client.networkWorld.battle.character.fighting)
				setState(StateDynamicObject.DEFENDING);
			else if (x - Client.networkWorld.battle.character.getX() < 100
					&& x - Client.networkWorld.battle.character.getX() > 0 && rand < 40)
				fightLeft();
			else if (Client.networkWorld.battle.character.getX() - x < 100
					&& Client.networkWorld.battle.character.getX() - x > 0 && rand < 40)
				fightRight();
		} else if (Client.networkWorld.battle.character.getX() > x && rand < 50)
			movesRight(dt);
		else if (x > Client.networkWorld.battle.character.getX() && rand < 50)
			movesLeft(dt);
	}

	@Override
	public boolean collide() {

		if (!((x > Client.networkWorld.battle.character.getX() + Client.networkWorld.battle.character.getWidth() / 2
				|| Client.networkWorld.battle.character.getX() > x + width / 2)
				|| (y > Client.networkWorld.battle.character.getY()
						+ Client.networkWorld.battle.character.getHeight() / 2
						|| Client.networkWorld.battle.character.getY() > y + height / 2)))
			return true;
		return false;
	}
	public void fightRight() {
		width += weapon.getWidth();
		if (collide())
			Client.networkWorld.battle.character.decreaseHealth(weapon);
		width -= weapon.getWidth();

		setState(StateDynamicObject.FIGHTINGRIGHT);
		fighting = true;

	}

	public void fightLeft() {
		x -= weapon.getWidth();
		if (collide())
			Client.networkWorld.battle.character.decreaseHealth(weapon);
		x += weapon.getWidth();

		setState(StateDynamicObject.FIGHTINGLEFT);
		fighting = true;

	}
}
