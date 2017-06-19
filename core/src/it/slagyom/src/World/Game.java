package it.slagyom.src.World;

import java.util.Iterator;

import it.slagyom.src.Character.Character;
import it.slagyom.src.Character.DynamicObjects;

public class Game {
	
	//ListenableGraph<Object, DefaultEdge> graph;
	public static World world;
	public static Character character;
	
	public Game(String name) {

		world = new World();
		character = new Character(name);
		
		world.getListDynamicObjects().add(character);
		while(!world.addDynamicObject());
		while(!world.addItems());
		world.getThread().start();
		//graph = new ListenableDirectedGraph<>(DefaultEdge.class);
		createStory();
	}
	public void createStory(){
		Iterator<DynamicObjects> it1 = world.getListDynamicObjects().iterator();
		while (it1.hasNext()) {
			Object ob = (Object) it1.next();
			if (ob instanceof Enemy) {
				//graph.addVertex(ob);
				
			}
		}
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
