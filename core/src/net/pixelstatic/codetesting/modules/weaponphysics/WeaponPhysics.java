package net.pixelstatic.codetesting.modules.weaponphysics;

import net.pixelstatic.codetesting.modules.Module;
import net.pixelstatic.codetesting.utils.Atlas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class WeaponPhysics extends Module{
	public WeaponWorld world;
	public Atlas atlas;
	public SpriteBatch batch;
	public OrthographicCamera camera;
	public float pixsize = 10;

	void draw(){
		for(int x = 0;x < world.size;x ++){
			for(int y = 0;y < world.size;y ++){
				if(world.world[x][y] != 0) draw("pixel", x * pixsize, y * pixsize, pixsize, pixsize);
			}
		}
	}

	public void scroll(float amount){
		float scl = 10f;
		if(camera.zoom + amount / scl > 0) camera.zoom += amount / scl;
	}

	void updateCamera(){
		camera.update();
	}

	public void placeBlock(float sx, float sy){
		int x = (int)(sx / pixsize);
		int y = (int)(sy / pixsize);
		world.world[x][y] = 1;
	}

	@Override
	public void init(){
		batch = new SpriteBatch();
		atlas = new Atlas(Gdx.files.internal("sprites/codetesting.pack"));
		camera = new OrthographicCamera();
		world = getModule(WeaponWorld.class);
	}

	@Override
	public void update(){
		clear();
		updateCamera();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		draw();
		batch.end();
	}

	public void resize(int width, int height){
		camera.setToOrtho(false, width, height);
		camera.position.x = world.size * pixsize / 2;
		camera.position.y = world.size * pixsize / 2;

	}

	void draw(String region, float x, float y, float w, float h){
		batch.draw(atlas.findRegion(region), x, y, w, h);
	}

	void clear(){
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
}
