package io.anuke.codetesting.modules.testing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.anuke.codetesting.modules.Module;
import io.anuke.ucore.UCore;
import io.anuke.ucore.graphics.Atlas;
import io.anuke.ucore.graphics.Hue;
import io.anuke.ucore.noise.Noise;
import io.anuke.ucore.spritesystem.RenderableHandler;
import io.anuke.ucore.spritesystem.SortProviders;
import io.anuke.ucore.spritesystem.SpriteRenderable;

public class LayerModule extends Module{
	SpriteBatch batch;
	OrthographicCamera camera;
	Atlas atlas;
	float scale = 4f;
	int size = 10;
	int[][][] blocks = new int[size][size][size];
	float sp = 11.999f;
	float s = size*sp;
	float step = 8;
	
	public LayerModule(){
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		
		atlas = new Atlas(Gdx.files.absolute("/home/anuke/Projects/Koru/core/assets/sprites/koru.atlas"));
		
		
		for(int x = 0; x < size; x ++){
			for(int y = 0; y < size; y ++){
				for(int z = 0; z < size; z ++){
					//if(y <= 1) blocks[x][y][z] = 1;
					
					int noise = (int)Noise.normalNoise(x, z, 5, 5)+3;
					if(y + 1 < noise){
						blocks[x][y][z] = 1;
					}else if(y < noise){
						blocks[x][y][z] = 2;
					}
					
				}
			}
		}
		
		
		//blocks[size/2][0][size/2] = 1;
		//blocks[size/2][0][size/2+1] = 1;
		//blocks[size/2][1][size/2+1] = 1;

		for(int x = 0; x < size; x ++){
			for(int y = 0; y < size; y ++){
				for(int z = 0; z < size; z ++){
					int i = blocks[x][y][z];
					
					if(i != 0){
						
						
						SpriteRenderable sprite = new SpriteRenderable(atlas.findRegion(i == 1 ? "dirtblock" : "grassblock"));
						sprite.setPosition(x*sp-s/2, z*sp-s/2 + step*y);
						sprite.setLayer(z*sp-s/2 - y *0.01f).setProvider(SortProviders.object);
						sprite.centerX();
						sprite.add();
						//if(block(x,y+1,z+1) && !block(x,y+1,z))sprite.setColor(Color.LIGHT_GRAY);
						
						sprite.setColor(Hue.blend(Hue.blend(new Color(0.1f, 0.1f, 0.4f, 1), Color.ORANGE, y/6f), Color.WHITE, 0.5f));
						
						
						SpriteRenderable shadow = new SpriteRenderable(atlas.findRegion("blank"));
						shadow.sprite.setSize(16, 16);
						shadow.setPosition(x*sp-s/2 - 8, z*sp-s/2 + step*y - 4);
						shadow.setLayer(z*(sp)-s/2 - y *0.01f + 0.005f).setProvider(SortProviders.object);
						shadow.setColor(new Color(0,0,0,0.14f));
						shadow.add();
						
						
					}
				}
			}
		}
	}
	
	boolean block(int x, int y, int z){
		return UCore.inBounds(x, y, z, blocks) ? blocks[x][y][z] != 0 : false; 
	}
	
	@Override
	public void update(){
		
		UCore.clearScreen(Color.BLACK);
		
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		RenderableHandler.instance().renderAll(batch);
		batch.end();
	}
	
	public void resize(int width, int height){
		camera.setToOrtho(false, width/scale, height/scale);
		camera.position.set(0,0,0);
		camera.update();
	}

}
