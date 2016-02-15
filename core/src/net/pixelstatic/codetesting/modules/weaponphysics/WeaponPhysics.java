package net.pixelstatic.codetesting.modules.weaponphysics;

import net.pixelstatic.codetesting.modules.Module;
import net.pixelstatic.codetesting.utils.Atlas;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class WeaponPhysics extends Module{
	WeaponWorld world;
	Atlas atlas;
	SpriteBatch batch;
	
	void draw(){
		for(int x = 0; x < world.size; x ++){
			for(int y = 0; y < world.size; y ++){
				
			}
		}
	}
	
	void input(){
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)) Gdx.app.exit();
	}

	@Override
	public void init(){
		batch = new SpriteBatch();
		atlas = new Atlas(Gdx.files.internal("sprites/codetesting.pack"));
		world = getModule(WeaponWorld.class);
	}

	@Override
	public void update(){
		input();
		clear();
		batch.begin();
		draw();
		batch.end();
	}
	
	void clear(){
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
}
