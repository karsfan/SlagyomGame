package battle;

import it.slagyom.src.Character.Player;

public class Battle{

	public CharacterBattle character;
	public Enemy enemyOri;
	public Enemy enemy;
	

	public Battle(Player player, Enemy enemy) {
		this.character = new CharacterBattle(player);
		enemyOri = enemy;
		this.enemy = new Enemy(enemy);
		//this.enemy = enemy;
	}

	public boolean update(float dt) {
	
		if (enemy.health <= 0){
			enemyOri.morto = true;
			return true;
		}
		if (character.getHealth() <= 0){
			return true;
		}
		character.update(dt);
		enemy.update(dt);
		return false;
	}


}
