package gameManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

import battle.CharacterBattle;
import battle.Enemy;
import battle.Fighting;
import character.DynamicObjects;
import character.Woman;
import character.DynamicObjects.StateDynamicObject;
import character.Player;
import character.Woman.WomanType;
import multiplayer.NetworkCharacterBattle;
import multiplayer.NetworkEnemy;
import multiplayer.NetworkPlayer;
import staticObjects.StaticObject;
import staticObjects.StaticObject.Element;
import weaponsAndItems.Item;
import weaponsAndItems.Item.Level;
import weaponsAndItems.Weapon.Type;

public class LoadingImage {
	public Texture texture;
	public static Texture title;
	public static Texture blackBg;
	private static Texture homeImage;
	private static Texture templeImage;
	private static Texture threeImage;
	private static Texture groundImage;
	private static Texture floorImage;
	private static Texture floor2Image;
	private static Texture floor3Image;
	private static Texture strawImage;
	private static Texture preEnemyHomeImage;
	private static Texture shopImage;
	private static Texture roadImage;
	private static Texture waterImage;
	private static Texture rockImage;
	private static Texture lampImage;
	private static Texture lightLampImage;
	public static Texture lightImage;
	private static Texture forest1Image;
	private static Texture forest2Image;
	private static Texture tableImage;
	public static Texture coinImage;
	private static Texture parchment1Image;
	private static Texture parchment2Image;
	private static Texture battleBackground;
	private static Texture bluPotionImage;
	private static Texture redPotionImage;
	private static Texture greenPotionImage;
	private static Texture youWinImage;
	private static Texture youLoseImage;
	private static Texture castleImage;
	public static Texture miniMap;
	public static Texture pointer;
	public static Texture miniMapPointer;
	public static Texture areFighting;
	public static Pixmap cursor;
	public static Pixmap noCursor;
	public static Drawable dialog;
	public static Texture miniMapPlayerPointer;
	public static Texture arrowLeft;
	public static Texture arrowRight;
	public static Texture bombImage;

	public TextureRegion battleCharacterStand;
	public TextureRegion battleFemaleCharacterStand;
	public Animation<TextureRegion>[] battleCharacterAnimationSpearLev1;
	public Animation<TextureRegion>[] battleFemaleCharacterAnimationSpearLev1;
	public Animation<TextureRegion>[] battleCharacterAnimationSpearLev2;
	public Animation<TextureRegion>[] battleFemaleCharacterAnimationSpearLev2;
	public Animation<TextureRegion>[] battleCharacterAnimationSpearLev3;
	public Animation<TextureRegion>[] battleFemaleCharacterAnimationSpearLev3;
	public Animation<TextureRegion>[] battleCharacterAnimationSwordLev1;
	public Animation<TextureRegion>[] battleFemaleCharacterAnimationSwordLev1;
	public Animation<TextureRegion>[] battleCharacterAnimationBowLev1;
	public Animation<TextureRegion>[] battleFemaleAnimationBowLev1;
	public Animation<TextureRegion>[] battleCharacterAnimationBowLev2;
	public Animation<TextureRegion>[] battleFemaleAnimationBowLev2;
	public Animation<TextureRegion>[] battleCharacterAnimationBowLev3;
	public Animation<TextureRegion>[] battleFemaleAnimationBowLev3;

	public TextureRegion playerStand;
	public Animation<TextureRegion>[] playerAnimation;

	public TextureRegion enemyStand;
	public Animation<TextureRegion>[] enemyAnimationSpearLev1;
	public Animation<TextureRegion>[] enemyAnimationSpearLev2;
	public Animation<TextureRegion>[] enemyAnimationSpearLev3;
	public Animation<TextureRegion>[] enemyAnimationBowLev1;
	public Animation<TextureRegion>[] enemyAnimationBowLev2;
	public Animation<TextureRegion>[] enemyAnimationBowLev3;
	public Animation<TextureRegion>[] enemyAnimationSword;

	public TextureRegion man1Stand;
	public Animation<TextureRegion>[] man1Animation;

	public TextureRegion woman1Stand;
	public Animation<TextureRegion>[] woman1Animation;

	private TextureRegion woman2Stand;
	public Animation<TextureRegion>[] woman2Animation;

	public Animation<TextureRegion>[] otherPlayerAnimation;
	public TextureRegion otherPlayerStand;

	public static Animation<TextureRegion> loadingAnimation;

	// BAG IMAGE BUTTONS
	public static ImageButton potionDescription;
	public static ImageButton bombDescription;
	public static ImageButton weaponDescription;
	public static ImageButton parchmentDescription;
	public static ImageButton emptyDescription;
	public static ImageButton bluePotion;
	public static ImageButton redPotion;
	public static ImageButton greenPotion;
	public static ImageButton bomb;
	public static ImageButton parchment;
	public static ImageButton parchment2;
	public static ImageButton spear;
	public static ImageButton sword;
	public static ImageButton bow;
	public static ImageButton emptyBagIcon;
	public static ImageButton emptyShopIcon;
	public static ImageButton leftArrow;
	public static ImageButton rightArrow;
	public static ImageButton close;
	public static ImageButton howMuch;

	// BATTLE IMAGE
	public static ImageButton battleBow;
	public static ImageButton battleSpear1;
	public static ImageButton battleSpear2;
	public static ImageButton battleSpear3;
	public static ImageButton battleSword;

	@SuppressWarnings("unchecked")
	public LoadingImage() {
		// TILES IMAGES

		title = new Texture("res/title.png");
		blackBg = new Texture("res/blackBg.png");
		homeImage = new Texture("res/home.png");
		templeImage = new Texture("res/bigHome.png");
		threeImage = new Texture("res/three.png");
		groundImage = new Texture("res/floor.png");
		floorImage = new Texture("res/floor.png");
		floor2Image = new Texture("res/floor3.png");
		floor3Image = new Texture("res/floor3.png");
		strawImage = new Texture("res/straw.png");
		preEnemyHomeImage = new Texture("res/preEnemyHome.png");
		shopImage = new Texture("res/shop.png");
		roadImage = new Texture("res/road.png");
		waterImage = new Texture("res/water.png");
		rockImage = new Texture("res/rock.png");
		lampImage = new Texture("res/lamp.png");
		lightLampImage = new Texture("res/lightLamp.png");
		lightImage = new Texture("res/light.png");
		forest1Image = new Texture("res/forest1.png");
		forest2Image = new Texture("res/forest2.png");
		tableImage = new Texture("res/table.png");
		battleBackground = new Texture("res/battleBg7.png");
		castleImage = new Texture("res/castle.png");
		youWinImage = new Texture("res/youWin.png");
		youLoseImage = new Texture("res/youLose.png");
		dialog = new TextureRegionDrawable(new TextureRegion(new Texture("res/dialogBox.png")));

		// WORLD ITEM IMAGES
		coinImage = new Texture("res/coin.png");
		bluPotionImage = new Texture("res/bluePotion.png");
		greenPotionImage = new Texture("res/greenPotion.png");
		redPotionImage = new Texture("res/redPotion.png");
		parchment1Image = new Texture("res/pickParchment.png");
		parchment2Image = new Texture("res/pickParchment2.png");
		pointer = new Texture("res/pointer.png");
		miniMap = new Texture("res/miniMap.png");
		miniMapPointer = new Texture("res/miniMapPointer.png");

		// BAG AND SHOP ITEM IMAGES
		potionDescription = new ImageButton(
				new TextureRegionDrawable(new TextureRegion(new Texture("res/bag/potionDescription.png"))));
		weaponDescription = new ImageButton(
				new TextureRegionDrawable(new TextureRegion(new Texture("res/bag/weaponDescription.png"))));
		parchmentDescription = new ImageButton(
				new TextureRegionDrawable(new TextureRegion(new Texture("res/bag/parchmentDescription.png"))));
		bombDescription = new ImageButton(
				new TextureRegionDrawable(new TextureRegion(new Texture("res/bag/bombDescription.png"))));
		emptyDescription = new ImageButton(
				new TextureRegionDrawable(new TextureRegion(new Texture("res/noCursor.png"))));

		bluePotion = new ImageButton(
				new TextureRegionDrawable(new TextureRegion(new Texture("res/bag/bluePotion.png"))));
		redPotion = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("res/bag/redPotion.png"))));
		greenPotion = new ImageButton(
				new TextureRegionDrawable(new TextureRegion(new Texture("res/bag/greenPotion.png"))));
		spear = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("res/bag/spear.png"))));
		sword = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("res/bag/sword.png"))));
		bow = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("res/bag/bow.png"))));
		bomb = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("res/bag/bomb.png"))));
		parchment = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("res/bag/parchment.png"))));
		parchment2 = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("res/parchment2.png"))));
		rightArrow = new ImageButton(
				new TextureRegionDrawable(new TextureRegion(new Texture("res/bag/rightArrow.png"))));
		leftArrow = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("res/bag/leftArrow.png"))));
		close = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("res/bag/close.png"))));

		emptyBagIcon = new ImageButton(
				new TextureRegionDrawable(new TextureRegion(new Texture("res/bag/emptyIcon.png"))));
		emptyShopIcon = new ImageButton(
				new TextureRegionDrawable(new TextureRegion(new Texture("res/shop/emptyIcon.png"))));
		howMuch = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("res/shop/howMuch.png"))));
		cursor = new Pixmap(Gdx.files.internal("res/cursor.png"));
		noCursor = new Pixmap(Gdx.files.internal("res/noCursor.png"));

		// BATTLE IMAGES
		battleBow = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("res/battleBow.png"))));
		battleSpear1 = new ImageButton(
				new TextureRegionDrawable(new TextureRegion(new Texture("res/battleSpear1.png"))));
		battleSpear2 = new ImageButton(
				new TextureRegionDrawable(new TextureRegion(new Texture("res/battleSpear2.png"))));
		battleSpear3 = new ImageButton(
				new TextureRegionDrawable(new TextureRegion(new Texture("res/battleSpear3.png"))));
		battleSword = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("res/battleSword.png"))));

		// WORLD ANIMATIONS
		playerAnimation = new Animation[4];
		otherPlayerAnimation = new Animation[4];
		man1Animation = new Animation[4];
		woman1Animation = new Animation[4];
		woman2Animation = new Animation[4];

		playerStand = new TextureRegion();
		battleCharacterStand = new TextureRegion();
		battleFemaleCharacterStand = new TextureRegion();

		battleCharacterAnimationSpearLev1 = new Animation[4];
		battleFemaleCharacterAnimationSpearLev1 = new Animation[4];
		battleCharacterAnimationSpearLev2 = new Animation[4];
		battleFemaleCharacterAnimationSpearLev2 = new Animation[4];
		battleCharacterAnimationSpearLev3 = new Animation[4];
		battleFemaleCharacterAnimationSpearLev3 = new Animation[4];
		battleCharacterAnimationSwordLev1 = new Animation[4];
		battleFemaleCharacterAnimationSwordLev1 = new Animation[4];
		battleCharacterAnimationBowLev1 = new Animation[4];
		battleFemaleAnimationBowLev1 = new Animation[4];
		battleCharacterAnimationBowLev2 = new Animation[4];
		battleFemaleAnimationBowLev2 = new Animation[4];
		battleCharacterAnimationBowLev3 = new Animation[4];
		battleFemaleAnimationBowLev3 = new Animation[4];

		enemyAnimationSpearLev1 = new Animation[4];
		enemyAnimationSpearLev2 = new Animation[4];
		enemyAnimationSpearLev3 = new Animation[4];
		enemyAnimationBowLev1 = new Animation[4];
		enemyAnimationBowLev2 = new Animation[4];
		enemyAnimationBowLev3 = new Animation[4];
		enemyAnimationSword = new Animation[4];

		enemyStand = new TextureRegion();
		man1Stand = new TextureRegion();
		woman1Stand = new TextureRegion();
		woman2Stand = new TextureRegion();
		otherPlayerStand = new TextureRegion();
		texture = new Texture("Character/walking.png");
		createFrame(texture, playerAnimation, playerStand);

		texture = new Texture("notPlaying.png");
		createFrame(texture, man1Animation, man1Stand);

		texture = new Texture("NotPlaying/woman1.png");
		createFrame(texture, woman1Animation, woman1Stand);

		texture = new Texture("NotPlaying/woman2.png");
		createFrame(texture, woman2Animation, woman2Stand);

		texture = new Texture("Enemy/walking.png");
		createFrame(texture, otherPlayerAnimation, otherPlayerStand);

		texture = new Texture("Character/spear.png");
		createBattleFrame(texture, battleCharacterAnimationSpearLev1, battleCharacterStand);

		texture = new Texture("Character/female/spear.png");
		createBattleFrame(texture, battleFemaleCharacterAnimationSpearLev1, battleFemaleCharacterStand);

		texture = new Texture("Character/swordOk.png");
		createBattleFrameBig(texture, battleCharacterAnimationSwordLev1, battleCharacterStand, 5);

		texture = new Texture("Character/female/sword.png");
		createBattleFrameBig(texture, battleFemaleCharacterAnimationSwordLev1, battleFemaleCharacterStand, 5);

		texture = new Texture("Character/spear2.png");
		createBattleFrameBig(texture, battleCharacterAnimationSpearLev2, battleCharacterStand, 5);

		texture = new Texture("Character/female/spear2.png");
		createBattleFrameBig(texture, battleFemaleCharacterAnimationSpearLev2, battleFemaleCharacterStand, 5);

		texture = new Texture("Character/spear3.png");
		createBattleFrameBig(texture, battleCharacterAnimationSpearLev3, battleCharacterStand, 5);

		texture = new Texture("Character/female/spear3.png");
		createBattleFrameBig(texture, battleFemaleCharacterAnimationSpearLev3, battleFemaleCharacterStand, 5);

		texture = new Texture("Enemy/spear.png");
		createBattleFrame(texture, enemyAnimationSpearLev1, enemyStand);

		texture = new Texture("Character/bow.png");
		createBattleFrame(texture, battleCharacterAnimationBowLev1, battleCharacterStand);

		texture = new Texture("Character/female/bow.png");
		createBattleFrame(texture, battleFemaleAnimationBowLev1, battleFemaleCharacterStand);

		texture = new Texture("Character/bow2.png");
		createBattleFrame(texture, battleCharacterAnimationBowLev2, battleCharacterStand);

		texture = new Texture("Character/female/bow2.png");
		createBattleFrame(texture, battleFemaleAnimationBowLev2, battleFemaleCharacterStand);

		texture = new Texture("Character/bow3.png");
		createBattleFrame(texture, battleCharacterAnimationBowLev3, battleCharacterStand);

		texture = new Texture("Character/female/bow3.png");
		createBattleFrame(texture, battleFemaleAnimationBowLev3, battleFemaleCharacterStand);

		texture = new Texture("Enemy/bow.png");
		createBattleFrame(texture, enemyAnimationBowLev1, enemyStand);

		texture = new Texture("Enemy/bow2.png");
		createBattleFrame(texture, enemyAnimationBowLev2, enemyStand);

		texture = new Texture("Enemy/bow3.png");
		createBattleFrame(texture, enemyAnimationBowLev3, enemyStand);

		texture = new Texture("Enemy/spear2.png");
		createBattleFrameBig(texture, enemyAnimationSpearLev2, enemyStand, 5);

		texture = new Texture("Enemy/spear3.png");
		createBattleFrameBig(texture, enemyAnimationSpearLev3, enemyStand, 5);

		texture = new Texture("Enemy/sword.png");
		createBattleFrameBig(texture, enemyAnimationSword, enemyStand, 5);

		loadLoadingTexture();
		arrowLeft = new Texture("res/sxArrow.png");
		arrowRight = new Texture("res/dxArrow.png");
		bombImage = new Texture("res/bomb30.png");
		areFighting = new Texture("res/areFighting.png");
		miniMapPlayerPointer = new Texture("res/multiplayerPointer.png");

	}

	public void loadLoadingTexture() {
		Array<TextureRegion> frames = new Array<TextureRegion>();
		for (int i = 0; i < 14; i++) {
			frames.add(new TextureRegion(new Texture("res/loadingAnimation/" + String.valueOf(i + 1) + ".png")));
			loadingAnimation = new Animation<TextureRegion>(0.2f, frames);
		}
	}

	private void createBattleFrame(Texture texture, Animation<TextureRegion>[] arrayAnimation, TextureRegion stand) {
		Array<TextureRegion> frames = new Array<TextureRegion>();
		Animation<TextureRegion> right;
		Animation<TextureRegion> left;
		Animation<TextureRegion> fightingRight;
		Animation<TextureRegion> fightingLeft;

		for (int i = 0; i < 8; i++) {
			frames.add(new TextureRegion(texture, i * 64, 65, 65, 65));
		}
		right = new Animation<TextureRegion>(0.2f, frames);
		frames.clear();

		for (int i = 7; i != 0; i--) {
			frames.add(new TextureRegion(texture, i * 64, 0, 65, 65));
		}
		left = new Animation<TextureRegion>(0.2f, frames);
		frames.clear();

		for (int i = 0; i < 8; i++) {
			frames.add(new TextureRegion(texture, i * 64, 195, 65, 65));
		}
		fightingRight = new Animation<TextureRegion>(0.2f, frames);
		frames.clear();

		for (int i = 0; i < 8; i++) {
			frames.add(new TextureRegion(texture, i * 64, 130, 65, 65));
		}
		fightingLeft = new Animation<TextureRegion>(0.2f, frames);
		frames.clear();

		arrayAnimation[0] = right;
		arrayAnimation[1] = left;
		arrayAnimation[2] = fightingRight;
		arrayAnimation[3] = fightingLeft;
		arrayAnimation[2].setFrameDuration(0.04f);
		arrayAnimation[3].setFrameDuration(0.04f);
		stand.setRegion(arrayAnimation[0].getKeyFrame(0, true));

	}

	private void createBattleFrameBig(Texture texture, Animation<TextureRegion>[] arrayAnimation, TextureRegion stand,
			int frame) {
		Array<TextureRegion> frames = new Array<TextureRegion>();
		Animation<TextureRegion> right;
		Animation<TextureRegion> left;
		Animation<TextureRegion> fightingRight;
		Animation<TextureRegion> fightingLeft;

		for (int i = 0; i <= frame; i++) {
			frames.add(new TextureRegion(texture, i * 190 + 10, 65, 120, 65));
		}
		right = new Animation<TextureRegion>(0.2f, frames);
		frames.clear();

		for (int i = 0; i <= frame; i++) {
			frames.add(new TextureRegion(texture, i * 190, 0, 120, 65));
		}
		left = new Animation<TextureRegion>(0.2f, frames);
		frames.clear();

		for (int i = 0; i <= frame; i++) {
			frames.add(new TextureRegion(texture, i * 190 + 10, 195, 120, 65));
		}
		fightingRight = new Animation<TextureRegion>(0.2f, frames);
		frames.clear();

		for (int i = 0; i <= frame; i++) {
			frames.add(new TextureRegion(texture, i * 190, 130, 120, 65));
		}
		fightingLeft = new Animation<TextureRegion>(0.2f, frames);
		frames.clear();

		arrayAnimation[0] = right;
		arrayAnimation[1] = left;
		arrayAnimation[2] = fightingRight;
		arrayAnimation[3] = fightingLeft;
		arrayAnimation[2].setFrameDuration(0.04f);
		arrayAnimation[3].setFrameDuration(0.04f);
		stand.setRegion(arrayAnimation[0].getKeyFrame(0, true));

	}

	public void createFrame(Texture texture, Animation<TextureRegion>[] arrayAnimation, TextureRegion stand) {

		Array<TextureRegion> frames = new Array<TextureRegion>();
		Animation<TextureRegion> right;
		Animation<TextureRegion> left;
		Animation<TextureRegion> up;
		Animation<TextureRegion> down;

		for (int i = 0; i < 8; i++) {
			frames.add(new TextureRegion(texture, i * 64 / 2, 93, 60 / 2, 60 / 2));
		}
		right = new Animation<TextureRegion>(0.2f, frames);
		frames.clear();

		for (int i = 8; i != 0; i--) {
			frames.add(new TextureRegion(texture, i * 64 / 2, 31, 60 / 2, 60 / 2));
		}
		left = new Animation<TextureRegion>(0.2f, frames);
		frames.clear();

		for (int i = 0; i < 8; i++) {
			frames.add(new TextureRegion(texture, i * 64 / 2, 62, 60 / 2, 60 / 2));
		}
		down = new Animation<TextureRegion>(0.2f, frames);
		frames.clear();

		for (int i = 0; i < 8; i++) {
			frames.add(new TextureRegion(texture, i * 64 / 2, 0, 60 / 2, 60 / 2));
		}
		up = new Animation<TextureRegion>(0.2f, frames);
		frames.clear();

		arrayAnimation[0] = right;
		arrayAnimation[1] = left;
		arrayAnimation[2] = up;
		arrayAnimation[3] = down;
		stand.setRegion(arrayAnimation[0].getKeyFrame(0, true));
	}

	public static Texture getBattleBgImage() {
		return battleBackground;
	}

	public TextureRegion getBattleFrame(Object ob) {
		TextureRegion region = new TextureRegion();
		StateDynamicObject state = null;
		float stateTimer = 0;
		if (ob instanceof CharacterBattle) {
			stateTimer = ((CharacterBattle) ob).getStateTimer();
			state = ((CharacterBattle) ob).getCurrentState();
		} else if (ob instanceof Enemy) {
			stateTimer = ((Enemy) ob).getStateTimer();
			state = ((Enemy) ob).getCurrentState();
		}
		switch (state) {
		case RUNNINGRIGHT:
			region = (TextureRegion) getAnimation(ob)[0].getKeyFrame(stateTimer, true);
			getFrameStand(ob).setRegion(getAnimation(ob)[0].getKeyFrame(0, true));
			break;
		case RUNNINGLEFT:
			region = (TextureRegion) getAnimation(ob)[1].getKeyFrame(stateTimer, true);
			getFrameStand(ob).setRegion(getAnimation(ob)[1].getKeyFrame(0, true));
			break;
		case FIGHTINGRIGHT:
			region = (TextureRegion) getAnimation(ob)[2].getKeyFrame(stateTimer, true);
			getFrameStand(ob).setRegion(getAnimation(ob)[2].getKeyFrame(0, true));
			break;
		case FIGHTINGLEFT:
			region = (TextureRegion) getAnimation(ob)[3].getKeyFrame(stateTimer, true);
			getFrameStand(ob).setRegion(getAnimation(ob)[3].getKeyFrame(0, true));
			break;
		case STANDING:
			if (((Fighting) ob).left)
				region = (TextureRegion) getAnimation(ob)[1].getKeyFrame(stateTimer, true);
			else
				region = (TextureRegion) getAnimation(ob)[0].getKeyFrame(stateTimer, true);
			break;
		default:
			// getFrameStand(ob).setRegion(getAnimation(ob)[0].getKeyFrame(0,
			// true));
			if (((Fighting) ob).left)
				region = (TextureRegion) getAnimation(ob)[1].getKeyFrame(stateTimer, true);
			else
				region = (TextureRegion) getAnimation(ob)[0].getKeyFrame(stateTimer, true);
			break;
		// region = getFrameStand(ob);
		// break;
		}
		return region;
	}

	public TextureRegion getFrame(Object ob) {
		TextureRegion region;
		switch (((DynamicObjects) ob).getCurrentState()) {
		case RUNNINGRIGHT:
			region = (TextureRegion) getAnimation(ob)[0].getKeyFrame(((DynamicObjects) ob).getStateTimer(), true);
			getFrameStand(ob).setRegion(getAnimation(ob)[0].getKeyFrame(0, true));
			break;
		case RUNNINGLEFT:
			region = (TextureRegion) getAnimation(ob)[1].getKeyFrame(((DynamicObjects) ob).getStateTimer(), true);
			getFrameStand(ob).setRegion(getAnimation(ob)[1].getKeyFrame(0, true));
			break;
		case RUNNINGUP:
			region = (TextureRegion) getAnimation(ob)[2].getKeyFrame(((DynamicObjects) ob).getStateTimer(), true);
			getFrameStand(ob).setRegion(getAnimation(ob)[2].getKeyFrame(0, true));
			break;
		case RUNNINGDOWN:
			region = (TextureRegion) getAnimation(ob)[3].getKeyFrame(((DynamicObjects) ob).getStateTimer(), true);
			getFrameStand(ob).setRegion(getAnimation(ob)[3].getKeyFrame(0, true));
			break;
		case STANDING:
			getFrameStand(ob).setRegion(getAnimation(ob)[0].getKeyFrame(0, true));
			region = getFrameStand(ob);
			break;
		default:
			getFrameStand(ob).setRegion(getAnimation(ob)[0].getKeyFrame(0, true));
			region = getFrameStand(ob);
			break;
		}
		return region;
	}

	public void setFrameDurationCharacter(float frameDuration) {
		playerAnimation[0].setFrameDuration(frameDuration);
		playerAnimation[1].setFrameDuration(frameDuration);
		playerAnimation[2].setFrameDuration(frameDuration);
		playerAnimation[3].setFrameDuration(frameDuration);
	}

	public Animation<TextureRegion>[] getAnimation(Object ob) {
		Class<? extends Object> a = ob.getClass();
		Animation<TextureRegion>[] animation = null;

		switch (a.getSimpleName()) {
		case "Player":
			if (((Player) ob).male)
				animation = playerAnimation;
			else
				animation = woman1Animation;
			break;
		case "NetworkPlayer":
			if (((NetworkPlayer) ob).player)
				animation = playerAnimation;
			else
				animation = otherPlayerAnimation;
			break;
		case "Man":
			animation = man1Animation;
			break;
		case "Woman":
			if (((Woman) ob).type == WomanType.WOMAN1)
				animation = woman1Animation;
			if (((Woman) ob).type == WomanType.WOMAN2)
				animation = woman2Animation;
			break;
		case "CharacterBattle":
			if (((CharacterBattle) ob).primary_weapon.getType() == Type.Spear) {
				if (((CharacterBattle) ob).male) {
					if (((CharacterBattle) ob).primary_weapon.getLevel() == weaponsAndItems.Weapon.Level.lev1)
						animation = battleCharacterAnimationSpearLev1;
					else if (((CharacterBattle) ob).primary_weapon.getLevel() == weaponsAndItems.Weapon.Level.lev2)
						animation = battleCharacterAnimationSpearLev2;
					else if (((CharacterBattle) ob).primary_weapon.getLevel() == weaponsAndItems.Weapon.Level.lev3)
						animation = battleCharacterAnimationSpearLev3;
				} else {
					if (((CharacterBattle) ob).primary_weapon.getLevel() == weaponsAndItems.Weapon.Level.lev1)
						animation = battleFemaleCharacterAnimationSpearLev1;
					else if (((CharacterBattle) ob).primary_weapon.getLevel() == weaponsAndItems.Weapon.Level.lev2)
						animation = battleFemaleCharacterAnimationSpearLev2;
					else if (((CharacterBattle) ob).primary_weapon.getLevel() == weaponsAndItems.Weapon.Level.lev3)
						animation = battleFemaleCharacterAnimationSpearLev3;
				}

			} else if (((CharacterBattle) ob).primary_weapon.getType() == Type.Sword) {
				if (((CharacterBattle) ob).male)
					animation = battleCharacterAnimationSwordLev1;
				else
					animation = battleFemaleCharacterAnimationSwordLev1;
			} else if (((CharacterBattle) ob).primary_weapon.getType() == Type.Bow) {
				if (((CharacterBattle) ob).male) {
					if (((CharacterBattle) ob).primary_weapon.getLevel() == weaponsAndItems.Weapon.Level.lev1)
						animation = battleCharacterAnimationBowLev1;
					else if (((CharacterBattle) ob).primary_weapon.getLevel() == weaponsAndItems.Weapon.Level.lev2)
						animation = battleCharacterAnimationBowLev2;
					else if (((CharacterBattle) ob).primary_weapon.getLevel() == weaponsAndItems.Weapon.Level.lev3)
						animation = battleCharacterAnimationBowLev3;
				} else {
					if (((CharacterBattle) ob).primary_weapon.getLevel() == weaponsAndItems.Weapon.Level.lev1)
						animation = battleFemaleAnimationBowLev1;
					else if (((CharacterBattle) ob).primary_weapon.getLevel() == weaponsAndItems.Weapon.Level.lev2)
						animation = battleFemaleAnimationBowLev2;
					else if (((CharacterBattle) ob).primary_weapon.getLevel() == weaponsAndItems.Weapon.Level.lev3)
						animation = battleFemaleAnimationBowLev3;
				}
			}
			break;
		case "NetworkCharacterBattle":
			// se sono il player
			if (((NetworkCharacterBattle) ob).player) {
				if (((NetworkCharacterBattle) ob).primary_weapon.getType() == Type.Spear) {
					if (((NetworkCharacterBattle) ob).primary_weapon.getLevel() == weaponsAndItems.Weapon.Level.lev1)
						animation = battleCharacterAnimationSpearLev1;
					else if (((NetworkCharacterBattle) ob).primary_weapon.getLevel() == weaponsAndItems.Weapon.Level.lev2)
						animation = battleCharacterAnimationSpearLev2;
					else if (((NetworkCharacterBattle) ob).primary_weapon.getLevel() == weaponsAndItems.Weapon.Level.lev3)
						animation = battleCharacterAnimationSpearLev3;
				} else if (((NetworkCharacterBattle) ob).primary_weapon.getType() == Type.Sword) {
					animation = battleCharacterAnimationSwordLev1;
				} else if (((NetworkCharacterBattle) ob).primary_weapon.getType() == Type.Bow) {
					if (((NetworkCharacterBattle) ob).primary_weapon.getLevel() == weaponsAndItems.Weapon.Level.lev1)
						animation = battleCharacterAnimationBowLev1;
					else if (((NetworkCharacterBattle) ob).primary_weapon.getLevel() == weaponsAndItems.Weapon.Level.lev2)
						animation = battleCharacterAnimationBowLev2;
					else if (((NetworkCharacterBattle) ob).primary_weapon.getLevel() == weaponsAndItems.Weapon.Level.lev3)
						animation = battleCharacterAnimationBowLev3;
				}
			} else {// se sono l'oggetto da diseganre è l'avversario
				if (((NetworkCharacterBattle) ob).primary_weapon.getType() == Type.Spear) {
					if (((NetworkCharacterBattle) ob).primary_weapon.getLevel() == weaponsAndItems.Weapon.Level.lev1)
						animation = enemyAnimationSpearLev1;
					else if (((NetworkCharacterBattle) ob).primary_weapon.getLevel() == weaponsAndItems.Weapon.Level.lev2)
						animation = enemyAnimationSpearLev2;
					else if (((NetworkCharacterBattle) ob).primary_weapon.getLevel() == weaponsAndItems.Weapon.Level.lev3)
						animation = enemyAnimationSpearLev3;
				} else if (((NetworkCharacterBattle) ob).primary_weapon.getType() == Type.Sword) {
					animation = enemyAnimationSword;
				} else if (((NetworkCharacterBattle) ob).primary_weapon.getType() == Type.Bow) {
					if (((NetworkCharacterBattle) ob).primary_weapon.getLevel() == weaponsAndItems.Weapon.Level.lev1)
						animation = enemyAnimationBowLev1;
					else if (((NetworkCharacterBattle) ob).primary_weapon.getLevel() == weaponsAndItems.Weapon.Level.lev2)
						animation = enemyAnimationBowLev2;
					else if (((NetworkCharacterBattle) ob).primary_weapon.getLevel() == weaponsAndItems.Weapon.Level.lev3)
						animation = enemyAnimationBowLev3;
				}

			}
			break;
		case "Enemy":
			if (((Enemy) ob).weapon.getType() == Type.Spear) {
				if (((Enemy) ob).weapon.getLevel() == weaponsAndItems.Weapon.Level.lev1)
					animation = enemyAnimationSpearLev1;
				else if (((Enemy) ob).weapon.getLevel() == weaponsAndItems.Weapon.Level.lev2)
					animation = enemyAnimationSpearLev2;
				else if (((Enemy) ob).weapon.getLevel() == weaponsAndItems.Weapon.Level.lev3)
					animation = enemyAnimationSpearLev3;
			} else if (((Enemy) ob).weapon.getType() == Type.Sword) {
				animation = enemyAnimationSword;
			} else if (((Enemy) ob).weapon.getType() == Type.Bow) {
				if (((Enemy) ob).weapon.getLevel() == weaponsAndItems.Weapon.Level.lev1)
					animation = enemyAnimationBowLev1;
				else if (((Enemy) ob).weapon.getLevel() == weaponsAndItems.Weapon.Level.lev2)
					animation = enemyAnimationBowLev2;
				else if (((Enemy) ob).weapon.getLevel() == weaponsAndItems.Weapon.Level.lev3)
					animation = enemyAnimationBowLev3;
			}
			break;
		case "NetworkEnemy":
			if (((NetworkEnemy) ob).weapon.getType() == Type.Spear) {
				if (((NetworkEnemy) ob).weapon.getLevel() == weaponsAndItems.Weapon.Level.lev1)
					animation = enemyAnimationSpearLev1;
				else if (((NetworkEnemy) ob).weapon.getLevel() == weaponsAndItems.Weapon.Level.lev2)
					animation = enemyAnimationSpearLev2;
				else if (((NetworkEnemy) ob).weapon.getLevel() == weaponsAndItems.Weapon.Level.lev3)
					animation = enemyAnimationSpearLev3;
			} else if (((NetworkEnemy) ob).weapon.getType() == Type.Sword) {
				animation = enemyAnimationSword;
			} else if (((NetworkEnemy) ob).weapon.getType() == Type.Bow) {
				if (((NetworkEnemy) ob).weapon.getLevel() == weaponsAndItems.Weapon.Level.lev1)
					animation = enemyAnimationBowLev1;
				else if (((NetworkEnemy) ob).weapon.getLevel() == weaponsAndItems.Weapon.Level.lev2)
					animation = enemyAnimationBowLev2;
				else if (((NetworkEnemy) ob).weapon.getLevel() == weaponsAndItems.Weapon.Level.lev3)
					animation = enemyAnimationBowLev3;
			}
			break;
		default:
			if (ob instanceof Enemy) {
				if (((Enemy) ob).weapon.getType() == Type.Spear) {
					if (((Enemy) ob).weapon.getLevel() == weaponsAndItems.Weapon.Level.lev1)
						animation = enemyAnimationSpearLev1;
					else if (((Enemy) ob).weapon.getLevel() == weaponsAndItems.Weapon.Level.lev2)
						animation = enemyAnimationSpearLev2;
					else if (((Enemy) ob).weapon.getLevel() == weaponsAndItems.Weapon.Level.lev3)
						animation = enemyAnimationSpearLev3;
				} else if (((Enemy) ob).weapon.getType() == Type.Sword) {
					animation = enemyAnimationSword;
				} else if (((Enemy) ob).weapon.getType() == Type.Bow) {
					if (((Enemy) ob).weapon.getLevel() == weaponsAndItems.Weapon.Level.lev1)
						animation = enemyAnimationBowLev1;
					else if (((Enemy) ob).weapon.getLevel() == weaponsAndItems.Weapon.Level.lev2)
						animation = enemyAnimationBowLev2;
					else if (((Enemy) ob).weapon.getLevel() == weaponsAndItems.Weapon.Level.lev3)
						animation = enemyAnimationBowLev3;
				}
			} else
				System.out.println("Errore in getAnimation");
			break;
		}
		return animation;
	}

	public TextureRegion getFrameStand(Object ob) {
		Class<? extends Object> a = ob.getClass();
		TextureRegion textureRegion = null;
		switch (a.getSimpleName()) {
		case "Player":
			if (((Player) ob).male)
				textureRegion = playerStand;
			else
				textureRegion = woman1Stand;
			break;
		case "NetworkPlayer":
			if (((NetworkPlayer) ob).player)
				textureRegion = playerStand;
			else
				textureRegion = otherPlayerStand;
			break;
		case "Man":
			textureRegion = man1Stand;
			break;
		case "Woman":
			if (((Woman) ob).type == WomanType.WOMAN1)
				textureRegion = woman1Stand;
			if (((Woman) ob).type == WomanType.WOMAN2)
				textureRegion = woman2Stand;
			break;
		case "CharacterBattle":
			if (((CharacterBattle) ob).male) {
				textureRegion = battleCharacterStand;
			} else {
				textureRegion = battleFemaleCharacterStand;
			}
			break;
		case "NetworkCharacterBattle":
			if (((NetworkCharacterBattle) ob).player) {
				textureRegion = battleCharacterStand;
			} else
				textureRegion = enemyStand;
			break;
		case "Enemy":
			textureRegion = enemyStand;
			break;
		case "NetworkEnemy":
			textureRegion = enemyStand;
			break;
		default:
			if (ob instanceof Enemy)
				textureRegion = enemyStand;
			else
				System.out.println("Errore in getFrameStand");
			break;
		}
		return textureRegion;
	}

	public Texture getTileImage(Object ob) {

		Texture texture = null;
		if (ob instanceof StaticObject) {

			Element element = ((StaticObject) ob).getElement();
			switch (element) {
			case HOME:
				texture = homeImage;
				break;
			case SHOP:
				texture = shopImage;
				break;
			case CASTLE:
				texture = castleImage;
				break;
			case THREE:
				texture = threeImage;
				break;
			case FOREST1:
				texture = forest1Image;
				break;
			case FOREST2:
				texture = forest2Image;
				break;
			case GROUND:
				texture = groundImage;
				break;
			case WATER:
				texture = waterImage;
				break;
			case ROCK:
				texture = rockImage;
				break;
			case LAMP:
				texture = lampImage;
				break;
			case LIGHTLAMP:
				texture = lightLampImage;
				break;
			case FLOOR:
				texture = floorImage;
				break;
			case FLOOR2:
				texture = floor2Image;
				break;
			case FLOOR3:
				texture = floor3Image;
				break;
			case PREENEMYHOME:
				texture = preEnemyHomeImage;
				break;
			case STRAW:
				texture = strawImage;
				break;
			case TEMPLE:
				texture = templeImage;
				break;
			case TABLE:
				texture = tableImage;
				break;
			case ROAD:
				texture = roadImage;
				break;
			case COIN:
				texture = coinImage;
				break;
			case PARCHMENT:
				if (((Item) ob).getLevel() == Level.FIRST)
					texture = parchment1Image;
				if (((Item) ob).getLevel() == Level.SECOND)
					texture = parchment2Image;
				break;
			case POTION:
				if (((Item) ob).getLevel() == Level.FIRST)
					texture = bluPotionImage;
				if (((Item) ob).getLevel() == Level.SECOND)
					texture = greenPotionImage;
				if (((Item) ob).getLevel() == Level.THIRD)
					texture = redPotionImage;
				break;
			default:
				break;
			}
		} else
			texture = coinImage;
		return texture;
	}

	public static Texture getArrowImage(boolean left) {
		if (left)
			return arrowLeft;
		else
			return arrowRight;
	}

	public static Texture getYouWinImage() {
		return youWinImage;
	}

	public static Texture getYouLoseImage() {
		return youLoseImage;
	}

}
