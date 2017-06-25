package battle;

import it.slagyom.src.Character.Player;

public class Battle implements Runnable {

	public CharacterBattle character;
	public Enemy enemy;
	public static int WIDTH;
	public static int HEIGHT;

	public Battle() {
		character = null;
		enemy = null;
	}

	public Battle(Player player, Enemy enemy) {
		this.character = new CharacterBattle(player);
		// this.enemy = enemy;
		this.enemy = new Enemy(null, 100, null, null, enemy.level);
		WIDTH = 720;
		System.out.println("qui");
		HEIGHT = 480;
	//	run();

	}

	public void moveEnemy(float dt) {
		enemy.update(dt);
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
		// moveEnemy(dt);
		if (enemy.health <= 0){
			System.out.println("Hai vinto"+ character.getHealth() );
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
		System.out.println("ciao");
		long start = System.currentTimeMillis();
		while (true) {
			long attuale = System.currentTimeMillis();
			float dt = (float) (attuale - start);
			update((float) dt / 1000);
			start = attuale;
		}
	}

}
