package it.slagyom;

import com.badlogic.gdx.Gdx;

import it.slagyom.src.World.Game;


public class ScreenManager {
	GameSlagyom gameSlagyom;
	public static enum State {
		MENU, NEWGAME, OPTIONMENU, MULTIPLAYER, PLAYING, BATTLE, PAUSE, WELCOME, BAG, SHOP
	};
	
	State currentState;
	private State previousState;
	
	static MenuScreen menuScreen;
	static NewCharacterScreen newCharacterScreen;
	static InitializerScreen initializerScreen;
	static OptionScreen optionScreen;
	static BattleScreen battlescreen;
	static PauseScreen pauseScreen;
	static PlayScreen playScreen;
	static BagScreen bagScreen;
	static ShopScreen shopScreen;
	static MultiplayerScreen multiplayerScreen;
	
	public ScreenManager(GameSlagyom gameSlagyom)
	{
		this.gameSlagyom = gameSlagyom;
		menuScreen = new MenuScreen(gameSlagyom);
		optionScreen = new OptionScreen(gameSlagyom);
		pauseScreen = new PauseScreen(gameSlagyom);
		multiplayerScreen = new MultiplayerScreen(gameSlagyom);
		currentState = State.MENU;
		swapScreen(State.MENU);
	}
	
	@SuppressWarnings("static-access")
	public void swapScreen(State newState) {
		setPreviousState(currentState);
		setCurrentState(newState);

		if (currentState == State.MENU) {
			gameSlagyom.musicManager.play("MAINMUSIC");
			gameSlagyom.setScreen(menuScreen);
			Gdx.input.setInputProcessor(menuScreen.stage);
		} else if (currentState == State.PLAYING) {
			gameSlagyom.musicManager.pause();
			gameSlagyom.musicManager.play("BACKGROUND");
			gameSlagyom.setScreen(getPlayScreen());
			menuScreen.menuMusic.stop();
			Game.world.semaphore.release();
			Gdx.input.setInputProcessor(null);
		} else if (currentState == State.MULTIPLAYER) {
			gameSlagyom.setScreen(multiplayerScreen);
			Gdx.input.setInputProcessor(multiplayerScreen.stage);
		} else if (currentState == State.OPTIONMENU) {
			gameSlagyom.setScreen(optionScreen);
			Gdx.input.setInputProcessor(optionScreen.stage);
		} else if (currentState == State.NEWGAME) {
			newCharacterScreen = new NewCharacterScreen(gameSlagyom);
			gameSlagyom.setScreen(newCharacterScreen);
			Gdx.input.setInputProcessor(newCharacterScreen.stage);
		} else if (currentState == State.WELCOME) {
			initializerScreen = new InitializerScreen(gameSlagyom);
			gameSlagyom.setScreen(initializerScreen);
			Gdx.input.setInputProcessor(initializerScreen.stage);
		} else if (currentState == State.BATTLE) {
			battlescreen = new BattleScreen(gameSlagyom, it.slagyom.src.World.Game.world.battle);
			gameSlagyom.setScreen(battlescreen);
		} else if (currentState == State.PAUSE) {
			gameSlagyom.musicManager.pause();
			gameSlagyom.musicManager.play("MAINMUSIC");
			gameSlagyom.setScreen(pauseScreen);
			Gdx.input.setInputProcessor(pauseScreen.stage);
		} else if (currentState == State.BAG) {
			bagScreen = new BagScreen(gameSlagyom);
			gameSlagyom.setScreen(bagScreen);
			Gdx.input.setInputProcessor(bagScreen.stage);
		} else if (currentState == State.SHOP) {
			shopScreen = new ShopScreen(gameSlagyom);
			gameSlagyom.setScreen(shopScreen);
			Gdx.input.setInputProcessor(shopScreen.stage);
		} 
	}

	public State getCurrentState() {
		return currentState;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}

	public State getPreviousState() {
		System.out.println(previousState);
		return previousState;
		
	}

	public void setPreviousState(State previousState) {
		this.previousState = previousState;
	}

	public static PlayScreen getPlayScreen() {
		return playScreen;
	}

	public static void setPlayScreen(PlayScreen playScreen) {
		ScreenManager.playScreen = playScreen;
	}
}
