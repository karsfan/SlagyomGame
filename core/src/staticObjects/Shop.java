package staticObjects;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import battle.Pack;
import character.Player;
import character.Weapon;
import character.Weapon.Level;
import character.Weapon.Type;

public class Shop extends StaticObject {

	ArrayList<Weapon> weapons;
	ArrayList<Pack> packs;
	private Rectangle door;

	public Shop(Point point) {
		weapons = new ArrayList<Weapon>();

		Weapon weapon = new Weapon(Level.lev1, Type.Spear);
		weapons.add(weapon);
		weapon = new Weapon(Level.lev2, Type.Spear);
		weapons.add(weapon);
		weapon = new Weapon(Level.lev3, Type.Spear);
		weapons.add(weapon);
		weapon = new Weapon(Level.lev1, Type.Sword);
		weapons.add(weapon);
		weapon = new Weapon(Level.lev2, Type.Sword);
		weapons.add(weapon);
		weapon = new Weapon(Level.lev3, Type.Sword);
		weapons.add(weapon);
		weapon = new Weapon(Level.lev1, Type.Bomba);
		weapons.add(weapon);
		weapon = new Weapon(Level.lev2, Type.Bomba);
		weapons.add(weapon);
		weapon = new Weapon(Level.lev3, Type.Bomba);
		weapons.add(weapon);
		weapon = new Weapon(Level.lev1, Type.Bow);
		weapons.add(weapon);
		weapon = new Weapon(Level.lev2, Type.Bow);
		weapons.add(weapon);
		weapon = new Weapon(Level.lev3, Type.Bow);
		weapons.add(weapon);

		this.element = Element.SHOP;
		shape = new Rectangle((int) point.getX(), (int) point.getY(), 64, 64);
	}
	
	public boolean collideDoor(Object e) {
		if (this.getElement() == Element.SHOP && e instanceof Player) {
			if (!((door.x > ((Player) e).getX() + ((Player) e).getWidth() / 2
					|| ((Player) e).getX() > door.x + door.width)
					|| (door.y > ((Player) e).getY() + ((Player) e).getHeight() / 2
							|| ((Player) e).getY() > door.y + door.height))) {
				return true;
			}
		}
		return false;
	}

	public void setPoint(Point point) {
		shape.x = point.x * 32;
		shape.y = point.y * 32;
		door = new Rectangle((int) (point.getX() * 32 + shape.getWidth() / 3 + 7), (int) point.getY() * 32, 15, 8);
	}
}
