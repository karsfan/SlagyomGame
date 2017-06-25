package it.slagyom.src.Character;

public class DynamicObjects {
	public enum StateDynamicObject {
		STANDING, RUNNINGRIGHT, RUNNINGLEFT, RUNNINGDOWN, RUNNINGUP, JUMPING, FIGHTINGRIGHT, FIGHTINGLEFT, DEFENDING
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

	public DynamicObjects(float x, float y, StateDynamicObject currentState,
			StateDynamicObject previousState, float stateTimer, float height, float width,
			float velocity) {
		super();
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

	public void movesLeft(float dt) {

	}

	public void movesRight(float dt) {

	}

	public void movesUp(float dt) {

	}

	public void movesDown(float dt) {
	}

	public String getName() {
		return name;
	}

}
