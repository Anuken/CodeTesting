package io.anuke.codetesting.modules.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.TimeUtils;

import io.anuke.codetesting.modules.Module;
import io.anuke.ucore.UCore;
import io.anuke.ucore.graphics.PixmapUtils;

//Results:
//Array spatial hash is about 2x faster.
public class PhysicsTester extends Module{
	SpriteBatch batch;
	BitmapFont font;
	Texture tex;
	Array<Entity> entities = new Array<Entity>();
	ArraySpatialHash<Entity> hash = new ArraySpatialHash<Entity>();
	ObjectSet<Entity> iterated = new ObjectSet<Entity>();
	long placetime = 0, locatetime, collidetime;
	float esize = 20;
	
	float range = 500;
	float crange = 30;
	float cellsize = 35;
	int num = 5000;

	public void init(){
		batch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
		font.getData().markupEnabled = true;
		tex = PixmapUtils.blankTexture();
		
		hash.gridsize = cellsize;

		for(int i = 0; i < num; i++)
			entities.add(new Entity(MathUtils.random(-range, range), MathUtils.random(-range, range)));
	}

	@Override
	public void update(){
		UCore.clearScreen(Color.BLACK);

		batch.begin();
		
		for(Entity entity : entities){
			batch.setColor(entity.color);
			batch.draw(tex, Gdx.graphics.getWidth() / 2 + entity.x - esize / 2,
					Gdx.graphics.getHeight() / 2 + entity.y - esize / 2, esize, esize);
		}
		
		batch.setColor(Color.WHITE);
		font.draw(batch, "[CORAL]" + placetime + "\n[SKY]" + locatetime + "\n[MAGENTA]" + collidetime + "\n[WHITE]" + 1000/60, 0, Gdx.graphics.getHeight());
		batch.end();

		if(Gdx.graphics.getFrameId() % 20 == 0){
			long ptime = TimeUtils.millis();

			hash.clear();
			for(Entity e : entities)
				hash.addEntity(e);

			placetime = TimeUtils.timeSinceMillis(ptime);

			for(int i = 0; i < 200; i++)
				hash.getNearbyEntities(0, 0, 100, (entity) -> {});

			locatetime = TimeUtils.timeSinceMillis(ptime+placetime);
			
			
			for(Entity entity : entities){
				hash.getNearbyEntities(entity.x, entity.y, crange, (other) -> {
					if(other == entity || iterated.contains(other)) return;
					//other logic
				});
						
				iterated.add(entity);
			}
			
			collidetime = TimeUtils.timeSinceMillis(ptime+placetime+locatetime);
		}
	}

	class Entity implements Spatial{
		float x, y;
		Color color = new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1f);

		public Entity(float x, float y) {
			this.x = x;
			this.y = y;
		}

		public float getX(){
			return x;
		}

		public float getY(){
			return y;
		}
	}

	@Override
	public void resize(int width, int height){
		batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
	}

}
