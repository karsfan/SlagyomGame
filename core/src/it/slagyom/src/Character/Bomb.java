package it.slagyom.src.Character;

import it.slagyom.src.World.Game;
import it.slagyom.src.World.GameConfig;
import it.slagyom.src.World.Weapon;

public class Bomb extends Weapon {

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
			setDamage(8f);
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

	public void lancia(int velocity) {
		mainX = ((int) Game.world.battle.getCharacter().getX());
		mainY = ((int) Game.world.battle.getCharacter().getY());
		velocityX = (int) (velocity * Math.cos(30));
		velocityY = 100;
		System.out.println(velocityY);
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
		//System.out.println(mainX + " " + mainY);
		mainX += velocityX * dt;
		mainY += velocityY * dt;
		velocityY -= GameConfig.gravity*dt;
	}

}
