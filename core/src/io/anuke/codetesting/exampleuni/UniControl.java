package io.anuke.codetesting.exampleuni;

import com.badlogic.gdx.Input.Keys;

import io.anuke.codetesting.exampleuni.GameState.State;
import io.anuke.ucore.core.Inputs;
import io.anuke.ucore.core.KeyBinds;
import io.anuke.ucore.core.Settings;
import io.anuke.ucore.entities.Entities;
import io.anuke.ucore.graphics.Atlas;
import io.anuke.ucore.modules.ControlModule;

public class UniControl extends ControlModule{
	
	public UniControl(){
		atlas = new Atlas("test.pack");
		
		KeyBinds.defaults(
			"up", Keys.W,
			"left", Keys.A,
			"down", Keys.S,
			"right", Keys.D,
			"pause", Keys.ESCAPE
		);
			
		Settings.loadAll("%PACKAGE%");
		
		Entities.initPhysics();
		
		EffectLoader.load();
	}
	
	@Override
	public void update(){
		
		if(GameState.is(State.playing)){
			setCamera(0, 0);
			
			if(Inputs.keyUp("pause")){
				GameState.set(State.paused);
				Vars.ui.showPaused();
			}
		}else if(GameState.is(State.paused)){
			if(Inputs.keyUp("pause")){
				GameState.set(State.playing);
				Vars.ui.hidePaused();
			}
		}
		
		Entities.update();
		drawDefault();
	}
	
	@Override
	public void draw(){
		Entities.draw();
	}
	
	@Override
	public void resize(){
		Entities.resizeTree(-screen.x/2f, -screen.y/2f, screen.x, screen.y);
	}
}
