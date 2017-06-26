package it.slagyom;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import it.slagyom.ScreenManager.State;

public class GameSlagyom extends Game {

	ScreenManager screenManager;
	static MusicManager musicManager;
	public static Preferences prefs;
	SpriteBatch batch;

	@Override
	public void create() {
		new LoadingMusic();
		musicManager = new MusicManager();
		batch = new SpriteBatch();

		screenManager = new ScreenManager(this);
		prefs = Gdx.app.getPreferences("My saved game");
	}

	public static MusicManager getMusicManager() {
		return musicManager;
	}

	public static void saveGame() {
		prefs.putString("map", it.slagyom.src.World.Game.world.getMap().getMapPath());
		prefs.putString("name", it.slagyom.src.World.Game.player.name);
		prefs.putFloat("xCharPosition", it.slagyom.src.World.Game.player.x);
		prefs.putFloat("yCharPosition", it.slagyom.src.World.Game.player.y);
		prefs.putFloat("health", it.slagyom.src.World.Game.player.health);
		prefs.putFloat("power", it.slagyom.src.World.Game.player.power);
		prefs.putInteger("coins", it.slagyom.src.World.Game.player.coins);

		prefs.flush();
	}

	@SuppressWarnings({ "deprecation", "static-access" })
	public void loadGame() {
		prefs = Gdx.app.getPreferences("My saved game");

		if (screenManager.currentState == State.PAUSE)
			it.slagyom.src.World.Game.world.getThread().stop();
		screenManager.playScreen = new PlayScreen(this, prefs.getString("map"), prefs.getString("name"));
		screenManager.swapScreen(State.PLAYING);
		//screenManager.setPlayScreen(new PlayScreen(this, prefs.getString("map"), prefs.getString("name")));
		it.slagyom.src.World.Game.player.x = prefs.getFloat("xCharPosition");
		it.slagyom.src.World.Game.player.y = prefs.getFloat("yCharPosition");
		it.slagyom.src.World.Game.player.health = prefs.getFloat("health");
		it.slagyom.src.World.Game.player.power = prefs.getFloat("power");
		it.slagyom.src.World.Game.player.coins = prefs.getInteger("coins");

	}

	@Override
	public void render() {
		super.render();
	}

}
