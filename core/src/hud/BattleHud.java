package hud;

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
import gameManager.GameSlagyom;
import screens.MenuScreen;
import staticObjects.StaticObject.Element;
import weaponsAndItems.Weapon;
import weaponsAndItems.Item.Level;
import weaponsAndItems.Weapon.Type;

public class BattleHud {

	public SpriteBatch spriteBatch;
	public Stage stage;
	private Viewport viewport;

	private Label nameCharacterLabel;
	private Label nameEnemyLabel;

	Integer characterHealth;
	Integer enemyHealth;
	
	static TextureAtlas atlasBar;
	public static Skin skinBar;
	ProgressBar playerBar;
	ProgressBar enemyBar;
	ProgressBar playerPowerBar;

	Battle battle;
	GameSlagyom gameSlagyom;
	
	Table table;
	Table potionTable;
	ImageButton currentWeapon; 
	Label currentWeaponName;
	Label bluePotion;
	Label greenPotion;
	Label redPotion;
	/**
	 * Constructore that receive a GameSlagyom and a Battle 
	 * @param gameSlagyom
	 * @param battle
	 */
	@SuppressWarnings("static-access")
	public BattleHud(GameSlagyom gameSlagyom, Battle battle) {
		table = new Table();
		potionTable = new Table();
		this.gameSlagyom = gameSlagyom;
		spriteBatch = gameSlagyom.batch;
		viewport = new FitViewport(1440, 960);
		stage = new Stage(viewport, gameSlagyom.batch);
		this.battle = battle;
		characterHealth = (int) this.battle.character.getHealth();
		enemyHealth = (int) this.battle.enemy.getHealth();

		playerBar = new ProgressBar(0.1f, 300, 0.1f, false, MenuScreen.skin);
		enemyBar = new ProgressBar(0.1f, 300, 0.1f, false, MenuScreen.skin);
		playerPowerBar = new ProgressBar(this.battle.character.power, 150, 0.1f, false, MenuScreen.skin);
		currentWeapon = gameSlagyom.loadingImage.battleSpear1;
		currentWeaponName = new Label ("SPEAR", MenuScreen.skin);

		table.top(); // la allinea sopra al centro
		table.setFillParent(true);

		Drawable hudBG = new TextureRegionDrawable(new TextureRegion(new Texture("res/battleHudBg.png")));
		table.setBackground(hudBG);

		nameCharacterLabel = new Label(this.battle.character.name, MenuScreen.skin);
		table.add(nameCharacterLabel).expandX().padTop(15);
		nameEnemyLabel = new Label(this.battle.enemy.getName(), MenuScreen.skin);
		table.add(nameEnemyLabel).expandX().padLeft(this.viewport.getWorldHeight() / 3);
		table.row().padTop(15);
		table.add(playerBar).expandX();
		table.add(enemyBar).expandX().padLeft(this.viewport.getWorldHeight() / 3);

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
		playerPowerBar.setPosition(200, 40);
		
		potionTable.add(bluePotion);
		potionTable.add(greenPotion);
		potionTable.add(redPotion);
		potionTable.add(currentWeapon);
		potionTable.add(currentWeaponName);
		stage.addActor(table);
		stage.addActor(potionTable);
	}
	/**
	 * It handles the changes of battle
	 * @param dt
	 */
	@SuppressWarnings("static-access")
	public void update(float dt) {
		characterHealth = (int) this.battle.character.getHealth();
		playerBar.setValue(characterHealth.intValue());
		enemyHealth = (int) this.battle.enemy.getHealth();
		enemyBar.setValue(enemyHealth.intValue());
		currentWeapon.setPosition(1250, 30);
		
		if (battle.character.booleanLaunchBomb) {
			potionTable.addActor(playerPowerBar);
			playerPowerBar.setValue(this.battle.character.power);
		}
		else
			potionTable.removeActor(playerPowerBar);
		
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
