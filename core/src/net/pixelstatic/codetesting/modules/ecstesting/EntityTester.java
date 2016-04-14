package net.pixelstatic.codetesting.modules.ecstesting;

import net.pixelstatic.codetesting.modules.Module;
import net.pixelstatic.codetesting.utils.Atlas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ObjectMap;

public class EntityTester extends Module{
	ObjectMap<Long, Entity> entities = new ObjectMap<Long, Entity>();
	public SpriteBatch batch;
	public Atlas atlas;
	
	public void init(){
		batch = new SpriteBatch();
		atlas = new Atlas(Gdx.files.internal("sprites/codetesting.pack"));
	}

	@Override
	public void update(){
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)) Gdx.app.exit();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		
		batch.end();
	}

}
