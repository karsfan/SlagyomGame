package multiplayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import battle.Battle;
import battle.Enemy;
import staticObjects.EnemyHome;
import staticObjects.Item;
import staticObjects.PreEnemyHouse;
import staticObjects.StaticObject;
import staticObjects.StaticObject.Element;
import world.Map;

public class NetworkWorld {
	public NetworkPlayer player;
	ArrayList<NetworkPlayer> otherPlayers;
	public Map map;
	public Battle battle;

	public NetworkWorld(String name) {
		otherPlayers = new ArrayList<NetworkPlayer>();
		map = new Map(getClass().getResource("/res/map/map.txt").getPath(), true, "Village");
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

	public void createBattle(PreEnemyHouse preEnemyHouse) {
		boolean creata = false;
		Iterator<Enemy> it1 = preEnemyHouse.enemy.iterator();
		while (it1.hasNext()) {
			NetworkEnemy ob = (NetworkEnemy) it1.next();
			if (!ob.morto) {
				creata = true;
				battle = new NetworkBattle(Client.networkWorld.player, ob);
				break;
			}
		}
		if (!creata) {
			battle = null;
			player.collideGym = false;
		}

	}

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
					battle = new NetworkBattle(player, ob);
					break;
				}
			}
			if (!creata) {
				player.collideGym = false;
			}
		} // se si tratta di un castle controllo prima che siano stati sconfitti
			// tutti i nemici, se lo sono allora partirà la battaglia con il
			// boss
		else if (enemyHome.getElement() == Element.CASTLE) {
			boolean creata = false;
			while (it.hasNext()) {
				StaticObject ob = (StaticObject) it.next();
				if (ob instanceof EnemyHome && ob.getElement() == Element.TEMPLE) {
					Iterator<Enemy> it1 = ((EnemyHome) ob).enemy.iterator();
					while (it1.hasNext()) {
						Enemy ob1 = it1.next();
						if (!ob1.morto) {
							creata = true;
							battle = new NetworkBattle(player, ob1);
							break;
						}

					}
				} else {
					player.collideGym = false;
					// PlayScreen.hud.setDialogText(
					// "Hai già sconfitto il boss di questo villaggio, adesso
					// puoi passare al prossimo villaggio");
				}

			}
		}
	}

	public void update(float dt) {
		Iterator<Item> it = getListItems().iterator();
		LinkedList<Item> daEliminare = new LinkedList<Item>();
		while (it.hasNext()) {
			Item ob = it.next();
			if (ob.picked)
				daEliminare.add(ob);
		}
		getListItems().removeAll(daEliminare);
	}

}
