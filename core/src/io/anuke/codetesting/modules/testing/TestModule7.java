package io.anuke.codetesting.modules.testing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.anuke.aabb.Collider;
import io.anuke.aabb.CollisionEngine;
import io.anuke.codetesting.modules.Module;
import io.anuke.gif.GifRecorder;
import io.anuke.ucore.UCore;
import io.anuke.ucore.graphics.PixmapUtils;
import io.anuke.ucore.graphics.ShapeUtils;

/**Collider testing with GDXAABB.*/
public class TestModule7 extends Module{
	SpriteBatch batch = new SpriteBatch();
	Texture tex = PixmapUtils.blankTexture();
	CollisionEngine engine;
	Collider player, wall;
	boolean playercolliding = false;
	GifRecorder recorder = new GifRecorder(batch);
	Vector2 begin;
	
	
	@Override
	public void init(){
		engine = new CollisionEngine();
		//engine.iterations = 20;
		
		Collider.defaultRestitution = 0f;
		
		player = new Collider(600, 700, 50, 50, 1);
		player.drag = 0.73f;
		
		engine.setContactListener((a, b)->{
			if(a == player) playercolliding = true;
		});
		
		//engine.gravity.set(0, -1);
		
		engine.addCollider(player);
		
		wall = new Collider(600, 400, 300, 200, 10);
		wall.kinematic = true;
		engine.addCollider(wall);
		
		for(int i = 0; i < 10; i ++){
			Collider n = new Collider(Gdx.graphics.getWidth()/2 + MathUtils.random(Gdx.graphics.getWidth()), Gdx.graphics.getHeight()+MathUtils.random(Gdx.graphics.getHeight()), 30, 30, 1);
			engine.addCollider(n);
		}
		
	}

	@Override
	public void update(){
		playercolliding = false;
		
		float speed = 6f;
		
		if(Gdx.input.isKeyJustPressed(Keys.W)){
			player.applyImpulse(0, speed*2);
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.A)){
			player.applyImpulse(-speed, 0);
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.S)){
			player.applyImpulse(0, -speed);
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.D)){
			player.applyImpulse(speed, 0);
			begin = new Vector2(player.x, player.y);
		}
		
		engine.update(Gdx.graphics.getDeltaTime());
		
		if(player.getVelocity().len() > 0.001f){
			System.out.println(player.getVelocity());
			System.out.println(begin.dst(player.x, player.y));
		}
		
		UCore.clearScreen(Color.BLACK);
		
		batch.begin();
		
		for(Collider c : engine.getAllColliders()){
			if(c == player && playercolliding){
				batch.setColor(Color.GREEN);
			}else{
				batch.setColor(Color.WHITE);
			}
			
			batch.draw(tex, c.x - c.w/2, c.y - c.h/2, c.w, c.h);
		}
		
		float s = engine.getCellSize();
		
		//draw spatial lines
		for(int x = 0; x < 10; x ++){
			for(int y = 0; y < 10; y ++){
				ShapeUtils.line(batch, x*s, y*s, x*s+s, y*s);
				ShapeUtils.line(batch, x*s+s, y*s, x*s+s, y*s+s);
				ShapeUtils.line(batch, x*s+s, y*s+s, x*s, y*s+s);
				ShapeUtils.line(batch, x*s, y*s+s, x*s, y*s);
			}
		}
		
		batch.end();
		
		recorder.update();
	}
	
	public void resize(int width, int height){
		batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
	}

}
