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
		if ((jumping || doubleJumping) && player.y + velocityY * dt > GameConfig.mainY_Battle) {
			player.y += velocityY * dt;
			// System.out.println(velocityY + " "+ velocityY*dt);
			updateVelocityY(dt);
			setState(StateDynamicObject.JUMPING, dt);

			if (collide() && player.x < Game.world.battle.enemy.getX())
				player.x = Client.networkWorld.battle.enemy.getX() - player.getWidth() / 2;
			else if (collide() && player.x > Client.networkWorld.battle.enemy.getX())
				player.x = Client.networkWorld.battle.enemy.getX() + Client.networkWorld.battle.enemy.getWidth() / 2;

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

	@Override
	public void fightRight(float dt) {
		player.width += player.primary_weapon.getWidth();
		if (collide())
			Client.networkWorld.battle.enemy.decreaseHealth(player.primary_weapon);
		player.width -= player.primary_weapon.getWidth();

		setState(StateDynamicObject.FIGHTINGRIGHT, dt);
		fighting = true;

	}

	@Override
	public void fightLeft(float dt) {
		player.x -= player.primary_weapon.getWidth();
		if (collide())
			Client.networkWorld.battle.enemy.decreaseHealth(player.primary_weapon);
		player.x += player.primary_weapon.getWidth();

		setState(StateDynamicObject.FIGHTINGLEFT, dt);
		fighting = true;

	}

	@Override
	public boolean collide() {

		if (!((player.x > Client.networkWorld.battle.enemy.getX() + Client.networkWorld.battle.enemy.getWidth() / 2
				|| Client.networkWorld.battle.enemy.getX() > player.x + player.width / 2)
				|| (player.y > Client.networkWorld.battle.enemy.getY()
						+ Client.networkWorld.battle.enemy.getHeight() / 2
						|| Client.networkWorld.battle.enemy.getY() > player.y + player.height / 2)))
			return true;
		return false;
	}

	@Override
	public void movesRight(float dt) {
		right = true;
		left = false;
		if (player.x + player.velocity * dt + player.getWidth() < GameConfig.WIDTH_BATTLE){
			player.x += player.velocity * dt;
			System.out.println(player.velocity);
		}
		if (collide()){
			System.out.println("collide");
			player.x -= player.velocity * dt;
		}
		if (!fighting)
			setState(StateDynamicObject.RUNNINGRIGHT, dt);
	}

}
