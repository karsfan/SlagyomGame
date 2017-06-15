package it.slagyom.src.World;

import com.badlogic.gdx.Gdx;
import it.slagyom.src.Character.DynamicObjects;
import it.slagyom.src.World.Weapon.Type;

public class Enemy extends DynamicObjects {

	public enum Level {
		EASY, MEDIUM, HARD
	};

	private String name;
	float health;
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

	public Enemy(String name, float life, float power, Weapon weapon, Pack win_bonus, Level level) {

		velocity = 60;
		this.setName(name);
		this.health = life;
		this.power = power;
		// this.weapon = weapon;
		this.weapon = new Weapon(it.slagyom.src.World.Weapon.Level.lev1, Type.Spear);
		this.win_bonus = win_bonus;
		this.level = level;

		stateTimer = 0;
		x = 700;
		y = 250;
		height = 120;
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
			int rand = (int) (Math.random() * 10);
			if (rand == 1)
				;//jump(dt);
			else if (x - Game.world.battle.character.getX() < 100 && x - Game.world.battle.character.getX() > 0)
				fightLeft();
			else if (Game.world.battle.character.getX() - x < 100 && Game.world.battle.character.getX() - x > 0)
				fightRight();
			else if (Game.world.battle.character.getX() > x)
				movesRight(dt);
			else if (x > Game.world.battle.character.getX())
				movesLeft(dt);
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
