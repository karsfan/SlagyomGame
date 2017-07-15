package battle;

import java.lang.reflect.InvocationTargetException;

import character.Player;
import multiplayer.NetworkCharacterBattle;
import multiplayer.NetworkEnemy;
import multiplayer.NetworkPlayer;
import world.Game;

public class Battle {

	public CharacterBattle character;
	public Player player;
	public Fighting enemyOri;
	public Fighting enemy;

	public Battle(Player player, Enemy enemy) {
		this.player = player;
		if (player instanceof NetworkPlayer)
			this.character = new NetworkCharacterBattle((NetworkPlayer) player);
		else
			this.character = new CharacterBattle(player);
		enemyOri = enemy;
		if (enemy instanceof NetworkEnemy) {
			this.enemy = new NetworkEnemy(enemy);
		} else {
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
	}

	public Battle(NetworkPlayer player, NetworkPlayer ob) {
		character = new NetworkCharacterBattle(player);
		enemy = new NetworkCharacterBattle(ob);
		enemy.x = 700;

	}

	public boolean update(float dt) {
		if (enemy.health <= 0) {
			player.health = character.health;
			enemyOri.morto = true;
			return true;
		}
		if (character.getHealth() <= 0) {
			if (!(character instanceof NetworkCharacterBattle))
				character.health = 10;
			return true;
		}
		character.update(dt);
		enemy.update(dt);
		return false;
	}

}
