package it.slagyom.src.Character;

import battle.Enemy;
import battle.Fighting;
import it.slagyom.src.Character.DynamicObjects.StateDynamicObject;
import it.slagyom.src.World.Game;
import it.slagyom.src.World.GameConfig;
import it.slagyom.src.World.ICollidable;
import it.slagyom.src.World.Weapon;

public class Bomb extends Weapon implements ICollidable {

	public boolean morta = false;
	int mainX;
	int mainY;
	int velocityX;
	int velocityY;
	int velocity;
	public boolean lanciata = false;

	public Bomb(Level level, Type type) {
		super(level, type);
		setLevel(level);
		mainX = 250;
		mainY = 1250;
		switch (level) {
		case lev1:
			setDamage(10);
			setWidth(10);
			break;
		case lev2:
			setDamage(15);
			setWidth(13);
			break;
		case lev3:
			setDamage(28);
			setWidth(15);
			break;
		default:
			break;
		}
	}

	public void lancia(int velocity, Fighting fighting) {
		lanciata = true;
		mainX = ((int) ((Fighting) fighting).getX());
		mainY = ((int) ((Fighting) fighting).getY());

		if (((Fighting) fighting).left)
			velocityX -= (int) (velocity * Math.cos(30 * (Math.PI / 180)));

		else
			velocityX = (int) (velocity * Math.cos(30 * (Math.PI / 180)));
		velocityY = (int) (velocity * Math.sin(90 * (Math.PI / 180)));

	}

	public int getMainX() {
		return mainX;
	}

	public void setMainX(int mainX) {
		this.mainX = mainX;
	}

	public int getMainY() {
		return mainY;
	}

	public void setMainY(int mainY) {
		this.mainY = mainY;
	}

	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}

	public void update(float dt) {

		mainX += velocityX * dt;
		mainY += velocityY * dt - GameConfig.gravity * dt * dt;

		velocityY -= GameConfig.gravity * dt;
		if (mainY <= 250)
			morta = true;
		if (collide())
			morta = true;
	}

	@Override
	public boolean collide(Object e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean collide() {
		Enemy enemy = Game.world.battle.enemy;
		if (!((mainX > enemy.getX() + enemy.getWidth() / 2 || enemy.getX() > mainX + getWidth())
				|| (mainY > enemy.getY() + enemy.getHeight() - enemy.getHeight() / 4
						|| enemy.getY() > mainY + getHeight()))) {
			enemy.decreaseHealth(this);
			System.out.println("collissione con il nemico");
			return true;
		}

		return false;
	}

}
