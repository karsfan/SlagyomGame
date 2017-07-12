package staticObjects;

import java.awt.Rectangle;
import java.util.Iterator;
import java.util.Random;

import character.Player;
import world.Game;
import world.GameConfig;

public class Item extends StaticObject {

	public enum Level {
		FIRST, SECOND, THIRD
	}

	public boolean picked;
	public float price;
	public Level level;

	public Item(float x, float y, Element element, Level level) {
		switch (element) {
		case COIN:
			this.level = Level.FIRST;
			shape = new Rectangle((int) x, (int) y, 14, 14);
			break;
		case POTION:
			shape = new Rectangle((int) x, (int) y, 14, 14);
			break;
		case PARCHMENT:
			System.out.println("akjnmnx a,");
			shape = new Rectangle((int) x, (int) y, 10, 10);
			break;
		default:
			System.out.println("Qui");
			break;
		}

		this.element = element;
		this.level = level;
		picked = false;
	}

	public Item(Element element, Level level) {
		this.element = element;
		if (level != null)
			setLevel(level);
	}

	public Item() {
		Random rand = new Random();
		int r = rand.nextInt(3);
		switch (r) {
		case 0:
			element = Element.COIN;
			shape = new Rectangle();
			shape.width = 11;
			shape.height = 11;
			positionItem();
			this.level = Level.FIRST;
			break;
		case 1:
			element = Element.POTION;
			shape = new Rectangle();
			shape.width = 14;
			shape.height = 14;
			positionItem();
			r = rand.nextInt(3);
			if (r == 0) {
				level = Level.FIRST;
				price = 10;
			}
			if (r == 1) {
				level = Level.SECOND;
				price = 20;
			}
			if (r == 2) {
				level = Level.THIRD;
				price = 30;
			}
			break;
		case 2:
			element = Element.PARCHMENT;
			shape = new Rectangle();
			shape.width = 15;
			shape.height = 15;
			positionItem();
			r = rand.nextInt(2);
			if (r == 0) {
				level = Level.FIRST;
			}
			if (r == 1) {
				level = Level.SECOND;
			}
			break;
		default:
			System.out.println("QUO");
			break;
		}
	}

	public Item(battle.Enemy.Level level) {
		Random rand = new Random();
		int r = rand.nextInt(3);
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
			shape.width = 15;
			shape.height = 15;
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

	public Item(float x, float y, String element, String level) {
		switch (element) {
		case "COIN":
			this.element = Element.COIN;
			this.level = Level.FIRST;
			shape = new Rectangle((int) x, (int) y, 14, 14);
			break;
		case "POTION":
			this.element = Element.POTION;
			shape = new Rectangle((int) x, (int) y, 14, 14);
			switch (level) {
			case "FIRST":
				this.level = Level.FIRST;
			case "SECOND":
				this.level = Level.SECOND;
			case "THIRD":
				this.level = Level.THIRD;
			default:
				break;
			}
			break;
		case "PARCHMENT":
			this.element = Element.PARCHMENT;
			switch (level) {
			case "FIRST":
				this.level = Level.FIRST;
			case "SECOND":
				this.level = Level.SECOND;
			case "THIRD":
				this.level = Level.THIRD;
			default:
				break;
			}
			shape = new Rectangle((int) x, (int) y, 15, 15);
			break;
		default:
			System.out.println("Errore letture Item da File");
			break;
		}
		picked = false;
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
		switch (level) {
		case FIRST:
			if (element == Element.POTION)
				price = 10;
			else if (element == Element.PARCHMENT)
				price = 4;
			break;
		case SECOND:
			if (element == Element.POTION)
				price = 20;
			else if (element == Element.PARCHMENT)
				price = 9;
			break;
		case THIRD:
			price = 30;
			break;
		default:
			break;
		}
	}

	public float getX() {
		return shape.x;
	}

	public float getY() {
		return shape.y;
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

				if (((StaticObject) ob).getElement() != Element.GROUND
						&& ((StaticObject) ob).getElement() != Element.ROAD)
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
