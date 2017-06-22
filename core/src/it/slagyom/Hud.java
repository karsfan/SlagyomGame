package it.slagyom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
		
		stage.addActor(table);
		stage.addActor(textTable);
	}
	public void update(){
		villageLabel.setText(String.format(Game.world.getMap().getNameVillage()));
		healthLabel.setText(String.format("%03d", Game.player.coins));
	}
	public void setDialogText (String text) { 
		showDialog = true;
		textDialog = text;
	}

}