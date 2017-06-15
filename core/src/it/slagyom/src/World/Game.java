package it.slagyom.src.World;

import it.slagyom.src.Character.Character;

public class Game {

	public static World world;
	public static Character character;

	public Game(String name) {

		world = new World();
		character = new Character(name);
		world.getListDynamicObjects().add(character);
		while(!world.addDynamicObject());
		while(!world.addItems());
		world.getThread().start();
		
	}

	public Game(String path, String name) {

		world = new World(path);
		character = new Character(name);
		world.getListDynamicObjects().add(character);
		while(!world.addDynamicObject());
		while(!world.addItems());

	}


	public void initialize() {
		setWorld(new World());

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
