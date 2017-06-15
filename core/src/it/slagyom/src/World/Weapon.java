package it.slagyom.src.World;

import java.util.Random;

public class Weapon {
	public enum Type {
		Sword, Bow, Spear, Freccia
	};

	public enum Level {
		lev1, lev2, lev3
	};

	float damage;
	Level level;

	Type type;

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
		default:
			break;
		}
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
