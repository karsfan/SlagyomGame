package it.slagyom.src.World;

import it.slagyom.src.Character.Character;
import it.slagyom.src.Character.CharacterBattle;

public class Battle implements Runnable {

	public CharacterBattle character;
	public Enemy enemy;
	public static int WIDTH;
	public static int HEIGHT;

	public Battle() {
		character = null;
		enemy = null;
	}

	public Battle(Character character, Enemy enemy) {
		this.character = new CharacterBattle(character);
		// this.enemy = enemy;
		this.enemy = new Enemy(null, 100, 100, null, null, null);
		WIDTH = 720;
		HEIGHT = 480;

	}

	public void moveEnemy(float dt) {
		enemy.update(dt);
	}

	public void jumpEnemy() {

	}

	public Enemy getEnemy() {
		return enemy;
	}

	public void setEnemy(Enemy enemy) {
		this.enemy = enemy;
	}

	public  boolean update(float dt) {
		// moveEnemy(dt);
		if (enemy.health <= 0){
			System.out.println("Hai vinto");
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

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
