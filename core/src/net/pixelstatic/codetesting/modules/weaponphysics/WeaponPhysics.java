package net.pixelstatic.codetesting.modules.weaponphysics;

import net.pixelstatic.codetesting.entities.Entity;
import net.pixelstatic.codetesting.entities.SolidEntity;
import net.pixelstatic.codetesting.modules.Module;
import net.pixelstatic.utils.Atlas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

public class WeaponPhysics extends Module{
	public WeaponWorld world;
	public Atlas atlas;
	public SpriteBatch batch;
	public BitmapFont font;
	public GlyphLayout layout;
	public OrthographicCamera camera;
	public Matrix4 matrix;
	public float pixsize = 10;
	public Material block = Material.iron;
	int lastrotation;

	private void draw(){
		for(int x = 0;x < world.size;x ++){
			for(int y = 0;y < world.size;y ++){
				if(!block(x,y).empty()) block(x,y).material.draw(this, x, y, block(x,y));
			}
		}
	}
	
	private void drawGUI(){
		Material[] blocks = Material.values();
		int blockamount = 20;
		for(int i = 0; i < blockamount; i ++){
			int offset = i - blockamount/2 + block.ordinal();
			if(offset < 0 || offset >= blocks.length) continue;
			Material block = blocks[offset];
			layout.setText(font, block.name());
			font.setColor(block == this.block ? Color.YELLOW : Color.WHITE);
			font.draw(batch, block.name(), Gdx.graphics.getWidth() - layout.width , Gdx.graphics.getHeight()/2 + layout.height/2 + font.getLineHeight() * (i-blockamount/2));
		}
	}

	public void scroll(float amount){
		float scl = 10f;
		if(camera.zoom + amount / scl > 0.1f) camera.zoom += amount / scl;
	}
	
	void updateCamera(){
		camera.update();
	}
	
	public void rotateBlock(int x, int y){
		if(!inBounds(x,y)) return;
		world.world[x][y].rotation ++;
		if(world.world[x][y].rotation > 3) world.world[x][y].rotation = 0;
		lastrotation = world.world[x][y].rotation;
	}

	public void placeBlock(int x, int y){
		if(!inBounds(x,y)) return;
		Material mat = world.world[x][y].material;
		world.world[x][y].material = block;
		world.world[x][y].rotation = lastrotation;
		if(mat != block)world.updateBlocks();
	}
	
	public void removeBlock(int x, int y){
		if(!inBounds(x,y)) return;
		Material mat = world.world[x][y].material;
		world.world[x][y].material = null;
		if(mat != null)world.updateBlocks();
	}
	
	public boolean inBounds(int x, int y){
		return !(x < 0 || y < 0 || x >= world.size || y >= world.size);
	}

	@Override
	public void init(){
		matrix = new Matrix4();
		batch = new SpriteBatch();
		atlas = new Atlas(Gdx.files.internal("sprites/codetesting.pack"));
		camera = new OrthographicCamera();
		world = getModule(WeaponWorld.class);
		font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
		layout = new GlyphLayout();
	}

	@Override
	public void update(){
		clear();
		updateCamera();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		draw();
		updateEntities();
		batch.end();
		batch.setProjectionMatrix(matrix);
		batch.begin();
		drawGUI();
		batch.end();
	}
	
	void updateEntities(){
		for(Entity entity : Entity.entities.values()){
			entity.Update();
			entity.Draw();
			if(entity instanceof SolidEntity){
				SolidEntity s = (SolidEntity)entity;
				int x = toGrid(s.x);
				int y = toGrid(s.y);
				if(!inBounds(x,y)) continue;
				if(world.solid(x, y)){
					s.collisionEvent(x, y);
				}
			}
		}
	}
	
	int toGrid(float x){
		return (int)(x / pixsize);
	}

	public void resize(int width, int height){
		matrix.setToOrtho2D(0, 0, width, height);
		camera.setToOrtho(false, width, height);
		camera.zoom = 0.3f;
		camera.position.x = world.size * pixsize / 2;
		camera.position.y = world.size * pixsize / 2;
	}

	public void draw(String region, float x, float y, float w, float h){
		batch.draw(atlas.findRegion(region), x, y, w, h);
	}
	
	public void draw(String region, int x, int y){
		batch.draw(atlas.findRegion(region), x * pixsize + pixsize/2 - atlas.RegionWidth(region)/2, y * pixsize + pixsize/2 - atlas.RegionHeight(region)/2);
	}
	
	public void draw(String region, int x, int y, int rotation){
		batch.draw(atlas.findRegion(region), x * pixsize + pixsize/2 - atlas.RegionWidth(region)/2, y * pixsize + pixsize/2 - atlas.RegionHeight(region)/2, atlas.RegionWidth(region)/2, atlas.RegionHeight(region)/2,  atlas.RegionWidth(region),  atlas.RegionHeight(region), 1, 1, rotation * 90);
	}
	
	public void draw(String region, float x, float y){
		batch.draw(atlas.findRegion(region), x- atlas.RegionWidth(region)/2, y - atlas.RegionHeight(region)/2);
	}
	
	public void draw(String region, float x, float y, float size){
		batch.draw(atlas.findRegion(region), x- size/2, y - size/2, size, size);
	}
	
	public Block block(int x, int y){
		return world.world[x][y];
	}
	
	public float fontwidth(String string){
		layout.setText(font, string);
		return layout.width;
	}

	void clear(){
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
}
