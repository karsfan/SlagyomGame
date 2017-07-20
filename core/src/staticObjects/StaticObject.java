package staticObjects;

import java.awt.Point;
import java.awt.Rectangle;

import character.DynamicObjects;
import world.ICollidable;

public class StaticObject implements ICollidable {

	public enum Element {
		HOME, THREE, FOREST1, FOREST2, GROUND, BUILDING, 
		WATER, ROCK, LAMP, FLOOR, ROAD, SHOP, TABLE, POTION, PARCHMENT, COIN, FLOOR2, PREENEMYHOME, FLOOR3, STRAW, ENEMYHOME, HEADHOME, CASTLE, TEMPLE, LIGHTLAMP 
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
		case "LAMP":
			this.element = Element.LAMP;
			shape = new Rectangle((int) point.getX(), (int) point.getY(), 25, 50);
			break;
		case "LIGHTLAMP":
			this.element = Element.LIGHTLAMP;
			shape = new Rectangle((int) point.getX(), (int) point.getY(), 25, 50);
			break;
		case "TEMPLE":
			this.element = Element.TEMPLE;
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
		if(e instanceof DynamicObjects)
		{
			if (!((shape.x > ((DynamicObjects) e).getX() + ((DynamicObjects) e).getWidth() / 2
					|| ((DynamicObjects) e).getX() > shape.x + shape.width)
					|| (shape.y > ((DynamicObjects) e).getY() + ((DynamicObjects) e).getHeight() / 2
							|| ((DynamicObjects) e).getY() > shape.y + shape.height))) {
				return true;
			}
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
