package multiplayer;

import java.util.Iterator;

import character.Player;
import staticObjects.BossHome;
import staticObjects.EnemyHome;
import staticObjects.PreEnemyHouse;
import staticObjects.Shop;
import staticObjects.StaticObject;
import staticObjects.StaticObject.Element;
import weaponsAndItems.Bag;
import weaponsAndItems.Item;
import weaponsAndItems.Weapon;
import weaponsAndItems.Weapon.Level;
import weaponsAndItems.Weapon.Type;

public class NetworkPlayer extends Player {

	public int ID = 0;
	public boolean player = false;
	public int IDOtherPlayer;
	public boolean readyToFight = false;
	public boolean isFighting = false;
	public boolean collideWithOtherPlayer = false;
	public boolean collisionWithObject = false;
	public Item itemPicked;
	public boolean invisible = false;
	/**
	 * Constructor for initialize a new Player online
	 * @param name name of Player
	 */
	public NetworkPlayer(String name) {
		super(name, true);
		this.name = name;
		bag = new Bag();
		primary_weapon = new Weapon(Level.lev1, Type.Spear);
		health = 300;
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
		coins = 0;

		velocity = 100;

		currentState = StateDynamicObject.STANDING;
		previousState = StateDynamicObject.STANDING;
		stateTimer = 0;
		height = 30;
		width = 30;
	}

	public void setY(int f) {
		y = f;

	}

	public void setX(int f) {
		x = f;

	}

	@Override
	public synchronized boolean collide(Object e) {
		return false;
	}

	public boolean collide() {
		if (!invisible) {
			Iterator<StaticObject> it = Client.networkWorld.getListTile().iterator();
			while (it.hasNext()) {
				Object ob = (Object) it.next();
				if (ob instanceof StaticObject) {
					if (((StaticObject) ob).getElement() != Element.GROUND
							&& ((StaticObject) ob).getElement() != Element.ROAD)
						if (((StaticObject) ob).collide(this)) {
							if (((StaticObject) ob).getElement() == Element.TABLE)
								textDialog = ((StaticObject) ob).getInfo();
							else if (((StaticObject) ob).getElement() == Element.PREENEMYHOME) {
								if (((PreEnemyHouse) ob).collideDoor(this)) {
									collideGym = true;
									Client.networkWorld.createBattle((PreEnemyHouse) ob);
									return true;
								}
							} else if (((StaticObject) ob).getElement() == Element.SHOP) {
								if (((Shop) ob).collideDoor(this)) {
									collideShop = true;
									return true;
								}
							} else if (((StaticObject) ob).getElement() == Element.TEMPLE) {
								if (((EnemyHome) ob).collideDoor(this)) {
									collideGym = true;
									Client.networkWorld.createBattle((EnemyHome) ob);
									return true;
								}
							} else if (((StaticObject) ob).getElement() == Element.CASTLE) {
								if (((BossHome) ob).collideDoor(this)) {
									collideGym = true;
									Client.networkWorld.createBattle((BossHome) ob);
									return true;
								}
							}
							return true;
						}
				}
			}
			Iterator<NetworkPlayer> otherPlayer = Client.networkWorld.otherPlayers.iterator();
			while (otherPlayer.hasNext()) {
				Object ob = (Object) otherPlayer.next();
				if (ob instanceof NetworkPlayer) {
					if (!((x > ((NetworkPlayer) ob).getX() + ((NetworkPlayer) ob).getWidth() / 2
							|| ((NetworkPlayer) ob).getX() > x + width / 2)
							|| (y > ((NetworkPlayer) ob).getY() + ((NetworkPlayer) ob).getHeight() / 2
									|| ((NetworkPlayer) ob).getY() > y + height / 2))) {
						if (!((NetworkPlayer) ob).isFighting) {
							collideWithOtherPlayer = true;
							IDOtherPlayer = ((NetworkPlayer) ob).ID;
						}
						return true;
					}
				}
			}
			Iterator<Item> it2 = Client.networkWorld.getListItems().iterator();
			while (it2.hasNext()) {
				Object ob = (Object) it2.next();
				if (ob instanceof Item) {
					if (((Item) ob).collide(this)) {
						if (((Item) ob).getElement() == Element.COIN)
							coins++;
						else
							bag.add(ob);

						collisionWithObject = true;
						itemPicked = (Item) ob;
						return false;
					}
				}
			}
		}
		collisionWithObject = false;
		collideWithOtherPlayer = false;
		return false;
	}

}
