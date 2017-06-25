package battle;

import java.util.ArrayList;

import battle.Enemy.Level;
import it.slagyom.src.World.Weapon;
import staticObjects.Item;

public class Pack {
	Weapon weapon;
	ArrayList<Item> items;

	public Pack(Level level) {
		items = new ArrayList<Item>();
		switch (level) {
		case EASY:
			weapon = new Weapon(Weapon.Level.lev1);
			for (int i = 0; i < 10; i++) {
				Item item = new Item();
				items.add(item);
			}

		case MEDIUM:
			weapon = new Weapon(Weapon.Level.lev2);
			for (int i = 0; i < 15; i++) {
				Item item = new Item();
				items.add(item);
			}

		case HARD:
			weapon = new Weapon(Weapon.Level.lev3);
			for (int i = 0; i < 30; i++) {
				Item item = new Item();
				items.add(item);
			}
		default:
			break;
		}
	}
}
