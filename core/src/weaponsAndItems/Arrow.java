package weaponsAndItems;

import battle.Fighting;
import world.ICollidable;

public class Arrow extends Weapon implements ICollidable {

	public float x;
	public float y;
	public boolean left;
	int velocity;

	/**
	 * Constructor for initialize a new Arrow
	 * 
	 * @param level
	 *            level of Arrow
	 * @param type
	 * @param x2
	 *            positionX
	 * @param y2
	 *            positionY
	 */
	public Arrow(Level level, Type type, float x2, float y2) {
		super(level, type);
		this.x = x2;
		this.y = y2;
		switch (level) {
		case lev1:
			velocity = 450;
			damage = 8;
			break;
		case lev2:
			velocity = 550;
			damage = 14;
			break;
		case lev3:
			velocity = 680;
			damage = 19;
			break;
		default:
			break;
		}
	}

	public void update(float dt) {
		if (left)
			x -= velocity * dt;
		else
			x += velocity * dt;
	}

	/**
	 * Verify if this collide with fighting
	 */
	@Override
	public boolean collide(Object fighting) {
		if (!((x > ((Fighting) fighting).getX() + ((Fighting) fighting).getWidth() / 2
				|| ((Fighting) fighting).getX() > x + getWidth())
				|| (y > ((Fighting) fighting).getY() + ((Fighting) fighting).getHeight()
						- ((Fighting) fighting).getHeight() / 4 || ((Fighting) fighting).getY() > y + getHeight()))) {
			((Fighting) fighting).decreaseHealth(this);
			return true;
		}
		return false;
	}

	@Override
	public boolean collide() {
		// TODO Auto-generated method stub
		return false;
	}

}
