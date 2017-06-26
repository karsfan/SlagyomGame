package staticObjects;

import battle.Enemy;
import battle.Enemy.Level;

public class EnemyHome extends StaticObject{
	public Enemy enemy;
	
	public EnemyHome(Level levelEnemy)
	{	
		element = Element.BIGHOME;
		enemy = new Enemy(levelEnemy);
	}
}
