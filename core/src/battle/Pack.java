package battle;

import java.util.ArrayList;

import battle.Enemy.Level;
import staticObjects.StaticObject.Element;
import weaponsAndItems.Item;

public class Pack {
	public ArrayList<Item> items;
	/**
	 * Constructor that receive the enemy's level
	 * @param level
	 */
	public Pack(Level level) {
		items = new ArrayList<Item>();
		switch (level) {
		case EASY:
			for (int i = 0; i < 10; i++) {
				Item item = new Item(level);
				items.add(item);
			}
			break;

		case MEDIUM:
			for (int i = 0; i < 15; i++) {
				Item item = new Item(level);
				items.add(item);
			}
			break;

		case HARD:
			for (int i = 0; i < 30; i++) {
				Item item = new Item(level);
				items.add(item);
			}
			break;
		default:
			break;
		}
	}

	public int getNumberOf(String string) {
		int number = 0;
		switch (string) {
		case "COIN":
			for (int i = 0; i < items.size(); i++)
				if (items.get(i).getElement() == Element.COIN)
					number ++;
			break;
		case "POTIONLEV1":
			for (int i = 0; i < items.size(); i++)
				if (items.get(i).getElement() == Element.POTION
						&& items.get(i).getLevel() == weaponsAndItems.Item.Level.FIRST)
					number++;
			break;
		case "POTIONLEV2":
			for (int i = 0; i < items.size(); i++)
				if (items.get(i).getElement() == Element.POTION
						&& items.get(i).getLevel() == weaponsAndItems.Item.Level.SECOND)
					number++;
			break;
		case "POTIONLEV3":
			for (int i = 0; i < items.size(); i++)
				if (items.get(i).getElement() == Element.POTION
						&& items.get(i).getLevel() == weaponsAndItems.Item.Level.THIRD)
					number++;
			break;
		case "PARCHLEV1":
			for (int i = 0; i < items.size(); i++)
				if (items.get(i).getElement() == Element.PARCHMENT
						&& items.get(i).getLevel() == weaponsAndItems.Item.Level.FIRST)
					number++;
			break;
		case "PARCHLEV2":
			for (int i = 0; i < items.size(); i++)
				if (items.get(i).getElement() == Element.PARCHMENT
						&& items.get(i).getLevel() == weaponsAndItems.Item.Level.SECOND)
					number++;
			break;
		}

		return number;
	}
}
