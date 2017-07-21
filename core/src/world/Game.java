package world;

import battle.Enemy;
import gameManager.GameSlagyom;

public class Game {

	public static World world;
	
	public static Class<? extends Enemy> enemy = null;
	/**
	 * Constructor fo initialize a new Game
	 * @param name Player's name
	 * @param male Player's gender
	 */
	public Game(String name, boolean male) {

		world = new World(name, male);

		while (!world.addDynamicObject())
			;
		while (!world.addItems())
			;
		world.player.x = 500;
		world.player.y = 500;
		
	}

	
	/**
	 * Constructor for load a Game
	 * @param path path of the Map
	 * @param name Player's name
	 * @param male Player's gender
	 */
	public Game(String path, String name, boolean male) {
		world = new World(path, name, male);
	}
	/**
	 * Constructor for create a new Game with enemy's level external
	 * @param text name of the class that extends Enemy
	 * @param game2
	 * @param charName player's name
	 * @param male gender's name
	 */
	@SuppressWarnings("unchecked")
	public Game(String text, GameSlagyom game2, String charName, boolean male) {
		try {
			enemy = ((Class<? extends Enemy>) Class.forName("enemy.Ai." + text));
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
			System.out.println("Impossibile trovare la classe enemy.Ai." + text);
		}
		world = new World(charName, male);
		while (!world.addDynamicObject())
			;
		while (!world.addItems())
			;
		world.player.x = 500;
		world.player.y = 500;
		
	}


	public void exit() {

	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		Game.world = world;
	}

}
