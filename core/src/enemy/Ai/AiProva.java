package enemy.Ai;

import battle.Enemy;
import world.Game;

public class AiProva extends Enemy{

	public AiProva(Level level) {
		super(level);
		
	}
	
	public void updateEnemyEasy(float dt) {
		int rand = (int) (Math.random() * 100);
		/*if ((x - Game.world.battle.character.getX() < 100 && x - Game.world.battle.character.getX() > 0)
				|| (Game.world.battle.character.getX() - x < 100 && Game.world.battle.character.getX() - x > 0)) {
			if (rand < 5 && Game.world.battle.character.fighting)
				jump(dt);
			else if (rand > 5 && rand < 20 && Game.world.battle.character.fighting)
				setState(StateDynamicObject.DEFENDING);
			else if (x - Game.world.battle.character.getX() < 100 && x - Game.world.battle.character.getX() > 0
					&& rand < 40)
				fightLeft();
			else if (Game.world.battle.character.getX() - x < 100 && Game.world.battle.character.getX() - x > 0
					&& rand < 40)
				fightRight();
		}*/ if (Game.world.battle.character.getX() > x && rand < 50)
			movesRight(dt);
		else if (x > Game.world.battle.character.getX() && rand < 50)
			movesLeft(dt);
	}

}
