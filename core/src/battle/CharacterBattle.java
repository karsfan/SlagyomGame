package battle;

import java.util.Iterator;

import character.Bag;
import character.Bomb;
import character.Player;
import character.Weapon;
import character.Weapon.Type;
import world.Game;
import world.GameConfig;

public class CharacterBattle extends Fighting implements world.ICollidable {

	public Bag bag;
	public Weapon primary_weapon;
	public float health;
	public CharacterBattle(Player player) {
		super();
		stateTimer = 0;
		
		bag = player.bag;
		primary_weapon = player.primary_weapon;
		System.out.println(player.health);
		this.health = player.health;
		this.x = 100;
		this.y = GameConfig.mainY_Battle;
		if (player.primary_weapon.getType() == Type.Sword)
			this.width = 200;
		else
			this.width = 120;
		right = true;
		velocity = 100;

	}

	public void swapWeapon() {
		Weapon temporary = new Weapon(Game.world.player.primary_weapon.getLevel(),
				Game.world.player.primary_weapon.getType());
		Game.world.player.primary_weapon = bag.secondary_weapon;
		bag.secondary_weapon = temporary;
		primary_weapon = Game.world.player.primary_weapon;

		if (primary_weapon.getType() == Type.Sword)
			width = 200;
		else
			width = 120;
	}

	public float getHealth() {
		return (float) health;
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
			setState(getCurrentState(), dt);
		} else if (fighting && fightingTimeCurrent > fightingTime) {
			fighting = false;
			fightingTimeCurrent = 0;
		}
		dt = 0.35f;
		if ((jumping || doubleJumping) && y + velocityY * dt > GameConfig.mainY_Battle) {
			y += velocityY * dt;
			// System.out.println(velocityY + " "+ velocityY*dt);
			updateVelocityY(dt);
			setState(StateDynamicObject.JUMPING, dt);

			if (collide() && x < Game.world.battle.enemy.getX())
				x = Game.world.battle.enemy.getX() - getWidth() / 2;
			else if (collide() && x > Game.world.battle.enemy.getX())
				x = Game.world.battle.enemy.getX() + Game.world.battle.enemy.getWidth() / 2;

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
					System.out.println("Bomba player eliminata");
					continue;
				}
			}
		}

	}

	public void fightRight(float dt) {
		width += primary_weapon.getWidth();
		if (collide())
			Game.world.battle.enemy.decreaseHealth(primary_weapon);
		width -= primary_weapon.getWidth();

		setState(StateDynamicObject.FIGHTINGRIGHT, dt);
		fighting = true;

	}

	public void fightLeft(float dt) {
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
			// velocityY = 400;
			velocityY = 100;
			velocityX = 10;
			setState(StateDynamicObject.JUMPING, dt);
		} else if (jumping && !doubleJumping) {
			jumping = false;
			doubleJumping = true;
			// velocityY = 400;
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

	private void setStateTimer(float f) {
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
				break;
			}
			// System.out.println(bomba.lanciata);
		}
	}

}
