package io.anuke.codetesting.examplesetup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import io.anuke.ucore.entities.Entities;
import io.anuke.ucore.graphics.Atlas;
import io.anuke.ucore.modules.RendererModule;

public class ExampleControl extends RendererModule<ExampleMain>{
	
	public ExampleControl(){
		atlas = new Atlas("codetesting.pack");
		
		Entities.initPhysics(0, 0, 0, 0);
	}
	
	@Override
	public void update(){
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE))
			Gdx.app.exit();
		
		camera.position.set(0, 0, 0);
		
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
