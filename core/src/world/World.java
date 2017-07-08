package world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import battle.Battle;
import battle.Enemy;
import character.DynamicObjects;
import character.Man;
import character.Woman;
import screens.PlayScreen;
import staticObjects.EnemyHome;
import staticObjects.Item;
import staticObjects.PreEnemyHouse;
import staticObjects.StaticObject;
import staticObjects.StaticObject.Element;

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
		//maps[0] = new Map("bin/res/map/map.txt", true, "Village one");
		//System.out.println(getClass().getResource("/res/map/map.txt").getPath());
		maps[0] = new Map(getClass().getResource("/res/map/map.txt").getPath(), true, "Village one");
		maps[1] = new Map(getClass().getResource("/res/map/map.txt").getPath(), false, "Village two");

		setThread(new ThreadWorld(this, semaphore));
	}

	public World(String path) {
		level = 0;
		semaphore = new Semaphore(0);
		people = new ArrayList<DynamicObjects>();
		//System.out.println(path);
		//System.out.println(getClass().getResource(path).getFile());
		maps = new Map[2];
		maps[0] = new Map(path, true, "Village one");
		// maps[1] = new Map("res/map/prova", false, "Village two");

		setThread(new ThreadWorld(this, semaphore));

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

		Iterator<DynamicObjects> it1 = people.iterator();
		while (it1.hasNext()) {
			Object ob = (Object) it1.next();
			if (ob instanceof Woman) {
				((Woman) ob).update(dt);
			}
			if (ob instanceof Man)
				((Man) ob).update(dt);
		}

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

	public void createBattle(EnemyHome enemyHome) {
		Iterator<StaticObject> it = getListTile().iterator();
		// se si tratta di un tempio controllo tra la lista dei nemici quali
		// deve affronatre in caso non ci sono nemici da affrontare uscirà un
		// avviso
		if (enemyHome.getElement() == Element.TEMPLE) {
			boolean creata = false;
			Iterator<Enemy> it1 = enemyHome.enemy.iterator();
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
		} // se si tratta di un castle controllo prima che siano stati sconfitti
			// tutti i nemici, se lo sono allora partirà la battaglia con il
			// boss
		else if (enemyHome.getElement() == Element.CASTLE) {
			boolean creata = true;
			while (it.hasNext()) {
				StaticObject ob = (StaticObject) it.next();
				if (ob instanceof EnemyHome && ob.getElement() == Element.TEMPLE) {
					Iterator<Enemy> it1 = ((EnemyHome) ob).enemy.iterator();
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
				if (!enemyHome.getEnemy().morto)
					battle = new Battle(Game.player, enemyHome.getEnemy());
				else {
					Game.player.collideGym = false;
					PlayScreen.hud.setDialogText("Hai già sconfitto il boss di questo villaggio, adesso puoi passare al prossimo villaggio");
				}

			} else {
				Game.player.collideGym = false;
				PlayScreen.hud.setDialogText("Non ci puoi accedere se prima non hai eliminati tutti i nemici");
			}
		}
	}

}
