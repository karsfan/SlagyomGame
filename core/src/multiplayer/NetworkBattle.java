package multiplayer;

import battle.Battle;
import battle.Enemy;

public class NetworkBattle extends Battle{
	
	public NetworkBattle(NetworkPlayer player, Enemy enemy) {
		super(player, enemy);
	}

	public NetworkBattle(NetworkPlayer player, NetworkPlayer otherPlayer) {
		super(player, otherPlayer);
	}

}
