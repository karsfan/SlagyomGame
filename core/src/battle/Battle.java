package battle;

import character.Player;
import multiplayer.NetworkCharacterBattle;
import multiplayer.NetworkEnemy;
import multiplayer.NetworkPlayer;

public class Battle {

	public CharacterBattle character;
	public Fighting enemyOri;
	public Fighting enemy;

	public Battle(Player player, Enemy enemy) {
		if (player instanceof NetworkPlayer)
			this.character = new NetworkCharacterBattle((NetworkPlayer) player);
		else
			this.character = new CharacterBattle(player);
		enemyOri = enemy;
		if (enemy instanceof NetworkEnemy) {
			this.enemy = new NetworkEnemy(enemy);
		} else
			this.enemy = new Enemy(enemy);
		// this.enemy = enemy;
	}

	public Battle(NetworkPlayer player, NetworkPlayer ob) {
		character = new NetworkCharacterBattle(player);
		enemy = new NetworkCharacterBattle(ob);
		enemy.x = 700;
		//tem.out.println("Sono"+player.ID+" "+ob.ID);
	}

	public boolean update(float dt) {

		if (enemy.health <= 0) {
			//enemyOri.morto = true;
			return true;
		}
		if (character.getHealth() <= 0) {
			return true;
		}
		character.update(dt);
		enemy.update(dt);
		return false;
	}

}
