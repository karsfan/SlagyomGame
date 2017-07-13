package staticObjects;

import java.awt.Point;
import java.awt.Rectangle;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import battle.Enemy;
import battle.Enemy.Level;
import character.Player;
import multiplayer.NetworkEnemy;
import world.Game;

public class PreEnemyHouse extends StaticObject {

	public ArrayList<Enemy> enemy;
	private Rectangle door;

	public PreEnemyHouse(Level levelEnemy, boolean online) {
		enemy = new ArrayList<>();
		element = Element.PREENEMYHOME;
		Enemy tmp;
		if (online) {
			tmp = new NetworkEnemy(levelEnemy);
			enemy.add(tmp);
			enemy.add(new NetworkEnemy(levelEnemy));
			enemy.add(new NetworkEnemy(levelEnemy));
		} else {
			tmp = new Enemy(levelEnemy);
			if (Game.enemy != null) {
				try {
					tmp = Game.enemy.getConstructor(Level.class).newInstance(levelEnemy);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			enemy.add(tmp);
			enemy.add(new Enemy(levelEnemy));
			enemy.add(new Enemy(levelEnemy));
		}
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
		shape = new Rectangle((int) point.getX() * 32, (int) (point.getY() - 96 / 32) * 32, 96, 96);
		door = new Rectangle((int) (shape.getX() + shape.getWidth() / 3), (int) shape.getY(), 26, 8);
	}
}
