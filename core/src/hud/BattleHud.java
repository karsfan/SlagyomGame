package hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import battle.Battle;
import gameManager.GameSlagyom;
import gameManager.LoadingImage;
import gameManager.LoadingMusic;
import screens.MenuScreen;
import staticObjects.Item;
import staticObjects.Item.Level;
import staticObjects.StaticObject.Element;

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
	ProgressBar playerPower;

	Battle battle;
	Table table;
	Table potionTable;

	Label bluePotion;
	Label greenPotion;
	Label redPotion;
	GameSlagyom gameSlagyom;
	public BattleHud(GameSlagyom gameSlagyom, Battle battle) {
		table = new Table();
		potionTable = new Table();
		this.gameSlagyom = gameSlagyom;
		spriteBatch = gameSlagyom.batch;
		viewport = new FitViewport(1440, 960);
		stage = new Stage(viewport, gameSlagyom.batch);
		this.battle = battle;
		healthCharacter = (int) this.battle.character.getHealth();
		healthEnemy = (int) this.battle.enemy.getHealth();

		atlasBar = new TextureAtlas("menu/glassy/glassy-ui.atlas");
		skinBar = new Skin(Gdx.files.internal("menu/glassy/glassy-ui.json"), atlasBar);

		barPlayer = new ProgressBar(0.1f, healthCharacter, 0.1f, false, MenuScreen.skin);
		barEnemy = new ProgressBar(0.1f, healthEnemy, 0.1f, false, MenuScreen.skin);
		playerPower = new ProgressBar(this.battle.character.forza, 300, 0.1f, false, MenuScreen.skin);

		/*
		 * barPlayer.setBounds(this.viewport.getWorldWidth() / 13,
		 * this.viewport.getWorldHeight() / 1.1f, healthCharacter, 15);
		 * barEnemy.setBounds(this.viewport.getWorldWidth() / 1.2f,
		 * this.viewport.getWorldHeight() / 1.1f, healthEnemy, 15);
		 */

		table.top(); // la allinea sopra al centro
		table.setFillParent(true);

		Drawable hudBG = new TextureRegionDrawable(new TextureRegion(new Texture("res/BattleHudBg.png")));
		table.setBackground(hudBG);

		nameCharacterLabel = new Label(this.battle.character.name, MenuScreen.skin);
		table.add(nameCharacterLabel).expandX().padTop(15);
		nameEnemyLabel = new Label(this.battle.enemy.getName(), MenuScreen.skin);
		table.add(nameEnemyLabel).expandX().padLeft(this.viewport.getWorldHeight() / 3);
		table.row().padTop(15);
		table.add(barPlayer).expandX();
		table.add(barEnemy).expandX().padLeft(this.viewport.getWorldHeight() / 3);

		potionTable.setLayoutEnabled(false);
		bluePotion = new Label("x" + String.valueOf(this.battle.character.bag.getNumberOf(Element.POTION, Level.FIRST)),
				MenuScreen.skin);
		greenPotion = new Label(
				"x" + String.valueOf(this.battle.character.bag.getNumberOf(Element.POTION, Level.SECOND)),
				MenuScreen.skin);
		redPotion = new Label("x" + String.valueOf(this.battle.character.bag.getNumberOf(Element.POTION, Level.THIRD)),
				MenuScreen.skin);

		bluePotion.setPosition(893, 20);
		greenPotion.setPosition(1022, 20);
		redPotion.setPosition(1153, 20);
		playerPower.setPosition(200, 40);

		// bluePotion.setFontScale((float) 0.8);
		potionTable.add(bluePotion);
		potionTable.add(greenPotion);
		potionTable.add(redPotion);
		potionTable.add(playerPower);
		stage.addActor(table);
		stage.addActor(potionTable);
	}

	public void update(float dt) {
		healthCharacter = (int) this.battle.character.getHealth();
		barPlayer.setValue(healthCharacter.intValue());
		healthEnemy = (int) this.battle.enemy.getHealth();
		barEnemy.setValue(healthEnemy.intValue());
		playerPower.setValue(this.battle.character.forza);

		bluePotion.setText("x" + String.valueOf(this.battle.character.bag.getNumberOf(Element.POTION, Level.FIRST)));
		greenPotion.setText("x" + String.valueOf(this.battle.character.bag.getNumberOf(Element.POTION, Level.SECOND)));
		redPotion.setText("x" + String.valueOf(this.battle.character.bag.getNumberOf(Element.POTION, Level.THIRD)));
		if (this.battle.character.health < 300) {
			if (Gdx.input.isKeyJustPressed(Keys.NUM_1)
					&& this.battle.character.bag.getNumberOf(Element.POTION, Level.FIRST) > 0) {
				this.battle.character.bag.useItem(new Item(Element.POTION, Level.FIRST));
				gameSlagyom.loadingMusic.upgradeSound.play();
			}
			if (Gdx.input.isKeyJustPressed(Keys.NUM_2)
					&& this.battle.character.bag.getNumberOf(Element.POTION, Level.SECOND) > 0) {
				this.battle.character.bag.useItem(new Item(Element.POTION, Level.SECOND));
				gameSlagyom.loadingMusic.upgradeSound.play();
			}
			if (Gdx.input.isKeyJustPressed(Keys.NUM_3)
					&& this.battle.character.bag.getNumberOf(Element.POTION, Level.THIRD) > 0) {
				this.battle.character.bag.useItem(new Item(Element.POTION, Level.THIRD));
				gameSlagyom.loadingMusic.upgradeSound.play();
			}
		}
	}
}
