package gameManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import gameManager.ScreenManager.State;
import screens.PlayScreen;

public class GameSlagyom extends Game {

	public ScreenManager screenManager;
	LoadingMusic musicLoader;
	public Preferences prefs;
	public SpriteBatch batch;

	@Override
	public void create() {
		new LoadingMusic();
		batch = new SpriteBatch();

		screenManager = new ScreenManager(this);
		// prefs = Gdx.app.getPreferences("My saved game");
	}

	public void saveGame() {
		prefs.putString("map", world.Game.world.getMap().getMapPath());
		prefs.putString("name", world.Game.player.name);
		prefs.putFloat("xCharPosition", world.Game.player.x);
		prefs.putFloat("yCharPosition", world.Game.player.y);
		prefs.putFloat("health", world.Game.player.health);
		prefs.putFloat("power", world.Game.player.power);
		prefs.putInteger("coins", world.Game.player.coins);

		prefs.flush();
	}

	@SuppressWarnings("static-access")
	public void loadGame(String path) {
		prefs = Gdx.app.getPreferences(path);
		if (prefs.getString("name") != "") {
			screenManager.playScreen = new PlayScreen(this, prefs.getString("map"), prefs.getString("name"));
			world.Game.player.x = prefs.getFloat("xCharPosition");
			world.Game.player.y = prefs.getFloat("yCharPosition");
			world.Game.player.health = prefs.getFloat("health");
			world.Game.player.power = prefs.getFloat("power");
			world.Game.player.coins = prefs.getInteger("coins");
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
