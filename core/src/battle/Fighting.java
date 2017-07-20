package battle;

import character.DynamicObjects;
import weaponsAndItems.Weapon;

public class Fighting extends DynamicObjects {

	public float stateTimer;
	public boolean fighting;
	public float fightingTimeCurrent;
	public float fightingTime;
	public boolean jumping;
	public boolean doubleJumping;
	public float velocityY;
	public float velocityX;
	public int power;
	public boolean booleanLaunchBomb;
	public boolean left;
	public boolean right;
	public int health;
	public boolean dead;

	public Fighting() {
		health = 300;
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
		booleanLaunchBomb = false;
		left = false;
		right = true;
		dead = false;
		power = 50;
	}

	public void decreaseHealth(Weapon weapon) {
		health -= weapon.getDamage();
	}

	public void update(float dt) {

	}

	public int getHealth() {
		return 0;
	}

}
