package io.anuke.codetesting.exampleuni;

import static io.anuke.codetesting.exampleuni.Vars.*;

import java.util.function.BooleanSupplier;

import com.badlogic.gdx.Gdx;

import io.anuke.ucore.modules.SceneModule;
import io.anuke.ucore.scene.builders.build;
import io.anuke.ucore.scene.builders.button;
import io.anuke.ucore.scene.builders.table;
import io.anuke.ucore.scene.ui.KeybindDialog;
import io.anuke.ucore.scene.ui.SettingsDialog;
import io.anuke.ucore.scene.ui.TextDialog;

public class UniUI extends SceneModule{
	BooleanSupplier play = ()->{return playing;};
	BooleanSupplier nplay = ()->{return !playing;};
	KeybindDialog keybind;
	SettingsDialog settings;
	TextDialog about;
	
	@Override
	public void init(){
		about = new TextDialog("About", aboutText).padText(10);
		
		keybind = new KeybindDialog();
		settings = new SettingsDialog();
		
		settings.volumePrefs();
		settings.screenshakePref();
		
		build.begin(scene);
		
		new table(){{
			defaults().width(200);
			
			new button("Play", ()->{
				playing = true;
			});
			
			row();
			
			new button("Settings", ()->{
				settings.show();
			});
			
			row();
			
			new button("Controls", ()->{
				keybind.show();
			});
			
			row();
			
			new button("About", ()->{
				about.show();
			});
			
			row();
			
			new button("exit", ()->{
				Gdx.app.exit();
			});
			
			visible(nplay);
		}};
		
		build.end();
	}
}
