package it.slagyom.src.Character;

import java.util.ArrayList;
import java.util.Iterator;

import battle.Fighting;
import it.slagyom.src.World.Weapon;
import it.slagyom.src.World.Weapon.Type;
import staticObjects.Item;
import staticObjects.Item.Level;
import staticObjects.StaticObject.Element;

public class Bag {
	int capacity;
	public ArrayList<Item> bagItems;
	public Weapon secondary_weapon;
	public ArrayList<Bomb> bombe;

	public void lancia(int vel, Fighting fighting) {
		Iterator<Bomb> itBomb = bombe.iterator();
		while (itBomb.hasNext()) {
			Bomb bomba = (Bomb) itBomb.next();
			if (!bomba.lanciata) {
				bomba.lanciata = true;
				bomba.lancia(vel, fighting);
				bomba.id = "Player";
				break;
			}
		}
	}

	public Bag() {
		capacity = 50;
		secondary_weapon = null;
		bagItems = new ArrayList<Item>();
		bombe = new ArrayList<Bomb>();
		bombe.add(new Bomb(it.slagyom.src.World.Weapon.Level.lev1, Type.Bomba));
		bombe.add(new Bomb(it.slagyom.src.World.Weapon.Level.lev1, Type.Bomba));
		bombe.add(new Bomb(it.slagyom.src.World.Weapon.Level.lev1, Type.Bomba));
		bombe.add(new Bomb(it.slagyom.src.World.Weapon.Level.lev1, Type.Bomba));
		bombe.add(new Bomb(it.slagyom.src.World.Weapon.Level.lev1, Type.Bomba));
		bombe.add(new Bomb(it.slagyom.src.World.Weapon.Level.lev1, Type.Bomba));
	}

	public boolean addTool(Item item) {
		if (bagItems.size() + bombe.size() < capacity) {
			bagItems.add(item);
			return true;
		}
		return false;
	}

	public void addWeapon(Weapon weapon) {
		secondary_weapon = weapon;
	}

	public boolean addBomb(Bomb bomb) {
		if (bagItems.size() + bombe.size() < capacity) {
			bombe.add(bomb);
			return true;
		}
		return false;
	}

	public void deleteParchments(Item item) {
		Iterator<Item> itParchment = bagItems.iterator();
		while (itParchment.hasNext()) {
			Item it = (Item) itParchment.next();
			if (it == item) {
				itParchment.remove();
				continue;
			}
		} // it controls that the numbers is sufficient to upgrade
	}

	public boolean deletePotion(Item potion) {
		Iterator<Item> itPotion = bagItems.iterator();
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
		Iterator<Item> itemIterator = bagItems.iterator();
		while (itemIterator.hasNext()) {
			Item searching = (Item) itemIterator.next();
			if (searching.getElement() == element && searching.level == level) {
				numberOf++;
			}
		}
		return numberOf;
	}

	public int getNumberOfBomb(it.slagyom.src.World.Weapon.Level level) {
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
		Iterator<Item> itemIterator = bagItems.iterator();
		while (itemIterator.hasNext()) {
			Item searching = (Item) itemIterator.next();
			if (searching.getElement() == element && searching.level == level) {
				itemIterator.remove();
				break;
			}
		}
	}

	public void removeBomb(it.slagyom.src.World.Weapon.Level level) {
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
