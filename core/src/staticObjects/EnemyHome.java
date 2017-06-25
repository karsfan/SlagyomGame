package staticObjects;

import battle.Enemy;
import battle.Enemy.Level;

public class EnemyHome extends StaticObject{
	Enemy enemy;
	
	public EnemyHome(Level levelEnemy)
	{
		enemy = new Enemy(levelEnemy);
	}
}
