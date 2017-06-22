package it.slagyom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import it.slagyom.src.World.Game;

public class Hud {

	public SpriteBatch spriteBatch;
	public Stage stage;
	private Viewport viewport;

	private Label healthLabel;
	private Label nameLabel;
	private Label villageLabel;

	public String textDialog = "";
	public boolean showDialog = true;
	public Table textTable = new Table();
	Integer health;

	
	ProgressBarStyle barStyle;
	ProgressBar progressBar;

	public Hud(SpriteBatch sb) {
		spriteBatch = sb;
		viewport = new FitViewport(1920, 1080);
		stage = new Stage(viewport, sb);

		Table table = new Table();
		table.top(); // la allinea sopra al centro
		table.setFillParent(true);

		nameLabel = new Label(Game.player.name, MenuScreen.skin);
		healthLabel = new Label(String.format("%3d", Game.player.coins), MenuScreen.skin);
		villageLabel = new Label(String.format(Game.world.getMap().getNameVillage()), MenuScreen.skin);

		table.add(nameLabel).expandX().pad(20);
		table.add(villageLabel).expandX().pad(20);
		table.add(healthLabel).expandX().pad(20);
		table.row(); // nuova colonna

		Drawable hudBG = new TextureRegionDrawable(new TextureRegion(new Texture("res/hudBg.png")));

		table.setBackground(hudBG);
		textTable.setX(Gdx.graphics.getWidth() - 236);
		textTable.setY(15);
		/*TextureRegionDrawable tmp = new TextureRegionDrawable(
				new TextureRegion(new Texture(Gdx.files.internal("res/CustomProgressBar.png"))));
		
		 * barStyle = new ProgressBarStyle(MenuScreen.skin.newDrawable("white",
		 * Color.DARK_GRAY), tmp); barStyle.knobBefore = barStyle.knob;
		 * progressBar = new ProgressBar(0, 10, 0.5f, false, barStyle);
		 * progressBar.setPosition(10, 10); progressBar.setSize(290,
		 * progressBar.getPrefHeight()); progressBar.setAnimateDuration(2);
		 * stage.addActor(progressBar);
		 */
		
		stage.addActor(table);
		stage.addActor(textTable);
	}

	public void update() {
		
		villageLabel.setText(String.format(Game.world.getMap().getNameVillage()));
		healthLabel.setText(String.format("%03d", Game.player.coins));
	}

	

	public void setDialogText(String text) {
		showDialog = true;
		textDialog = text;
	}

}