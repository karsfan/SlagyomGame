package gameManager;

import com.badlogic.gdx.Gdx;

import multiplayer.NetworkPlayScreen;
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
import world.Game;

public class ScreenManager {
	GameSlagyom gameSlagyom;

	public static enum State {
		MENU, NEWGAME, OPTIONMENU, MULTIPLAYERMENU, MULTIPLAYERGAME, PLAYING, BATTLE, PAUSE, WELCOME, BAG, SHOP, 
	};

	public State currentState;
	private State previousState;

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
	public NetworkPlayScreen networkPlayScreen;

	public ScreenManager(GameSlagyom gameSlagyom) {
		this.gameSlagyom = gameSlagyom;
		menuScreen = new MenuScreen(gameSlagyom);
		optionScreen = new OptionScreen(gameSlagyom);
		pauseScreen = new PauseScreen(gameSlagyom);
		multiplayerScreen = new MultiplayerScreen(gameSlagyom);
		currentState = State.MENU;
		swapScreen(State.MENU);
	}

	public void swapScreen(State newState) {
		if (currentState == State.PLAYING || currentState == State.BATTLE || currentState == State.MULTIPLAYERGAME)
			setPreviousState(currentState);
		setCurrentState(newState);

		if (currentState == State.PLAYING || currentState == State.BATTLE)
			Gdx.graphics.setCursor(Gdx.graphics.newCursor(LoadingImage.noCursor, 0, 0));
		else
			Gdx.graphics.setCursor(Gdx.graphics.newCursor(LoadingImage.cursor, 0, 0));

		if (currentState == State.MENU) {
			// LoadingMusic.mainMusic.play();
			gameSlagyom.setScreen(menuScreen);
			Gdx.input.setInputProcessor(menuScreen.stage);

		} else if (currentState == State.PLAYING) {
			LoadingMusic.pause();
			gameSlagyom.setScreen(getPlayScreen());

			// STOPPING MENU MUSIC AND PLAYING GAME MUSIC
			menuScreen.menuMusic.stop();
			LoadingMusic.backgroundSound.loop(10.0f);

			Game.world.semaphore.release();
			Gdx.input.setInputProcessor(null);
		} else if (currentState == State.MULTIPLAYERMENU) {
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
			System.out.println(gameSlagyom.screenManager.newCharacterScreen.charName);
			gameSlagyom.prefs = Gdx.app.getPreferences(newCharacterScreen.charName);
			initializerScreen = new InitializerScreen(gameSlagyom);
			gameSlagyom.setScreen(initializerScreen);
			Gdx.input.setInputProcessor(initializerScreen.stage);

		} else if (currentState == State.BATTLE) {
			// battlescreen = new BattleScreen(gameSlagyom,
			// it.slagyom.src.World.Game.world.battle);
			gameSlagyom.setScreen(battlescreen);
			Gdx.input.setInputProcessor(null);
		} else if (currentState == State.PAUSE) {
			LoadingMusic.pause();
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
		else if (currentState == State.MULTIPLAYERGAME) {
			gameSlagyom.setScreen(networkPlayScreen);
			Gdx.input.setInputProcessor(null);
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
