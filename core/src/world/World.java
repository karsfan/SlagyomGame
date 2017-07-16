package world;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

import battle.Battle;
import battle.Enemy;
import character.DynamicObjects;
import character.Man;
import character.Player;
import character.Woman;
import screens.PlayScreen;
import staticObjects.EnemyHome;
import staticObjects.Item;
import staticObjects.PreEnemyHouse;
import staticObjects.StaticObject;
import staticObjects.StaticObject.Element;

public class World {

	private ArrayList<DynamicObjects> people;
	public Player player;
	public Map[] maps;
	public Battle battle;

	int level;
	float timerItem = 0;

	public static Stack<String> dialogues;
	public static Stack<String> manNames;
	public static Stack<String> womanNames;

	private FileReader fileReader;
	private Scanner input;

	public World(String name, boolean male) {
		level = 0;
		people = new ArrayList<DynamicObjects>();
		maps = new Map[2];
		System.out.println(getClass().getResource("/res/map/map.txt").getPath());
		maps[0] = new Map(getClass().getResource("/res/map/village1.txt").getPath(), true, "Village one");
		maps[1] = new Map(getClass().getResource("/res/map/map.txt").getPath(), false, "Village two");
		player = new Player(name, male);
		getListDynamicObjects().add(player);

		manNames = new Stack<String>();
		womanNames = new Stack<String>();
		dialogues = new Stack<String>();
		try {
			loadFromFile(manNames, getClass().getResource("/res/strings/manNames.txt").getPath());
			loadFromFile(womanNames, getClass().getResource("/res/strings/womanNames.txt").getPath());
			loadFromFile(dialogues, getClass().getResource("/res/strings/dialogues.txt").getPath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public World(String path, String name, boolean male) {
		level = 0;
		people = new ArrayList<DynamicObjects>();

		maps = new Map[2];
		System.out.println(path);
		maps[0] = new Map(path, true, "Village one");
		maps[1] = new Map(path, false, "Village two");
		player = new Player(name, male);

		getListDynamicObjects().add(player);

		manNames = new Stack<String>();
		womanNames = new Stack<String>();
		dialogues = new Stack<String>();
		try {
			loadFromFile(manNames, getClass().getResource("/res/strings/manNames.txt").getPath());
			loadFromFile(womanNames, getClass().getResource("/res/strings/womanNames.txt").getPath());
			loadFromFile(dialogues, getClass().getResource("/res/strings/dialogues.txt").getPath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadFromFile(Stack<String> list, String path) throws IOException {
		input = new Scanner(System.in);
		try {
			fileReader = new FileReader(path);
			input = new Scanner(fileReader);
			String line = input.nextLine();
			while (input.hasNextLine()) {
				line = input.nextLine();
				list.push(line);
			}
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

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

		LinkedList<Item> daEliminare = new LinkedList<Item>();
		Iterator<Item> it2 = getListItems().iterator();
		while (it2.hasNext()) {
			Item ob = it2.next();
			if (ob.isPicked())
				daEliminare.add(ob);
		}
		getListItems().removeAll(daEliminare);

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
				battle = new Battle(player, ob);
				break;
			}
		}
		if (!creata) {
			player.collideGym = false;
			PlayScreen.hud.setDialogText("Non ci sono nemici in questa casa");
		}
	}

	public ArrayList<DynamicObjects> getListDynamicObjects() {
		return people;
	}

	public void nextLevel() throws InterruptedException {

		if (level < 1) {
			level++;

			// semaphore.acquire();
			people = new ArrayList<DynamicObjects>();
			getMap().setCurrent(false);
			maps[level].setCurrent(true);
			people.add(player);
			while (!addDynamicObject())
				;
			addItems();
			// semaphore.release();
		}

	}

	// public void setThread(ThreadWorld thread) {
	// this.thread = thread;
	// }

	public void createBattle(EnemyHome enemyHome) {
		Iterator<StaticObject> it = getListTile().iterator();
		// se si tratta di un tempio controllo tra la lista dei nemici quali
		// deve affronatre in caso non ci sono nemici da affrontare uscir� un
		// avviso
		if (enemyHome.getElement() == Element.TEMPLE) {
			boolean creata = false;
			Iterator<Enemy> it1 = enemyHome.enemy.iterator();
			while (it1.hasNext()) {
				Enemy ob = (Enemy) it1.next();
				if (!ob.morto) {
					creata = true;
					battle = new Battle(player, ob);
					break;
				}
			}
			if (!creata) {
				player.collideGym = false;
				PlayScreen.hud.setDialogText("Non ci sono nemici in questa casa");
			}
		} // se si tratta di un castle controllo prima che siano stati sconfitti
			// tutti i nemici, se lo sono allora partir� la battaglia con il
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
					battle = new Battle(player, enemyHome.getEnemy());
				else {
					player.collideGym = false;
					PlayScreen.hud.setDialogText(
							"Hai gi� sconfitto il boss di questo villaggio, adesso puoi passare al prossimo villaggio");
				}

			} else {
				player.collideGym = false;
				PlayScreen.hud.setDialogText("Non ci puoi accedere se prima non hai eliminati tutti i nemici");
			}
		}
	}	
}
