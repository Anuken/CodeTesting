package io.anuke.codetesting.examplemodules;

import io.anuke.ucore.modules.SceneModule;
import io.anuke.ucore.scene.builders.build;
import io.anuke.ucore.scene.builders.button;
import io.anuke.ucore.scene.builders.table;
import io.anuke.ucore.scene.ui.KeybindDialog;
import io.anuke.ucore.scene.ui.ListDialog;
import io.anuke.ucore.scene.ui.SettingsDialog;

public class ExampleUI extends SceneModule<ExampleMain>{
	public ListDialog settings;
	public SettingsDialog graphics, game;
	
	public ExampleUI(){
		skin.font().setUseIntegerPositions(false);
	}
	
	@Override
	public void init(){
		graphics = new SettingsDialog();
		game = new SettingsDialog();
		
		settings 
		= new ListDialog("settings")
			.addOption("Controls", ()->{
				new KeybindDialog().show(scene);
			})
			.addOption("Graphics", ()->{
				graphics.show(scene);
			})
			.addOption("Game", ()->{
				game.show(scene);
			});
		
		settings.addCloseButton();
		
		graphics.checkPref("garbage", "Garbage", true);
		graphics.volumePrefs();
		graphics.screenshakePref();
		
		build.begin(scene);
		new table(){{
			aleft();
			atop();
			new button("Press me.", ()->{
				settings.show(scene);
			});
		}};
		build.end();
	}
}
