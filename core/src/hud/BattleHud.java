package hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import battle.Battle;
import character.Weapon;
import character.Weapon.Type;
import gameManager.GameSlagyom;
import screens.MenuScreen;
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

		barPlayer = new ProgressBar(0.1f, 300, 0.1f, false, MenuScreen.skin);
		barEnemy = new ProgressBar(0.1f, 300, 0.1f, false, MenuScreen.skin);
		playerPower = new ProgressBar(this.battle.character.forza, 150, 0.1f, false, MenuScreen.skin);
		currentWeapon = gameSlagyom.loadingImage.battleSpear1;
		currentWeaponName = new Label ("SPEAR", MenuScreen.skin);
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
		currentWeapon.setPosition(1250, 30);
		currentWeaponName.setPosition(1270, 10);
		currentWeaponName.setFontScale(0.7f);
		playerPower.setPosition(200, 40);
		
		potionTable.add(bluePotion);
		potionTable.add(greenPotion);
		potionTable.add(redPotion);
		potionTable.add(currentWeapon);
		potionTable.add(currentWeaponName);
		stage.addActor(table);
		stage.addActor(potionTable);
	}
	
	ImageButton currentWeapon; 
	Label currentWeaponName;
	public void update(float dt) {
		healthCharacter = (int) this.battle.character.getHealth();
		barPlayer.setValue(healthCharacter.intValue());
		healthEnemy = (int) this.battle.enemy.getHealth();
		barEnemy.setValue(healthEnemy.intValue());
		currentWeapon.setPosition(1250, 30);
		
		if (battle.character.lanciaBomba) {
			potionTable.addActor(playerPower);
			playerPower.setValue(this.battle.character.forza);
		}
		else
			potionTable.removeActor(playerPower);
		
		potionTable.removeActor(currentWeapon);
		if (battle.character.primary_weapon.getType() == Type.Spear) {
			if (battle.character.primary_weapon.getLevel() == Weapon.Level.lev1) {
				currentWeapon = gameSlagyom.loadingImage.battleSpear1;
				currentWeaponName.setText("SPEAR");
			}
			else if (battle.character.primary_weapon.getLevel() == Weapon.Level.lev2) {
				currentWeapon = gameSlagyom.loadingImage.battleSpear2;
				currentWeaponName.setText("SPEAR");
			}
			else {
				currentWeapon = gameSlagyom.loadingImage.battleSpear3;
				currentWeaponName.setText("SPEAR");
			}
		}
		
		else if (battle.character.primary_weapon.getType() == Type.Sword) {
			currentWeapon = gameSlagyom.loadingImage.battleSword;
			currentWeaponName.setText("SWORD");
		}
		else {
			currentWeapon = gameSlagyom.loadingImage.battleBow;
			currentWeaponName.setText("BOW");
		}
		potionTable.addActor(currentWeapon);

		bluePotion.setText("x" + String.valueOf(this.battle.character.bag.getNumberOf(Element.POTION, Level.FIRST)));
		greenPotion.setText("x" + String.valueOf(this.battle.character.bag.getNumberOf(Element.POTION, Level.SECOND)));
		redPotion.setText("x" + String.valueOf(this.battle.character.bag.getNumberOf(Element.POTION, Level.THIRD)));
	
	}
}
