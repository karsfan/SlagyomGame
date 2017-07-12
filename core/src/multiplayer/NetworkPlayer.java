package multiplayer;

import java.util.Iterator;

import character.Bag;
import character.Weapon;
import character.Player;
import character.Weapon.Level;
import character.Weapon.Type;
import staticObjects.PreEnemyHouse;
import staticObjects.Shop;
import staticObjects.StaticObject;
import staticObjects.StaticObject.Element;
import world.GameConfig;

public class NetworkPlayer extends Player {

	public int ID = 0;
	public boolean player = false;
	public int IDOtherPlayer;
	public boolean readyToFight = false;
	public boolean isFighting = false;
	public boolean collideWithOtherPlayer = false;
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

	public void movesRight(float dt) {
		if (x < GameConfig.WIDTH - width / 2) {
			float velocityX = velocity;
			x += (int) (velocityX * dt);
			if (collide(this))
				x -= (int) (velocityX * dt);
		}
		setState(StateDynamicObject.RUNNINGRIGHT);
	}

	public void movesLeft(float dt) {

		if (x > 5) {
			float velocityX = velocity;
			x -= (int) (velocityX * dt);
			if (collide(this))
				x += (int) (velocityX * dt);

		}
		setState(StateDynamicObject.RUNNINGLEFT);
	}

	public void movesUp(float dt) {

		if (y < GameConfig.HEIGHT - height - 5) {
			float velocityY = velocity;

			y += (int) (velocityY * dt);
			if (collide(this)) {
				y -= (int) (velocityY * dt);
			}
		}
		setState(StateDynamicObject.RUNNINGUP);
	}

	public void movesDown(float dt) {
		if (y > 0) {
			float velocityY = velocity;

			y -= (int) (velocityY * dt);
			if (collide(this))
				y += (int) (velocityY * dt);
		}
		setState(StateDynamicObject.RUNNINGDOWN);
	}

	@Override
	public synchronized boolean collide(Object e) {
		Iterator<StaticObject> it = Client.networkWorld.getListTile().iterator();
		while (it.hasNext()) {
			Object ob = (Object) it.next();
			if (ob instanceof StaticObject) {
				if (((StaticObject) ob).getElement() != Element.GROUND
						&& ((StaticObject) ob).getElement() != Element.ROAD)
					if (((StaticObject) ob).collide(this)) {
						if (((StaticObject) ob).getElement() == Element.PREENEMYHOME) {
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
					collideWithOtherPlayer = true;
//					Client.writer.println(
//							1 + " " + ID + " " + 0 + " " + 0 + " " + 0 + ";" + ((NetworkPlayer) ob).ID + ";");
//					Client.writer.flush();
					IDOtherPlayer = ((NetworkPlayer) ob).ID;
					System.out.println(ID + "collisione con " + IDOtherPlayer);
					return true;
				}
			}
		}
		collideWithOtherPlayer = false;
		return false;
	}

}
