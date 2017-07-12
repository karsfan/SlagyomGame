package gameManager;

import java.util.Iterator;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import character.Bomb;
import character.DynamicObjects;
import character.Man;
import character.Weapon;
import character.Weapon.Type;
import character.Woman;
import gameManager.ScreenManager.State;
import screens.PlayScreen;
import staticObjects.Item;
import staticObjects.Item.Level;
import staticObjects.StaticObject.Element;

public class GameSlagyom extends Game {

	public ScreenManager screenManager;
	LoadingMusic musicLoader;
	public Preferences prefs;
	public SpriteBatch batch;
	public static boolean Modality;
	@Override
	public void create() {
		new LoadingMusic();
		batch = new SpriteBatch();
		Modality = false;
		screenManager = new ScreenManager(this);
	}

	public void saveGame() {
		prefs.clear();
		Iterator<Item> item = world.Game.world.getListItems().iterator();
		int cont = 0;
		prefs.putInteger("NumItem", world.Game.world.getListItems().size());
		while (item.hasNext()) {
			Item it = item.next();
			prefs.putString("Item " + cont, it.getElement().toString());
			prefs.putString("LevelItem " + cont, it.getLevel().toString());
			prefs.putFloat("X " + cont, it.getX());
			prefs.putFloat("Y " + cont, it.getY());
			prefs.flush();
			cont++;
		}

		cont = 0;
		Iterator<DynamicObjects> dynamicObject = world.Game.world.getListDynamicObjects().iterator();
		prefs.putInteger("NumDynamicObjects ", world.Game.world.getListDynamicObjects().size());
		while (dynamicObject.hasNext()) {
			Object dynamicOb = dynamicObject.next();
			if (!dynamicOb.getClass().getSimpleName().equals("Player")) {

				prefs.putString("DynamicObject " + cont, dynamicOb.getClass().getSimpleName());
				if (dynamicOb.getClass().getSimpleName().equals("Woman"))
					prefs.putString("WomanType " + cont, ((Woman) dynamicOb).getType().toString());
				prefs.putFloat("X " + cont, ((DynamicObjects) dynamicOb).getX());
				prefs.putFloat("Y " + cont, ((DynamicObjects) dynamicOb).getY());
				prefs.flush();
				cont++;
			}
		}

		prefs.putString("map", world.Game.world.getMap().getMapPath());
		prefs.putString("name", world.Game.world.player.name);
		prefs.putBoolean("gender", world.Game.world.player.male);
		prefs.putInteger("POTION1", world.Game.world.player.bag.getNumberOf(Element.POTION, Level.FIRST));
		prefs.putInteger("POTION2", world.Game.world.player.bag.getNumberOf(Element.POTION, Level.SECOND));
		prefs.putInteger("POTION3", world.Game.world.player.bag.getNumberOf(Element.POTION, Level.THIRD));
		prefs.putInteger("PARCHMENT1", world.Game.world.player.bag.getNumberOf(Element.PARCHMENT, Level.FIRST));
		prefs.putInteger("PARCHMENT2", world.Game.world.player.bag.getNumberOf(Element.PARCHMENT, Level.SECOND));
		prefs.putInteger("BOMB1", world.Game.world.player.bag.getNumberOfBomb(character.Weapon.Level.lev1));
		prefs.putInteger("BOMB2", world.Game.world.player.bag.getNumberOfBomb(character.Weapon.Level.lev2));
		prefs.putInteger("BOMB3", world.Game.world.player.bag.getNumberOfBomb(character.Weapon.Level.lev3));
		prefs.putString("PrimaryWeapon", world.Game.world.player.primary_weapon.getType().toString());
		prefs.putString("PrimaryWeaponLevel", world.Game.world.player.primary_weapon.level.toString());
		if (world.Game.world.player.bag.secondary_weapon != null) {
			prefs.putString("SecondaryWeapon", world.Game.world.player.bag.secondary_weapon.getType().toString());
			prefs.putString("SecondaryWeaponLevel", world.Game.world.player.bag.secondary_weapon.level.toString());
		}
		prefs.putInteger("xCharPosition", world.Game.world.player.x);
		prefs.putInteger("yCharPosition", world.Game.world.player.y);
		prefs.putInteger("health", world.Game.world.player.health);
		prefs.putInteger("coins", world.Game.world.player.coins);
		
		prefs.flush();
	}

	@SuppressWarnings("static-access")
	public void loadGame(String path) {
		prefs = Gdx.app.getPreferences(path);
		if (prefs.getString("name") != "") {
<<<<<<< HEAD
			screenManager.playScreen = new PlayScreen(this, prefs.getString("map"), prefs.getString("name"));
			world.Game.world.player.x = prefs.getInteger("xCharPosition");
			world.Game.world.player.y = prefs.getInteger("yCharPosition");
=======
			screenManager.playScreen = new PlayScreen(this, prefs.getString("map"), prefs.getString("name"), prefs.getBoolean("gender"));
			world.Game.world.player.x = prefs.getFloat("xCharPosition");
			world.Game.world.player.y = prefs.getFloat("yCharPosition");
>>>>>>> d4aec36ceefe7d4aa175a9cbe0ea152f252fc681
			world.Game.world.player.health = prefs.getInteger("health");
			world.Game.world.player.coins = prefs.getInteger("coins");
			
			for (int i = 0; i < prefs.getInteger("POTION1"); i++) {
				Item potion = new Item(Element.POTION, Level.FIRST);
				world.Game.world.player.bag.add(potion);
			}

			for (int i = 0; i < prefs.getInteger("POTION2"); i++) {
				Item potion = new Item(Element.POTION, Level.SECOND);
				world.Game.world.player.bag.add(potion);
			}

			for (int i = 0; i < prefs.getInteger("POTION3"); i++) {
				Item potion = new Item(Element.POTION, Level.THIRD);
				world.Game.world.player.bag.add(potion);
			}

			for (int i = 0; i < prefs.getInteger("PARCHMENT1"); i++) {
				Item potion = new Item(Element.PARCHMENT, Level.FIRST);
				world.Game.world.player.bag.add(potion);
			}

			for (int i = 0; i < prefs.getInteger("PARCHMENT2"); i++) {
				Item potion = new Item(Element.PARCHMENT, Level.FIRST);
				world.Game.world.player.bag.add(potion);
			}

			for (int i = 0; i < prefs.getInteger("BOMB1"); i++) {
				Bomb bomb = new Bomb(character.Weapon.Level.lev1, Type.Bomba);
				world.Game.world.player.bag.add(bomb);
			}

			for (int i = 0; i < prefs.getInteger("BOMB2"); i++) {
				Bomb bomb = new Bomb(character.Weapon.Level.lev2, Type.Bomba);
				world.Game.world.player.bag.add(bomb);
			}

			for (int i = 0; i < prefs.getInteger("BOMB3"); i++) {
				Bomb bomb = new Bomb(character.Weapon.Level.lev3, Type.Bomba);
				world.Game.world.player.bag.add(bomb);
			}
			world.Game.world.player.primary_weapon = new Weapon(prefs.getString("PrimaryWeapon"),
					prefs.getString("PrimaryWeaponLevel"));
			if (prefs.getString("SecondaryWeapon") != null){
				world.Game.world.player.bag.secondary_weapon = new Weapon(prefs.getString("SecondaryWeapon"),
						prefs.getString("SecondaryWeaponLevel"));
			}
			
			int numItem = prefs.getInteger("NumItem");
			numItem--;
			while (numItem >= 0) {
				Item item = new Item(prefs.getFloat("X " + numItem), prefs.getFloat("Y " + numItem),
						prefs.getString("Item " + numItem), prefs.getString("LevelItem " + numItem));
				world.Game.world.getListItems().add(item);
				numItem--;
			}

			int numDynamicObjects = prefs.getInteger("NumDynamicObjects ");
			numDynamicObjects--;
			while (numDynamicObjects >= 0) {
				DynamicObjects add;
				if (prefs.getString("DynamicObject " + numDynamicObjects).equals("Man")) {
					add = new Man(prefs.getInteger("X " + numDynamicObjects), prefs.getInteger("Y " + numDynamicObjects));
					world.Game.world.getListDynamicObjects().add(add);
				} else if (prefs.getString("DynamicObject " + numDynamicObjects).equals("Woman")) {
					add = new Woman(prefs.getInteger("X " + numDynamicObjects), prefs.getInteger("Y " + numDynamicObjects),
							prefs.getString("WomanType " + numDynamicObjects));
					world.Game.world.getListDynamicObjects().add(add);
				}
				numDynamicObjects--;
			}
			screenManager.swapScreen(State.PLAYING);
			screenManager.playScreen.hud.textTable.clear();
			screenManager.playScreen.hud.textDialog = "Game loaded";
		}
	}

	@Override
	public void render() {
		super.render();
	}

}
