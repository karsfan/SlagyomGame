package it.slagyom.src.World;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import it.slagyom.src.Character.Character;
import it.slagyom.src.Character.Man;
import it.slagyom.src.Map.StaticObject;

public class Tile extends StaticObject implements ICollidable {

	private Rectangle door;
	private String info;

	public Tile(Point point, Element element, Dimension size) {
		shape = new Rectangle((int) point.getX(), (int) point.getY(), (int) size.getWidth(), (int) size.getHeight());
		this.element = element;
		door = new Rectangle();
	}

	public Tile(String element, Point point) {
		switch (element) {
		case "HOME":
			this.element = Element.HOME;
			shape = new Rectangle((int) point.getX(), (int) point.getY(), 64, 64);
			info = "CASA";
			break;
		case "BUILDING":
			this.element = Element.BUILDING;
			shape = new Rectangle((int) point.getX(), (int) point.getY(), 96, 128);
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
			this.element = Element.FLOOR;
			shape = new Rectangle((int) point.getX(), (int) point.getY(), 32, 32);
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
			info = "ABC";
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

	public void setElement(Element element) {
		this.element = element;
	}

	public Element getElement() {
		return element;
	}

	public void setCod(Element element) {
		this.element = element;
	}

	public void setPoint(Point point) {
		shape.x = point.x * 32;
		shape.y = point.y * 32;
		if(element == Element.FOREST2)
		System.out.println(shape.y);
		if (element == Element.HOME) {
			door = new Rectangle((int) point.getX() * 32, (int) point.getY() * 32, 8, 5);
			info = "In questa casa c'è sto cazzo";
		}
	}

	public boolean collideDoor(Object e) {
		if (this.getElement() == Element.HOME && e instanceof Character) {
			if (!((door.x  + shape.getWidth() / 4 > ((Character) e).getX() + ((Character) e).getWidth() / 2 - 5
					|| ((Character) e).getX() > door.x  + door.width + shape.getWidth() / 4)
					|| (door.y  > ((Character) e).getY() + ((Character) e).getHeight() / 2
							|| ((Character) e).getY() > door.y + door.height))) {
				System.out.println("porta");
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean collide(Object e) {
	
		if (e instanceof Character) {
			if (!((shape.x  > ((Character) e).getX() + ((Character) e).getWidth() / 2 
					|| ((Character) e).getX() > shape.x  + shape.width)
					|| (shape.y > ((Character) e).getY() + ((Character) e).getHeight() / 2
							|| ((Character) e).getY() > shape.y + shape.height))) {
				return true;
			}
		}
		if (e instanceof Man) {
			if (!((shape.x > ((Man) e).getX() + ((Man) e).getWidth() / 2 
					|| ((Man) e).getX() > shape.x + shape.width)
					|| (shape.y > ((Man) e).getY() + ((Man) e).getHeight() / 2
							|| ((Man) e).getY() > shape.y + shape.height)))
				return true;
		}

		return false;

	}

	public String getInfo() {
		return info;
	}

	public void setInfoHouse(String info) {
		this.info = info;
	}

	@Override
	public boolean collide() {
		// TODO Auto-generated method stub
		return false;
	}

}
