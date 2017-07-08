package multiplayer;

import java.util.ArrayList;
import java.util.LinkedList;

import character.Player;
import staticObjects.Item;
import staticObjects.StaticObject;
import world.Map;

public class NetworkWorld {
	NetworkPlayer player;
	ArrayList <NetworkPlayer> otherPlayers;
	Map map;
	
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


}
