package character;

import battle.CharacterBattle;
import battle.Fighting;
import gameManager.GameSlagyom;
import multiplayer.Client;
import world.Game;
import world.GameConfig;
import world.ICollidable;

public class Bomb extends Weapon implements ICollidable {

	public boolean morta = false;
	int mainX;
	int mainY;
	int velocityX;
	int velocityY;
	int velocity;
	public String id;
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
		else if (((Fighting) fighting).right)
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
		if (mainY <= GameConfig.mainY_Battle)
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
		if (!GameSlagyom.modalityMultiplayer) {
			Fighting enemy = Game.world.battle.enemy;
			if (id != "Enemy")
				if (!((mainX > enemy.getX() + enemy.getWidth() / 2 || enemy.getX() > mainX + getWidth())
						|| (mainY > enemy.getY() + enemy.getHeight() - enemy.getHeight() / 4
								|| enemy.getY() > mainY + getHeight()))) {
					enemy.decreaseHealth(this);
					System.out.println("collissione con il nemico");
					return true;
				}
			CharacterBattle player = Game.world.battle.character;
			if (id != "Player")
				if (!((mainX > player.getX() + player.getWidth() / 2 || player.getX() > mainX + getWidth())
						|| (mainY > player.getY() + player.getHeight() - player.getHeight() / 4
								|| player.getY() > mainY + getHeight()))) {
					player.decreaseHealth(this);
					System.out.println("collissione della bomba con il player");
					return true;
				}
		}
		else
		{
			Fighting enemy = Client.networkWorld.battle.enemy;
			if (id != "Enemy")
				if (!((mainX > enemy.getX() + enemy.getWidth() / 2 || enemy.getX() > mainX + getWidth())
						|| (mainY > enemy.getY() + enemy.getHeight() - enemy.getHeight() / 4
								|| enemy.getY() > mainY + getHeight()))) {
					((Fighting) enemy).decreaseHealth(this);
					System.out.println("collissione con il nemico");
					return true;
				}
			CharacterBattle player = Client.networkWorld.battle.character;
			if (id != "Player")
				if (!((mainX > player.getX() + player.getWidth() / 2 || player.getX() > mainX + getWidth())
						|| (mainY > player.getY() + player.getHeight() - player.getHeight() / 4
								|| player.getY() > mainY + getHeight()))) {
					player.decreaseHealth(this);
					System.out.println("collissione della bomba con il player");
					return true;
				}
		}
		return false;
	}

}
