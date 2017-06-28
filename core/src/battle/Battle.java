package battle;

import it.slagyom.src.Character.Player;

public class Battle{

	public CharacterBattle character;
	public Enemy enemyOri;
	public Enemy enemy;
	public static int WIDTH;
	public static int HEIGHT;

	public Battle(Player player, Enemy enemy) {
		this.character = new CharacterBattle(player);
		enemyOri = enemy;
		this.enemy = enemy;
		WIDTH = 720;
		HEIGHT = 480;
	}


	public void jumpEnemy() {

	}

	public Enemy getEnemy() {
		return enemy;
	}
	public CharacterBattle getCharacter()
	{
		return character;
	}
	public void setEnemy(Enemy enemy) {
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
