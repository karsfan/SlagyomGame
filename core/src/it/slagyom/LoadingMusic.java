package it.slagyom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class LoadingMusic {
	public static Music mainMusic;
	public static Sound backgroundSound;
	public static Sound tickSound;
	public static Sound coinSound;
	public static Sound itemSound;
	public static Sound cashSound;


	public LoadingMusic() {
		backgroundSound = Gdx.audio.newSound(Gdx.files.internal("assets/res/audio/backgroundAudio.ogg"));
		tickSound = Gdx.audio.newSound(Gdx.files.internal("assets/res/audio/tick.ogg"));
		mainMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/res/audio/mainMusic.mp3"));
		coinSound = Gdx.audio.newSound(Gdx.files.internal("assets/res/audio/coin.ogg"));
		itemSound = Gdx.audio.newSound(Gdx.files.internal("assets/res/audio/item.ogg"));
		cashSound = Gdx.audio.newSound(Gdx.files.internal("assets/res/audio/cash.mp3"));

	}
}
