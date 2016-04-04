package net.pixelstatic.codetesting.desktop;

import net.pixelstatic.codetesting.CodeTester;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config.samples = 10;
		new LwjglApplication(new CodeTester(), config);
	}
}
