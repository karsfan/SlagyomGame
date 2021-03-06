package character;

import java.util.Iterator;

import staticObjects.StaticObject;
import staticObjects.StaticObject.Element;
import world.Game;
import world.GameConfig;
import world.ICollidable;
import world.World;

public class Man extends DynamicObjects implements ICollidable {

	public static enum ManType {
		MAN1, MAN2, MAN3
	};

	public String name;
	public String info;
	public boolean collision;
	public boolean collisionWithCharacter;
	int steps;

	public Man() {
		super();
		collision = false;
		collisionWithCharacter = false;
		stateTimer = 0;
		width = 30;
		height = 30;
		while (!positionMan())
			;

		velocity = 80;
		name = World.manNames.pop();
		info = World.dialogues.pop();
		currentState = StateDynamicObject.STANDING;
		previousState = StateDynamicObject.STANDING;
	}

	public Man(float x, float y) {
		super();
		collision = false;
		collisionWithCharacter = false;
		stateTimer = 0;
		width = 30;
		height = 30;
		this.x = x;
		this.y = y;

		velocity = 80;
		name = World.manNames.pop();
		info = World.dialogues.pop();
		currentState = StateDynamicObject.STANDING;
		previousState = StateDynamicObject.STANDING;
	}

	public boolean positionMan() {
		int rand = (int) (Math.random() * GameConfig.WIDTH);
		x = rand;
		rand = (int) (Math.random() * GameConfig.HEIGHT);
		y = rand;
		if (collide())
			positionMan();
		return true;
	}

	public String getInfo() {
		return info;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public String getName() {
		return name;
	}

	public void movesRight(float dt) {
		if (x + velocity * dt < GameConfig.WIDTH - width / 2) {
			x += velocity * dt;
			if (collide()) {
				collision = true;
				x -= velocity * dt;
			}
		} else {
			changeDirection(getCurrentState());
		}
		if (steps < 50) {
			steps++;
			setState(StateDynamicObject.RUNNINGRIGHT, dt);
		} else {
			int rand = (int) (Math.random() * 7);

			if (rand == 1) {
				steps = 0;
				setState(StateDynamicObject.RUNNINGLEFT, dt);
			} else if (rand == 2) {
				steps = 0;
				setState(StateDynamicObject.RUNNINGUP, dt);
			} else if (rand == 3) {
				steps = 0;
				setState(StateDynamicObject.RUNNINGDOWN, dt);
			} else if (rand == 4) {
				steps = 0;
				setState(StateDynamicObject.STANDING, dt);
			} else {
				steps = 0;
				setState(StateDynamicObject.RUNNINGRIGHT, dt);
			}
		}
	}

	public void movesLeft(float dt) {
		if (x - velocity * dt > 5) {
			x -= velocity * dt;
			if (collide()) {
				collision = true;
				x += velocity * dt;
			}
		} else {
			changeDirection(getCurrentState());
		}
		if (steps < 50) {
			steps++;
			setState(StateDynamicObject.RUNNINGLEFT, dt);
		} else {
			int rand = (int) (Math.random() * 7);

			if (rand == 1) {
				steps = 0;
				setState(StateDynamicObject.RUNNINGRIGHT, dt);
			} else if (rand == 2) {
				steps = 0;
				setState(StateDynamicObject.RUNNINGUP, dt);
			} else if (rand == 3) {
				steps = 0;
				setState(StateDynamicObject.RUNNINGDOWN, dt);
			} else if (rand == 4) {
				steps = 0;
				setState(StateDynamicObject.RUNNINGLEFT, dt);
			} else {
				steps = 0;
				setState(StateDynamicObject.STANDING, dt);
			}
		}
	}

	public void movesUp(float dt) {
		if (y + velocity * dt < GameConfig.HEIGHT - height - 5) {
			y += velocity * dt;
			if (collide()) {
				collision = true;
				y -= velocity * dt;
			}
		} else {
			changeDirection(getCurrentState());
		}

		if (steps < 50) {
			steps++;
			setState(StateDynamicObject.RUNNINGUP, dt);
		} else {
			int rand = (int) (Math.random() * 7);

			if (rand == 1) {
				steps = 0;
				setState(StateDynamicObject.RUNNINGLEFT, dt);
			} else if (rand == 2) {
				steps = 0;
				setState(StateDynamicObject.RUNNINGRIGHT, dt);
			} else if (rand == 3) {
				steps = 0;
				setState(StateDynamicObject.RUNNINGDOWN, dt);
			} else if (rand == 4) {
				steps = 0;
				setState(StateDynamicObject.RUNNINGUP, dt);
			} else {
				steps = 0;
				setState(StateDynamicObject.STANDING, dt);
			}
		}
	}

	public void movesDown(float dt) {
		if (y - velocity * dt > 0) {
			y -= velocity * dt;
			if (collide()) {
				y += velocity * dt;
				collision = true;
			}
		} else {
			changeDirection(getCurrentState());
		}
		if (steps < 50) {
			steps++;
			setState(StateDynamicObject.RUNNINGDOWN, dt);
		} else {
			int rand = (int) (Math.random() * 7);

			if (rand == 1) {
				steps = 0;
				setState(StateDynamicObject.RUNNINGLEFT, dt);
			} else if (rand == 2) {
				steps = 0;
				setState(StateDynamicObject.RUNNINGUP, dt);
			} else if (rand == 3) {
				steps = 0;
				setState(StateDynamicObject.RUNNINGRIGHT, dt);
			} else if (rand == 4) {
				steps = 0;
				setState(StateDynamicObject.RUNNINGDOWN, dt);
			} else {
				steps = 0;
				setState(StateDynamicObject.STANDING, dt);
			}
		}
	}

	private void setStateTimer(float dt) {
		stateTimer = dt;
	}

	@Override
	public float getStateTimer() {
		return stateTimer;
	}

	private void setState(StateDynamicObject state, float dt) {
		previousState = currentState;
		currentState = state;

		if (previousState == currentState)
			setStateTimer(getStateTimer() + dt);
		else
			setStateTimer(0);
	}

	public StateDynamicObject getState() {
		return currentState;
	}

	public float getHeight() {
		return height;
	}

	public float getWidth() {
		return width;
	}

	public void changeDirection(StateDynamicObject state) {
		int rand = (int) (Math.random() * 4);
		if (collisionWithCharacter) {
			setState(StateDynamicObject.STANDING, 0);
			collisionWithCharacter = true;
			return;
		}

		StateDynamicObject newState = null;
		if (rand == 0)
			newState = StateDynamicObject.RUNNINGDOWN;
		else if (rand == 1)
			newState = StateDynamicObject.STANDING;
		else if (rand == 2)
			newState = StateDynamicObject.RUNNINGLEFT;
		else if (rand == 3)
			newState = StateDynamicObject.RUNNINGUP;
		else if (rand == 4)
			newState = StateDynamicObject.RUNNINGRIGHT;
		if (newState == state)
			changeDirection(newState);
		setState(newState, 0);
		collision = false;
	}

	public void update(float dt) {
		collide();
		if (collision || collisionWithCharacter)
			changeDirection(getCurrentState());

		else if (currentState == StateDynamicObject.RUNNINGLEFT)
			movesLeft(dt);
		else if (currentState == StateDynamicObject.RUNNINGRIGHT)
			movesRight(dt);
		else if (currentState == StateDynamicObject.RUNNINGUP)
			movesUp(dt);
		else if (currentState == StateDynamicObject.RUNNINGDOWN)
			movesDown(dt);
		else if (currentState == StateDynamicObject.STANDING && !collisionWithCharacter) {
			if (steps < 50) {
				steps++;
				setState(StateDynamicObject.STANDING, dt);
			} else {
				steps = 0;
				int rand = (int) (Math.random() * 5);
				if (rand == 1)
					setState(StateDynamicObject.RUNNINGLEFT, dt);
				else if (rand == 2)
					setState(StateDynamicObject.RUNNINGDOWN, dt);
				else if (rand == 3)
					setState(StateDynamicObject.RUNNINGUP, dt);
				else if (rand == 4)
					setState(StateDynamicObject.RUNNINGRIGHT, dt);
			}
		}

	}

	@Override
	public boolean collide() {
		Iterator<StaticObject> it = Game.world.getListTile().iterator();
		while (it.hasNext()) {
			Object ob = (Object) it.next();
			if (((StaticObject) ob).getElement() != Element.GROUND && ((StaticObject) ob).getElement() != Element.ROAD)
				if (((StaticObject) ob).collide(this)) {
					collision = true;
					return true;
				}
		}

		Iterator<DynamicObjects> it1 = Game.world.getListDynamicObjects().iterator();
		while (it1.hasNext()) {
			Object ob = (Object) it1.next();
			if (ob instanceof DynamicObjects && ob != this) {
				if (!((x > ((DynamicObjects) ob).getX() + ((DynamicObjects) ob).getWidth() / 2
						|| ((DynamicObjects) ob).getX() > x + width / 2)
						|| (y > ((DynamicObjects) ob).getY() + ((DynamicObjects) ob).getHeight() / 2
								|| ((DynamicObjects) ob).getY() > y + height / 2))) {
					if (ob instanceof Player) {
						collisionWithCharacter = true;
					} else {
						collision = true;
					}
					return true;
				}
			}
		}
		collisionWithCharacter = false;
		collision = false;
		return false;
	}

}
