package io.anuke.codetesting.exampleuni;

import static io.anuke.codetesting.exampleuni.Vars.*;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;

import io.anuke.codetesting.exampleuni.entities.UniEnemy;
import io.anuke.codetesting.exampleuni.entities.UniPlayer;
import io.anuke.ucore.core.*;
import io.anuke.ucore.entities.Effect;
import io.anuke.ucore.entities.Entities;
import io.anuke.ucore.graphics.Hue;
import io.anuke.ucore.modules.RendererModule;

public class UniControl extends RendererModule{
	
	public UniControl(){
		KeyBinds.defaults(
			"up", Keys.W,
			"left", Keys.A,
			"down", Keys.S,
			"right", Keys.D
		);
			
		Settings.loadAll("io.anuke.codetesting.example");
		
		Sounds.load("shoot.wav");
		
		player = new UniPlayer().add();
		enemy = new UniEnemy().add();
		
		Entities.initPhysics();
		
		Effect.create("hit", 10, e->{
			Draw.thickness(3f);
			Draw.color(Hue.mix(Color.WHITE, Color.ORANGE, e.ifract()));
			Draw.spikes(e.x, e.y, 5+e.ifract()*40f, 10, 8);
			Draw.reset();
		});
	}
	
	@Override
	public void update(){
		if(Inputs.keyUp(Keys.ESCAPE))
			playing = !playing;
		
		if(Inputs.keyUp(Keys.SPACE))
			Effects.sound("shoot", enemy);
		
		setCamera(player.x, player.y);
		
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
