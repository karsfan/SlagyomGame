package it.slagyom;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import it.slagyom.ScreenManager.State;

public class GameSlagyom extends Game {

	ScreenManager screenManager;
	LoadingMusic musicLoader;
	public Preferences prefs;
	SpriteBatch batch;

	@Override
	public void create() {
		new LoadingMusic();
		batch = new SpriteBatch();

		screenManager = new ScreenManager(this);
		// prefs = Gdx.app.getPreferences("My saved game");
	}

	public void saveGame() {
		prefs.putString("map", it.slagyom.src.World.Game.world.getMap().getMapPath());
		prefs.putString("name", it.slagyom.src.World.Game.player.name);
		prefs.putFloat("xCharPosition", it.slagyom.src.World.Game.player.x);
		prefs.putFloat("yCharPosition", it.slagyom.src.World.Game.player.y);
		prefs.putFloat("health", it.slagyom.src.World.Game.player.health);
		prefs.putFloat("power", it.slagyom.src.World.Game.player.power);
		prefs.putInteger("coins", it.slagyom.src.World.Game.player.coins);

		prefs.flush();
	}

	@SuppressWarnings("static-access")
	public void loadGame(String path) {
		prefs = Gdx.app.getPreferences(path);
		if (prefs.getString("name") != "") {
			screenManager.playScreen = new PlayScreen(this, prefs.getString("map"), prefs.getString("name"));
			it.slagyom.src.World.Game.player.x = prefs.getFloat("xCharPosition");
			it.slagyom.src.World.Game.player.y = prefs.getFloat("yCharPosition");
			it.slagyom.src.World.Game.player.health = prefs.getFloat("health");
			it.slagyom.src.World.Game.player.power = prefs.getFloat("power");
			it.slagyom.src.World.Game.player.coins = prefs.getInteger("coins");
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
