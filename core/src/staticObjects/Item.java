package staticObjects;

import java.awt.Rectangle;
import java.util.Iterator;
import java.util.Random;

import it.slagyom.src.Character.Player;
import it.slagyom.src.World.Game;
import it.slagyom.src.World.GameConfig;

public class Item extends StaticObject {

	public enum Level {
		FIRST, SECOND, THIRD
	}

	public boolean picked;
	float x, y;
	public String info;
	public static float stateTimer;
	public Level level;

	public Item(float x, float y, Element element, Level level) {
		switch (element) {
		case COIN:
			this.x = x;
			this.y = y;
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
			if (r == 0)
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

	public Item(battle.Enemy.Level level) {
		Random rand = new Random();
		int r = rand.nextInt(2);
		switch (r) {
		case 0:
			element = Element.COIN;
			shape = new Rectangle();
			shape.width = 11;
			shape.height = 11;
			break;
		case 1:
			element = Element.POTION;
			shape = new Rectangle();
			shape.width = 14;
			shape.height = 14;
			
			switch (level) {
			case EASY:
				this.level = Level.FIRST;
				break;
			case MEDIUM:
				this.level = Level.SECOND;
				break;
			case HARD:
				this.level = Level.THIRD;
				break;
			default:
				break;

			}
			break;
		case 2:
			element = Element.PARCHMENT;
			shape = new Rectangle();
			shape.width = 10;
			shape.height = 10;
			switch (level) {
			case EASY:
				this.level = Level.FIRST;
				break;
			case MEDIUM:
				this.level = Level.SECOND;
				break;
			case HARD:
				this.level = Level.THIRD;
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}

	private void positionItem() {
		Random rand = new Random();
		int r = rand.nextInt((int) GameConfig.WIDTH);
		shape.x = r;
		r = rand.nextInt((int) GameConfig.HEIGHT);
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
		return x;
	}

	public void setX(float f) {
		this.x = f;
	}

	public float getY() {
		return y;
	}

	public void setY(float f) {
		this.y = f;
	}

	public float getWidth() {
		return shape.width;
	}

	public void setWidth(int width) {
		this.shape.width = width;
	}

	public float getHeight() {
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
		if (!Game.world.getListTile().isEmpty()) {

			Iterator<StaticObject> it = Game.world.getListTile().iterator();
			while (it.hasNext()) {
				Object ob = (Object) it.next();

				if (((StaticObject) ob).getElement() != Element.GROUND && ((StaticObject) ob)
						.getElement() != Element.ROAD)/*
														 * && ((Tile)
														 * ob).getElement() !=
														 * Element.FLOOR &&
														 * ((Tile)
														 * ob).getElement() !=
														 * Element.FLOOR2 &&
														 * ((Tile)
														 * ob).getElement() !=
														 * Element.FLOOR3)
														 */
					if (!((shape.x > ((StaticObject) ob).getX() + ((StaticObject) ob).getWidth()
							|| ((StaticObject) ob).getX() > shape.x + shape.width)
							|| (shape.y > ((StaticObject) ob).getY() + ((StaticObject) ob).getHeight()
									|| ((StaticObject) ob).getY() > shape.y + shape.height))) {
						return true;
					}

			}
		}

		if (e instanceof Player) {
			if (!((shape.x > ((Player) e).getX() + ((Player) e).getWidth() / 2
					|| ((Player) e).getX() > shape.x + shape.width)
					|| (shape.y > ((Player) e).getY() + ((Player) e).getHeight()
							|| ((Player) e).getY() > shape.y + shape.height))) {
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
