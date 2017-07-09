package multiplayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import battle.Battle;
import battle.Enemy;
import character.Player;
import staticObjects.Item;
import staticObjects.PreEnemyHouse;
import staticObjects.StaticObject;
import world.Game;
import world.Map;

public class NetworkWorld {
	NetworkPlayer player;
	ArrayList <NetworkPlayer> otherPlayers;
	Map map;
	Battle battle;
	
	public NetworkWorld(String name) {
		otherPlayers = new ArrayList<NetworkPlayer>();
		map = new Map(getClass().getResource("/res/map/map.txt").getPath(), true, "Village");
		player = new NetworkPlayer(name);
	}

	public LinkedList<Item> getListItems() {
		return map.getListItems();
	}
	

	public LinkedList<StaticObject> getListTile() {
		return map.getListTile();
	}
	
	public ArrayList <NetworkPlayer> getOtherPlayersList () {
		return otherPlayers;
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
		
	}


}
