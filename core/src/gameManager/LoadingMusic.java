package gameManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class LoadingMusic {
<<<<<<< HEAD
	public   Music mainMusic;
	public Sound backgroundSound;
	public Sound tickSound;
	public Sound coinSound;
	public Sound itemSound;
	public Sound cashSound;
	public Sound swordSound;
=======
	public static Music mainMusic;
	public static Music battleMusic;
	public static Sound backgroundSound;
	public static Sound tickSound;
	public static Sound coinSound;
	public static Sound itemSound;
	public static Sound cashSound;
	public static Sound swordSound;
	public static Sound selectionSound;
	public static Sound upgradeSound;
>>>>>>> f9a7cb937188b86483025816301e87b63457db8c


	public LoadingMusic() {
		mainMusic = Gdx.audio.newMusic(Gdx.files.internal("res/audio/mainMusic.mp3"));
		battleMusic = Gdx.audio.newMusic(Gdx.files.internal("res/audio/battleMusic.ogg"));
		backgroundSound = Gdx.audio.newSound(Gdx.files.internal("res/audio/backgroundAudio.ogg"));		
		tickSound = Gdx.audio.newSound(Gdx.files.internal("res/audio/tick.ogg"));
		coinSound = Gdx.audio.newSound(Gdx.files.internal("res/audio/coin.ogg"));
		itemSound = Gdx.audio.newSound(Gdx.files.internal("res/audio/item.ogg"));
		cashSound = Gdx.audio.newSound(Gdx.files.internal("res/audio/cash.mp3"));
		swordSound = Gdx.audio.newSound(Gdx.files.internal("res/audio/sword.ogg"));
		selectionSound = Gdx.audio.newSound(Gdx.files.internal("res/audio/selection.ogg"));
		upgradeSound = Gdx.audio.newSound(Gdx.files.internal("res/audio/upgrade.ogg"));
	}

	public  void pause() {
		if (mainMusic.isPlaying())
			mainMusic.pause();
		backgroundSound.pause();
	}
}
