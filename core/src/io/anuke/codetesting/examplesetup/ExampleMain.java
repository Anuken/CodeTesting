package io.anuke.codetesting.examplesetup;

import com.badlogic.gdx.Input.Keys;

import io.anuke.codetesting.examplesetup.entities.ExampleEnemy;
import io.anuke.codetesting.examplesetup.entities.ExamplePlayer;
import io.anuke.ucore.core.KeyBinds;
import io.anuke.ucore.core.Settings;
import io.anuke.ucore.modules.ModuleController;

public class ExampleMain extends ModuleController<ExampleMain>{
	public static ExampleMain i;
	
	public ExamplePlayer player;
	public ExampleEnemy enemy;
	
	public ExampleMain(){
		i = this;
	}
	
	@Override
	public void init(){
		addModule(ExampleControl.class);
		addModule(ExampleUI.class);
	}
	
	@Override
	public void setup(){
		
		KeyBinds.defaults(
			"up", Keys.W,
			"left", Keys.A,
			"down", Keys.S,
			"right", Keys.D
		);
		
		Settings.loadAll("io.anuke.codetesting.example");
		
		player = new ExamplePlayer().add();
		enemy = new ExampleEnemy().add();
	}
}
