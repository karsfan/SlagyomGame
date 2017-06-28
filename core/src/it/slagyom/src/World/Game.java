package it.slagyom.src.World;


import battle.Enemy;
import it.slagyom.GameSlagyom;

//import org.jgrapht.ListenableGraph;
//import org.jgrapht.graph.DefaultEdge;

import it.slagyom.src.Character.Player;

public class Game {
	
	//ListenableGraph<Object, DefaultEdge> graph;
	public static World world;
	public static Player player;
	public static Class<? extends Enemy> enemy = null;
	
	public Game(String name) {

		world = new World();
		
		while(!world.addDynamicObject());
		while(!world.addItems());
		player = new Player(name);
		world.getListDynamicObjects().add(player);
		world.getThread().start();
		//graph = new ListenableDirectedGraph<>(DefaultEdge.class);
		//createStory();
	}
	/*public void createStory(){
		Iterator<DynamicObjects> it1 = world.getListDynamicObjects().iterator();
		while (it1.hasNext()) {
			Object ob = (Object) it1.next();
			//if (ob instanceof Enemy) {
		//		graph.addVertex(ob);
		//	}
		}
	}*/

	public Game(String path, String name) {

		world = new World(path);
		player = new Player(name);
		world.getListDynamicObjects().add(player);
		while(!world.addDynamicObject());
		while(!world.addItems());

	}


	@SuppressWarnings("unchecked")
	public Game(String text, GameSlagyom game2, String charName) {
		try {
			enemy = ((Class<? extends Enemy>) Class.forName("enemy.Ai."+text));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		world = new World();
		
		while(!world.addDynamicObject());
		while(!world.addItems());
		player = new Player(charName);
		world.getListDynamicObjects().add(player);
		world.getThread().start();
		
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
