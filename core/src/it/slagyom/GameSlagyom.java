package it.slagyom;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameSlagyom extends Game {

	public static enum State {
		MENU, NEWGAME, CONTINUEGAME, OPTIONMENU, PLAYING, BATTLE, PAUSE, WELCOME, BAG, SHOP
	};

	private static State currentState;

	static MenuScreen menuScreen;
	static NewCharacterScreen newCharacterScreen;
	static InitializerScreen initializerScreen;
	static OptionScreen optionScreen;
	static BattleScreen battlescreen;
	static PauseScreen pauseScreen;
	static PlayScreen playScreen;
	static BagScreen bagScreen;
	static ShopScreen shopScreen;


	public static Preferences prefs;
	SpriteBatch batch;
	
	public GameSlagyom() {
		
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
	
		menuScreen = new MenuScreen(this);
		optionScreen = new OptionScreen(this);
		currentState = State.MENU;
		pauseScreen = new PauseScreen(this);
		setScreen(menuScreen);

		prefs = Gdx.app.getPreferences("My saved game");

	}

	public static void saveGame() {
		prefs.putString("map", it.slagyom.src.World.Game.world.getMap().getMapPath());
		prefs.putString("name", it.slagyom.src.World.Game.character.name);
		prefs.putFloat("xCharPosition", it.slagyom.src.World.Game.character.x);
		prefs.putFloat("yCharPosition", it.slagyom.src.World.Game.character.y);
		prefs.putFloat("health", it.slagyom.src.World.Game.character.health);
		prefs.putFloat("power", it.slagyom.src.World.Game.character.power);
		prefs.putInteger("coins", it.slagyom.src.World.Game.character.coins);

		prefs.flush();
	}

	@SuppressWarnings({ "deprecation" })
	public void loadGame() {
		prefs = Gdx.app.getPreferences("My saved game");

		if (currentState == State.PAUSE)
			it.slagyom.src.World.Game.world.getThread().stop();

		playScreen = new PlayScreen(this, prefs.getString("map"), prefs.getString("name"));

		it.slagyom.src.World.Game.character.x = prefs.getFloat("xCharPosition");
		it.slagyom.src.World.Game.character.y = prefs.getFloat("yCharPosition");
		it.slagyom.src.World.Game.character.health = prefs.getFloat("health");
		it.slagyom.src.World.Game.character.power = prefs.getFloat("power");
		it.slagyom.src.World.Game.character.coins = prefs.getInteger("coins");

	}

	@Override
	public void render() {
		super.render();
	}

	public static void setState(State newState) {
		currentState = newState;
	}

	public void swapScreen(State newState) {

		setState(newState);

		if (currentState == State.MENU) {
			setScreen(menuScreen);
			Gdx.input.setInputProcessor(menuScreen.stage);
		} else if (currentState == State.PLAYING) {
			setScreen(playScreen);
			menuScreen.music.stop();
			Gdx.input.setInputProcessor(null);
		} else if (currentState == State.OPTIONMENU) {
			setScreen(optionScreen);
			Gdx.input.setInputProcessor(optionScreen.stage);
		} else if (currentState == State.NEWGAME) {
			newCharacterScreen = new NewCharacterScreen(this);
			setScreen(newCharacterScreen);
			Gdx.input.setInputProcessor(newCharacterScreen.stage);
		} else if (currentState == State.WELCOME) {
			initializerScreen = new InitializerScreen(this);
			setScreen(initializerScreen);
			Gdx.input.setInputProcessor(initializerScreen.stage);
		} else if (currentState == State.BATTLE) {
			battlescreen = new BattleScreen(this, it.slagyom.src.World.Game.world.battle);
			setScreen(battlescreen);
		} else if (currentState == State.PAUSE) {
			setScreen(pauseScreen);
			try {
				it.slagyom.src.World.Game.world.semaphore.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Gdx.input.setInputProcessor(pauseScreen.stage);
		} else if (currentState == State.BAG) {
			bagScreen = new BagScreen(this);
			setScreen(bagScreen);
			Gdx.input.setInputProcessor(bagScreen.stage);
		} else if (currentState == State.SHOP) {
			shopScreen = new ShopScreen(this);
			setScreen(shopScreen);
			Gdx.input.setInputProcessor(shopScreen.stage);
		} else if (currentState == State.CONTINUEGAME) {
			setScreen(playScreen);
			Gdx.input.setInputProcessor(null);
		}
	}
}
