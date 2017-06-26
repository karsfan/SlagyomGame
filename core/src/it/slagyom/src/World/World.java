package it.slagyom.src.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

import battle.Battle;
import battle.Enemy;
import it.slagyom.PlayScreen;
import it.slagyom.src.Character.Bomb;
import it.slagyom.src.Character.DynamicObjects;
import it.slagyom.src.Character.Man;
import it.slagyom.src.Character.Woman;
import staticObjects.HeadHome;
import staticObjects.Item;
import staticObjects.PreEnemyHouse;
import staticObjects.StaticObject;
import it.slagyom.src.Map.Map;

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
		maps[1] = new Map("res/map/prova", false, "Village two");

		setThread(new ThreadWorld(this, semaphore));
		//getThread().start();
	}

	public World(String path) {
		level = 0;
		semaphore = new Semaphore(1);
		people = new ArrayList<DynamicObjects>();

		maps = new Map[2];
		maps[0] = new Map(path, true, "Village one");
		maps[1] = new Map("res/map/prova", false, "Village two");

		setThread(new ThreadWorld(this, semaphore));
		//getThread().start();

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
		for (int i = 0; i < GameConfig.numWoman; i++) {
			Woman woman = new Woman();
			people.add(woman);
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
		/*try {
			semaphore.acquire();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}*/
		Iterator<DynamicObjects> it1 = people.iterator();
		while (it1.hasNext()) {
			Object ob = (Object) it1.next();
			if (ob instanceof Woman) {
				((Woman) ob).update(dt);
			}
			if (ob instanceof Man)
				((Man) ob).update(dt);
		}

		//semaphore.release();
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
		boolean creata = false;
		Iterator<Enemy> it1 = preEnemyHouse.enemy.iterator();
		while (it1.hasNext()) {
			Enemy ob = (Enemy) it1.next();
			if (!ob.morto) {
				creata = true;
				battle = new Battle(Game.player, ob);
				break;
			}
		}
		if (!creata) {
			Game.player.collideGym = false;
			PlayScreen.hud.setDialogText("Non ci sono nemici in questa casa");
		}
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

	public void createBattle(HeadHome headHome) {
		boolean creata = true;
		Iterator<StaticObject> it = getListTile().iterator();
		while (it.hasNext()) {
			StaticObject ob = (StaticObject) it.next();
			if (ob instanceof PreEnemyHouse) {
				Iterator<Enemy> it1 = ((PreEnemyHouse) ob).enemy.iterator();
				while (it1.hasNext()) {
					Enemy ob1 = (Enemy) it1.next();
					if (!ob1.morto) {
						creata = false;
						break;
					}
					if (!creata)
						break;
				}
			}
		}
		if (creata) {
			battle = new Battle(Game.player, headHome.enemy);
		} else{
			Game.player.collideGym = false;
			PlayScreen.hud.setDialogText("Non ci puoi accedere se prima non hai eliminati tutti i nemici");
		}

	}

}
