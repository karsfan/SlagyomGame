package gameManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controllers;

import screens.BagScreen;
import screens.BattleScreen;
import screens.InitializerScreen;
import screens.MenuScreen;
import screens.MultiplayerScreen;
import screens.NewCharacterScreen;
import screens.OptionScreen;
import screens.PauseScreen;
import screens.PlayScreen;
import screens.ShopScreen;

public class ScreenManager {
	GameSlagyom gameSlagyom;

	public static enum State {
		MENU, NEWGAME, OPTIONMENU, MULTIPLAYERMENU, PLAYING, BATTLE, PAUSE, WELCOME, BAG, SHOP,
	};

	public State currentState;
	public State previousState;

	public MenuScreen menuScreen;
	public NewCharacterScreen newCharacterScreen;
	public InitializerScreen initializerScreen;
	public OptionScreen optionScreen;
	public BattleScreen battlescreen;
	public PauseScreen pauseScreen;
	public PlayScreen playScreen;
	public BagScreen bagScreen;
	public ShopScreen shopScreen;
	public MultiplayerScreen multiplayerScreen;

	public ScreenManager(GameSlagyom gameSlagyom) {
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
		if (currentState == State.PLAYING || currentState == State.BATTLE )
			setPreviousState(currentState);
		setCurrentState(newState);

		if (currentState == State.PLAYING || currentState == State.BATTLE )
			Gdx.graphics.setCursor(Gdx.graphics.newCursor(gameSlagyom.loadingImage.noCursor, 0, 0));
		else
			Gdx.graphics.setCursor(Gdx.graphics.newCursor(gameSlagyom.loadingImage.cursor, 0, 0));

		if (currentState == State.MENU) {
			// LoadingMusic.mainMusic.play();
			gameSlagyom.setScreen(menuScreen);
			Gdx.input.setInputProcessor(menuScreen.stage);
			gameSlagyom.loadingMusic.backgroundSound.stop();
			gameSlagyom.loadingMusic.battleMusic.stop();
			Controllers.clearListeners();
			Controllers.addListener(menuScreen);
		} else if (currentState == State.PLAYING) {
			gameSlagyom.setScreen(getPlayScreen());
			Controllers.clearListeners();
			Controllers.addListener(playScreen);
			// STOPPING MENU MUSIC AND PLAYING GAME MUSIC
			gameSlagyom.loadingMusic.battleMusic.pause();
			menuScreen.menuMusic.stop();
			Gdx.input.setInputProcessor(null);
		} else if (currentState == State.MULTIPLAYERMENU) {
			gameSlagyom.setScreen(multiplayerScreen);
			Gdx.input.setInputProcessor(multiplayerScreen.stage);
			Controllers.clearListeners();
			Controllers.addListener(multiplayerScreen);
		} else if (currentState == State.OPTIONMENU) {
			gameSlagyom.setScreen(optionScreen);
			Gdx.input.setInputProcessor(optionScreen.stage);
			Controllers.clearListeners();
			Controllers.addListener(optionScreen);
		} else if (currentState == State.NEWGAME) {
			newCharacterScreen = new NewCharacterScreen(gameSlagyom);
			gameSlagyom.setScreen(newCharacterScreen);
			Gdx.input.setInputProcessor(newCharacterScreen.stage);
			Controllers.clearListeners();
			Controllers.addListener(newCharacterScreen);
		} else if (currentState == State.WELCOME) {
			gameSlagyom.prefs = Gdx.app.getPreferences(newCharacterScreen.charName);
			initializerScreen = new InitializerScreen(gameSlagyom);
			gameSlagyom.setScreen(initializerScreen);
			Gdx.input.setInputProcessor(initializerScreen.stage);
			Controllers.clearListeners();
			Controllers.addListener(initializerScreen);
		} else if (currentState == State.BATTLE) {
			gameSlagyom.setScreen(battlescreen);
			gameSlagyom.loadingMusic.backgroundSound.pause();
			gameSlagyom.loadingMusic.battleMusic.setVolume((float) 0.07);
			gameSlagyom.loadingMusic.battleMusic.play();
			Gdx.input.setInputProcessor(null);
		} else if (currentState == State.PAUSE) {
			gameSlagyom.loadingMusic.pause();
			gameSlagyom.setScreen(pauseScreen);
			Gdx.input.setInputProcessor(pauseScreen.stage);
		} else if (currentState == State.BAG) {
			bagScreen = new BagScreen(gameSlagyom);
			gameSlagyom.setScreen(bagScreen);
			Gdx.input.setInputProcessor(bagScreen.stage);
			Controllers.addListener(new MenuControllerListener(bagScreen.potionsTable));
			Controllers.addListener(new MenuControllerListener(bagScreen.weaponsTable));
			Controllers.addListener(new MenuControllerListener(bagScreen.parchmentsTable));
			Controllers.addListener(new MenuControllerListener(bagScreen.bombsTable));
			Controllers.addListener(new MenuControllerListener(bagScreen.optionsTable));
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
		return previousState;

	}

	public void setPreviousState(State previousState) {
		this.previousState = previousState;
	}

	public PlayScreen getPlayScreen() {
		return playScreen;
	}

	public void setPlayScreen(PlayScreen playScreen) {
		this.playScreen = playScreen;
	}
}
