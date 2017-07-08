package battle;

import java.util.ArrayList;

import battle.Enemy.Level;
import character.Weapon;
import staticObjects.Item;
import staticObjects.StaticObject.Element;

public class Pack {
	Weapon weapon;
	ArrayList<Item> items;

	public Pack(Level level) {
		items = new ArrayList<Item>();
		switch (level) {
		case EASY:
			weapon = new Weapon(Weapon.Level.lev1);
			for (int i = 0; i < 10; i++) {
				Item item = new Item(level);
				items.add(item);
			}

		case MEDIUM:
			weapon = new Weapon(Weapon.Level.lev2);
			for (int i = 0; i < 15; i++) {
				Item item = new Item(level);
				items.add(item);
			}

		case HARD:
			weapon = new Weapon(Weapon.Level.lev3);
			for (int i = 0; i < 30; i++) {
				Item item = new Item(level);
				items.add(item);
			}
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
					number++;
			break;
		case "POTIONLEV1":
			for (int i = 0; i < items.size(); i++)
				if (items.get(i).getElement() == Element.POTION && items.get(i).getLevel() == staticObjects.Item.Level.FIRST)
					number++;
			break;
		case "POTIONLEV2":
			for (int i = 0; i < items.size(); i++)
				if (items.get(i).getElement() == Element.POTION && items.get(i).getLevel() == staticObjects.Item.Level.SECOND)
					number++;
			break;
		case "POTIONLEV3":
			for (int i = 0; i < items.size(); i++)
				if (items.get(i).getElement() == Element.POTION && items.get(i).getLevel() == staticObjects.Item.Level.THIRD)
					number++;
			break;
		case "PARCHLEV1":
			for (int i = 0; i < items.size(); i++)
				if (items.get(i).getElement() == Element.PARCHMENT && items.get(i).getLevel() == staticObjects.Item.Level.FIRST)
					number++;
			break;
		case "PARCHLEV2":
			for (int i = 0; i < items.size(); i++)
				if (items.get(i).getElement() == Element.PARCHMENT && items.get(i).getLevel() == staticObjects.Item.Level.SECOND)
					number++;
			break;
		}
		
		return number;
	}
}
