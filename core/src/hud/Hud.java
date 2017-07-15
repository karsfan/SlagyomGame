package hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import gameManager.GameSlagyom;
import gameManager.LoadingImage;
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

	public String textDialog = "";
	public boolean showDialog = true;
	public Table textTable = new Table();
	static TextureAtlas atlasBar;
	public static Skin skinBar;
	ProgressBar barPlayer;
	Integer health;

	public boolean isNight = false;
	public int dayTime = 20;

	public static Texture night = new Texture("res/night.png");
	Image light = new Image(LoadingImage.lightImage);
	private Image splash = new Image(night);
	private Image coin = new Image(LoadingImage.coinImage);
	public float timer = 0;

	@SuppressWarnings("static-access")
	public Hud(GameSlagyom gameSlagyom) {
		atlasBar = new TextureAtlas("menu/golden-spiral/golden-ui-skin.atlas");
		skinBar = new Skin(Gdx.files.internal("menu/golden-spiral/golden-ui-skin.json"), atlasBar);
		this.gameSlagyom = gameSlagyom;
		spriteBatch = gameSlagyom.batch;
		viewport = new FitViewport(1920, 1080);
		stage = new Stage(viewport, spriteBatch);

		barPlayer = new ProgressBar(0.1f, 1f, 0.1f, false, skinBar);
		barPlayer.setBounds(100, 500, 40, 15);
		Table table = new Table();
		table.top(); // la allinea sopra al centro
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
		table.row(); // nuova colonna

		Drawable hudBG = new TextureRegionDrawable(new TextureRegion(new Texture("res/hudBg.png")));

		splash.setPosition(0, 0);

		// splash.addAction(Actions.alpha(0f));
		// splash.addAction(Actions.fadeIn(5f)); //120

		// coin.setPosition(15, 15);
		coin.setScale(2f);
		splash.addAction(Actions.alpha(0f));

		stage.addActor(splash);
		stage.addActor(coin);

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
		// if (timer <= dayTime) {
		// timer += delta;
		// isNight = false;
		// splash.addAction(Actions.alpha(0f));
		// splash.addAction(Actions.fadeIn(5f));
		// }
		//
		// if (timer >= dayTime) {
		// isNight = true;
		// splash.addAction(Actions.fadeOut(5f)); //120
		// }

		if (timer <= dayTime) {
			timer += delta;
			isNight = isNight;
			if (!isNight) {
				splash.addAction(Actions.fadeIn(dayTime));
			} else {
				splash.addAction(Actions.fadeOut(dayTime)); // 120
			}
		}
		else {
			isNight = !isNight;
			timer = 0;
		}
			
		System.out.println(timer);
	}

	public void drawAnimation(float x, float y) {
		coin.setPosition(x, y);
		coin.addAction(Actions.moveTo(1530, 1037, 0.5f));
	}

}