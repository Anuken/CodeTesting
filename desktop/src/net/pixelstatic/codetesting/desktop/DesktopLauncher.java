package net.pixelstatic.codetesting.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import io.anuke.codetesting.exampleuni.UniCore;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("CodeTesting");
		config.setMaximized(true);
		//config.useVsync(false);
		new Lwjgl3Application(new UniCore(), config);
	}
}
