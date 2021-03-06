package multiplayer;

import character.DynamicObjects.StateDynamicObject;
import staticObjects.StaticObject.Element;
import weaponsAndItems.Item;
import weaponsAndItems.Weapon;
import weaponsAndItems.Weapon.Level;
import weaponsAndItems.Weapon.Type;

public class NetworkMessage {
	int action;
	int ID;
	float x;
	float y;
	int IDreceiver;
	StateDynamicObject currentState;
	Weapon.Type typeWeapon;
	Weapon.Level levelWeapon;
	Item.Level levelItem;
	Element elementItem;
	/**
	 * NetworkMessage needs to decode the message of other Clients
	 * @param message Message to be decoded
	 */
	public NetworkMessage(String message) {
		char[] charMessage = new char[message.length()];
		message.getChars(0, message.length(), charMessage, 0);
		String actionString = "", IDstring = "", xString = "", yString = "", currentStateString = "",
				IDreceiverString = "";
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
		for (; charMessage[i] != ';'; i++)
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
		case "lev1":
			x = 1;
			break;
		case "lev2":
			x = 2;
			break;
		case "lev3":
			x = 3;
			break;
		default:
			// System.out.println("Errore codifica currentState nel messaggio.
			// CurrentState: "+currentStateString);
			break;
		}
		action = Integer.parseInt(actionString);
		ID = Integer.parseInt(IDstring);
		IDreceiver = Integer.parseInt(IDreceiverString);
		if (action != 5 && action != 6) {
			x = Float.parseFloat(xString);
			y = Float.parseFloat(yString);
		}
		if (action == 5) {
			switch (xString) {
			case "Spear":
				typeWeapon = Type.Spear;
				break;
			case "Sword":
				typeWeapon = Type.Sword;
				break;
			case "Bow":
				typeWeapon = Type.Bow;
				break;
			default:
				break;
			}
			switch (yString) {
			case "lev1":
				levelWeapon = Level.lev1;
				break;
			case "lev2":
				levelWeapon = Level.lev2;
				break;
			case "lev3":
				levelWeapon = Level.lev3;
				break;
			default:
				break;
			}
		}
		
		if(action == 6){
			elementItem = Element.POTION;
			switch (yString) {
			case "FIRST":
				levelItem = Item.Level.FIRST;
				break;
			case "SECOND":
				levelItem = Item.Level.SECOND;
				break;
			case "THIRD":
				levelItem = Item.Level.THIRD;
				break;
			default:
				break;
			}
		}
	}

}
