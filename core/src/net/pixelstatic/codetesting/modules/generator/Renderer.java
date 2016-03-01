package net.pixelstatic.codetesting.modules.generator;

import net.pixelstatic.codetesting.modules.Module;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Renderer extends Module{
	SpriteBatch batch;
	
	public void init(){
		batch = new SpriteBatch();
	}
	
	@Override
	public void update(){
		
	}

}
