package battle;

import it.slagyom.src.Character.DynamicObjects;
import it.slagyom.src.Character.DynamicObjects.StateDynamicObject;

public class Fighting extends DynamicObjects{
	
	public float stateTimer;
	public boolean fighting;
	public float fightingTimeCurrent;
	public float fightingTime;
	public boolean jumping;
	public boolean doubleJumping;
	public float velocityY;
	public float velocityX;
	public int forza=50;
	public boolean bomba = false;
	public boolean left = false;
	public boolean right = true;
	
	public Fighting()
	{
		stateTimer = 0;
		height = 150;
		width = 120;
		currentState = StateDynamicObject.STANDING;
		previousState = null;
		fighting = false;
		fightingTimeCurrent = 0;
		fightingTime = 0.4f;
		
		jumping = false;
		doubleJumping = false;
		velocityY = 0;
		velocityX = 10;
		
	}
	
}
