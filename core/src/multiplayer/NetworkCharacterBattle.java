package multiplayer;

import java.util.Iterator;

import battle.CharacterBattle;
import staticObjects.StaticObject.Element;
import weaponsAndItems.Bomb;
import weaponsAndItems.Item;
import weaponsAndItems.Weapon;
import weaponsAndItems.Weapon.Level;
import weaponsAndItems.Weapon.Type;
import world.GameConfig;

public class NetworkCharacterBattle extends CharacterBattle {
	public boolean player = false;
	public int ID;
	public int IDOtherPlayer;
	public boolean fightingLeft;
	public boolean fightingRight;
	public Bomb bomb;
	public boolean booleanLaunchBomb;
	public boolean weaponChanged;

	public NetworkCharacterBattle(NetworkPlayer player) {
		super(player);
		this.player = player.player;
		this.ID = player.ID;
		this.IDOtherPlayer = player.IDOtherPlayer;
		bag.add(new Bomb(Level.lev1, Type.Bomb));
		bag.add(new Bomb(Level.lev1, Type.Bomb));
		bag.add(new Bomb(Level.lev1, Type.Bomb));
		bag.add(new Bomb(Level.lev1, Type.Bomb));
		bag.add(new Bomb(Level.lev1, Type.Bomb));
		fightingLeft = false;
		fightingRight = false;
		booleanLaunchBomb = false;
		weaponChanged = false;
	}

	@Override
	public void update(float dt) {
		if (fighting && fightingTimeCurrent < fightingTime) {
			fightingTimeCurrent += dt;
			if (fightingTimeCurrent >= fightingTime / 2 && primary_weapon.getType() == Type.Bow)
				if (!arrowShooted) {
					shootArrow();
					arrowShooted = true;
				}
			setState(getCurrentState(), dt);
		} else if (fighting && fightingTimeCurrent > fightingTime) {
			fighting = false;
			fightingTimeCurrent = 0;
			arrowShooted = false;
			fightingLeft = false;
			fightingRight = false;
			// setState(StateDynamicObject.STANDING, dt);
		}
		dt = 0.35f;
		if ((jumping || doubleJumping) && y + velocityY * dt > GameConfig.mainY_Battle) {
			y += velocityY * dt;
			updateVelocityY(dt);
			setState(StateDynamicObject.JUMPING, dt);
			if (Client.networkWorld.battle.enemy instanceof NetworkCharacterBattle) {
				if (ID != ((NetworkCharacterBattle) Client.networkWorld.battle.enemy).ID) {
					if (collide() && x < Client.networkWorld.battle.enemy.getX())
						x = (Client.networkWorld.battle.enemy.getX() - getWidth() / 2);
					else if (collide() && x > Client.networkWorld.battle.enemy.getX())
						x = (Client.networkWorld.battle.enemy.getX() + Client.networkWorld.battle.enemy.getWidth() / 2);
				} else {
					if (collide() && x < Client.networkWorld.battle.character.getX())
						x = (Client.networkWorld.battle.character.getX() - getWidth() / 2);
					else if (collide() && x > Client.networkWorld.battle.character.getX())
						x = (Client.networkWorld.battle.character.getX()
								+ Client.networkWorld.battle.character.getWidth() / 2);
				}
			} else {
				if (collide() && x < Client.networkWorld.battle.enemy.getX())
					x = (Client.networkWorld.battle.enemy.getX() - getWidth() / 2);
				else if (collide() && x > Client.networkWorld.battle.enemy.getX())
					x = (Client.networkWorld.battle.enemy.getX() + Client.networkWorld.battle.enemy.getWidth() / 2);
			}
		} else {
			jumping = false;
			doubleJumping = false;
			y = GameConfig.mainY_Battle;
			velocityY = 0;
		}
		Iterator<Bomb> it1 = bag.bombe.iterator();
		while (it1.hasNext()) {
			Bomb ob = (Bomb) it1.next();
			if (ob.launched == true) {
				((Bomb) ob).update(dt);
				if (ob.dead) {
					it1.remove();
					continue;
				}
			}
		}
	}

	public void swapWeapon() {
		if (bag.secondary_weapon != null) {
			Weapon temporary = new Weapon(Client.networkWorld.player.primary_weapon.getLevel(),
					Client.networkWorld.player.primary_weapon.getType());
			Client.networkWorld.player.primary_weapon = bag.secondary_weapon;
			bag.secondary_weapon = temporary;
			primary_weapon = Client.networkWorld.player.primary_weapon;
			if (primary_weapon.getType() == Type.Sword
					|| (primary_weapon.getType() == Type.Spear && primary_weapon.getLevel() == Weapon.Level.lev2)
					|| (primary_weapon.getType() == Type.Spear && primary_weapon.getLevel() == Weapon.Level.lev3))
				this.width = 200;
			else
				this.width = 120;
			weaponChanged = true;
		}
	}

	@Override
	public void fightRight(float dt) {
		right = true;
		left = false;
		width += primary_weapon.getWidth();
		if (collide()) {
			if (Client.networkWorld.battle.enemy instanceof NetworkCharacterBattle) {
				if (ID != ((NetworkCharacterBattle) Client.networkWorld.battle.enemy).ID) {
					Client.networkWorld.battle.enemy.decreaseHealth(primary_weapon);
				} else
					Client.networkWorld.battle.character.decreaseHealth(primary_weapon);
			} else
				Client.networkWorld.battle.enemy.decreaseHealth(primary_weapon);
		}
		width -= primary_weapon.getWidth();

		setState(StateDynamicObject.FIGHTINGRIGHT, dt);
		fighting = true;
		fightingRight = true;
	}

	@Override
	public void fightLeft(float dt) {
		right = false;
		left = true;
		x -= primary_weapon.getWidth();
		if (collide()) {
			if (Client.networkWorld.battle.enemy instanceof NetworkCharacterBattle) {
				if (ID != ((NetworkCharacterBattle) Client.networkWorld.battle.enemy).ID) {
					Client.networkWorld.battle.enemy.decreaseHealth(primary_weapon);
				} else
					Client.networkWorld.battle.character.decreaseHealth(primary_weapon);
			} else
				Client.networkWorld.battle.enemy.decreaseHealth(primary_weapon);
		}
		x += primary_weapon.getWidth();

		setState(StateDynamicObject.FIGHTINGLEFT, dt);
		fighting = true;
		fightingLeft = true;
	}

	@Override
	public boolean collide() {
		if (Client.networkWorld.battle.enemy instanceof NetworkCharacterBattle) {
			if (ID != ((NetworkCharacterBattle) Client.networkWorld.battle.enemy).ID) {
				if (!((x > Client.networkWorld.battle.enemy.getX() + Client.networkWorld.battle.enemy.getWidth() / 2
						|| Client.networkWorld.battle.enemy.getX() > x + width / 2)
						|| (y > Client.networkWorld.battle.enemy.getY()
								+ Client.networkWorld.battle.enemy.getHeight() / 2
								|| Client.networkWorld.battle.enemy.getY() > y + height / 2)))
					return true;
			} else {
				if (!((x > Client.networkWorld.battle.character.getX()
						+ Client.networkWorld.battle.character.getWidth() / 2
						|| Client.networkWorld.battle.character.getX() > x + width / 2)
						|| (y > Client.networkWorld.battle.character.getY()
								+ Client.networkWorld.battle.character.getHeight() / 2
								|| Client.networkWorld.battle.character.getY() > y + height / 2)))
					return true;
			}
		} else {
			if (!((x > Client.networkWorld.battle.enemy.getX() + Client.networkWorld.battle.enemy.getWidth() / 2
					|| Client.networkWorld.battle.enemy.getX() > x + width / 2)
					|| (y > Client.networkWorld.battle.enemy.getY() + Client.networkWorld.battle.enemy.getHeight() / 2
							|| Client.networkWorld.battle.enemy.getY() > y + height / 2)))
				return true;
		}

		return false;

	}

	public void setState(StateDynamicObject state, float dt) {
		previousState = currentState;

		currentState = state;
		if (previousState == currentState && currentState != StateDynamicObject.STANDING)
			setStateTimer(getStateTimer() + dt);
		else
			setStateTimer(0);
		if (state == StateDynamicObject.JUMPING)
			setStateTimer(0);
	}

	public void launch() {
		Iterator<Bomb> itBomb = bag.bombe.iterator();
		while (itBomb.hasNext()) {
			Bomb ob = (Bomb) itBomb.next();
			if (!ob.launched) {
				ob.launched = true;
				bomb = ob;
				ob.launch(power, this);
				booleanLaunchBomb = true;
				if (Client.networkWorld.battle.enemy instanceof NetworkCharacterBattle)
					ob.id = String.valueOf(ID);
				else
					ob.id = "Player";

				break;
			}
		}
	}

	public void useItem(Item item) {
		if (item.getElement() == Element.POTION) {
			switch (item.getLevel()) {
			case FIRST:
				health += 15;
				if (health > 300)
					health = 300;
				break;
			case SECOND:
				health += 25;
				if (health > 300)
					health = 300;
				break;
			case THIRD:
				health += 25;
				if (health > 300)
					health = 300;
				break;
			default:
				System.out.println("potion non assegnata");
				break;
			}

		}
	}

}
