package battle;

import com.badlogic.gdx.Gdx;
import it.slagyom.src.Character.DynamicObjects;
import it.slagyom.src.World.Game;
import it.slagyom.src.World.GameConfig;
import it.slagyom.src.World.Weapon;
import it.slagyom.src.World.Weapon.Type;

public class Enemy extends DynamicObjects {

	public enum Level {
		EASY, MEDIUM, HARD
	};


	public float health;
	float power;
	Weapon weapon;
	Pack win_bonus;
	public Level level;

	public float stateTimer;
	public boolean fighting;
	public float fightingTimeCurrent;
	public float fightingTime;
	public boolean jumping;
	public boolean doubleJumping;
	public float velocityY;
	public float velocityX;

	public Enemy(String name, float life, Weapon weapon, Pack win_bonus, Level level) {

		velocity = 60;
		this.setName(name);
		this.health = 300;
		// this.weapon = weapon;
		this.weapon = new Weapon(it.slagyom.src.World.Weapon.Level.lev1, Type.Spear);
		this.win_bonus = win_bonus;
		this.level = level;

		stateTimer = 0;
		x = 700;
		y = 250;
		height = 150;
		width = 120;
		currentState = StateDynamicObject.STANDING;
		previousState = null;
		fighting = false;
		fightingTimeCurrent = 0;
		fightingTime = 0.2f;

		jumping = false;
		doubleJumping = false;
		velocityY = 0;
		velocityX = 10;
	}

	public Enemy(Level level) {
		this.level = level;
		switch (level) {
		case EASY:
			name = "Bob";
			weapon = new Weapon(it.slagyom.src.World.Weapon.Level.lev1);
			health = 100;
			//win_bonus = new Pack(Level.EASY);
			velocity = 40;
			break;
		case MEDIUM:
			name = "John";
			weapon = new Weapon(it.slagyom.src.World.Weapon.Level.lev2);
			health = 250;
			win_bonus = new Pack(Level.MEDIUM);
			velocity = 60;
			break;
		case HARD:
			name = "Ciccio";
			weapon = new Weapon(it.slagyom.src.World.Weapon.Level.lev3);
			health = 400;
			win_bonus = new Pack(Level.HARD);
			velocity = 80;
			break;
		default:
			break;
		}
		stateTimer = 0;
		x = 700;
		y = 250;
		height = 150;
		width = 120;
		currentState = StateDynamicObject.STANDING;
		previousState = null;
		fighting = false;
		fightingTimeCurrent = 0;
		fightingTime = 0.2f;

		jumping = false;
		doubleJumping = false;
		velocityY = 0;
		velocityX = 10;
	}

	public float getHealth() {
		return health;
	}

	private void setStateTimer(float f) {
		stateTimer = f;
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
			fightingTimeCurrent += 0.02;
			setState(getCurrentState());

		} else if (fighting && fightingTimeCurrent > fightingTime) {
			fighting = false;
			fightingTimeCurrent = 0;
		}
		dt = 0.35f;
		if ((jumping || doubleJumping) && y + velocityY * dt > 250) {
			y += velocityY * dt;
			updateVelocityY(dt);
			setState(StateDynamicObject.JUMPING);
			if (collide() && x < Game.world.battle.character.getX())
				x = Game.world.battle.character.getX() - getWidth() / 2;
			else if (collide() && x > Game.world.battle.character.getX())
				x = Game.world.battle.character.getX() + Game.world.battle.character.getWidth() / 2;
		} else {
			jumping = false;
			doubleJumping = false;
			y = 250;
			velocityY = 0;
		}

	}

	public void updateEnemyHard(float dt) {
		int rand = (int) (Math.random() * 100);
		if ((x - Game.world.battle.character.getX() < 100 && x - Game.world.battle.character.getX() > 0)
				|| (Game.world.battle.character.getX() - x < 100 && Game.world.battle.character.getX() - x > 0)) {

			if (rand < 15 && Game.world.battle.character.fighting)
				jump(dt);
			if (rand > 15 && rand < 30 && Game.world.battle.character.fighting) {
				setState(StateDynamicObject.DEFENDING);
			} else if (x - Game.world.battle.character.getX() < 100 && x - Game.world.battle.character.getX() > 0
					&& rand < 90)
				fightLeft();
			else if (Game.world.battle.character.getX() - x < 100 && Game.world.battle.character.getX() - x > 0
					&& rand < 90)
				fightRight();
		} else if (Game.world.battle.character.getX() > x && rand < 90)
			movesRight(dt);
		else if (x > Game.world.battle.character.getX() && rand < 90)
			movesLeft(dt);

	}

	public void updateEnemyMedium(float dt) {
		int rand = (int) (Math.random() * 100);
		if ((x - Game.world.battle.character.getX() < 100 && x - Game.world.battle.character.getX() > 0)
				|| (Game.world.battle.character.getX() - x < 100 && Game.world.battle.character.getX() - x > 0)) {

			if (rand < 10 && Game.world.battle.character.fighting)
				jump(dt);
			else if (rand > 10 && rand < 25 && Game.world.battle.character.fighting)
				setState(StateDynamicObject.DEFENDING);
			else if (x - Game.world.battle.character.getX() < 100 && x - Game.world.battle.character.getX() > 0
					&& rand < 55)
				fightLeft();
			else if (Game.world.battle.character.getX() - x < 100 && Game.world.battle.character.getX() - x > 0
					&& rand < 55)
				fightRight();

		} else if (Game.world.battle.character.getX() > x && rand < 70)
			movesRight(dt);

		else if (x > Game.world.battle.character.getX() && rand < 70)
			movesLeft(dt);
	}

	public void updateEnemyEasy(float dt) {
		int rand = (int) (Math.random() * 100);
		if ((x - Game.world.battle.character.getX() < 100 && x - Game.world.battle.character.getX() > 0)
				|| (Game.world.battle.character.getX() - x < 100 && Game.world.battle.character.getX() - x > 0)) {
			if (rand < 5 && Game.world.battle.character.fighting)
				jump(dt);
			else if (rand > 5 && rand < 20 && Game.world.battle.character.fighting)
				setState(StateDynamicObject.DEFENDING);
			else if (x - Game.world.battle.character.getX() < 100 && x - Game.world.battle.character.getX() > 0
					&& rand < 40)
				fightLeft();
			else if (Game.world.battle.character.getX() - x < 100 && Game.world.battle.character.getX() - x > 0
					&& rand < 40)
				fightRight();
		} else if (Game.world.battle.character.getX() > x && rand < 50)
			movesRight(dt);
		else if (x > Game.world.battle.character.getX() && rand < 50)
			movesLeft(dt);
	}

	public void fightRight() {
		width += weapon.getWidth();
		if (collide())
			Game.world.battle.character.decreaseHealth(weapon);
		width -= weapon.getWidth();

		setState(StateDynamicObject.FIGHTINGRIGHT);
		fighting = true;

	}

	public void fightLeft() {
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

		if (x + velocity * dt + getWidth() < 1100)
			x += velocity * dt;
		if (collide())
			x -= velocity * dt;
		if (!fighting)
			setState(StateDynamicObject.RUNNINGRIGHT);
	}

	public void movesLeft(float dt) {

		if (x - width / 2 > 0)
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
		if (currentState == StateDynamicObject.DEFENDING) {
			health -= weaponCharacter.getDamage() / 2;
			System.out.println("quiiiiiiiiiii");
		} else
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
}
