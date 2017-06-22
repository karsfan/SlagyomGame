package it.slagyom.src.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import it.slagyom.src.Character.DynamicObjects;
import it.slagyom.src.Character.Man;
import it.slagyom.src.Map.PreEnemyHouse;
import it.slagyom.src.Map.Item;
import it.slagyom.src.Map.Map;
import it.slagyom.src.Map.StaticObject;

public class World {

	
	private ArrayList<DynamicObjects> people;
	public Map[] maps;
	public Battle battle;
	private ThreadWorld thread;
	public Semaphore semaphore;
	int level;
	float timerItem = 0;
	public boolean remove = false;

	public World() {
		semaphore = new Semaphore(1);
		level = 0;
		people = new ArrayList<DynamicObjects>();
		maps = new Map[2];
		maps[0] = new Map("res/map/mappaNUOVAOK", true, "Village one");
		maps[1] = new Map("res/map/newMap", false, "Village two");

		setThread(new ThreadWorld(this, semaphore));

	}

	public World(String path) {
		level = 0;
		semaphore = new Semaphore(0);
		people = new ArrayList<DynamicObjects>();

		maps = new Map[2];
		maps[0] = new Map(path, true, "Village one");
		maps[1] = new Map("res/map/Map1", false, "Village two");

		setThread(new ThreadWorld(this, semaphore));
		getThread().start();

	}

	public Map getMap() {
		for (int i = 0; i < 2; i++)
			if (maps[i].current())
				return maps[i];
		return null;
	}

	public boolean addDynamicObject() {
		for (int i = 0; i < GameConfig.numMan; i++) {
			Man man = new Man();
			people.add(man);
		}
		return true;
	}

	public boolean addItems() {
		for (int i = 0; i < GameConfig.numItems; i++) {
			Item item = new Item();
			getListItems().add(item);
		}
		return true;
	}

	public LinkedList<Item> getListItems() {
		return getMap().getListItems();
	}

	public void update(float dt) {
		timerItem += dt;
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		Iterator<DynamicObjects> it1 = people.iterator();
		while (it1.hasNext()) {
			Object ob = (Object) it1.next();
			if (ob instanceof Man) {
				((Man) ob).update(dt);
			}
		}
	

		semaphore.release();
		if (timerItem >= 60) {
			Item item = new Item();
			getMap().getListItems().add(item);
			timerItem = 0;
		}

	}

	public LinkedList<StaticObject> getListTile() {
		return getMap().getListTile();
	}

	public void createBattle(PreEnemyHouse preEnemyHouse) {
		
		battle = new Battle(Game.player, preEnemyHouse.enemy);
	}

	public ArrayList<DynamicObjects> getListDynamicObjects() {
		return people;
	}

	public void nextLevel() throws InterruptedException {

		if (level < 1) {
			level++;

			semaphore.acquire();
			people = new ArrayList<DynamicObjects>();
			getMap().setCurrent(false);
			maps[level].setCurrent(true);
			people.add(Game.player);
			while (!addDynamicObject())
				;
			addItems();
			semaphore.release();
		}

	}

	public ThreadWorld getThread() {
		return thread;
	}

	public void setThread(ThreadWorld thread) {
		this.thread = thread;
	}

}
