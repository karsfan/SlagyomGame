package it.slagyom.desktop;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import it.slagyom.GameSlagyom;

public class DesktopLauncher {
	public static void main(String[] arg) {
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "SLAGYOM";
		config.width = 854;
		config.height = 480; 
		config.resizable = false;
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		gd.getDisplayMode().getWidth();
		new LwjglApplication(new GameSlagyom(), config);

	}
}
