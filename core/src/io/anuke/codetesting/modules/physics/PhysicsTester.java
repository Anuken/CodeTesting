package io.anuke.codetesting.modules.physics;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import io.anuke.codetesting.modules.Module;
import io.anuke.ucore.UCore;

public class PhysicsTester extends Module{
	SpriteBatch batch;
	BitmapFont font;
	Array<Entity> entities = new Array<Entity>();
	SpatialHash<Entity> hash = new SpatialHash<Entity>();
	long elapsed = 0;
	
	public void init(){
		batch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
	}
	
	@Override
	public void update(){
		UCore.clearScreen(Color.BLACK);
		
		batch.begin();
		font.draw(batch, elapsed + "", 0, 0);
		batch.end();
		
		long time = TimeUtils.millis();
		
		hash.clear();
		for(Entity e : entities)
			hash.addEntity(e);
		
		elapsed = TimeUtils.timeSinceMillis(time);
	}
	
	class Entity implements Spatial{
		float x, y;

		public float getX(){
			return x;
		}

		public float getY(){
			return y;
		}
	}

}
