package world;

import battle.Enemy;
import gameManager.GameSlagyom;

public class Game {

	// ListenableGraph<Object, DefaultEdge> graph;
	public static World world;
	
	public static Class<? extends Enemy> enemy = null;

	public Game(String name) {

		world = new World(name);

		while (!world.addDynamicObject())
			;
		while (!world.addItems())
			;
		//world.player = new Player(name);
		//world.player.positionCharacter();
		//world.getListDynamicObjects().add(player);
		world.player.positionCharacter();
		world.getThread().start();
		
	}

	
	// constructor for loadGame
	public Game(String path, String name) {

		world = new World(path, name);
		world.player.positionCharacter();
		//player = new Player(name);
		//world.getListDynamicObjects().add(player);
		// while(!world.addDynamicObject());
		// while(!world.addItems());

	}

	@SuppressWarnings("unchecked")
	public Game(String text, GameSlagyom game2, String charName) {
		try {
			enemy = ((Class<? extends Enemy>) Class.forName("enemy.Ai." + text));
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
			System.out.println("Impossibile trovare la classe enemy.Ai." + text);
		}
		world = new World(charName);

		while (!world.addDynamicObject())
			;
		while (!world.addItems())
			;
		//player = new Player(charName);
		//world.getListDynamicObjects().add(player);
		world.getThread().start();

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
