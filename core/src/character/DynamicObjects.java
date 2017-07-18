package character;


import com.badlogic.gdx.Gdx;


import world.GameConfig;
import world.ICollidable;

public class DynamicObjects implements ICollidable{
	public enum StateDynamicObject {
		STANDING, RUNNINGRIGHT, RUNNINGLEFT, RUNNINGDOWN, RUNNINGUP, JUMPING, FIGHTINGRIGHT, FIGHTINGLEFT
	};

	public String name;
	public float x;
	public float y;
	public StateDynamicObject currentState;
	public StateDynamicObject previousState;
	public float stateTimer;
	public float height;
	public float width;
	public float velocity;

	public DynamicObjects(float x, float y, StateDynamicObject currentState, StateDynamicObject previousState,
			float stateTimer, float height, float width, float velocity) {
		this.x = x;
		this.y = y;
		this.currentState = currentState;
		this.previousState = previousState;
		this.stateTimer = stateTimer;
		this.height = height;
		this.width = width;
		this.velocity = velocity;
	}

	public DynamicObjects() {
		// TODO Auto-generated constructor stub
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public StateDynamicObject getCurrentState() {
		return currentState;
	}

	public StateDynamicObject getPreviousState() {
		return previousState;
	}

	public float getStateTimer() {
		return stateTimer;
	}

	public float getHeight() {
		return height;
	}

	public float getWidth() {
		return width;
	}

	public float getVelocity() {
		return velocity;
	}

	public void setVelocity(float f) {

	}

	public void movesRight(float dt) {
		if (x < GameConfig.WIDTH - width / 2) {
			float velocityX = velocity;
			x += (velocityX * dt);
			if (collide(this))
				x -= (velocityX * dt);
		}
		setState(StateDynamicObject.RUNNINGRIGHT);
	}

	public void movesLeft(float dt) {

		if (x > 5) {
			float velocityX = velocity;
			x -= (velocityX * dt);
			if (collide(this))
				x += (velocityX * dt);
			

		}
		setState(StateDynamicObject.RUNNINGLEFT);
	}

	public void movesUp(float dt) {

		if (y < GameConfig.HEIGHT - height - 5) {
			float velocityY = velocity;

			y += (velocityY * dt);
			if (collide(this)) {
				y -= (velocityY * dt);
			}
		}
		setState(StateDynamicObject.RUNNINGUP);
	}

	public void movesDown(float dt) {
		if (y > 0) {
			float velocityY = velocity;

			y -= (velocityY * dt);
			if (collide(this))
				y += (velocityY * dt);
		}
		setState(StateDynamicObject.RUNNINGDOWN);
	}

	private void setStateTimer(float f) {
		stateTimer = f;
	}


	public void setState(StateDynamicObject state) {
		previousState = currentState;
		currentState = state;

		if (previousState == currentState)
			setStateTimer(getStateTimer() + Gdx.graphics.getDeltaTime());
		else
			setStateTimer(0);

	}

	public StateDynamicObject getState() {
		return currentState;
	}


	@Override
	public boolean collide(Object e) {
		
		return false;

	}

	@Override
	public boolean collide() {
		
		return false;
	}

	public String getName() {
		return name;
	}

}
