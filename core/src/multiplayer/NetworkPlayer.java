package multiplayer;

import character.Bag;
import character.DynamicObjects;
import character.Weapon;
import character.DynamicObjects.StateDynamicObject;
import character.Weapon.Level;
import character.Weapon.Type;
import world.GameConfig;

public class NetworkPlayer extends DynamicObjects {

	public Bag bag;
	public Weapon primary_weapon;
	public float health;
	public float power;
	public int coins;
	public boolean collideShop = false;
	public boolean collideGym = false;
	public int ID = 0;
	public boolean player = false;

	public NetworkPlayer(String name) {
		super();
		this.name = name;
		bag = new Bag();
		primary_weapon = new Weapon(Level.lev1, Type.Spear);
		health = 300;
		power = 100;
		coins = 0;

		velocity = 100;

		currentState = StateDynamicObject.STANDING;
		previousState = StateDynamicObject.STANDING;
		stateTimer = 0;
		height = 30;
		width = 30;
	}

	public NetworkPlayer() {
		super();
		// this.name = name;
		bag = new Bag();
		primary_weapon = new Weapon(Level.lev1, Type.Spear);
		health = 300;
		power = 100;
		coins = 0;

		velocity = 100;

		currentState = StateDynamicObject.STANDING;
		previousState = StateDynamicObject.STANDING;
		stateTimer = 0;
		height = 30;
		width = 30;
	}

	public void setY(float f) {
		y = f;

	}

	public void setX(float f) {
		x = f;

	}

	public void movesRight(float dt) {
		if (x < GameConfig.WIDTH - width / 2) {
			float velocityX = velocity;
			x += (int)(velocityX * dt);
			if (collide(this))
				x -= (int)(velocityX * dt);
		}
		setState(StateDynamicObject.RUNNINGRIGHT);
	}

	public void movesLeft(float dt) {

		if (x > 5) {
			float velocityX = velocity;
			x -= (int)(velocityX * dt);
			if (collide(this))
				x +=(int) (velocityX * dt);

		}
		setState(StateDynamicObject.RUNNINGLEFT);
	}

	public void movesUp(float dt) {

		if (y < GameConfig.HEIGHT - height - 5) {
			float velocityY = velocity;

			y += (int)(velocityY * dt);
			if (collide(this)) {
				y -= (int)(velocityY * dt);
			}
		}
		setState(StateDynamicObject.RUNNINGUP);
	}

	public void movesDown(float dt) {
		if (y > 0) {
			float velocityY = velocity;

			y -= (int)(velocityY * dt);
			if (collide(this))
				y += (int)(velocityY * dt);
		}
		setState(StateDynamicObject.RUNNINGDOWN);
	}

}
