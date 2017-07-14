package gameManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class LoadingMusic {
	public   Music mainMusic;
	public Sound backgroundSound;
	public Sound tickSound;
	public Sound coinSound;
	public Sound itemSound;
	public Sound cashSound;
	public Sound swordSound;


	public LoadingMusic() {
		backgroundSound = Gdx.audio.newSound(Gdx.files.internal("res/audio/backgroundAudio.ogg"));
		tickSound = Gdx.audio.newSound(Gdx.files.internal("res/audio/tick.ogg"));
		mainMusic = Gdx.audio.newMusic(Gdx.files.internal("res/audio/mainMusic.mp3"));
		coinSound = Gdx.audio.newSound(Gdx.files.internal("res/audio/coin.ogg"));
		itemSound = Gdx.audio.newSound(Gdx.files.internal("res/audio/item.ogg"));
		cashSound = Gdx.audio.newSound(Gdx.files.internal("res/audio/cash.mp3"));
		swordSound = Gdx.audio.newSound(Gdx.files.internal("res/audio/sword.ogg"));
	}

	public  void pause() {
		if (mainMusic.isPlaying())
			mainMusic.pause();
		backgroundSound.pause();
	}
}
