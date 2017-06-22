package it.slagyom.src.Map;

import java.awt.Point;
import java.awt.Rectangle;

import it.slagyom.src.Character.Player;
import it.slagyom.src.World.Enemy;
import it.slagyom.src.World.Enemy.Level;

public class PreEnemyHouse extends StaticObject {

	public Enemy enemy;
	private Rectangle door;
	
	public PreEnemyHouse(Level levelEnemy) {
		element = Element.PREENEMYHOME;
		enemy = new Enemy(null, 100, 100, null, null, levelEnemy);
	}

	public boolean collideDoor(Object e) {
		if (e instanceof Player) {
			if (!((door.x > ((Player) e).getX() + ((Player) e).getWidth() / 2
					|| ((Player) e).getX() > door.x + door.width)
					|| (door.y > ((Player) e).getY() + ((Player) e).getHeight() / 2
							|| ((Player) e).getY() > door.y + door.height))) {
				return true;
			}
		}
		return false;
	}

	public void setPoint(Point point) {
		shape = new Rectangle((int) point.getX() * 32, (int) (point.getY() - 96/32) * 32, 96, 96);
		door = new Rectangle((int) (shape.getX() + shape.getWidth() / 3), (int) shape.getY(), 26, 8);
	}
}
