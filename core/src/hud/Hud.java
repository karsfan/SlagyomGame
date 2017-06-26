package hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import it.slagyom.MenuScreen;
import it.slagyom.src.World.Game;

public class Hud {

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

	public Hud(SpriteBatch sb) {
		atlasBar = new TextureAtlas("menu/golden-spiral/golden-ui-skin.atlas");
		skinBar = new Skin(Gdx.files.internal("menu/golden-spiral/golden-ui-skin.json"), atlasBar);
		spriteBatch = sb;
		viewport = new FitViewport(1920, 1080);
		stage = new Stage(viewport, sb);
		
		barPlayer = new ProgressBar(0.1f, 1f, 0.1f, false, skinBar);
		barPlayer.setBounds(100, 500, 40, 15);
		//barPlayer.setPosition(100, 500);
		//barEnemy.setPosition(300, 500);
		Table table = new Table();
		table.top(); // la allinea sopra al centro
		table.setFillParent(true);

		nameLabel = new Label(Game.player.name, MenuScreen.skin);
		coinsLabel = new Label(String.format("%3d", Game.player.coins), MenuScreen.skin);
		villageLabel = new Label(String.format(Game.world.getMap().getNameVillage()), MenuScreen.skin);

		table.add(nameLabel).expandX().pad(20);
		table.add(villageLabel).expandX().pad(20);
		table.add(coinsLabel).expandX().pad(20);
		table.row(); // nuova colonna

		Drawable hudBG = new TextureRegionDrawable(new TextureRegion(new Texture("res/hudBg.png")));

		table.setBackground(hudBG);
		textTable.setX(Gdx.graphics.getWidth() - 236);
		textTable.setY(15);

		stage.addActor(table);
		stage.addActor(textTable);
	}

	public void update() {
		villageLabel.setText(String.format(Game.world.getMap().getNameVillage()));	
		coinsLabel.setText(String.format("%03d", Game.player.coins));
	}

	public void setDialogText(String text) {
		showDialog = true;
		textDialog = text;
	}

}