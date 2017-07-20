package character;

import java.util.Random;

import staticObjects.Item;
import staticObjects.StaticObject.Element;

public class Weapon {
	public enum Type {
		Sword, Bow, Spear, Arrow, Bomb
	};

	public enum Level {
		lev1, lev2, lev3
	};

	float damage;
	public Level level;
	public int price;
	Type type;
	float height;
	private float width;

	public Weapon(Level level, Type type) {
		this.level = level;
		this.type = type;
		switch (this.type) {
		case Sword:
			setWeapon(type, level);
			break;
		case Bow:
			setWeapon(type, level);
			break;
		case Spear:
			setWeapon(type, level);
			break;
		case Arrow:
			width = 15;
			height = 5;
			break;
		case Bomb:
			setWeapon(type, level);
			break;
		default:
			break;
		}
	}

	public Weapon(Level level) {
		this.level = level;
		Random rand = new Random();
		int r = rand.nextInt(3);
		switch (r) {
		case 0:
			type = Type.Sword;
			setWeapon(type, level);
			break;
		case 1:
			type = Type.Spear;
			setWeapon(type, level);
			break;
		case 2:
			type = Type.Bow;
			setWeapon(type, level);
			break;
		default:
			System.out.println("default in Weapon(level)");
			break;
		}
	}

	// constructor per inizializzare leggendo da file il salvataggio
	public Weapon(String type, String level) {
		switch (type) {
		case "Sword":
			this.type = Type.Sword;
			switch (level) {
			case "lev1":
				this.level = Level.lev1;
				setWeapon(Type.Sword, Level.lev1);
				break;
			case "lev2":
				this.level = Level.lev2;
				setWeapon(Type.Sword, Level.lev2);
				break;
			case "lev3":
				this.level = Level.lev3;
				setWeapon(Type.Sword, Level.lev3);
				break;
			default:
				break;
			}
			break;

		case "Spear":
			this.type = Type.Spear;
			switch (level) {
			case "lev1":
				this.level = Level.lev1;
				setWeapon(Type.Spear, Level.lev1);
				break;
			case "lev2":
				this.level = Level.lev2;
				setWeapon(Type.Spear, Level.lev2);
				break;
			case "lev3":
				this.level = Level.lev3;
				setWeapon(Type.Spear, Level.lev3);
				break;
			default:
				break;
			}
			break;

		case "Bow":
			this.type = Type.Bow;
			switch (level) {
			case "lev1":
				this.level = Level.lev1;
				setWeapon(Type.Bow, Level.lev1);
				break;
			case "lev2":
				this.level = Level.lev2;
				setWeapon(Type.Bow, Level.lev2);
				break;
			case "lev3":
				this.level = Level.lev3;
				setWeapon(Type.Bow, Level.lev3);
				break;
			default:
				break;
			}
			break;

		default:
			break;
		}
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public void setWeapon(Type type, Level level) {
		this.level = level;
		switch (type) {
		case Sword:
			switch (level) {
			case lev1:
				damage = 10;
				width = 10;
				price = 15;
				break;
			case lev2:
				damage = 17;
				width = 14;
				price = 60;
				break;
			case lev3:
				damage = 30;
				width = 18;
				price = 100;
				break;
			default:
				break;
			}
			break;

		case Spear:
			switch (level) {
			case lev1:
				damage = 8;
				width = 35;
				price = 10;
				break;
			case lev2:
				damage = 14;
				width = 39;
				price = 35;
				break;
			case lev3:
				damage = 22;
				width = 45;
				price = 70;
				break;
			default:
				break;
			}
			break;

		case Bow:
			switch (level) {
			case lev1:
				damage = 8;
				width = 15;
				price = 10;
				break;
			case lev2:
				damage = 14;
				width = 15;
				price = 40;
				break;
			case lev3:
				damage = 21;
				width = 15;
				price = 70;
				break;
			default:
				break;
			}
			break;

		case Bomb:
			switch (level) {
			case lev1:
				damage = 8;
				width = 30;
				height = 10;
				price = 3;
				break;
			case lev2:
				damage = 20;
				width = 33;
				height = 13;
				price = 5;
				break;
			case lev3:
				damage = 35;
				width = 38;
				height = 15;
				price = 15;
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}

	public boolean upgrade(Bag bag) {
		if (level != Level.lev3) {
			if (level == Level.lev1) {
				if (bag.getNumberOf(Element.PARCHMENT, Item.Level.FIRST) >= 10) {
					setWeapon(type, Level.lev2);
					bag.deleteParchments(Item.Level.FIRST);
					return true;
				}
			} else if (level == Level.lev2) {
				if (bag.getNumberOf(Element.PARCHMENT, Item.Level.SECOND) >= 20) {
					setWeapon(type, Level.lev3);
					bag.deleteParchments(Item.Level.SECOND);
					return true;
				}
			}
		}
		return false;
	}

	public float getDamage() {
		return damage;
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	public Level getLevel() {
		return level;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

}
