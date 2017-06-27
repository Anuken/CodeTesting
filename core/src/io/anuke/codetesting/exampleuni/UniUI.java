package io.anuke.codetesting.exampleuni;

import static io.anuke.codetesting.exampleuni.Vars.*;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;

import io.anuke.codetesting.exampleuni.GameState.State;
import io.anuke.ucore.core.DrawContext;
import io.anuke.ucore.function.VisibilityProvider;
import io.anuke.ucore.modules.SceneModule;
import io.anuke.ucore.scene.builders.*;
import io.anuke.ucore.scene.ui.*;

public class UniUI extends SceneModule{
	VisibilityProvider play = ()->{return GameState.is(State.playing);};
	VisibilityProvider inmenu = ()->{return GameState.is(State.menu);};
	
	KeybindDialog keybind;
	SettingsDialog settings;
	TextDialog about;
	TextDialog tutorial;
	Dialog paused;
	
	@Override
	public void init(){
		DrawContext.font.setUseIntegerPositions(true);
		
		paused = new Dialog("Paused", "dialog");
		
		about = new TextDialog("About", aboutText).padText(10);
		about.left();
		tutorial = new TextDialog("Tutorial", tutorialText).padText(10);
		
		keybind = new KeybindDialog();
		settings = new SettingsDialog();
		
		settings.volumePrefs();
		settings.screenshakePref();
		
		build.begin(paused.content());
		
		paused.content().defaults().width(200);
		
		new button("Resume", ()->{
			GameState.set(State.playing);
			paused.hide();
		});
		
		paused.content().row();
		
		new button("Settings", ()->{
			settings.show();
		});
		
		paused.content().row();
		
		new button("Controls", ()->{
			keybind.show();
		});
		
		paused.content().row();
		
		new button("Back to Menu", ()->{
			paused.hide();
			//TODO back to menu code
			GameState.set(State.menu);
		});
		
		build.end();
		
		build.begin(scene);
		
		new table(){{
			defaults().width(200);
			
			new button("Play", ()->{
				//TODO playing setup code, call reset()
				GameState.set(State.playing);
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
			
			if(Gdx.app.getType() == ApplicationType.Desktop)
			new button("Exit", ()->{
				Gdx.app.exit();
			});
			
			visible(inmenu);
		}}.end();
		
		new table(){{
			atop();
			
			new label("%APP_NAME%").scale(2);
			
			visible(inmenu);
		}}.end();
		
		build.end();
	}
	
	public void hidePaused(){
		paused.hide();
	}
	
	public void showPaused(){
		paused.show();
	}
}
