package it.slagyom.src.Map;

import java.awt.Rectangle;
import java.util.Iterator;
import java.util.Random;

import it.slagyom.src.Character.Character;
import it.slagyom.src.World.Game;
import it.slagyom.src.World.Tile;

public class Item extends StaticObject {

	public enum Level {
		FIRST, SECOND, THIRD
	}

	public boolean picked;

	public String info;
	public static float stateTimer;
	public Level level;

	public Item(float x, float y, Element element, Level level) {
		switch (element) {
		case COIN:
			shape = new Rectangle((int) x, (int) y, 14, 14);
			break;
		case POTION:
			shape = new Rectangle((int) x, (int) y, 14, 14);
			level = Level.FIRST;
			break;
		case PARCHMENT:
			shape = new Rectangle((int) x, (int) y, 10, 10);
			break;
		default:
			break;
		}

		this.element = element;
		this.level = level;
		picked = false;
		stateTimer = 0;
	}

	public Item() {
		Random rand = new Random();
		int r = rand.nextInt(2);
		switch (r) {
		case 0:
			element = Element.COIN;
			shape = new Rectangle();
			shape.width = 11;
			shape.height = 11;
			positionItem();
			break;
		case 1:
			element = Element.POTION;
			shape = new Rectangle();
			shape.width = 14;
			shape.height = 14;
			positionItem();
			r = rand.nextInt(3);
			if(r == 0)
				level = Level.FIRST;
			if (r == 1)
				level = Level.SECOND;
			if (r == 2)
				level = Level.THIRD;
			break;
		case 2:
			element = Element.PARCHMENT;
			shape = new Rectangle();
			shape.width = 10;
			shape.height = 10;
			positionItem();
			break;
		default:
			break;
		}
	}

	private void positionItem() {
		Random rand = new Random();
		int r = rand.nextInt(1440);
		shape.x = r;
		r = rand.nextInt(960);
		shape.y = r;
		if (collide(this))
			positionItem();
	}

	
	
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public float getStateTimer() {
		return stateTimer;
	}

	public float getX() {
		return shape.x;
	}

	public void setX(int x) {
		this.shape.x = (int) x;
	}

	public int getY() {
		return shape.y;
	}

	public void setY(int y) {
		this.shape.y = y;
	}

	public int getWidth() {
		return shape.width;
	}

	public void setWidth(int width) {
		this.shape.width = width;
	}

	public int getHeight() {
		return shape.height;
	}

	public void setHeight(int height) {
		this.shape.height = height;
	}

	public boolean isPicked() {
		return picked;
	}

	public void setPicked(boolean picked) {
		this.picked = picked;
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

	@Override
	public boolean collide(Object e) {
		Iterator<Tile> it = Game.world.getListTile().iterator();
		while (it.hasNext()) {
			Object ob = (Object) it.next();
			if (ob instanceof Tile) {
				if (((Tile) ob).getElement() != Element.GROUND && ((Tile) ob).getElement() != Element.ROAD)
					if (!((shape.x > ((Tile) ob).getX() + ((Tile) ob).getWidth() 
							|| ((Tile) ob).getX() > shape.x + shape.width)
							|| (shape.y > ((Tile) ob).getY() + ((Tile) ob).getHeight() 
									|| ((Tile) ob).getY() > shape.y + shape.height))) {
						return true;
					}
			}
		}

		if (e instanceof Character) {
			if (!((shape.x > ((Character) e).getX() + ((Character) e).getWidth() / 2
					|| ((Character) e).getX() > shape.x + shape.width)
					|| (shape.y > ((Character) e).getY() + ((Character) e).getHeight() / 2
							|| ((Character) e).getY() > shape.y + shape.height))) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean collide() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean getPick() {
		// TODO Auto-generated method stub
		return false;
	}

}
