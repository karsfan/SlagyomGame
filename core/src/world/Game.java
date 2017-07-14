package world;

import battle.Enemy;
import gameManager.GameSlagyom;

public class Game {

	// ListenableGraph<Object, DefaultEdge> graph;
	public static World world;
	
	public static Class<? extends Enemy> enemy = null;

	public Game(String name, boolean male) {

		world = new World(name, male);

		while (!world.addDynamicObject())
			;
		while (!world.addItems())
			;
		world.player.x = 500;
		world.player.y = 500;
		
	}

	
	// constructor for loadGame
	public Game(String path, String name, boolean male) {
		world = new World(path, name, male);
	}

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
		
		world.player.positionCharacter();
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
