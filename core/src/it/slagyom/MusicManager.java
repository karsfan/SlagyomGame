package it.slagyom;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class MusicManager {
	public static Music mainMusic;
	public static Sound backgroundSound;
	public static Sound tickSound;
	public static Sound coinSound;
	public static Sound itemSound;
	public static Sound cashSound;

	public MusicManager() {
		mainMusic = LoadingMusic.mainMusic;
		backgroundSound = LoadingMusic.backgroundSound;
		tickSound = LoadingMusic.tickSound;
		coinSound = LoadingMusic.coinSound;
		itemSound = LoadingMusic.itemSound;
		cashSound = LoadingMusic.cashSound;
	}

	public static void play(String sound) {
		switch (sound) {
		case "COIN":
			coinSound.play(0.7f);
			break;
		case "ITEM":
			itemSound.play(1.0f);
			break;
		case "BACKGROUND":
			backgroundSound.loop(10.0f);
			break;
		case "TICK":
			tickSound.play(1.0f);
			break;
		case "MAINMUSIC":
			mainMusic.play();
			break;
		case "CASH":
			cashSound.play(1.0f);
		default:
			break;
		}
	}

	public void pause() {
		if (mainMusic.isPlaying())
			mainMusic.pause();
		backgroundSound.pause();
	}
}
