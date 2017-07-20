package multiplayer;

import java.util.Iterator;

import battle.Enemy;
import weaponsAndItems.Bomb;
import weaponsAndItems.Weapon.Type;
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
			if (fightingTimeCurrent >= fightingTime / 2 && weapon.getType() == Type.Bow)
			if (!arrowShooted) {
				shootArrow();
				arrowShooted = true;
			}
			setState(getCurrentState());

		} else if (fighting && fightingTimeCurrent > fightingTime) {
			fighting = false;
			fightingTimeCurrent = 0;
			arrowShooted = false;
		}
		dt = 0.35f;
		if ((jumping || doubleJumping) && y + velocityY * dt > GameConfig.mainY_Battle) {
			y += velocityY * dt;
			updateVelocityY(dt);
			setState(StateDynamicObject.JUMPING);
			if (collide() && x < Client.networkWorld.battle.character.getX())
				x = (int) (Client.networkWorld.battle.character.getX() - getWidth() / 2);
			else if (collide() && x > Client.networkWorld.battle.character.getX())
				x = (int) (Client.networkWorld.battle.character.getX()
						+ Client.networkWorld.battle.character.getWidth() / 2);
		} else {
			jumping = false;
			doubleJumping = false;
			y = GameConfig.mainY_Battle;
			velocityY = 0;
		}
		Iterator<Bomb> it1 = bombs.iterator();
		while (it1.hasNext()) {
			Bomb ob = (Bomb) it1.next();
			if (ob.launched == true) {
				((Bomb) ob).update(dt);
				if (ob.dead) {
					it1.remove();
					continue;
				}
			}
		}

	}

	@Override
	public void updateEnemyHard(float dt) {
		int rand = (int) (Math.random() * 100);
		if (rand > 80 && rand < 95 && weapon.getType() == Type.Bow) {
			if (x < Client.networkWorld.battle.character.x)
				fightRight();
			else
				fightLeft();
		} else if ((x - Client.networkWorld.battle.character.getX() < Client.networkWorld.battle.character.width / 3
				&& x - Client.networkWorld.battle.character.getX() > 0)
				|| (Client.networkWorld.battle.character.getX() - x < Client.networkWorld.battle.character.width / 3
						&& Client.networkWorld.battle.character.getX() - x > 0)) {
			if (rand < 20 && Client.networkWorld.battle.character.fighting)
				jump(dt);
			else if (x - Client.networkWorld.battle.character.getX() < Client.networkWorld.battle.character.width / 3
					&& x - Client.networkWorld.battle.character.getX() > 0 && rand < 90)
				fightLeft();
			else if (Client.networkWorld.battle.character.getX() - x < Client.networkWorld.battle.character.width / 3
					&& Client.networkWorld.battle.character.getX() - x > 0 && rand < 90)
				fightRight();
		} else if (Client.networkWorld.battle.character.getX() > x && rand < 90)
			movesRight(dt);
		else if (x > Client.networkWorld.battle.character.getX() && rand < 90) {
			if (rand > 75 && rand < 80)
				launchBomb(dt);
			movesLeft(dt);
		}
	}

	@Override
	public void launchBomb(float dt) {
		if (left && !bombs.isEmpty()) {
			int velocityy = 200;
			// calcolo della gittata
			velocityy = (int) Math.sqrt(((x - Client.networkWorld.battle.character.getX()) * GameConfig.gravity)
					/ ((2 * Math.cos(30 * (Math.PI / 180)) * Math.sin(90 * (Math.PI / 180)))));
			Iterator<Bomb> it1 = bombs.iterator();
			while (it1.hasNext()) {
				Bomb ob = (Bomb) it1.next();
				if (!ob.launched) {
					ob.launch(velocityy, this);
					ob.id = "Enemy";
					break;
				}
			}
		} else if (right && !bombs.isEmpty()) {
			int velocityy = 200;
			// calcolo della gittata
			velocityy = (int) Math
					.sqrt(((Client.networkWorld.battle.character.getX() + width / 3 - x) * GameConfig.gravity)
							/ ((2 * Math.cos(30 * (Math.PI / 180)) * Math.sin(90 * (Math.PI / 180)))));
			Iterator<Bomb> it1 = bombs.iterator();
			while (it1.hasNext()) {
				Bomb ob = (Bomb) it1.next();
				if (!ob.launched) {
					ob.launch(velocityy, this);
					ob.id = "Enemy";
					break;
				}
			}
		}
	}

	@Override
	public void updateEnemyMedium(float dt) {
		int rand = (int) (Math.random() * 100);
		if (rand > 85 && rand < 95 && weapon.getType() == Type.Bow) {
			if (x < Client.networkWorld.battle.character.x)
				fightRight();
			else
				fightLeft();
		} else if ((x - Client.networkWorld.battle.character.getX() < Client.networkWorld.battle.character.width / 2
				&& x - Client.networkWorld.battle.character.getX() > 0)
				|| (Client.networkWorld.battle.character.getX() - x < Client.networkWorld.battle.character.width / 2
						&& Client.networkWorld.battle.character.getX() - x > 0)) {

			if (rand < 10 && Client.networkWorld.battle.character.fighting)
				jump(dt);
			else if (x - Client.networkWorld.battle.character.getX() < Client.networkWorld.battle.character.width / 2
					&& x - Client.networkWorld.battle.character.getX() > 0 && rand < 55)
				fightLeft();
			else if (Client.networkWorld.battle.character.getX() - x < Client.networkWorld.battle.character.width / 2
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
		if (rand > 93 && rand < 95 && weapon.getType() == Type.Bow) {
			if (x < Client.networkWorld.battle.character.x)
				fightRight();
			else
				fightLeft();
		} else if ((x - Client.networkWorld.battle.character.getX()
				- Client.networkWorld.battle.character.width / 2 < Client.networkWorld.battle.character.width / 3
				&& x - Client.networkWorld.battle.character.getX() > 0)
				|| (Client.networkWorld.battle.character.getX() + Client.networkWorld.battle.character.width / 2
						- x < Client.networkWorld.battle.character.width / 3
						&& Client.networkWorld.battle.character.getX() - x > 0)) {
			if (rand < 7 && Client.networkWorld.battle.character.fighting)
				jump(dt);
			else if (x - Client.networkWorld.battle.character.getX()
					- Client.networkWorld.battle.character.width / 2 < Client.networkWorld.battle.character.width / 3
					&& x - Client.networkWorld.battle.character.getX() > 0 && rand < 40)
				fightLeft();
			else if (Client.networkWorld.battle.character.getX() + Client.networkWorld.battle.character.width / 2
					- x < Client.networkWorld.battle.character.width / 3
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
		right = true;
		left = false;
		width += weapon.getWidth();
		if (collide())
			Client.networkWorld.battle.character.decreaseHealth(weapon);
		width -= weapon.getWidth();

		setState(StateDynamicObject.FIGHTINGRIGHT);
		fighting = true;

	}

	public void fightLeft() {
		right = false;
		left = true;
		x -= weapon.getWidth();
		if (collide())
			Client.networkWorld.battle.character.decreaseHealth(weapon);
		x += weapon.getWidth();

		setState(StateDynamicObject.FIGHTINGLEFT);
		fighting = true;

	}
}
