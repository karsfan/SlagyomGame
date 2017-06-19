package it.slagyom.src.World;

import java.util.Random;

public class Weapon {
	public enum Type {
		Sword, Bow, Spear, Freccia, Bomba
	};

	public enum Level {
		lev1, lev2, lev3
	};

	float damage;
	public Level level;

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
		case Freccia:
			damage = 8;
			setWidth(0);
		case Bomba:
			setWeapon(type, level);
			setWidth(10);
			setHeight(10);
		default:
			break;
		}
	}

	public Weapon(Level level) {
		this.level = level;
		Random rand = new Random();
		int r = rand.nextInt(5);
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
		case 3:
			type = Type.Bomba;
			setWeapon(type, level);
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
		switch (type) {
		case Sword:
			switch (level) {
			case lev1:
				damage = 10;
				width = 10;
				break;
			case lev2:
				damage = 17;
				width = 14;
				break;
			case lev3:
				damage = 30;
				width = 18;
				break;
			default:
				break;
			}

		case Spear:
			switch (level) {
			case lev1:
				damage = 8;
				width = 35;
				break;
			case lev2:
				damage = 14;
				width = 39;
				break;
			case lev3:
				damage = 22;
				width = 45;
				break;
			default:
				break;
			}

		case Bow:
			switch (level) {
			case lev1:
				damage = 8;
				width = 15;
				break;
			case lev2:
				damage = 14;
				width = 15;
				break;
			case lev3:
				damage = 21;
				width = 15;
				break;
			default:
				break;
			}
		case Bomba:
			switch (level) {
			case lev1:
				damage = 8;
				width = 10;
				height = 10;
				break;
			case lev2:
				damage = 20;
				width = 13;
				height = 13;
				break;
			case lev3:
				damage = 35;
				width = 15;
				height = 15;
				break;
			default:
				break;
			}
		default:
			break;

		}
	}
	/*
	 * public void upgrade(Bag bag) { if (level < 3) { level++; powerPoints =
	 * level * 20; damage += 20; bag.deleteParchments(level); } }
	 */

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
