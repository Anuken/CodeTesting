package net.pixelstatic.codetesting.desktop;

import java.awt.Toolkit;

import net.pixelstatic.codetesting.CodeTester;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		Toolkit tk = Toolkit.getDefaultToolkit();
		//config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
		config.setWindowedMode(tk.getScreenSize().width, tk.getScreenSize().height);
		config.disableAudio(true);
		config.setTitle("CodeTesting");
		new Lwjgl3Application(new CodeTester(), config);
	}
}
