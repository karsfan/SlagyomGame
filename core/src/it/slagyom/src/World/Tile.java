package it.slagyom.src.World;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import it.slagyom.src.Character.Player;
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
			info = "Questa e' una tabella informativa! Qui troverai informazioni riguardanti il nemico.";
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

		if (element == Element.SHOP) {
			door = new Rectangle((int) (point.getX() * 32 + shape.getWidth() / 3 + 7), (int) point.getY() * 32, 15, 8);
		}

		if (element == Element.PREENEMYHOME) {
			System.out.println("qui");
			door = new Rectangle((int) (point.getX() * 32+shape.getWidth()/3), (int) point.getY() * 32, 15, 20);
			System.out.println(point.getX() * 32 + shape.getWidth() / 3 + " " + point.getX() * 32);
		}

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
		if (this.getElement() == Element.PREENEMYHOME && e instanceof Player) {
			if (!((door.x > ((Player) e).getX() + ((Player) e).getWidth() / 2
					|| ((Player) e).getX() > door.x + door.width)
					|| (door.y > ((Player) e).getY() + ((Player) e).getHeight() 
							|| ((Player) e).getY() > door.y + door.height))) {
				return true;
			}
		}
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

	public void setInfoHouse(String info) {
		this.info = info;
	}

	@Override
	public boolean collide() {
		// TODO Auto-generated method stub
		return false;
	}


}
