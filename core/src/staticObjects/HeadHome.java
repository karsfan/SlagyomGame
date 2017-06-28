package staticObjects;

import java.awt.Point;
import java.awt.Rectangle;

import battle.Enemy;
import battle.Enemy.Level;

public class HeadHome extends EnemyHome{
	public HeadHome(Level levelEnemy, Element type) {
		super(levelEnemy, type);
	}
	
	
	public void setPoint(Point point) {
			shape = new Rectangle((int) point.getX() * 32, (int) (point.getY() - 160/32) * 32, 160, 160);
			System.out.println(shape.getY());
			door = new Rectangle((int) (shape.getX() + shape.getWidth() / 3), (int) shape.getY(), 26, 8);		
	}
	public Enemy getEnemy(){
		return enemy.get(0);
	}
}
