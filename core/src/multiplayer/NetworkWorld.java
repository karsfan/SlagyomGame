package multiplayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import battle.Battle;
import battle.Enemy;
import staticObjects.Item;
import staticObjects.PreEnemyHouse;
import staticObjects.StaticObject;
import world.Map;

public class NetworkWorld {
	NetworkPlayer player;
	ArrayList <NetworkPlayer> otherPlayers;
	Map map;
	Battle battle;
	
	public NetworkWorld(String name) {
		otherPlayers = new ArrayList<NetworkPlayer>();
		map = new Map(getClass().getResource("/res/map/map.txt").getPath(), true, "Village", true);
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
			NetworkEnemy ob = (NetworkEnemy) it1.next();
			if (!ob.morto) {
				creata = true;
				battle = new Battle(Client.networkWorld.player, ob);
				break;
			}
		}
		if(!creata){
			Client.networkWorld.player.collideGym = false;
		}
		
	}


}
