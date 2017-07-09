package multiplayer;

import character.DynamicObjects.StateDynamicObject;

public class NetworkMessage {
	int ID;
	int x;
	int y;
	StateDynamicObject currentState;
	public NetworkMessage(String message){
		String[] split;
		char [] charMessage = new char[message.length()];
		message.getChars(0, message.length(), charMessage, 0);
		String IDstring="", xString="", yString="", currentStateString="";
		int i = 0;
		for(; charMessage[i]!= ' '; i++)
			IDstring += charMessage[i];
		i++;
		for(; charMessage[i]!= ' '; i++)
			xString += charMessage[i];
		i++;
		for(; charMessage[i]!= ' '; i++)
			yString += charMessage[i];
		i++;
		for(; charMessage[i]!= ';'; i++)
			currentStateString += charMessage[i];
		switch(currentStateString){
		case "RUNNINGRIGHT":
			currentState = StateDynamicObject.RUNNINGRIGHT;
			break;
		case "RUNNINGLEFT":
			currentState = StateDynamicObject.RUNNINGLEFT;
			break;
		case "RUNNINGUP":
			currentState = StateDynamicObject.RUNNINGUP;
			break;
		case "RUNNINGDOWN":
			currentState = StateDynamicObject.RUNNINGDOWN;
			break;
		}
		ID = Integer.parseInt(IDstring);
		x = convert(xString);
		y = convert(yString);
	}
	private int convert(String sCode) {
		char[] tmp =sCode.toCharArray();
		int result=0;
		for (int i = 0; i < tmp.length-2; i++) {
			result*=10;
			result+=tmp[i]-'0';
		}
		return result;
	}
}
