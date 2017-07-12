package staticObjects;

import java.awt.Point;
import java.awt.Rectangle;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import battle.Enemy;
import battle.Enemy.Level;
import character.Player;
import world.Game;

public class EnemyHome extends StaticObject {
	public ArrayList<Enemy> enemy;
	public Rectangle door;

	public EnemyHome(Level levelEnemy, Element type) {
		element = type;
		enemy = new ArrayList<Enemy>();
		if (type == Element.TEMPLE) {
			Enemy enemyOne = new Enemy(levelEnemy);
			Enemy enemyTwo = new Enemy(levelEnemy);
			Enemy enemyThree = new Enemy(levelEnemy);
			if (Game.enemy != null) {
				try {
					enemyOne = Game.enemy.getConstructor(Level.class).newInstance(levelEnemy);
					enemyTwo = Game.enemy.getConstructor(Level.class).newInstance(levelEnemy);
					enemyThree = Game.enemy.getConstructor(Level.class).newInstance(levelEnemy);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			enemy.add(enemyOne);
			enemy.add(enemyTwo);
			enemy.add(enemyThree);
		} else {
			Enemy boss = new Enemy(levelEnemy);
			if (Game.enemy != null) {
				try {
					boss = Game.enemy.getConstructor(Level.class).newInstance(levelEnemy);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			enemy.add(boss);
		}
	}

	public void setPoint(Point point) {
		shape = new Rectangle((int) point.getX() * 32, (int) (point.getY() - 96 / 32) * 32, 128, 96);
		door = new Rectangle((int) (shape.getX() + shape.getWidth() / 3), (int) shape.getY(), 26, 8);

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

	public Enemy getEnemy() {
		return null;
	}
}
