package character;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;

import staticObjects.EnemyHome;
import staticObjects.BossHome;
import staticObjects.PreEnemyHouse;
import staticObjects.Shop;
import staticObjects.StaticObject;
import staticObjects.StaticObject.Element;
import weaponsAndItems.Bag;
import weaponsAndItems.Item;
import weaponsAndItems.Weapon;
import weaponsAndItems.Weapon.Level;
import weaponsAndItems.Weapon.Type;
import world.Game;
import world.GameConfig;
import world.ICollidable;

public class Player extends DynamicObjects implements ICollidable {

	public String gender;
	public Bag bag;
	public Weapon primary_weapon;
	public int health;
	public int coins;
	public boolean male;
	public boolean collideShop;
	public boolean collideGym;
	public boolean collideCoin;
	public boolean collideItem;
	public String textDialog;
	/**
	 * Constructor that receive the name and the gender of the Player
	 * @param name name of the Player
	 * @param male true if Player is a man, false if Player is a woman
	 */
	public Player(String name, boolean male) {
		super();
		this.name = name;
		this.male = male;
		bag = new Bag();
		primary_weapon = new Weapon(Level.lev1, Type.Spear);
		health = 300;
		coins = 25;

		velocity = 100;
		currentState = StateDynamicObject.STANDING;
		previousState = StateDynamicObject.STANDING;
		stateTimer = 0;
		height = 30;
		width = 30;
		collideShop = false;
		collideGym = false;
		collideCoin = false;
		collideItem = false;
		textDialog = "";
	}

	public Player() {
		super();
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
		collideShop = false;
		collideGym = false;
		collideCoin = false;
		collideItem = false;
		textDialog = "";
	}

	public boolean positionCharacter() {
		int rand = (int) (Math.random() * GameConfig.WIDTH);
		x = rand;
		rand = (int) (Math.random() * GameConfig.HEIGHT);
		y = rand;
		if (collide()) {
			positionCharacter();
		}
		return true;
	}

	public Player(Player player) {
		super(player.x, player.y, player.currentState, player.previousState, player.stateTimer, player.width,
				player.height, player.velocity);
		this.bag = player.bag;
		this.primary_weapon = player.primary_weapon;
		this.health = player.health;
		this.coins = player.coins;
	}

	public float getHealth() {
		return health;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setPrimary_weapon(Weapon primary_weapon) {
		this.primary_weapon = primary_weapon;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void pickParchment(Item parchment) {
		bag.add(parchment);
		parchment.picked = true;
	}

	public void pickPotion(Item potion) {
		bag.add(potion);
		potion.picked = true;
	}
	/**
	 * Method that update the health's Player
	 * @param potion
	 */
	public void usePotionHealth(Item potion) {
		switch (potion.getLevel()) {
		case FIRST:
			health += 20;
			break;
		case SECOND:
			health += 30;
			break;
		case THIRD:
			health += 50;
			break;
		default:
			break;
		}
		bag.deletePotion(potion);
	}

	public void movesRight(float dt) {
		if (x < GameConfig.WIDTH - width / 2) {
			float velocityX = velocity;
			x += (velocityX * dt);
			if (collide())
				x -= (velocityX * dt);
		}
		setState(StateDynamicObject.RUNNINGRIGHT);
	}

	public void movesLeft(float dt) {
		if (x > 5) {
			float velocityX = velocity;
			x -= (velocityX * dt);
			if (collide())
				x += (velocityX * dt);
		}

		setState(StateDynamicObject.RUNNINGLEFT);
	}

	public void movesUp(float dt) {

		if (y < GameConfig.HEIGHT - height - 5) {
			float velocityY = velocity;
			y += (velocityY * dt);
			if (collide()) {
				y -= (velocityY * dt);
			}
		}

		setState(StateDynamicObject.RUNNINGUP);
	}

	public void movesDown(float dt) {
		if (y > 0) {
			float velocityY = velocity;

			y -= (velocityY * dt);
			if (collide())
				y += (velocityY * dt);
		}
		setState(StateDynamicObject.RUNNINGDOWN);
	}

	private void setStateTimer(float dt) {
		stateTimer = dt;
	}

	@Override
	public float getStateTimer() {
		return stateTimer;
	}

	public void setState(StateDynamicObject state) {
		previousState = currentState;
		currentState = state;

		if (previousState == currentState)
			setStateTimer(getStateTimer() + Gdx.graphics.getDeltaTime());
		else
			setStateTimer(0);

	}

	public StateDynamicObject getState() {
		return currentState;
	}

	public float getHeight() {
		return height;
	}

	public float getWidth() {
		return width;
	}

	@Override
	public synchronized boolean collide(Object o) {
		return false;
	}

	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}

	@Override
	public boolean collide() {
		Iterator<StaticObject> it = Game.world.getListTile().iterator();
		while (it.hasNext()) {
			Object ob = (Object) it.next();
			if (ob instanceof StaticObject) {
				if (((StaticObject) ob).getElement() != Element.GROUND
						&& ((StaticObject) ob).getElement() != Element.ROAD)
					if (((StaticObject) ob).collide(this)) {
						if (((StaticObject) ob).getElement() == Element.TABLE)
							textDialog = ((StaticObject) ob).getInfo();
						else if (((StaticObject) ob).getElement() == Element.SHOP) {
							if (((Shop) ob).collideDoor(this)) {
								collideShop = true;
								return true;
							}
						} else if (((StaticObject) ob).getElement() == Element.PREENEMYHOME) {
							if (((PreEnemyHouse) ob).collideDoor(this)) {
								collideGym = true;
								Game.world.createBattle((PreEnemyHouse) ob);
								return true;
							}
						} else if (((StaticObject) ob).getElement() == Element.TEMPLE) {
							if (((EnemyHome) ob).collideDoor(this)) {
								collideGym = true;
								Game.world.createBattle((EnemyHome) ob);
								return true;
							}
						} else if (((StaticObject) ob).getElement() == Element.CASTLE) {
							if (((BossHome) ob).collideDoor(this)) {
								collideGym = true;
								Game.world.createBattle((BossHome) ob);
								return true;
							}
						}
						return true;
					}
			}

		}

		Iterator<DynamicObjects> it1 = Game.world.getListDynamicObjects().iterator();
		while (it1.hasNext()) {
			Object ob = (Object) it1.next();
			if (ob instanceof DynamicObjects && ob != this) {
				if (!((x > ((DynamicObjects) ob).getX() + ((DynamicObjects) ob).getWidth() / 2
						|| ((DynamicObjects) ob).getX() > x + width / 2)
						|| (y > ((DynamicObjects) ob).getY() + ((DynamicObjects) ob).getHeight() / 2
								|| ((DynamicObjects) ob).getY() > y + height / 2))) {
					if (ob instanceof Man) {
						((Man) ob).collisionWithCharacter = true;
					}
					if (ob instanceof Woman)
						((Woman) ob).collisionWithCharacter = true;
					return true;
				}

			}
		}
		synchronized (Game.world.getListItems()) {
			Iterator<Item> it2 = Game.world.getListItems().iterator();
			while (it2.hasNext()) {
				Object ob = (Object) it2.next();
				if (ob instanceof Item) {
					if (((Item) ob).collide(this)) {
						if (((Item) ob).getElement() == Element.COIN) {
							coins++;
							collideCoin = true;
						} else {
							bag.add(ob);
							collideItem = true;
						}
						((Item) ob).picked = true;
						return false;
					}
				}
			}
		}
		collideCoin = false;
		collideGym = false;
		collideShop = false;
		collideItem = false;
		return false;
	}

	public Weapon getWeapon() {
		return primary_weapon;
	}
}
