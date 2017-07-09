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

import battle.Battle;
import screens.MenuScreen;

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
	Battle battle;
	Table table = new Table();
	public BattleHud(SpriteBatch sb, Battle battle) {

		spriteBatch = sb;
		viewport = new FitViewport(1440, 960);
		stage = new Stage(viewport, sb);
		this.battle = battle;
		healthCharacter = (int) this.battle.character.getHealth();
		healthEnemy = (int) this.battle.enemy.getHealth();
	
		atlasBar = new TextureAtlas("menu/glassy/glassy-ui.atlas");
		skinBar = new Skin(Gdx.files.internal("menu/glassy/glassy-ui.json"), atlasBar);

		barPlayer = new ProgressBar(0.1f, healthCharacter, 0.1f, false, MenuScreen.skin);
		barEnemy = new ProgressBar(0.1f, healthEnemy, 0.1f, false, MenuScreen.skin);

		/*barPlayer.setBounds(this.viewport.getWorldWidth() / 13, this.viewport.getWorldHeight() / 1.1f, healthCharacter,
				15);
		barEnemy.setBounds(this.viewport.getWorldWidth() / 1.2f, this.viewport.getWorldHeight() / 1.1f, healthEnemy,
				15);*/

		table.top(); // la allinea sopra al centro
		table.setFillParent(true);
		
		Drawable hudBG = new TextureRegionDrawable(new TextureRegion(new Texture("res/BattleHudBg.png")));
		table.setBackground(hudBG);
		
		nameCharacterLabel = new Label(this.battle.character.name, MenuScreen.skin);
		table.add(nameCharacterLabel).expandX().padTop(15);
		nameEnemyLabel = new Label(this.battle.enemy.getName(), MenuScreen.skin);
		table.add(nameEnemyLabel).expandX().padLeft(this.viewport.getWorldHeight()/3);
		
		table.row().padTop(15);
		
		table.add(barPlayer).expandX();
		table.add(barEnemy).expandX().padLeft(this.viewport.getWorldHeight()/3);

		stage.addActor(table);

	}

	
	public void update(float dt) {
		
		healthCharacter = (int) this.battle.character.getHealth();
		barPlayer.setValue(healthCharacter.intValue());
		healthEnemy = (int) this.battle.enemy.getHealth();
		barEnemy.setValue(healthEnemy.intValue());
	}
}
