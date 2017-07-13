package multiplayer;

import java.util.Random;

import battle.Battle;
import battle.Enemy;
import battle.Enemy.Level;
import battle.Pack;
import character.DynamicObjects.StateDynamicObject;

public class NetworkBattle extends Battle {
	public Pack win_bonus;

	public NetworkBattle(NetworkPlayer player, Enemy enemy) {
		super(player, enemy);
		win_bonus = enemy.win_bonus;
	}

	public NetworkBattle(NetworkPlayer player, NetworkPlayer otherPlayer) {
		super(player, otherPlayer);
		otherPlayer.currentState = StateDynamicObject.RUNNINGLEFT;
		Random rand = new Random();
		int r = rand.nextInt(3);
		switch (r) {
		case 0:
			win_bonus = new Pack(Level.EASY);
			break;
		case 1:
			win_bonus = new Pack(Level.MEDIUM);
			break;
		case 2:
			win_bonus = new Pack(Level.HARD);
			break;
		default:
			break;
		}
	}

	public boolean update(float dt) {
		if (enemy instanceof NetworkEnemy) {
			if (enemy.health <= 0) {
				enemyOri.morto = true;
				return true;
			}
			if (character.getHealth() <= 0) {
				return true;
			}
		} else {
			if (enemy.health <= 0) {
				enemy.morto = true;
				return true;
			}
			if (character.getHealth() <= 0) {
				character.morto = true;
				return true;
			}
		}
		character.update(dt);
		enemy.update(dt);
		return false;

	}
}
