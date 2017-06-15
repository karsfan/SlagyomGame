package it.slagyom.src.Character;

import java.util.Iterator;

import it.slagyom.src.Map.StaticObject.Element;
import it.slagyom.src.World.Game;
import it.slagyom.src.World.GameConfig;
import it.slagyom.src.World.ICollidable;
import it.slagyom.src.World.Tile;

public class Woman extends DynamicObjects implements ICollidable{
	public static enum ManType {
		WOMAN1, WOMAN2, WOMAN3
	};

	private String name;
	public int x;
	public int y;
	public int mainX;
	public int mainY;
	public int velocity;
	private String info;

	public boolean collision;
	public boolean collisionWithCharacter;
	int passi;
	public Woman() {
		super();
		collision = false;
		collisionWithCharacter = false;
		stateTimer = 0;
		width = 30;
		height = 30;
		positionMan();

		mainX = 100;
		mainY = 100;
		velocity = 80;
		name = "Ciccio";
		info = "Ciao sono Ciccio";
		currentState = StateDynamicObject.STANDING;
		previousState = StateDynamicObject.STANDING;
	}

	public void positionMan() {
		int rand = (int) (Math.random() * 1440);
		x = rand;
		rand = (int) (Math.random() * 960);
		y = rand;
		if (collide(this))
			positionMan();
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
		if (dt > 0.017)
			dt = (float) 0.0165;
		if (x < GameConfig.WIDTH - width / 2) {
			x += velocity * dt;
			if (collide(this)) {
				// collision = true;
				x -= velocity * dt;
			}
		}
		if (passi < 50000) {
			passi++;
			setState(StateDynamicObject.RUNNINGRIGHT, dt);
		} else {
			int rand = (int) (Math.random() * 10);

			if (rand == 1) {
				passi = 0;
				setState(StateDynamicObject.RUNNINGLEFT, dt);
			} else if (rand == 2) {
				passi = 0;
				setState(StateDynamicObject.RUNNINGUP, dt);
			} else if (rand == 3) {
				passi = 0;
				setState(StateDynamicObject.RUNNINGDOWN, dt);
			} else if (rand == 4) {
				passi = 0;
				setState(StateDynamicObject.STANDING, dt);
			} else {
				passi = 0;
				setState(StateDynamicObject.RUNNINGRIGHT, dt);
			}
		}
	}

	public void movesLeft(float dt) {

		if (dt > 0.017)
			dt = (float) 0.0165;
		if (x > 5) {
			x -= velocity * dt;
			if (collide(this)) {
				// collision = true;
				x += velocity * dt;
			}
		}
		if (passi < 50000) {
			passi++;
			setState(StateDynamicObject.RUNNINGLEFT, dt);
		} else {
			int rand = (int) (Math.random() * 10);

			if (rand == 1) {
				passi = 0;
				setState(StateDynamicObject.RUNNINGRIGHT, dt);
			} else if (rand == 2) {
				passi = 0;
				setState(StateDynamicObject.RUNNINGUP, dt);
			} else if (rand == 3) {
				passi = 0;
				setState(StateDynamicObject.RUNNINGDOWN, dt);
			} else if (rand == 4) {
				passi = 0;
				setState(StateDynamicObject.RUNNINGLEFT, dt);
			} else {
				passi = 0;
				setState(StateDynamicObject.STANDING, dt);
			}
		}
	}

	public void movesUp(float dt) {
		if (dt > 0.017)
			dt = (float) 0.0165;
		if (y < GameConfig.HEIGHT - height - 5) {
			y += velocity * dt;
			if (collide(this)) {
				y -= velocity * dt;
				// collision = true;
			}
		}

		if (passi < 50000) {
			passi++;
			setState(StateDynamicObject.RUNNINGUP, dt);
		} else {
			int rand = (int) (Math.random() * 10);

			if (rand == 1) {
				passi = 0;
				setState(StateDynamicObject.RUNNINGLEFT, dt);
			} else if (rand == 2) {
				passi = 0;
				setState(StateDynamicObject.RUNNINGRIGHT, dt);
			} else if (rand == 3) {
				passi = 0;
				setState(StateDynamicObject.RUNNINGDOWN, dt);
			} else if (rand == 4) {
				passi = 0;
				setState(StateDynamicObject.RUNNINGUP, dt);
			} else {
				passi = 0;
				setState(StateDynamicObject.STANDING, dt);
			}
		}
	}

	public void movesDown(float dt) {
		if (dt > 0.017)
			dt = (float) 0.0165;
		if (y > 0) {
			y -= velocity * dt;
			if (collide(this)) {
				y += velocity * dt;
				// collision = true;
			}
		}
		if (passi < 50000) {
			passi++;
			setState(StateDynamicObject.RUNNINGDOWN, dt);
		} else {
			int rand = (int) (Math.random() * 10);

			if (rand == 1) {
				passi = 0;
				setState(StateDynamicObject.RUNNINGLEFT, dt);
			} else if (rand == 2) {
				passi = 0;
				setState(StateDynamicObject.RUNNINGUP, dt);
			} else if (rand == 3) {
				passi = 0;
				setState(StateDynamicObject.RUNNINGRIGHT, dt);
			} else if (rand == 4) {
				passi = 0;
				setState(StateDynamicObject.RUNNINGDOWN, dt);
			} else {
				passi = 0;
				setState(StateDynamicObject.STANDING, dt);
			}
		}
	}

	private void setStateTimer(float f) {
		stateTimer = f;
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

	@Override
	public boolean collide(Object e) {
		
		Iterator<Tile> it =  Game.world.getListTile().iterator();
		while (it.hasNext()) {
			Object ob = (Object) it.next();
			if (ob instanceof Tile) {
				if (((Tile) ob).getElement() != Element.GROUND && ((Tile) ob).getElement() != Element.ROAD)
					if (((Tile) ob).collide(this)) {
						collision = true;
						return true;
					}
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
					if (ob instanceof Character)
						collisionWithCharacter = true;
					else
						collision = true;
					return true;
				}
			}
		}

		collision = false;
		return false;
	}

	public void changeDirection(StateDynamicObject state) {
		int rand = (int) (Math.random() * 4);
		if (collisionWithCharacter) {
			setState(StateDynamicObject.STANDING, 0);
			collisionWithCharacter = false;
			return;
		}

		if (collisionWithCharacter)
			System.out.println("collision");

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
			if (passi < 50000) {
				passi++;
				setState(StateDynamicObject.STANDING, dt);
			} else {
				passi = 0;
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
		// TODO Auto-generated method stub
		return false;
	}



}
