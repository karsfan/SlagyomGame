package staticObjects;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import battle.Enemy;
import battle.Enemy.Level;
import it.slagyom.src.Character.Player;

public class EnemyHome extends StaticObject {
	public ArrayList<Enemy> enemy;
	public Rectangle door;

	public EnemyHome(Level levelEnemy, Element type) {
		element = type;
		enemy = new ArrayList<Enemy>();
		if(type == Element.TEMPLE){
			enemy.add(new Enemy(levelEnemy));
			enemy.add(new Enemy(levelEnemy));
			enemy.add(new Enemy(levelEnemy));
		}
		else
			enemy.add(new Enemy(levelEnemy));
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
	public Enemy getEnemy(){
		return null;
	}
}
