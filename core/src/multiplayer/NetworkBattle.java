package multiplayer;

import battle.Battle;
import battle.Enemy;
import character.DynamicObjects.StateDynamicObject;

public class NetworkBattle extends Battle{
	
	public NetworkBattle(NetworkPlayer player, Enemy enemy) {
		super(player, enemy);
	}

	public NetworkBattle(NetworkPlayer player, NetworkPlayer otherPlayer) {
		super(player, otherPlayer);
		otherPlayer.currentState = StateDynamicObject.RUNNINGLEFT;
	}
	
	public boolean update(float dt){
		//System.out.println("update Netbattle");
		character.update(dt);
		enemy.update(dt);
		return false;
	}
}
