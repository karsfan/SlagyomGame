package multiplayer;

import java.util.Iterator;
import java.util.Random;

import battle.Battle;
import battle.Enemy;
import battle.Enemy.Level;
import battle.Pack;
import character.Arrow;
import character.Weapon;
import character.DynamicObjects.StateDynamicObject;
import world.GameConfig;

public class NetworkBattle extends Battle {
	public Pack win_bonus;
	public NetworkPlayer otherPlayer;

	public NetworkBattle(NetworkPlayer player, Enemy enemy) {
		super(player);
		enemyOri = enemy;
		this.enemy = new NetworkEnemy(enemy);
		this.character = new NetworkCharacterBattle(player);
		win_bonus = enemy.win_bonus;
	}

	public NetworkBattle(NetworkPlayer player, NetworkPlayer otherPlayer) {
		super(player);
		this.otherPlayer = otherPlayer;
		character = new NetworkCharacterBattle(player);
		enemy = new NetworkCharacterBattle(otherPlayer);
		character.x = 0;
		enemy.x = 1320;
		// System.out.println(character.health);
		// System.out.println(enemy.health);
		enemy.currentState = StateDynamicObject.RUNNINGLEFT;
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
		Iterator<Weapon> it = character.arrowsShooted.iterator();
		while (it.hasNext()) {
			Arrow ob = (Arrow) it.next();
			((Arrow) ob).update(dt);
			if (ob.collide(enemy)) {
				character.arrowsShooted.remove(ob);
				break;
			}
			if (ob.x < 0 || ob.x > GameConfig.WIDTH_BATTLE) {
				character.arrowsShooted.remove(ob);
				break;
			}
		}
		if (enemy instanceof NetworkCharacterBattle) {
			Iterator<Weapon> it2 = ((NetworkCharacterBattle) enemy).arrowsShooted.iterator();
			while (it2.hasNext()) {
				Arrow ob = (Arrow) it2.next();
				((Arrow) ob).update(dt);
				if (ob.collide(character)) {
					((NetworkCharacterBattle) enemy).arrowsShooted.remove(ob);
					break;
				}
				if (ob.x < 0 || ob.x > GameConfig.WIDTH_BATTLE) {
					((NetworkCharacterBattle) enemy).arrowsShooted.remove(ob);
					break;
				}
			}
		} else {
			Iterator<Weapon> it2 = ((Enemy) enemy).arrowsShooted.iterator();
			while (it2.hasNext()) {
				Arrow ob = (Arrow) it2.next();
				((Arrow) ob).update(dt);
				if (ob.collide(character)) {
					((Enemy) enemy).arrowsShooted.remove(ob);
					break;
				}
				if (ob.x < 0 || ob.x > GameConfig.WIDTH_BATTLE) {
					((Enemy) enemy).arrowsShooted.remove(ob);
					break;
				}
			}
		}
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
				player.health = character.health;
				// otherPlayer.morto = true;
				// enemy.morto = true;
				// System.out.println(enemy.health);
				return true;
			}
			if (character.getHealth() <= 0) {
				player.health = character.health;
				character.morto = true;
				return true;
			}
		}
		// System.out.println(enemy.health);
		character.update(dt);
		enemy.update(dt);
		return false;

	}
}
