package battle;

import java.util.ArrayList;
import java.util.Iterator;

import character.Arrow;
import character.Bag;
import character.Bomb;
import character.Player;
import character.Weapon;
import character.Weapon.Level;
import character.Weapon.Type;
import staticObjects.Item;
import staticObjects.StaticObject.Element;
import world.Game;
import world.GameConfig;

public class CharacterBattle extends Fighting implements world.ICollidable {

	public Bag bag;
	public Weapon primary_weapon;
	public boolean male;
	public boolean arrowShooted = false;
	public ArrayList<Weapon> arrowsShooted = new ArrayList<Weapon>();

	public CharacterBattle(Player player) {
		super();
		stateTimer = 0;
		name = player.name;
		bag = player.bag;
		male = player.male;
		primary_weapon = player.primary_weapon;
		this.health = player.health;
		this.x = 100;
		this.y = GameConfig.mainY_Battle;
		if (player.primary_weapon.getType() == Type.Sword
				|| (player.primary_weapon.getType() == Type.Spear && player.primary_weapon.getLevel() == Level.lev2)
				|| (player.primary_weapon.getType() == Type.Spear && player.primary_weapon.getLevel() == Level.lev3))
			this.width = 200;
		else
			this.width = 120;
		right = true;
		velocity = 100;

	}

	public void swapWeapon() {
		if (bag.secondary_weapon != null) {
			Weapon temporary = new Weapon(Game.world.player.primary_weapon.getLevel(),
					Game.world.player.primary_weapon.getType());
			Game.world.player.primary_weapon = bag.secondary_weapon;
			bag.secondary_weapon = temporary;
			primary_weapon = Game.world.player.primary_weapon;

			if (primary_weapon.getType() == Type.Sword
					|| (primary_weapon.getType() == Type.Spear && primary_weapon.getLevel() == Level.lev2)
					|| (primary_weapon.getType() == Type.Spear && primary_weapon.getLevel() == Level.lev3))

				width = 200;
			else
				width = 120;
		}
	}

	public int getHealth() {
		return health;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getHeight() {
		return height;
	}

	public float getWidth() {
		return width;
	}

	public void update(float dt) {
		if (fighting && fightingTimeCurrent < fightingTime) {
			fightingTimeCurrent += dt;
			if (fightingTimeCurrent >= fightingTime / 2 && primary_weapon.getType() == Type.Bow)
				if (!arrowShooted) {
					shootArrow();
					arrowShooted = true;
				}
			setState(getCurrentState(), dt);
		} else if (fighting && fightingTimeCurrent > fightingTime) {
			fighting = false;
			arrowShooted = false;
			fightingTimeCurrent = 0;
		}
		dt = 0.35f;
		if ((jumping || doubleJumping) && y + velocityY * dt > GameConfig.mainY_Battle) {
			y += velocityY * dt;
			// System.out.println(velocityY + " "+ velocityY*dt);
			updateVelocityY(dt);
			setState(StateDynamicObject.JUMPING, dt);

			if (collide() && x < Game.world.battle.enemy.getX())
				x = (Game.world.battle.enemy.getX() - getWidth() / 2);
			else if (collide() && x > Game.world.battle.enemy.getX())
				x = (Game.world.battle.enemy.getX() + Game.world.battle.enemy.getWidth() / 2);

		} else {
			jumping = false;
			doubleJumping = false;
			y = GameConfig.mainY_Battle;
			velocityY = 0;
		}
		Iterator<Bomb> it1 = bag.bombe.iterator();
		while (it1.hasNext()) {
			Bomb ob = (Bomb) it1.next();
			if (ob.lanciata == true) {
				((Bomb) ob).update(dt);
				if (ob.morta) {
					it1.remove();
					// System.out.println("Bomba player eliminata");
					continue;
				}
			}
		}

	}

	public void shootArrow() {
		Weapon arrow = null;
		if (primary_weapon.getLevel() == Level.lev1)
			arrow = new Arrow(Level.lev1, Type.Freccia, x, y + width / 2);
		else if (primary_weapon.getLevel() == Level.lev2)
			arrow = new Arrow(Level.lev2, Type.Freccia, x, y + width / 2);
		else if (primary_weapon.getLevel() == Level.lev3)
			arrow = new Arrow(Level.lev3, Type.Freccia, x, y + width / 2);
		if (left)
			((Arrow) arrow).left = true;
		arrowsShooted.add(arrow);
	}

	public void fightRight(float dt) {
		right = true;
		left = false;
		width += primary_weapon.getWidth();
		if (collide())
			Game.world.battle.enemy.decreaseHealth(primary_weapon);
		width -= primary_weapon.getWidth();

		setState(StateDynamicObject.FIGHTINGRIGHT, dt);
		fighting = true;

	}

	public void fightLeft(float dt) {
		left = true;
		right = false;
		x -= primary_weapon.getWidth();
		if (collide())
			Game.world.battle.enemy.decreaseHealth(primary_weapon);
		x += primary_weapon.getWidth();

		setState(StateDynamicObject.FIGHTINGLEFT, dt);
		fighting = true;

	}

	public void updateVelocityY(float dt) {
		velocityY -= GameConfig.gravity * dt;
	}

	public void stand() {
		if (!fighting) {
			setState(StateDynamicObject.STANDING, 0);
			stateTimer = 0;
		}
	}

	public void movesRight(float dt) {
		right = true;
		left = false;
		if (x + velocity * dt + getWidth() < GameConfig.WIDTH_BATTLE)
			x += velocity * dt;
		if (collide())
			x -= velocity * dt;
		if (!fighting)
			setState(StateDynamicObject.RUNNINGRIGHT, dt);
	}

	public void movesLeft(float dt) {
		left = true;
		right = false;
		if (x > 0)
			x -= velocity * dt;
		if (collide())
			x += velocity * dt;
		if (!fighting)
			setState(StateDynamicObject.RUNNINGLEFT, dt);
	}

	public void jump(float dt) {
		if (!jumping && !doubleJumping) {
			jumping = true;
			velocityY = 100;
			velocityX = 10;
			setState(StateDynamicObject.JUMPING, dt);
		} else if (jumping && !doubleJumping) {
			jumping = false;
			doubleJumping = true;
			velocityY = 100;
			velocityX = 10;
		}
	}

	public void setState(StateDynamicObject state, float dt) {
		previousState = currentState;
		currentState = state;
		if (previousState == currentState && currentState != StateDynamicObject.STANDING)
			setStateTimer(getStateTimer() + dt);
		else
			setStateTimer(0);
	}

	public void setStateTimer(float f) {
		stateTimer = f;
	}

	@Override
	public boolean collide(Object e) {
		return false;
	}

	@Override
	public boolean collide() {

		if (!((x > Game.world.battle.enemy.getX() + Game.world.battle.enemy.getWidth() / 2
				|| Game.world.battle.enemy.getX() > x + width / 2)
				|| (y > Game.world.battle.enemy.getY() + Game.world.battle.enemy.getHeight() / 2
						|| Game.world.battle.enemy.getY() > y + height / 2)))
			return true;
		return false;
	}

	public Weapon getWeapon() {
		return primary_weapon;
	}

	public float getStateTimer() {
		return stateTimer;
	}

	public StateDynamicObject getPreviousState() {
		return previousState;
	}

	public StateDynamicObject getCurrentState() {
		return currentState;
	}

	public void decreaseHealth(Weapon weapon) {
		health -= weapon.getDamage();
	}

	public void caricaBomba(float dt) {
		forza += 100 * dt;
	}

	public void lancia() {
		Iterator<Bomb> itBomb = bag.bombe.iterator();
		while (itBomb.hasNext()) {
			Bomb bomba = (Bomb) itBomb.next();
			if (!bomba.lanciata) {
				bomba.lanciata = true;
				bomba.lancia(forza, this);
				bomba.id = "Player";
				// System.out.println(bomba.lanciata);
				break;
			}
		}
	}

	// public void useItem(Item item) {
	// if (item.getElement() == Element.POTION) {
	// switch (item.getLevel()) {
	// case FIRST:
	// if (!GameSlagyom.modalityMultiplayer){
	// Game.world.battle.character.health += 15;
	// if(Game.world.battle.character.health > 300)
	// Game.world.battle.character.health = 300;
	// }
	// else{
	// Client.networkWorld.battle.character.health += 15;
	// if(Client.networkWorld.battle.character.health > 300)
	// Client.networkWorld.battle.character.health = 300;
	// }
	// break;
	// case SECOND:
	// if (!GameSlagyom.modalityMultiplayer){
	// Game.world.battle.character.health += 25;
	// if(Game.world.battle.character.health > 300)
	// Game.world.battle.character.health = 300;
	// }
	// else{
	// Client.networkWorld.battle.character.health += 25;
	// if(Client.networkWorld.battle.character.health > 300)
	// Client.networkWorld.battle.character.health = 300;
	// }
	// break;
	// case THIRD:
	// if (!GameSlagyom.modalityMultiplayer){
	// Game.world.battle.character.health += 45;
	// if(Game.world.battle.character.health > 300)
	// Game.world.battle.character.health = 300;}
	// else{
	// Client.networkWorld.battle.character.health += 45;
	// if(Client.networkWorld.battle.character.health > 300)
	// Client.networkWorld.battle.character.health = 300;
	// }
	// break;
	// default:
	// System.out.println("potion non assegnata");
	// break;
	// }
	// removeItem(item.getElement(), item.getLevel());
	// }
	// }
	public void useItem(Item item) {
		if (item.getElement() == Element.POTION) {
			switch (item.getLevel()) {
			case FIRST:
				health += 15;
				if (health > 300)
					health = 300;
				break;
			case SECOND:
				health += 25;
				if (health > 300)
					health = 300;
				break;
			case THIRD:
				health += 25;
				if (health > 300)
					health = 300;
				break;
			default:
				System.out.println("potion non assegnata");
				break;
			}
			bag.removeItem(item.getElement(), item.getLevel());
		}
	}

}
