package multiplayer;

import character.Bag;
import character.DynamicObjects;
import character.Weapon;
import character.Weapon.Level;
import character.Weapon.Type;

public class NetworkPlayer extends DynamicObjects {

	public Bag bag;
	public Weapon primary_weapon;
	public float health;
	public float power;
	public int coins;
	public boolean collideShop = false;
	public boolean collideGym = false;
	
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
}
