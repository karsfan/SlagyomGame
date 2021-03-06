package it.slagyom.desktop;


import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import gameManager.GameSlagyom;

public class DesktopLauncher {
	public static void main(String[] arg) {
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "SLAGYOM";
		config.width = 854;
		config.height = 480; 
		config.resizable = false;
        config.addIcon("res/gameIcon.png", FileType.Internal);
		new LwjglApplication(new GameSlagyom(), config);

	}
}
