package multiplayer;

import java.util.Iterator;

import battle.CharacterBattle;
import character.Bomb;
import character.Player;
import world.Game;
import world.GameConfig;

public class NetworkCharacterBattle extends CharacterBattle {

	public NetworkCharacterBattle(Player character1) {
		super(character1);
	}

	@Override
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
				x = Client.networkWorld.battle.enemy.getX() - getWidth() / 2;
			else if (collide() && x > Client.networkWorld.battle.enemy.getX())
				x = Client.networkWorld.battle.enemy.getX() + Client.networkWorld.battle.enemy.getWidth() / 2;

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

	@Override
	public void fightRight(float dt) {
		width += primary_weapon.getWidth();
		if (collide())
			Client.networkWorld.battle.enemy.decreaseHealth(primary_weapon);
		width -= primary_weapon.getWidth();

		setState(StateDynamicObject.FIGHTINGRIGHT, dt);
		fighting = true;

	}

	@Override
	public void fightLeft(float dt) {
		x -= primary_weapon.getWidth();
		if (collide())
			Client.networkWorld.battle.enemy.decreaseHealth(primary_weapon);
		x += primary_weapon.getWidth();

		setState(StateDynamicObject.FIGHTINGLEFT, dt);
		fighting = true;

	}

	@Override
	public boolean collide() {

		if (!((x > Client.networkWorld.battle.enemy.getX() + Client.networkWorld.battle.enemy.getWidth() / 2
				|| Client.networkWorld.battle.enemy.getX() > x + width / 2)
				|| (y > Client.networkWorld.battle.enemy.getY()
						+ Client.networkWorld.battle.enemy.getHeight() / 2
						|| Client.networkWorld.battle.enemy.getY() > y + height / 2)))
			return true;
		return false;
	}

	@Override
	public void movesRight(float dt) {
		right = true;
		left = false;
		if (x + velocity * dt + getWidth() < GameConfig.WIDTH_BATTLE){
			x += velocity * dt;
		}
		if (collide()){
			System.out.println("collide");
			x -= velocity * dt;
		}
		if (!fighting)
			setState(StateDynamicObject.RUNNINGRIGHT, dt);
	}

}
