package it.slagyom;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import it.slagyom.src.World.Game;

public class BattleHud {
	
	public SpriteBatch spriteBatch;
	public Stage stage;
	private Viewport viewport;
	private Label healthCharacterLabel;
	private Label nameCharacterLabel;
	private Label healthEnemyLabel;
	private Label nameEnemyLabel;
	
	Integer healthCharacter;
	Integer healthEnemy;

	public BattleHud(SpriteBatch batch) {
		spriteBatch = batch;
		
		viewport = new FitViewport(1200,1200, new OrthographicCamera());
		stage = new Stage(viewport, spriteBatch);
		
		healthCharacter = (int) Game.world.battle.character.getHealth();
		healthEnemy = (int) Game.world.battle.enemy.getHealth();
		Table table = new Table();
		table.top(); // la allinea sopra al centro
		table.setFillParent(true);


		nameCharacterLabel = new Label(Game.character.name, MenuScreen.skin);
		healthCharacterLabel = new Label(String.format("%03d", healthCharacter), MenuScreen.skin);
	
		table.add(nameCharacterLabel).expandX().pad(20);
		table.add(healthCharacterLabel).expandX().pad(20);
		nameEnemyLabel = new Label(Game.world.battle.enemy.getName(), MenuScreen.skin);
		healthEnemyLabel = new Label(String.format("%03d", healthEnemy), MenuScreen.skin);
	
		table.add(nameEnemyLabel).expandX().pad(20);
		table.add(healthEnemyLabel).expandX().pad(20);
		table.row(); // nuova colonna

		stage.addActor(table);

	}

	public void update(float dt){
		
		healthCharacter = (int) Game.world.battle.character.getHealth();
		healthEnemy = (int) Game.world.battle.enemy.getHealth();
		healthCharacterLabel.setText(String.format("%03d",healthCharacter));
		healthEnemyLabel.setText(String.format("%03d",healthEnemy));
	}
}
