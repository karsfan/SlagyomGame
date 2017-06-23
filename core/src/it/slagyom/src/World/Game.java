package it.slagyom.src.World;

import java.util.Iterator;

//import org.jgrapht.ListenableGraph;
//import org.jgrapht.graph.DefaultEdge;

import it.slagyom.src.Character.Player;
import it.slagyom.src.Character.DynamicObjects;

public class Game {
	
	//ListenableGraph<Object, DefaultEdge> graph;
	public static World world;
	public static Player player;
	
	public Game(String name) {

		world = new World();
		player = new Player(name);
		
		world.getListDynamicObjects().add(player);
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
			//if (ob instanceof Enemy) {
		//		graph.addVertex(ob);
		//	}
		}
	}

	public Game(String path, String name) {

		world = new World(path);
		player = new Player(name);
		world.getListDynamicObjects().add(player);
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
