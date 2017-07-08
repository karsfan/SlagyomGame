package character;

import java.util.ArrayList;
import java.util.Iterator;

import staticObjects.Item;
import staticObjects.Item.Level;
import staticObjects.StaticObject.Element;
import world.Game;

public class Bag {
	int capacity;
	public ArrayList<Item> items;
	public Weapon secondary_weapon;
	public ArrayList<Bomb> bombe;

	public Bag() {
		capacity = 50;
		secondary_weapon = null;
		items = new ArrayList<Item>();
		bombe = new ArrayList<Bomb>();

		/*
		 * bombe.add(new Bomb(it.slagyom.src.World.Weapon.Level.lev1,
		 * Type.Bomba)); bombe.add(new
		 * Bomb(it.slagyom.src.World.Weapon.Level.lev1, Type.Bomba));
		 * bombe.add(new Bomb(it.slagyom.src.World.Weapon.Level.lev1,
		 * Type.Bomba)); bombe.add(new
		 * Bomb(it.slagyom.src.World.Weapon.Level.lev1, Type.Bomba));
		 * bombe.add(new Bomb(it.slagyom.src.World.Weapon.Level.lev1,
		 * Type.Bomba)); bombe.add(new
		 * Bomb(it.slagyom.src.World.Weapon.Level.lev1, Type.Bomba));
		 */

	}

	public void deleteParchments(Item item) {
		Iterator<Item> itParchment = items.iterator();
		while (itParchment.hasNext()) {
			Item it = (Item) itParchment.next();
			if (it == item) {
				itParchment.remove();
				continue;
			}
		} // it controls that the numbers is sufficient to upgrade
	}

	public boolean deletePotion(Item potion) {
		Iterator<Item> itPotion = items.iterator();
		while (itPotion.hasNext()) {
			Item tool = (Item) itPotion.next();
			if (tool == potion) {
				itPotion.remove();
				return true;
			}
		}
		return false;
	}

	public boolean deleteBomb(Bomb bomb) {
		Iterator<Bomb> itBomb = bombe.iterator();
		while (itBomb.hasNext()) {
			Bomb bomba = (Bomb) itBomb.next();
			if (bomba == bomb) {
				itBomb.remove();
				return true;
			}
		}
		return false;
	}

	public int getNumberOf(Element element, Level level) {
		int numberOf = 0;
		Iterator<Item> itemIterator = items.iterator();
		while (itemIterator.hasNext()) {
			Item searching = (Item) itemIterator.next();
			if (searching.getElement() == element && searching.level == level) {
				numberOf++;
			}
		}
		return numberOf;
	}

	public int getNumberOfBomb(character.Weapon.Level level) {
		int numberOf = 0;
		Iterator<Bomb> bombIterator = bombe.iterator();
		while (bombIterator.hasNext()) {
			Bomb searching = (Bomb) bombIterator.next();
			if (searching.level == level) {
				numberOf++;
			}
		}
		return numberOf;
	}

	public void removeItem(Element element, Level level) {
		Iterator<Item> itemIterator = items.iterator();
		while (itemIterator.hasNext()) {
			Item searching = (Item) itemIterator.next();
			if (searching.getElement() == element && searching.level == level) {
				itemIterator.remove();
				break;
			}
		}
	}

	public boolean add(Object object) {
		if (items.size() + bombe.size() < capacity) {
			if (object instanceof Bomb) {
				Bomb bomb = new Bomb(((Bomb) object).getLevel(), ((Bomb) object).getType());
				bombe.add(bomb);
			}

			else if (object instanceof Item) {
				Item item = new Item(((Item) object).getElement(), ((Item) object).getLevel());
				items.add(item);
			}

			else if (object instanceof Weapon) {
				secondary_weapon = new Weapon(((Weapon) object).getLevel(), ((Weapon) object).getType());
			}
			return true;
		}
		return false;
	}

	public void useItem(Item item) {
		if(item.getElement() == Element.POTION){
			switch(item.getLevel()){
			case FIRST:
				System.out.println(Game.world.battle.character.player.health);
				Game.world.battle.character.player.health += 15;
				System.out.println(Game.world.battle.character.player.health);
				break;
			case SECOND:
				Game.world.battle.character.player.health += 25;
				break;
			case THIRD:
				Game.world.battle.character.player.health += 45;
				break;
			default:
				System.out.println("potion non assegnata");
				break;
			}
			removeItem(item.getElement(), item.getLevel());
	}
}
	public void removeBomb(character.Weapon.Level level) {
		Iterator<Bomb> bombIterator = bombe.iterator();
		while (bombIterator.hasNext()) {
			Bomb searching = (Bomb) bombIterator.next();
			if (searching.level == level) {
				bombIterator.remove();
				break;
			}
		}
	}

}
