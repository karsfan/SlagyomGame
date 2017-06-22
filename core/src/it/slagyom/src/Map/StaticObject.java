package it.slagyom.src.Map;

import java.awt.Point;
import java.awt.Rectangle;

import it.slagyom.src.Character.Man;
import it.slagyom.src.Character.Player;
import it.slagyom.src.World.ICollidable;

public class StaticObject implements ICollidable {

	public enum Element {
		HOME, THREE, FOREST1, FOREST2, GROUND, BUILDING, WATER, ROCK, FLOOR, ROAD, BIGHOME, SHOP, TABLE, POTION, PARCHMENT, COIN, FLOOR2, PREENEMYHOME, FLOOR3, STRAW
	};

	protected Element element;
	public Rectangle shape;
	String info = " a";
	public StaticObject() {
		element = null;
		shape = new Rectangle();
	}

	public StaticObject(String element, Point point) {

		switch (element) {
		case "HOME":
			this.element = Element.HOME;
			shape = new Rectangle((int) point.getX(), (int) point.getY(), 64, 64);
			break;
		case "GROUND":
			this.element = Element.GROUND;
			shape = new Rectangle((int) point.getX(), (int) point.getY(), 32, 32);
			break;
		case "TREE":
			this.element = Element.THREE;
			shape = new Rectangle((int) point.getX(), (int) point.getY(), 32, 32);
			break;
		case "WATER":
			this.element = Element.WATER;
			shape = new Rectangle((int) point.getX(), (int) point.getY(), 32, 32);
			break;
		case "ROCK":
			this.element = Element.ROCK;
			shape = new Rectangle((int) point.getX(), (int) point.getY(), 32, 32);
			break;
		case "BIGHOME":
			this.element = Element.BIGHOME;
			shape = new Rectangle((int) point.getX(), (int) point.getY(), 128, 96);
			break;
		case "FLOOR":
			this.element = Element.GROUND;
			shape = new Rectangle((int) point.getX(), (int) point.getY(), 32, 32);
			break;
		case "FLOOR2":
			this.element = Element.GROUND;
			shape = new Rectangle((int) point.getX(), (int) point.getY(), 32, 32);
			break;
		case "FLOOR3":
			this.element = Element.GROUND;
			shape = new Rectangle((int) point.getX(), (int) point.getY(), 32, 32);
			break;
		case "SHOP":
			this.element = Element.SHOP;
			shape = new Rectangle((int) point.getX(), (int) point.getY(), 64, 64);
			break;
		case "PREENEMYHOME":
			this.element = Element.PREENEMYHOME;
			shape = new Rectangle((int) point.getX(), (int) point.getY(), 96, 96);
			break;
		case "STRAW":
			this.element = Element.STRAW;
			shape = new Rectangle((int) point.getX(), (int) point.getY(), 64, 64);
			break;
		case "ROAD":
			this.element = Element.ROAD;
			shape = new Rectangle((int) point.getX(), (int) point.getY(), 32, 32);
			break;
		case "FOREST1":
			this.element = Element.FOREST1;
			shape = new Rectangle((int) point.getX(), (int) point.getY(), 64, 96);
			break;
		case "FOREST2":
			this.element = Element.FOREST2;
			shape = new Rectangle((int) point.getX(), (int) point.getY(), 64, 96);
			break;
		case "TABLE":
			this.element = Element.TABLE;
			shape = new Rectangle((int) point.getX(), (int) point.getY(), 16, 16);
			// info = "Questa e' una tabella informativa! Qui troverai
			// informazioni riguardanti il nemico.";
			break;
		default:
			break;
		}
	}

	public float getHeight() {
		return (float) shape.getHeight();
	}

	public float getWidth() {
		return (float) shape.getWidth();
	}

	public float getX() {
		return (float) shape.getX();
	}

	public float getY() {
		return (float) shape.getY();
	}

	public void setPoint(Point point) {
		shape.x = point.x * 32;
		shape.y = point.y * 32;

	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

	@Override
	public boolean collide() {
		return false;
	}

	@Override
	public boolean collide(Object e) {
		if (e instanceof Player) {
			if (!((shape.x > ((Player) e).getX() + ((Player) e).getWidth() / 2
					|| ((Player) e).getX() > shape.x + shape.width)
					|| (shape.y > ((Player) e).getY() + ((Player) e).getHeight() / 2
							|| ((Player) e).getY() > shape.y + shape.height))) {
				return true;
			}
		}
		if (e instanceof Man) {
			if (!((shape.x > ((Man) e).getX() + ((Man) e).getWidth() / 2 || ((Man) e).getX() > shape.x + shape.width)
					|| (shape.y > ((Man) e).getY() + ((Man) e).getHeight() / 2
							|| ((Man) e).getY() > shape.y + shape.height)))
				return true;
		}

		return false;

	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}
