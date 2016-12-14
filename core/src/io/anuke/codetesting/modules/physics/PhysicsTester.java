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
import io.anuke.ucore.UCore;
import io.anuke.ucore.graphics.PixmapUtils;
import io.anuke.utils.io.GifRecorder;

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
	float esize = 1;

	float range = 500;
	float crange = esize*2;
	float cellsize = 3;
	int num = 2000;
	GifRecorder record;

	public void init(){
		batch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
		font.getData().markupEnabled = true;
		tex = PixmapUtils.blankTexture();
		record = new GifRecorder(batch);

		hash.gridsize = cellsize;

		//for(int i = 0; i < num; i++)
		//	entities.add(new Entity(MathUtils.random(-range, range), MathUtils.random(-range, range)));
	}

	@Override
	public void update(){
		
		if(Gdx.input.isKeyJustPressed(Keys.SPACE))
			for(int i = 0; i < num; i++){
				//float x = (float)Math.cos(i/num*Math.PI*2)*100;
				//float y = (float)Math.sin(i/num*Math.PI*2)*100;
				Entity entity = new Entity(0,0);
				entities.add(entity);
			}
		
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
		
		if(false)
		for(Entity entity : entities){
			hash.getNearbyEntities(entity.x, entity.y, crange, (other) -> {
				if(other == entity || iterated.contains(other))
					return;

				CollisionParams p = collide(entity, other);
				if(p != null)
					resolveCollision(entity, other, p.normal);

			});

			

			iterated.add(entity);
		}

		if(Gdx.graphics.getFrameId() % 20 == 0)
			collidetime = TimeUtils.timeSinceMillis(t);
		if(false)
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

	void resolveCollision(Entity a, Entity b, Vector2 normal){

		// Calculate relative velocity
		Vector2 rv = b.velocity.cpy().sub(a.velocity);

		// Calculate relative velocity in terms of the normal direction
		float velAlongNormal = Vector2.dot(rv.x, rv.y, normal.x, normal.y);

		// Do not resolve if velocities are separating
		if(velAlongNormal > 0)
			return;

		// Calculate restitution
		float e = 0.1f;

		float massa = 1f;
		float massb = 1f;

		// Calculate impulse scalar
		float j = -(1 + e) * velAlongNormal;
		j /= 1 / massa + 1 / massb;

		// Apply impulse
		Vector2 impulse = normal.scl(j);

		a.velocity.sub(impulse.cpy().scl(1f / massa));
		b.velocity.add(impulse.cpy().scl(1f / massb));

		// a.velocity.set(0,0);
		// b.velocity.set(0,0);
	}

	CollisionParams collide(Entity a, Entity b){
		// Setup a couple pointers to each object

		// Vector from A to B
		Vector2 n = new Vector2(b.x - a.x, b.y - a.y);// B->pos - A->pos

		// Calculate half extents along x axis for each object
		float a_extent = esize;
		float b_extent = esize;

		Vector2 normal = null;
		float penetration = 0;

		// Calculate overlap on x axis
		float x_overlap = a_extent + b_extent - Math.abs(n.x);

		// SAT test on x axis
		if(x_overlap > 0){

			// Calculate overlap on y axis
			float y_overlap = a_extent + b_extent - Math.abs(n.y);

			// SAT test on y axis
			if(y_overlap > 0){
				// Find out which axis is axis of least penetration
				if(x_overlap > y_overlap){
					// Point towards B knowing that n points from A to B
					if(n.x < 0)
						normal = new Vector2(-1, 0);
					else
						normal = new Vector2(0, 0);
					penetration = x_overlap;
					return new CollisionParams(normal, penetration);
				}else{
					// Point toward B knowing that n points from A to B
					if(n.y < 0)
						normal = new Vector2(0, -1);
					else
						normal = new Vector2(0, 1);
					penetration = y_overlap;
					return new CollisionParams(normal, penetration);
				}
			}
		}
		return null;
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
		Vector2 velocity = new Vector2().setToRandomDirection().scl(3f);
		Color color = new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1f);

		public Entity(float x, float y) {
			this.x = x;
			this.y = y;
		}

		public void update(){
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
