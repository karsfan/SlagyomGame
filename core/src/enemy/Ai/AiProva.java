package enemy.Ai;


import battle.Enemy;
import world.Game;

public class AiProva extends Enemy{

	public AiProva(Level level) {
		super(level);
	}
	public AiProva(Enemy enemy) {
		super(enemy);
	}
	@Override
	public void updateEnemyEasy(float dt) {
		int rand = (int) (Math.random() * 100);
		if (Game.world.battle.character.getX() > x && rand < 50)
			movesRight(dt);
		else if (x > Game.world.battle.character.getX() && rand < 50)
			movesLeft(dt);
	}
	
}
