package io.anuke.codetesting.modules.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.TimeUtils;

import io.anuke.codetesting.modules.Module;
import io.anuke.gif.GifRecorder;
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
	Entity player = new Entity(0,0);
	long placetime = 0, locatetime, collidetime;
	float esize = 10;

	float range = 400;
	float crange = esize*2;
	float cellsize = esize*2;
	int num = 1500;
	GifRecorder record;

	public void init(){
		batch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
		font.getData().markupEnabled = true;
		tex = PixmapUtils.blankTexture();
		record = new GifRecorder(batch);
		entities.add(player);

		hash.gridsize = cellsize;

		for(int i = 0; i < num; i++)
			entities.add(new Entity(MathUtils.random(-range, range), MathUtils.random(-range, range)));
	}

	@Override
	public void update(){
		float s = 1f;
		
		if(Gdx.input.isKeyPressed(Keys.W)) player.velocity.y += s;
		if(Gdx.input.isKeyPressed(Keys.A)) player.velocity.x -= s;
		if(Gdx.input.isKeyPressed(Keys.S)) player.velocity.y -= s;
		if(Gdx.input.isKeyPressed(Keys.D)) player.velocity.x += s;
		
		player.velocity.limit(3f);
		
		
		UCore.clearScreen(Color.BLACK);

		batch.begin();

		for(Entity entity : entities){
			batch.setColor(entity.color);
			batch.draw(tex, Gdx.graphics.getWidth() / 2 + entity.x - esize / 2,
					Gdx.graphics.getHeight() / 2 + entity.y - esize / 2, esize, esize);
			entity.update();
		}

		batch.setColor(Color.WHITE);
		font.draw(batch,
				entities.size +"\n[CORAL]" + placetime + "\n[SKY]" + locatetime + "\n[MAGENTA]" + collidetime + "\n[WHITE]" + 1000 / 60,
				0, Gdx.graphics.getHeight());
		batch.end();
		
		batch.begin();
		record.update();
		batch.end();

		long t = TimeUtils.millis();

		iterated.clear();
		

		for(Entity entity : entities){
			hash.getNearbyEntities(entity.x, entity.y, crange, (other) -> {
				if(other == entity || iterated.contains(other))
					return;

				if(Math.abs(entity.x - other.x) < esize && Math.abs(entity.y - other.y) < esize){
					collide(entity, other);
				}

			});

			

			iterated.add(entity);
		}

		if(Gdx.graphics.getFrameId() % 20 == 0)
			collidetime = TimeUtils.timeSinceMillis(t);

		if(Gdx.graphics.getFrameId() % 20 == 0){
			long ptime = TimeUtils.millis();

			hash.clear();
			for(Entity e : entities)
				hash.addEntity(e);

			placetime = TimeUtils.timeSinceMillis(ptime);

			//for(int i = 0; i < 200; i++)
			//	hash.getNearbyEntities(0, 0, 100, (entity) -> {});

			locatetime = TimeUtils.timeSinceMillis(ptime + placetime);
		}
	}
	
	void collide(Entity a, Entity b){
		Vector2 vector = new Vector2(a.x-b.x, a.y-b.y).nor();
		
		//vector.setAngle((int)(vector.angle()/90)*90);
		
		a.velocity.add(vector.scl(1f));
		b.velocity.add(vector.scl(-1f));
		
	}

	class CollisionParams{
		Vector2 normal;
		float penetration;

		public CollisionParams(Vector2 v, float f) {
			this.normal = v;
			this.penetration = f;
		}
	}

	class Entity implements Spatial{
		float x, y;
		Vector2 velocity = new Vector2();//.setToRandomDirection().scl(3f);
		Color color;

		public Entity(float x, float y) {
			this.x = x;
			this.y = y;
			this.color = new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1f);;
		}

		public void update(){
			//velocity.add(new Vector2(Gdx.input.getX() - (x+Gdx.graphics.getWidth()/2), (Gdx.graphics.getHeight() - Gdx.input.getY()) - (y+Gdx.graphics.getHeight()/2)).nor().scl(1f)).limit(3f);
			velocity.scl(0.9f);
			x += velocity.x;
			y += velocity.y;

			if(Math.abs(x) > range)
				velocity.x *= -1;

			if(Math.abs(y) > range)
				velocity.y *= -1;

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
