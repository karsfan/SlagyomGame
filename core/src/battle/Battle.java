package battle;

import java.lang.reflect.InvocationTargetException;

import character.Player;
import world.Game;

public class Battle {

	public CharacterBattle character;
	public Player player;
	public Fighting enemyOri;
	public Fighting enemy;

	public Battle(Player player, Enemy enemy) {
		this.player = player;
		this.character = new CharacterBattle(player);
		enemyOri = enemy;
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

	// constructor for online
	public Battle(Player player) {
		this.player = player;
	}

	public boolean update(float dt) {
		if (enemy.health <= 0) {
			player.health = character.health;
			enemyOri.morto = true;
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
