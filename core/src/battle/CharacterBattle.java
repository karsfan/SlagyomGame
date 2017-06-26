package battle;

import java.util.Iterator;

import it.slagyom.src.Character.Bomb;
import it.slagyom.src.Character.Player;
import it.slagyom.src.Character.DynamicObjects.StateDynamicObject;
import it.slagyom.src.World.Game;
import it.slagyom.src.World.GameConfig;
import it.slagyom.src.World.Weapon;

public class CharacterBattle extends Fighting implements it.slagyom.src.World.ICollidable {

	public Player player;

	
	public CharacterBattle(final Player character1) {
		super();
		stateTimer = 0;

		player = new Player(character1);
		player.x = 100;
		player.y = 250;
		player.width = 120;
		player.height = 150;
		player.currentState = StateDynamicObject.STANDING;
		player.previousState = null;
		
	}

	public float getHealth() {
		return player.getHealth();
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
		if ((jumping || doubleJumping) && player.y + velocityY * dt > 250) {
			player.y += velocityY * dt;

			updateVelocityY(dt);
			setState(StateDynamicObject.JUMPING, dt);

			if (collide() && player.x < Game.world.battle.enemy.getX())
				player.x = Game.world.battle.enemy.getX() - player.getWidth() / 2;
			else if (collide() && player.x > Game.world.battle.enemy.getX())
				player.x = Game.world.battle.enemy.getX() + Game.world.battle.enemy.getWidth() / 2;

		} else {
			jumping = false;
			doubleJumping = false;
			player.y = 250;
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
		left = false;
		right = true;
		if (player.x + player.velocity * dt + player.getWidth() < 1100)
			player.x += player.velocity * dt;
		if (collide())
			player.x -= player.velocity * dt;
		if (!fighting)
			setState(StateDynamicObject.RUNNINGRIGHT, dt);
	}

	public void movesLeft(float dt) {
		left = true;
		right = false;
		if (player.x - player.width / 2 > 0)
			player.x -= player.velocity * dt;
		if (collide())
			player.x += player.velocity * dt;
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

}
