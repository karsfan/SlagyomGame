package hud;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gameManager.GameSlagyom;
import multiplayer.Client;
import screens.MenuScreen;
import world.Game;

public class Hud {
	GameSlagyom gameSlagyom;

	public SpriteBatch spriteBatch;
	public Stage stage;
	private Viewport viewport;

	private Label coinsLabel;
	private Label nameLabel;
	private Label villageLabel;

	public String textDialog;
	public boolean showDialog;
	public Table textTable;

	public boolean isNight;
	public int dayTime;

	public Texture night;
	private Image splash;
	private Drawable hudBG;
	public float timer;

	@SuppressWarnings("static-access")
	public Hud(GameSlagyom gameSlagyom) {
		showDialog = true;
		textDialog = "";
		textTable = new Table();
		dayTime = 20;
		timer = 0;
		isNight = false;
		this.gameSlagyom = gameSlagyom;
		spriteBatch = gameSlagyom.batch;
		viewport = new FitViewport(1920, 1080);
		stage = new Stage(viewport, spriteBatch);
		hudBG = new TextureRegionDrawable(new TextureRegion(new Texture("res/hudBg.png")));
		night = new Texture("res/night.png");
		splash = new Image(night);

		Table table = new Table();
		table.top();
		table.setFillParent(true);

		if (!gameSlagyom.modalityMultiplayer) {
			nameLabel = new Label(Game.world.player.name, MenuScreen.skin);
			coinsLabel = new Label(String.format("%3d", Game.world.player.coins), MenuScreen.skin);
			villageLabel = new Label(String.format(Game.world.getMap().getNameVillage()), MenuScreen.skin);
		} else {
			nameLabel = new Label(Client.networkWorld.player.name, MenuScreen.skin);
			coinsLabel = new Label(String.format("%3d", Client.networkWorld.player.coins), MenuScreen.skin);
			villageLabel = new Label(String.format(Client.networkWorld.map.getNameVillage()), MenuScreen.skin);
		}

		table.add(nameLabel).expandX().pad(20);
		table.add(villageLabel).expandX().pad(20);
		table.add(coinsLabel).expandX().pad(20);
		table.row();

		splash.setPosition(0, 0);
		splash.addAction(Actions.alpha(0f));
		stage.addActor(splash);

		table.setBackground(hudBG);
		textTable.setX(854 - 236);
		textTable.setY(15);
		stage.addActor(table);
		stage.addActor(textTable);
	}

	@SuppressWarnings("static-access")
	public void update() {
		if (!gameSlagyom.modalityMultiplayer) {
			villageLabel.setText(String.format(Game.world.getMap().getNameVillage()));
			coinsLabel.setText(String.format("%03d", Game.world.player.coins));
		} else {
			villageLabel.setText(String.format(Client.networkWorld.map.getNameVillage()));
			coinsLabel.setText(String.format("%03d", Client.networkWorld.player.coins));
		}
	}

	public void setDialogText(String text) {
		showDialog = true;
		textDialog = text;
	}

	public void updateNight(float delta) {
		stage.act(delta);

		if (timer <= dayTime) {
			timer += delta;
			if (!isNight) {
				splash.addAction(Actions.fadeIn(dayTime));
			} else {
				splash.addAction(Actions.fadeOut(dayTime)); // 120
			}
		} else {
			isNight = !isNight;
			timer = 0;
		}
	}
}