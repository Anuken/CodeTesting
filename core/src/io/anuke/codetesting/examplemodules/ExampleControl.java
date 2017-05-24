package io.anuke.codetesting.examplemodules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;

import io.anuke.ucore.core.Draw;
import io.anuke.ucore.entities.Effect;
import io.anuke.ucore.entities.Entities;
import io.anuke.ucore.graphics.Atlas;
import io.anuke.ucore.graphics.Hue;
import io.anuke.ucore.modules.RendererModule;

public class ExampleControl extends RendererModule<ExampleMain>{
	
	public ExampleControl(){
		atlas = new Atlas("codetesting.pack");
		
		Effect.create("hit", 10, e->{
			Draw.thickness(3f);
			Draw.color(Hue.mix(Color.WHITE, Color.ORANGE, e.ifract()));
			Draw.spikes(e.x, e.y, 5+e.ifract()*40f, 10, 8);
			Draw.reset();
		});
		
		Entities.initPhysics();
	}
	
	@Override
	public void update(){
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE))
			Gdx.app.exit();
		
		setCamera(0, 0);
		
		Entities.update();
		
		drawDefault();
	}
	
	@Override
	public void draw(){
		Entities.draw();
	}
	
	@Override
	public void resize(int width, int height){
		super.resize(width, height);
		Entities.resizeTree(-width/2f, -height/2f, width, height);
	}
}