package multiplayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import battle.Battle;
import battle.Enemy;
import staticObjects.BossHome;
import staticObjects.EnemyHome;
import staticObjects.PreEnemyHouse;
import staticObjects.StaticObject;
import staticObjects.StaticObject.Element;
import weaponsAndItems.Item;
import world.Map;

public class NetworkWorld {
	public NetworkPlayer player;
	public ArrayList<NetworkPlayer> otherPlayers;
	public Map map;
	public Battle battle;
	/**
	 * Constructor for initialize a new World online
	 * @param name Player's name
	 */
	public NetworkWorld(String name) {
		otherPlayers = new ArrayList<NetworkPlayer>();
		map = new Map("res/map/village1.txt", true, "Village");
		player = new NetworkPlayer(name);
		player.coins = 100;
	}

	public LinkedList<Item> getListItems() {
		return map.getListItems();
	}

	public LinkedList<StaticObject> getListTile() {
		return map.getListTile();
	}

	public ArrayList<NetworkPlayer> getOtherPlayersList() {
		return otherPlayers;
	}

	public LinkedList<StaticObject> getListLightLamps() {
		return map.getListLightLamps();
	}

	public LinkedList<StaticObject> getListObjectsMiniMap() {
		return map.getListObjectsMiniMap();
	}
	/**
	 * Create a new Battle if there are other enemy in the house
	 * @param preEnemyHouse
	 */
	public void createBattle(PreEnemyHouse preEnemyHouse) {
		boolean created = false;
		Iterator<Enemy> it1 = preEnemyHouse.enemy.iterator();
		while (it1.hasNext()) {
			NetworkEnemy ob = (NetworkEnemy) it1.next();
			if (!ob.dead) {
				created = true;
				battle = new NetworkBattle(Client.networkWorld.player, ob);
				break;
			}
		}
		if (!created) {
			battle = null;
			player.textDialog = "There aren't enemies in this home";
			player.collideGym = false;
		}

	}
	/**
	 * Create a new Battle between two Players
	 * @param iDOtherPlayer
	 */
	public void createBattle(int iDOtherPlayer) {
		Iterator<NetworkPlayer> otherP = otherPlayers.iterator();
		while (otherP.hasNext()) {
			NetworkPlayer ob = otherP.next();
			if (ob.ID == iDOtherPlayer) {
				battle = new NetworkBattle(player, ob);
				break;
			}
		}

	}
	/**
	 * Create a new Battle if there are other enemy in the house
	 * @param enemyHome
	 */
	public void createBattle(EnemyHome enemyHome) {
		Iterator<StaticObject> it = getListTile().iterator();
		// se si tratta di un tempio controllo tra la lista dei nemici quali
		// deve affronatre in caso non ci sono nemici da affrontare uscir� un
		// avviso
		if (enemyHome.getElement() == Element.TEMPLE) {
			boolean created = false;
			Iterator<Enemy> it1 = enemyHome.enemy.iterator();
			while (it1.hasNext()) {
				Enemy ob = (Enemy) it1.next();
				if (!ob.dead) {
					created = true;
					battle = new NetworkBattle(player, ob);
					break;
				}
			}
			if (!created) {
				player.collideGym = false;
				player.textDialog = "There aren't enemies in this home";
			}
		} // se si tratta di un castle controllo prima che siano stati sconfitti
			// tutti i nemici, se lo sono allora partir� la battaglia con il
			// boss
		else if (enemyHome.getElement() == Element.CASTLE) {
			boolean created = true;
			while (it.hasNext()) {
				StaticObject ob = (StaticObject) it.next();
				if (ob instanceof EnemyHome && ob.getElement() == Element.TEMPLE) {
					Iterator<Enemy> it1 = ((EnemyHome) ob).enemy.iterator();
					while (it1.hasNext()) {
						Enemy ob1 = it1.next();
						if (!ob1.dead) {
							created = false;
							battle = new NetworkBattle(player, ob1);
							break;
						}

					}
				}
				if (created) {
					if (!((BossHome)enemyHome).getEnemy().dead) {
						battle = new Battle(player, ((BossHome)enemyHome).getEnemy());
					}
					else {
						player.collideGym = false;
						player.textDialog = "You have defeated the village boss";
					}
				}

			}
		}
	}

}
