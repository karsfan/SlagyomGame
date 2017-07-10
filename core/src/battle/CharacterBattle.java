package battle;

import java.util.Iterator;

import character.Bomb;
import character.Player;
import character.Weapon;
import character.Weapon.Type;
import world.Game;
import world.GameConfig;

public class CharacterBattle extends Fighting implements world.ICollidable {

	public Player player;
	
	public CharacterBattle(final Player character1) {
		super();
		stateTimer = 0;
	
		player = new Player(character1);
		player.primary_weapon = new Weapon(character1.primary_weapon.getLevel(), character1.primary_weapon.getType());
		player.bag.secondary_weapon = new Weapon(character1.bag.secondary_weapon.getLevel(), character1.bag.secondary_weapon.getType());
		player.x = 100;
		player.y = GameConfig.mainY_Battle;
		player.width = 120;
		player.height = 150;
		player.currentState = StateDynamicObject.RUNNINGRIGHT;
		player.previousState = null;
		right = true;
		
	}
	public void swapWeapon() {
		Weapon temporary = new Weapon(player.primary_weapon.getLevel(), player.primary_weapon.getType());
		player.primary_weapon = new Weapon(player.bag.secondary_weapon.getLevel(),player.bag.secondary_weapon.getType()) ;
		player.bag.secondary_weapon = new Weapon(temporary.getLevel(), temporary.getType());
		if(player.primary_weapon.getType() == Type.Sword)
			player.width = 200;
		else
			player.width = 120;
		System.out.println(player.primary_weapon.getType() + " "+temporary.getType());
	}

	public float getHealth() {
		return (float) player.getHealth();
	}

	public float getX() {
		return player.getX();
	}

	public float getY() {
		return player.getY();
	}

	public float getHeight() {
		return player.getHeight();
	}

	public float getWidth() {
		return player.getWidth();
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
		if ((jumping || doubleJumping) && player.y + velocityY * dt > GameConfig.mainY_Battle) {
			player.y += velocityY * dt;
		//	System.out.println(velocityY + " "+ velocityY*dt);
			updateVelocityY(dt);
			setState(StateDynamicObject.JUMPING, dt);

			if (collide() && player.x < Game.world.battle.enemy.getX())
				player.x = Game.world.battle.enemy.getX() - player.getWidth() / 2;
			else if (collide() && player.x > Game.world.battle.enemy.getX())
				player.x = Game.world.battle.enemy.getX() + Game.world.battle.enemy.getWidth() / 2;

		} else {
			jumping = false;
			doubleJumping = false;
			player.y = GameConfig.mainY_Battle;
			velocityY = 0;
		}
		Iterator<Bomb> it1 = player.bag.bombe.iterator();
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
		player.width += player.primary_weapon.getWidth();
		if (collide())
			Game.world.battle.enemy.decreaseHealth(player.primary_weapon);
		player.width -= player.primary_weapon.getWidth();

		setState(StateDynamicObject.FIGHTINGRIGHT, dt);
		fighting = true;

	}

	public void fightLeft(float dt) {
		player.x -= player.primary_weapon.getWidth();
		if (collide())
			Game.world.battle.enemy.decreaseHealth(player.primary_weapon);
		player.x += player.primary_weapon.getWidth();

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
		if (player.x + player.velocity * dt + player.getWidth() < GameConfig.WIDTH_BATTLE)
			player.x += player.velocity * dt;
		if (collide())
			player.x -= player.velocity * dt;
		if (!fighting)
			setState(StateDynamicObject.RUNNINGRIGHT, dt);
	}

	public void movesLeft(float dt) {
		left = true;
		right = false;
		if (player.x > 0)
			player.x -= player.velocity * dt;
		if (collide())
			player.x += player.velocity * dt;
		if (!fighting)
			setState(StateDynamicObject.RUNNINGLEFT, dt);
	}

	public void jump(float dt) {
		if (!jumping && !doubleJumping) {
			jumping = true;
			//velocityY = 400;
			velocityY = 100;
			velocityX = 10;
			setState(StateDynamicObject.JUMPING, dt);
		} else if (jumping && !doubleJumping) {
			jumping = false;
			doubleJumping = true;
			//velocityY = 400;
			velocityY = 100;
			velocityX = 10;
		}
	}

	public void setState(StateDynamicObject state, float dt) {
		player.previousState = player.currentState;
		player.currentState = state;
		if (player.previousState == player.currentState && player.currentState != StateDynamicObject.STANDING)
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

		if (!((player.x > Game.world.battle.enemy.getX() + Game.world.battle.enemy.getWidth() / 2
				|| Game.world.battle.enemy.getX() > player.x + player.width / 2)
				|| (player.y > Game.world.battle.enemy.getY() + Game.world.battle.enemy.getHeight() / 2
						|| Game.world.battle.enemy.getY() > player.y + player.height / 2)))
			return true;
		return false;
	}

	public Weapon getWeapon() {
		return player.getWeapon();
	}

	public float getStateTimer() {
		return stateTimer;
	}

	public StateDynamicObject getPreviousState() {
		return player.previousState;
	}

	public StateDynamicObject getCurrentState() {
		return player.currentState;
	}

	public void decreaseHealth(Weapon weapon) {
		player.health -= weapon.getDamage();
	}
	public void caricaBomba(float dt) {
		forza+=100*dt;
	}
	public void lancia() {
		Iterator<Bomb> itBomb = player.bag.bombe.iterator();
		while (itBomb.hasNext()) {
			Bomb bomba = (Bomb) itBomb.next();
			if (!bomba.lanciata) {
				bomba.lanciata = true;
				bomba.lancia(forza, this);
				bomba.id = "Player";
				break;
			}
			//System.out.println(bomba.lanciata);
		}
	}

}
