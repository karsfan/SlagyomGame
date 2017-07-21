package battle;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import character.Player;
import weaponsAndItems.Arrow;
import weaponsAndItems.Weapon;
import world.Game;
import world.GameConfig;

public class Battle {

	public CharacterBattle character;
	public Player player;
	public Fighting originalEnemy;
	public Fighting enemy;
	
	/**
	 * Constructor of the battle in the classic mode
	 * @param player who plays the battle
	 * @param enemy who fight against the player
	 */
	public Battle(Player player, Enemy enemy) {
		this.player = player;
		this.character = new CharacterBattle(player);
		originalEnemy = enemy;
		if (Game.enemy != null) {
			try {
				this.enemy = Game.enemy.getConstructor(Enemy.class).newInstance(enemy);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				System.out.println("Errore creazione nemico nella battaglia");
				e.printStackTrace();
			}
		} else
			this.enemy = new Enemy(enemy);
	}

	/**
	 * Constructor for multiplayer game
	 * @param player who plays the battle
	 */
	public Battle(Player player) {
		this.player = player;
	}
	
	
	/**
	 * It handles all the collision and shots
	 * @param deltatime of the render
	 * @return  
	 */
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
		if (enemy.health <= 0) {
			player.health = character.health;
			originalEnemy.dead = true;
			return true;
		}
		if (character.getHealth() <= 0) {
			character.health = 10;
			player.health = character.health;
			return true;
		}
		character.update(dt);
		enemy.update(dt);

		return false;
	}

}
