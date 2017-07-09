package gameManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

import battle.CharacterBattle;
import battle.Enemy;
import character.DynamicObjects;
import character.Woman;
import character.DynamicObjects.StateDynamicObject;
import character.Woman.WomanType;
import multiplayer.NetworkPlayer;
import staticObjects.Item;
import staticObjects.StaticObject;
import staticObjects.Item.Level;
import staticObjects.StaticObject.Element;

public class LoadingImage {

	public Texture texture;
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
	private static Texture forest1Image;
	private static Texture forest2Image;
	private static Texture tableImage;
	private static Texture coinImage;
	private static Texture battleBackground;
	private static Texture bluPotionImage;
	private static Texture redPotionImage;
	private static Texture greenPotionImage;
	private static Texture youWinImage;
	private static Texture youLoseImage;
	private static Texture castleImage;
	public static Texture pointer;
	public static Pixmap cursor;
	public static Pixmap noCursor;

	public static TextureRegion battleCharacterStand;
	public static Animation<TextureRegion>[] battleCharacterAnimation;

	public static TextureRegion playerStand;
	public static Animation<TextureRegion>[] playerAnimation;

	public static TextureRegion enemyStand;
	public static Animation<TextureRegion>[] enemyAnimation;

	public static TextureRegion man1Stand;
	public static Animation<TextureRegion>[] man1Animation;

	// private static TextureRegion man2Stand;
	public Animation<TextureRegion>[] man2Animation;

	// private static TextureRegion man3Stand;
	public Animation<TextureRegion>[] man3Animation;

	public static TextureRegion woman1Stand;
	public static Animation<TextureRegion>[] woman1Animation;

	private static TextureRegion woman2Stand;
	public static Animation<TextureRegion>[] woman2Animation;

	// private static TextureRegion woman3Stand;
	public Animation<TextureRegion>[] woman3Animation;

	// BAG IMAGE BUTTONS
	public static ImageButton bluePotion;
	public static ImageButton redPotion;
	public static ImageButton greenPotion;
	public static ImageButton bomb;
	public static ImageButton parchment;
	public static ImageButton spear;
	public static ImageButton sword;
	public static ImageButton bow;
	public static ImageButton emptyBagIcon;
	public static ImageButton emptyShopIcon;
	public static ImageButton leftArrow;
	public static ImageButton rightArrow;
	public static ImageButton howMuch;

	// AUDIO & MUSIC

	@SuppressWarnings("unchecked")
	public LoadingImage() {
		// TILES IMAGES
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
		forest1Image = new Texture("res/forest1.png");
		forest2Image = new Texture("res/forest2.png");
		tableImage = new Texture("res/table.png");
		battleBackground = new Texture("res/battleBg6.png");
		castleImage = new Texture("res/castle.png");
		youWinImage = new Texture("res/youWin.png");
		youLoseImage = new Texture("res/youLose.png");
		// WORLD ITEM IMAGES
		coinImage = new Texture("res/coin.png");
		bluPotionImage = new Texture("res/bluePotion.png");
		greenPotionImage = new Texture("res/greenPotion.png");
		redPotionImage = new Texture("res/redPotion.png");
		pointer = new Texture("res/pointer.png");

		// BAG AND SHOP ITEM IMAGES
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
		rightArrow = new ImageButton(
				new TextureRegionDrawable(new TextureRegion(new Texture("res/bag/rightArrow.png"))));
		leftArrow = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("res/bag/leftArrow.png"))));

		emptyBagIcon = new ImageButton(
				new TextureRegionDrawable(new TextureRegion(new Texture("res/bag/emptyIcon.png"))));
		emptyShopIcon = new ImageButton(
				new TextureRegionDrawable(new TextureRegion(new Texture("res/shop/emptyIcon.png"))));
		howMuch = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("res/shop/howMuch.png"))));
		cursor = new Pixmap(Gdx.files.internal("res/cursor.png"));
		noCursor = new Pixmap(Gdx.files.internal("res/noCursor.png"));

		// WORLD ANIMATIONS
		playerAnimation = new Animation[4];
		enemyAnimation = new Animation[4];
		man1Animation = new Animation[4];
		man2Animation = new Animation[4];
		man3Animation = new Animation[4];
		woman1Animation = new Animation[4];
		woman2Animation = new Animation[4];
		woman3Animation = new Animation[4];

		playerStand = new TextureRegion();
		battleCharacterStand = new TextureRegion();
		battleCharacterAnimation = new Animation[4];
		enemyStand = new TextureRegion();
		man1Stand = new TextureRegion();
		woman1Stand = new TextureRegion();
		woman2Stand = new TextureRegion();

		texture = new Texture("bpj.png");
		createFrame(texture, playerAnimation, playerStand);

		texture = new Texture("notPlaying.png");
		createFrame(texture, man1Animation, man1Stand);

		texture = new Texture("NotPlaying/woman1.png");
		createFrame(texture, woman1Animation, woman1Stand);

		texture = new Texture("NotPlaying/woman2.png");
		createFrame(texture, woman2Animation, woman2Stand);

		texture = new Texture("lancia.png");
		createBattleFrame(texture, battleCharacterAnimation, battleCharacterStand);

		texture = new Texture("lancia.png");
		createBattleFrame(texture, enemyAnimation, enemyStand);

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

	public static TextureRegion getBattleFrame(Object ob) {
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
			region = getFrameStand(ob);
			break;
		default:
			region = getFrameStand(ob);
			break;
		}
		return region;
	}

	public static TextureRegion getFrame(Object ob) {
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
			region = getFrameStand(ob);
			break;
		default:
			region = getFrameStand(ob);
			break;
		}
		return region;
	}

	public static void setFrameDurationCharacter(float frameDuration) {
		playerAnimation[0].setFrameDuration(frameDuration);
		playerAnimation[1].setFrameDuration(frameDuration);
		playerAnimation[2].setFrameDuration(frameDuration);
		playerAnimation[3].setFrameDuration(frameDuration);
	}

	public static Animation<TextureRegion>[] getAnimation(Object ob) {
		Class<? extends Object> a = ob.getClass();
		Animation<TextureRegion>[] animation = null;

		switch (a.getSimpleName()) {
		case "Player":
			animation = playerAnimation;
			break;
		case "NetworkPlayer":
			if (((NetworkPlayer) ob).player)
				animation = playerAnimation;
			else
				animation = man1Animation;
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
			animation = battleCharacterAnimation;
			break;
		case "NetworkCharacterBattle":
			animation = battleCharacterAnimation;
			break;
		case "Enemy":
			animation = enemyAnimation;
			break;
		case "NetworkEnemy":
			animation = enemyAnimation;
			break;
			
		default:
			
			System.out.println("Errore in getAnimation");
			break;
		}
		if (ob instanceof Enemy)
			animation = enemyAnimation;
		return animation;
	}

	public static TextureRegion getFrameStand(Object ob) {
		Class<? extends Object> a = ob.getClass();
		TextureRegion textureRegion = null;
		switch (a.getSimpleName()) {
		case "Player":
			textureRegion = playerStand;
			break;
		case "NetworkPlayer":
			if (((NetworkPlayer) ob).player)
				textureRegion = playerStand;
			else
				textureRegion = man1Stand;
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
			textureRegion = battleCharacterStand;
			break;
		case "NetworkCharacterBattle":
			textureRegion = battleCharacterStand;
			break;
		case "Enemy":
			textureRegion = enemyStand;
			break;
		case "NetworkEnemy":
			textureRegion = enemyStand;
			break;
		default:
			System.out.println("Errore in getFrameStand");
			break;
		}
		if (ob instanceof Enemy)
			textureRegion = enemyStand;
		return textureRegion;
	}

	public static Texture getTileImage(Object ob) {

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

	public static Texture getYouWinImage() {
		return youWinImage;
	}

	public static Texture getYouLoseImage() {
		return youLoseImage;
	}

}
