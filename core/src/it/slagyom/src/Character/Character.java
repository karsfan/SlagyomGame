package it.slagyom.src.Character;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import it.slagyom.src.World.Game;
import it.slagyom.src.World.GameConfig;
import it.slagyom.src.World.ICollidable;
import it.slagyom.src.World.Tile;
import it.slagyom.src.World.Weapon;
import it.slagyom.src.World.Weapon.Level;
import it.slagyom.src.World.Weapon.Type;
import it.slagyom.PlayScreen;
import it.slagyom.src.Map.Item;
import it.slagyom.src.Map.StaticObject.Element;

public class Character extends DynamicObjects implements ICollidable {

	public String name;
	public Bag bag;
	public Weapon primary_weapon;
	public float health;
	public float power;
	public int coins;
	public boolean collideDoor=false;
	public Character(String name) {
		super();
		this.name = name;
		bag = new Bag();
		primary_weapon = new Weapon(Level.lev1, Type.Spear);
		health = 100;
		power = 100;
		coins = 0;
		while(!positionCharacter());
		velocity = 100;

		currentState = StateDynamicObject.STANDING;
		previousState = StateDynamicObject.STANDING;
		stateTimer = 0;
		height = 30;
		width = 30;
	}

	public boolean positionCharacter() {
		int rand = (int) (Math.random() * 1440);
		x = rand;
		rand = (int) (Math.random() * 960);
		y = rand;
		if (collide(this)){
			return false;
		}
		return true;
	}

	public Character(Character character) {
		super(character.x, character.y, character.currentState, character.previousState, character.stateTimer,
				character.width, character.height, character.velocity);
		this.bag = character.bag;
		this.primary_weapon = character.primary_weapon;
		this.health = character.health;
		this.power = character.power;
		this.coins = character.coins;
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
		System.out.println(name);
	}

	public void swapWeapon() {
		Weapon temporary = primary_weapon;
		primary_weapon = bag.secondary_weapon;
		bag.secondary_weapon = temporary;
	}

	public void pickParchment(Item parchment) {
		bag.addTool(parchment);
		// eliminata dalla mappa
		parchment.picked = true;
	}

	public void pickPotion(Item potion) {
		bag.addTool(potion);
		// eliminata dalla mappa
		potion.picked = true;
	}

	public void upgradeWeapon(Weapon weapon) {
		// weapon.upgrade(bag);
	}

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
			x += (velocity * dt);
			if (collide(this))
				x -= (velocity * dt);
		}
		setState(StateDynamicObject.RUNNINGRIGHT);
	}

	public void movesLeft(float dt) {

		if (x > 5) {
			x -= (velocity * dt);
			if (collide(this))
				x += (velocity * dt);
		}
		setState(StateDynamicObject.RUNNINGLEFT);
	}

	public void movesUp(float dt) {

		if (y < GameConfig.HEIGHT - height - 5) {
			y += (velocity * dt);
			if (collide(this)) {
				y -= (velocity * dt);
			}
		}
		setState(StateDynamicObject.RUNNINGUP);
	}

	public void movesDown(float dt) {

		if (y > 0) {
			y -= (velocity * dt);
			if (collide(this))
				y += (velocity * dt);
		}
		setState(StateDynamicObject.RUNNINGDOWN);
	}

	private void setStateTimer(float f) {
		stateTimer = f;
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

	boolean pickItem(Item item) {
		if (item.getElement() != Element.COIN) {
			if (bag.addTool(item)) {
				item.setPicked(true);
				return true;
			}
		} else {
			coins++;
			item.setPicked(true);
			return true;
		}
		return false;

	}

	@Override
	public synchronized boolean collide(Object e) {
		Iterator<Tile> it = Game.world.getListTile().iterator();
		while (it.hasNext()) {
			Object ob = (Object) it.next();
			if (ob instanceof Tile) {
				if (((Tile) ob).getElement() != Element.GROUND && ((Tile) ob).getElement() != Element.ROAD)
					if (((Tile) ob).collide(this)) {
						if (((Tile) ob).getElement() == Element.TABLE)
							PlayScreen.hud.setDialogText(((Tile) ob).getInfo());
						else if(((Tile) ob).getElement() == Element.HOME)
							if(((Tile) ob).collideDoor(this))
								collideDoor = true;
						return true;
					}
			}

		}

		Iterator<Item> it2 = Game.world.getListItems().iterator();
		while (it2.hasNext()) {
			Object ob = (Object) it2.next();
			if (ob instanceof Item) {
				if (((Item) ob).collide(this)) {
					if (pickItem((Item) ob)) {
						PlayScreen.pickAnimation((Item) ob, ((Item) ob).getX(), ((Item) ob).getY());
						it2.remove();
						return false;
					} else {
						PlayScreen.hud.setDialogText("Zaino pieno! " + "Per raccogliere abbandona qualcosa.");
						return true;
					}
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
					return true;
				}
			}
		}

		return false;
	}

	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}

	@Override
	public boolean collide() {
		// TODO Auto-generated method stub
		return false;
	}

	public Weapon getWeapon() {
		return primary_weapon;
	}

}
