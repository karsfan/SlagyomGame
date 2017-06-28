package battle;

import it.slagyom.src.Character.Player;

public class Battle{

	public CharacterBattle character;
	public Enemy enemyOri;
	public Enemy enemy;
	

	public Battle(Player player, Enemy enemy) {
		this.character = new CharacterBattle(player);
		enemyOri = enemy;
		this.enemy = enemy;
	}

	public  boolean update(float dt) {
	
		if (enemy.health <= 0){
			System.out.println("Hai vinto"+ character.getHealth());
			enemyOri.morto = true;
			return true;
		}
		if (character.getHealth() <= 0){
			System.out.println("Hai perso. Riprova quando sarai pronto!");
			return true;
		}
		character.update(dt);
		enemy.update(dt);
		return false;
	}


}
