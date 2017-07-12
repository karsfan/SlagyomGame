package multiplayer;

import character.DynamicObjects.StateDynamicObject;

public class NetworkMessage {
	int action;
	int ID;
	float x;
	float  y;
	int IDreceiver;
	StateDynamicObject currentState;

	public NetworkMessage(String message) {
		char[] charMessage = new char[message.length()];
		message.getChars(0, message.length(), charMessage, 0);
		String actionString = "", IDstring = "", xString = "", yString = "", currentStateString = "", IDreceiverString="";
		int i = 0;
		for (; charMessage[i] != ' '; i++)
			actionString += charMessage[i];
		i++;
		for (; charMessage[i] != ' '; i++)
			IDstring += charMessage[i];
		i++;
		for (; charMessage[i] != ' '; i++)
			xString += charMessage[i];
		i++;
		for (; charMessage[i] != ' '; i++)
			yString += charMessage[i];
		i++;
		for (; charMessage[i] != ';'; i++)
			currentStateString += charMessage[i];
		i++;
		for(; charMessage[i] != ';';i++)
			IDreceiverString += charMessage[i];
		switch (currentStateString) {
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
		case "FIGHTINGLEFT":
			currentState = StateDynamicObject.FIGHTINGLEFT;
			break;
		case "FIGHTINGRIGHT":
			currentState = StateDynamicObject.FIGHTINGRIGHT;
			break;
		case "JUMPING":
			currentState = StateDynamicObject.JUMPING;
			break;
		default:
			//System.out.println("Errore codifica currentState nel messaggio. CurrentState: "+currentStateString);
			break;
		}
		action = Integer.parseInt(actionString);
		ID = Integer.parseInt(IDstring);
		IDreceiver = Integer.parseInt(IDreceiverString);
		x = Float.parseFloat(xString);
		y = Float.parseFloat(yString);
		
		//System.out.println("Messaggio "+actionString+" "+ID+" "+x+" "+y+currentState);
	}

}
