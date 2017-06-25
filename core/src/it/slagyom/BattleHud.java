package it.slagyom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import it.slagyom.src.World.Game;

public class BattleHud {
	
	public SpriteBatch spriteBatch;
	public Stage stage;
	private Viewport viewport;
	
	private Label nameCharacterLabel;
	private Label nameEnemyLabel;
	
	Integer healthCharacter;
	Integer healthEnemy;
	static TextureAtlas atlasBar;
	public static Skin skinBar;
	ProgressBar barPlayer;
	ProgressBar barEnemy;
	
	public BattleHud(SpriteBatch batch, Viewport viewport) {
		
		spriteBatch = batch;
		//viewport = new ExtendViewport(854, 480, new OrthographicCamera());
		//this.viewport = viewport;
		this.viewport = new FitViewport(1200,1200, new OrthographicCamera());
		stage = new Stage(this.viewport, spriteBatch);

		healthCharacter = (int) Game.world.battle.character.getHealth();
		healthEnemy = (int) Game.world.battle.enemy.getHealth();

		atlasBar = new TextureAtlas("menu/glassy/glassy-ui.atlas");
		skinBar = new Skin(Gdx.files.internal("menu/glassy/glassy-ui.json"), atlasBar);

		barPlayer = new ProgressBar(0.1f, healthCharacter, 0.1f, false, skinBar);
		barEnemy = new ProgressBar(0.1f,healthEnemy, 0.1f,false,skinBar);
		
		barPlayer.setBounds(this.viewport.getWorldWidth()/13, this.viewport.getWorldHeight()/1.1f, healthCharacter, 15);
		barEnemy.setBounds(this.viewport.getWorldWidth()/1.2f, this.viewport.getWorldHeight()/1.1f, healthEnemy,15);
		
		stage.addActor(barEnemy);
		stage.addActor(barPlayer);
		
		
		Table table = new Table();
		table.top(); // la allinea sopra al centro
		table.setFillParent(true);
	
		nameCharacterLabel = new Label(Game.player.name, MenuScreen.skin);
		
	
		table.add(nameCharacterLabel).expandX().pad(20);
		nameEnemyLabel = new Label(Game.world.battle.enemy.getName(), MenuScreen.skin);
	
		table.add(nameEnemyLabel).expandX().pad(20);
		table.row(); // nuova colonna

		stage.addActor(table);

	}

	public void update(float dt){
		
		healthCharacter = (int) Game.world.battle.character.getHealth();
		barPlayer.setValue(healthCharacter.intValue());
		healthEnemy = (int) Game.world.battle.enemy.getHealth();
		barEnemy.setValue(healthEnemy.intValue());
	}
}
