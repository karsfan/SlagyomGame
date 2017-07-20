package battle;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;

import character.Arrow;
import character.Bomb;
import character.Weapon;
import character.Weapon.Type;
import world.Game;
import world.GameConfig;

public class Enemy extends Fighting {

	public enum Level {
		EASY, MEDIUM, HARD
	};

	public Weapon weapon;
	public ArrayList<Bomb> bombs;
	public Pack winBonus;
	public boolean arrowShooted;
	public ArrayList<Weapon> arrowsShooted;

	public Pack getWin_bonus() {
		return winBonus;
	}

	public Level level;

	public Enemy(Enemy enemy) {
		bombs = new ArrayList<>();
		arrowShooted = false;
		arrowsShooted = new ArrayList<Weapon>();
		winBonus = enemy.winBonus;
		x = 700;
		y = GameConfig.mainY_Battle;
		weapon = enemy.weapon;
		level = enemy.level;
		name = enemy.name;
		health = enemy.health;
		velocity = enemy.velocity;
		stateTimer = 0;
		height = 150;
		width = 120;
		currentState = StateDynamicObject.STANDING;
		previousState = null;
		fighting = false;
		fightingTimeCurrent = 0;
		fightingTime = 0.4f;

		jumping = false;
		doubleJumping = false;
		velocityY = 0;
		velocityX = 10;
		bombs.add(new Bomb(character.Weapon.Level.lev1, Type.Bomb));
		bombs.add(new Bomb(character.Weapon.Level.lev1, Type.Bomb));
		bombs.add(new Bomb(character.Weapon.Level.lev1, Type.Bomb));
		bombs.add(new Bomb(character.Weapon.Level.lev2, Type.Bomb));
		if (weapon.getType() == Type.Sword || (weapon.getType() == Type.Spear && weapon.getLevel() == Weapon.Level.lev2)
				|| (weapon.getType() == Type.Spear && weapon.getLevel() == Weapon.Level.lev3))
			this.width = 200;
		else
			this.width = 120;

	}

	public Enemy(Level level) {
		super();
		bombs = new ArrayList<>();
		arrowShooted = false;
		arrowsShooted = new ArrayList<Weapon>();
		this.level = level;
		switch (level) {
		case EASY:
			name = "Bob";
			weapon = new Weapon(character.Weapon.Level.lev1);
			health = 100;
			winBonus = new Pack(Level.EASY);
			velocity = 40;
			bombs.add(new Bomb(character.Weapon.Level.lev1, Type.Bomb));
			bombs.add(new Bomb(character.Weapon.Level.lev1, Type.Bomb));
			bombs.add(new Bomb(character.Weapon.Level.lev1, Type.Bomb));
			break;
		case MEDIUM:
			name = "John";
			weapon = new Weapon(character.Weapon.Level.lev2);
			health = 250;
			winBonus = new Pack(Level.MEDIUM);
			velocity = 60;
			bombs.add(new Bomb(character.Weapon.Level.lev2, Type.Bomb));
			bombs.add(new Bomb(character.Weapon.Level.lev2, Type.Bomb));
			bombs.add(new Bomb(character.Weapon.Level.lev2, Type.Bomb));
			break;
		case HARD:
			name = "Mike";
			weapon = new Weapon(character.Weapon.Level.lev3);
			health = 400;
			winBonus = new Pack(Level.HARD);
			bombs.add(new Bomb(character.Weapon.Level.lev3, Type.Bomb));
			bombs.add(new Bomb(character.Weapon.Level.lev3, Type.Bomb));
			bombs.add(new Bomb(character.Weapon.Level.lev3, Type.Bomb));
			velocity = 80;
			break;
		default:
			break;
		}
		winBonus = new Pack(level);
		stateTimer = 0;
		x = 700;
		y = GameConfig.mainY_Battle;

		bombs.add(new Bomb(character.Weapon.Level.lev1, Type.Bomb));
		bombs.add(new Bomb(character.Weapon.Level.lev1, Type.Bomb));
		bombs.add(new Bomb(character.Weapon.Level.lev1, Type.Bomb));
		if (weapon.getType() == Type.Sword || (weapon.getType() == Type.Spear && weapon.getLevel() == Weapon.Level.lev2)
				|| (weapon.getType() == Type.Spear && weapon.getLevel() == Weapon.Level.lev3))
			this.width = 200;
		else
			this.width = 120;
	}

	public int getHealth() {
		return health;
	}

	private void setStateTimer(float dt) {
		stateTimer = dt;
	}

	public void setState(StateDynamicObject state) {
		previousState = currentState;
		currentState = state;
		if (previousState == currentState && currentState != StateDynamicObject.STANDING)
			setStateTimer(getStateTimer() + Gdx.graphics.getDeltaTime());
		else
			setStateTimer(0);
	}

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
			arrowShooted = false;
			fightingTimeCurrent = 0;
			setState(StateDynamicObject.STANDING);
		}

		dt = 0.35f;
		if ((jumping || doubleJumping) && y + velocityY * dt > GameConfig.mainY_Battle) {
			y += velocityY * dt;
			updateVelocityY(dt);
			setState(StateDynamicObject.JUMPING);
			if (collide() && x < Game.world.battle.character.getX())
				x = (Game.world.battle.character.getX() - getWidth() / 2);
			else if (collide() && x > Game.world.battle.character.getX())
				x = (Game.world.battle.character.getX() + Game.world.battle.character.getWidth() / 2);
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

	public void shootArrow() {
		Weapon arrow = null;
		if (weapon.getLevel() == Weapon.Level.lev1)
			arrow = new Arrow(Weapon.Level.lev1, Type.Arrow, x, y + width / 2);
		else if (weapon.getLevel() == Weapon.Level.lev2)
			arrow = new Arrow(Weapon.Level.lev2, Type.Arrow, x, y + width / 2);
		else if (weapon.getLevel() == Weapon.Level.lev3)
			arrow = new Arrow(Weapon.Level.lev3, Type.Arrow, x, y + width / 2);
		if (left)
			((Arrow) arrow).left = true;
		arrowsShooted.add(arrow);
	}

	public void updateEnemyHard(float dt) {
		int rand = (int) (Math.random() * 100);
		if (rand > 80 && rand < 95 && weapon.getType() == Type.Bow) {
			if (x < Game.world.battle.character.x)
				fightRight();
			else
				fightLeft();
		} else if ((x - Game.world.battle.character.getX()
				- Game.world.battle.character.width / 2 < Game.world.battle.character.width / 3
				&& x - Game.world.battle.character.getX() > 0)
				|| (Game.world.battle.character.getX() - width / 2 - x < Game.world.battle.character.width / 3
						&& Game.world.battle.character.getX() - x > 0)) {

			if (rand < 20 && Game.world.battle.character.fighting)
				jump(dt);
			else if (x - Game.world.battle.character.getX()
					- Game.world.battle.character.width / 2 < Game.world.battle.character.width / 3
					&& x - Game.world.battle.character.getX() > 0 && rand < 90)
				fightLeft();
			else if (Game.world.battle.character.getX() - width / 2 - x < Game.world.battle.character.width / 3
					&& Game.world.battle.character.getX() - x > 0 && rand < 90)
				fightRight();
		} else if (Game.world.battle.character.getX() > x && rand < 90) {
			if (rand > 75 && rand < 80)
				launchBomb(dt);
			movesRight(dt);
		} else if (x > Game.world.battle.character.getX() && rand < 90) {
			if (rand > 75 && rand < 80)
				launchBomb(dt);
			movesLeft(dt);
		}

	}

	public void launchBomb(float dt) {
		if (left && !bombs.isEmpty()) {
			int velocityy = 200;

			// calcolo della gittata
			velocityy = (int) Math.sqrt(((x - Game.world.battle.character.getX()) * GameConfig.gravity)
					/ ((2 * Math.cos(30 * (Math.PI / 180)) * Math.sin(90 * (Math.PI / 180)))));
			Iterator<Bomb> it1 = bombs.iterator();
			while (it1.hasNext()) {
				Bomb ob = (Bomb) it1.next();
				if (!ob.launched) {
					ob.launch(velocityy, this);
					break;
				}
			}
		} else if (right && !bombs.isEmpty()) {
			int velocityy = 200;
			// calcolo della gittata
			velocityy = (int) Math.sqrt(((Game.world.battle.character.getX() + width / 3 - x) * GameConfig.gravity)
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

	public void updateEnemyMedium(float dt) {
		int rand = (int) (Math.random() * 100);
		if (rand > 85 && rand < 95 && weapon.getType() == Type.Bow) {
			if (x < Game.world.battle.character.x)
				fightRight();
			else
				fightLeft();
		} else if ((x - Game.world.battle.character.getX()
				- Game.world.battle.character.width / 2 < Game.world.battle.character.width / 3
				&& x - Game.world.battle.character.getX() > 0)
				|| (Game.world.battle.character.getX() - width / 2 - x < Game.world.battle.character.width / 3
						&& Game.world.battle.character.getX() - x > 0)) {

			if (rand < 10 && Game.world.battle.character.fighting)
				jump(dt);
			else if (x - Game.world.battle.character.getX()
					- Game.world.battle.character.width / 2 < Game.world.battle.character.width / 3
					&& x - Game.world.battle.character.getX() > 0 && rand < 55)
				fightLeft();
			else if (Game.world.battle.character.getX() - width / 2 - x < Game.world.battle.character.width / 3
					&& Game.world.battle.character.getX() - x > 0 && rand < 55)
				fightRight();

		} else if (Game.world.battle.character.getX() > x && rand <= 70) {
			if (rand > 68 && rand <= 70)
				launchBomb(dt);
			movesRight(dt);
		}

		else if (x > Game.world.battle.character.getX() && rand <= 70) {
			if (rand > 68 && rand <= 70)
				launchBomb(dt);
			movesLeft(dt);
		}
	}

	public void updateEnemyEasy(float dt) {
		int rand = (int) (Math.random() * 100);
		if (rand > 93 && rand < 95 && weapon.getType() == Type.Bow) {
			if (x < Game.world.battle.character.x)
				fightRight();
			else
				fightLeft();
		} else if ((x - Game.world.battle.character.getX()
				- Game.world.battle.character.width / 2 < Game.world.battle.character.width / 3
				&& x - Game.world.battle.character.getX() > 0)
				|| (Game.world.battle.character.getX() - width / 2 - x < Game.world.battle.character.width / 3
						&& Game.world.battle.character.getX() - x > 0)) {
			if (rand < 7 && Game.world.battle.character.fighting)
				jump(dt);
			else if (x - Game.world.battle.character.getX()
					- Game.world.battle.character.width / 2 < Game.world.battle.character.width / 3
					&& x - Game.world.battle.character.getX() > 0 && rand < 40)
				fightLeft();
			else if (Game.world.battle.character.getX() - width / 2 - x < Game.world.battle.character.width / 3
					&& Game.world.battle.character.getX() - x > 0 && rand < 40)
				fightRight();
		} else if (Game.world.battle.character.getX() > x && rand <= 50) {
			if (rand == 50)
				launchBomb(dt);
			movesRight(dt);
		} else if (x > Game.world.battle.character.getX() && rand <= 50) {
			if (rand == 50)
				launchBomb(dt);
			movesLeft(dt);
		}

	}

	public void fightRight() {
		right = true;
		left = false;
		width += weapon.getWidth();
		if (collide())
			Game.world.battle.character.decreaseHealth(weapon);
		width -= weapon.getWidth();

		setState(StateDynamicObject.FIGHTINGRIGHT);
		fighting = true;

	}

	public void fightLeft() {
		right = false;
		left = true;
		x -= weapon.getWidth();
		if (collide())
			Game.world.battle.character.decreaseHealth(weapon);
		x += weapon.getWidth();

		setState(StateDynamicObject.FIGHTINGLEFT);
		fighting = true;

	}

	public void updateVelocityY(float dt) {
		velocityY -= GameConfig.gravity * dt;
	}

	public void stand() {
		if (!fighting) {
			setState(StateDynamicObject.STANDING);
			stateTimer = 0;
		}
	}

	public void movesRight(float dt) {
		left = false;
		right = true;
		if (x + velocity * dt + getWidth() < GameConfig.WIDTH_BATTLE)
			x += velocity * dt;
		if (collide())
			x -= velocity * dt;
		if (!fighting)
			setState(StateDynamicObject.RUNNINGRIGHT);
	}

	public void movesLeft(float dt) {
		left = true;
		right = false;
		if (x > 0)
			x -= velocity * dt;
		if (collide())
			x += velocity * dt;
		if (!fighting)
			setState(StateDynamicObject.RUNNINGLEFT);
	}

	public void jump(float dt) {
		if (!jumping && !doubleJumping) {
			jumping = true;
			velocityY = 100;
			velocityX = 10;
			setState(StateDynamicObject.JUMPING);
		} else if (jumping && !doubleJumping) {
			jumping = false;
			doubleJumping = true;
			velocityY = 100;
			velocityX = 10;
		}
	}

	public boolean collide() {

		if (!((x > Game.world.battle.character.getX() + Game.world.battle.character.getWidth() / 2
				|| Game.world.battle.character.getX() > x + width / 2)
				|| (y > Game.world.battle.character.getY() + Game.world.battle.character.getHeight() / 2
						|| Game.world.battle.character.getY() > y + height / 2)))
			return true;
		return false;
	}

	public void decreaseHealth(Weapon weaponCharacter) {
		health -= weaponCharacter.getDamage();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getStateTimer() {
		return stateTimer;
	}

	public StateDynamicObject getCurrentState() {
		return currentState;
	}

	public ArrayList<Bomb> getBombe() {
		return bombs;
	}
}
