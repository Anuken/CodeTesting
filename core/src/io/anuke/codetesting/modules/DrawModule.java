package io.anuke.codetesting.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class DrawModule extends Module{
	public SpriteBatch batch = new SpriteBatch();
	public float scale = 1f;
	public float w, h;
	
	public void update(){};
	
	public void resize(){
		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	public void resize(int width, int height){
		batch.getProjectionMatrix().setToOrtho2D(0, 0, width/scale, height/scale);
		this.w = width/scale;
		this.h = height/scale;
	}
}
