package gameManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class LoadingMusic {

	public Music mainMusic;
	public Music battleMusic;
	public Sound backgroundSound;
	public Sound tickSound;
	public Sound coinSound;
	public Sound itemSound;
	public Sound cashSound;
	public Sound swordSound;
	public Sound selectionSound;
	public Sound upgradeSound;
	public Sound arrowSound;
	public Sound bombSound;
	public Sound overMenuSound;

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
		arrowSound = Gdx.audio.newSound(Gdx.files.internal("res/audio/arrow.ogg"));
		bombSound = Gdx.audio.newSound(Gdx.files.internal("res/audio/bomb.ogg"));
		overMenuSound = Gdx.audio.newSound(Gdx.files.internal("res/audio/over.ogg"));
	}

}
