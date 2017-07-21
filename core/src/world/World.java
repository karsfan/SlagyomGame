package world;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

import battle.Battle;
import battle.Enemy;
import character.DynamicObjects;
import character.Man;
import character.Player;
import character.Woman;
import screens.PlayScreen;
import staticObjects.BossHome;
import staticObjects.EnemyHome;
import staticObjects.PreEnemyHouse;
import staticObjects.StaticObject;
import staticObjects.StaticObject.Element;
import weaponsAndItems.Item;

public class World {

	private ArrayList<DynamicObjects> people;
	public Player player;
	public Map[] maps;
	public Battle battle;

	int level;
	int numberOfLevels;
	float timerItem;

	public static Stack<String> dialogues;
	public static Stack<String> manNames;
	public static Stack<String> womanNames;

	/**
	 * Constructor for initialize new World
	 * 
	 * @param name Player's name
	 *            
	 * @param male Player's gender
	 */
	public World(String name, boolean male) {
		numberOfLevels = 2;
		level = 0;
		timerItem = 0;
		people = new ArrayList<DynamicObjects>();
		maps = new Map[numberOfLevels];

		maps[0] = new Map("res/map/mapProva.txt", true, "Village one");
		maps[1] = new Map("res/map/village1.txt", false, "Village two");

		player = new Player(name, male);
		getListDynamicObjects().add(player);

		manNames = new Stack<String>();
		womanNames = new Stack<String>();
		dialogues = new Stack<String>();
		try {
			loadFromFile(manNames, "res/strings/manNames.txt");
			loadFromFile(womanNames, "res/strings/womanNames.txt");
			loadFromFile(dialogues, "res/strings/dialogues.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public World(String path, String name, boolean male) {
		level = 0;
		people = new ArrayList<DynamicObjects>();

		maps = new Map[numberOfLevels];
		maps[0] = new Map(path, true, "Village one");
		maps[1] = new Map(path, false, "Village two");
		player = new Player(name, male);

		getListDynamicObjects().add(player);

		manNames = new Stack<String>();
		womanNames = new Stack<String>();
		dialogues = new Stack<String>();
		try {
			loadFromFile(manNames, "res/strings/manNames.txt");
			loadFromFile(womanNames, "res/strings/womanNames.txt");
			loadFromFile(dialogues, "res/strings/dialogues.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadFromFile(Stack<String> list, String path) throws IOException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader(getClass().getClassLoader().getResourceAsStream(path)));
		String line;
		try {
			line = br.readLine();
			while (line != null) {
				line = br.readLine();
				if (line != null) {
					list.push(line);
				}
			}
			br.close();
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

		LinkedList<Item> delete = new LinkedList<Item>();
		Iterator<Item> it2 = getListItems().iterator();
		while (it2.hasNext()) {
			Item ob = it2.next();
			if (ob.isPicked())
				delete.add(ob);
		}
		getListItems().removeAll(delete);

		if (timerItem >= 60) {
			Item item = new Item();
			getMap().getListItems().add(item);
			timerItem = 0;
		}

	}

	public LinkedList<StaticObject> getListTile() {
		return getMap().getListTile();
	}

	public LinkedList<StaticObject> getListLightLamps() {
		return getMap().getListLightLamps();
	}

	public LinkedList<StaticObject> getListObjectsMiniMap() {
		return getMap().getListObjectsMiniMap();
	}

	public void createBattle(PreEnemyHouse preEnemyHouse) {
		boolean created = false;
		Iterator<Enemy> it1 = preEnemyHouse.enemy.iterator();
		while (it1.hasNext()) {
			Enemy ob = (Enemy) it1.next();
			if (!ob.dead) {
				created = true;
				battle = new Battle(player, ob);
				break;
			}
		}
		if (!created) {
			player.collideGym = false;
			player.textDialog = "There aren't enemies in this home";
		}
	}

	public ArrayList<DynamicObjects> getListDynamicObjects() {
		return people;
	}

	public void nextLevel() {

		if (level < numberOfLevels - 1) {
			level++;
			people = new ArrayList<DynamicObjects>();
			getMap().setCurrent(false);
			maps[level].setCurrent(true);
			player.x = 500;
			player.y = 500;
			people.add(player);
			while (!addDynamicObject()) {
			}
			while (!addItems()) {
			}
		}

	}

	public void createBattle(EnemyHome enemyHome) {
		Iterator<StaticObject> it = getListTile().iterator();
		// se si tratta di un tempio controllo tra la lista dei nemici quali
		// deve affronatre in caso non ci sono nemici da affrontare uscirà un
		// avviso
		if (enemyHome.getElement() == Element.TEMPLE) {
			boolean created = false;
			Iterator<Enemy> it1 = enemyHome.enemy.iterator();
			while (it1.hasNext()) {
				Enemy ob = (Enemy) it1.next();
				if (!ob.dead) {
					created = true;
					battle = new Battle(player, ob);
					break;
				}
			}
			if (!created) {
				player.collideGym = false;
				player.textDialog = "There aren't enemies in this home";
			}
		} // se si tratta di un castle controllo prima che siano stati sconfitti
			// tutti i nemici, se lo sono allora partirà la battaglia con il
			// boss
		else if (enemyHome.getElement() == Element.CASTLE) {
			boolean created = true;
			while (it.hasNext()) {
				StaticObject ob = (StaticObject) it.next();
				if (ob instanceof EnemyHome && ob.getElement() == Element.TEMPLE) {
					Iterator<Enemy> it1 = ((EnemyHome) ob).enemy.iterator();
					while (it1.hasNext()) {
						Enemy ob1 = (Enemy) it1.next();
						if (!ob1.dead) {
							created = false;
							break;
						}
						if (!created)
							break;
					}
				}
			}
			if (created) {
				if (!((BossHome) enemyHome).getEnemy().dead) {
					battle = new Battle(player, ((BossHome) enemyHome).getEnemy());
				} else {
					player.collideGym = false;
					if (level < numberOfLevels - 1)
						player.textDialog = "You have defeated the village boss, now you expect other challenges";
					else
						player.textDialog = "You have defeated all enemies. Congratulations!!!";
					nextLevel();
				}

			} else {
				player.collideGym = false;
				PlayScreen.hud.setDialogText("You can't enter if you haven't fighted against all the enemies");
			}
		}
	}
}
