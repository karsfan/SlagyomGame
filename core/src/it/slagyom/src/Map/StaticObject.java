package it.slagyom.src.Map;

import java.awt.Rectangle;

import it.slagyom.src.World.ICollidable;

public class StaticObject implements ICollidable {

	public enum Element {
		HOME, THREE, FOREST1, FOREST2, GROUND, BUILDING, WATER, ROCK, FLOOR, ROAD, BIGHOME, SHOP, TABLE, POTION, PARCHMENT, COIN, FLOOR2, PREENEMYHOME, FLOOR3, STRAW
	};

	protected Element element;
	public Rectangle shape;

	public StaticObject() {
		element = null;
		shape = new Rectangle();
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

	@Override
	public boolean collide(Object e) {
		return false;
	}

	@Override
	public boolean collide() {
		return false;
	}
}
