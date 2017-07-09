package staticObjects;

import java.awt.Point;
import java.awt.Rectangle;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import battle.Enemy;
import battle.Enemy.Level;
import character.Player;
import multiplayer.NetworkEnemy;
import multiplayer.NetworkPlayer;
import world.Game;

public class PreEnemyHouse extends StaticObject {

	public ArrayList<Enemy> enemy;
	private Rectangle door;

	public PreEnemyHouse(Level levelEnemy, boolean online) {
		enemy = new ArrayList<>();
		Enemy tmp;
		if (online) {
			tmp = new NetworkEnemy(levelEnemy);
		} else
			tmp = new Enemy(levelEnemy);
		element = Element.PREENEMYHOME;
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

	public boolean collideDoor(Object e) {
		if (e instanceof Player) {
			if (!((door.x > ((Player) e).getX() + ((Player) e).getWidth() / 2
					|| ((Player) e).getX() > door.x + door.width)
					|| (door.y > ((Player) e).getY() + ((Player) e).getHeight() / 2
							|| ((Player) e).getY() > door.y + door.height))) {
				return true;
			}
		}
		if (e instanceof NetworkPlayer) {
			if (!((door.x > ((NetworkPlayer) e).getX() + ((NetworkPlayer) e).getWidth() / 2
					|| ((NetworkPlayer) e).getX() > door.x + door.width)
					|| (door.y > ((NetworkPlayer) e).getY() + ((NetworkPlayer) e).getHeight() / 2
							|| ((NetworkPlayer) e).getY() > door.y + door.height))) {
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
