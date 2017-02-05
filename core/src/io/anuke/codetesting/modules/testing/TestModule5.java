package io.anuke.codetesting.modules.testing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import io.anuke.codetesting.modules.Module;
import io.anuke.layer3d.LayeredObject;
import io.anuke.layer3d.LayeredRenderer;
import io.anuke.ucore.UCore;
import io.anuke.ucore.noise.Noise;
import io.anuke.ucore.noise.RidgedPerlin;

/** Testing layered terrain generation with GDXLayered3D */
public class TestModule5 extends Module{
	SpriteBatch batch;
	OrthographicCamera camera;
	LayeredObject object;
	int scale = 6;
	RidgedPerlin ridge = new RidgedPerlin(1, 1, 0.5f);

	public void init(){
		batch = new SpriteBatch();
		
		camera = new OrthographicCamera(Gdx.graphics.getWidth()/scale, Gdx.graphics.getHeight()/scale);

		Array<Texture> textures = new Array<Texture>();

		int size = 100;
		int height = 10;

		for(int y = 0; y < height; y++){
			Pixmap pixmap = new Pixmap(size, size, Format.RGBA8888);
			
			for(int z = 0; z < size; z++){
				for(int x = 0; x < size; x++){
					
					double top = 2 + Noise.nnoise(x, z, 10f, 5f);
					double rid = ridge.getValue(x, z, 0.005f) + Noise.nnoise(x, z, 10f, 0.13f);
					
					double slope = 0.001;
					double riv = 0.13f;
					
					if(rid > riv)
						top = 1;
					else if(rid > slope)
						top *= (1f-(rid-slope)/(riv-slope));
					
					
					if(y < top){
						Color color = Color.FOREST;
						
						if(rid > riv)
							color = Color.BLUE;
						else if(rid > 0.03)
							color = Color.TAN;
						
						
						pixmap.drawPixel(x, z, Color.rgba8888(color));
					}
				}
			}
			
			textures.add(new Texture(pixmap));
		}
		
		LayeredRenderer.instance().camera = camera;
		LayeredRenderer.instance().steps = 8;
		LayeredRenderer.instance().drawShadows = true;
		LayeredRenderer.instance().spacing = 1;
		object = new LayeredObject((Texture[])textures.toArray(Texture.class));
		
		
		object.add();
	}

	@Override
	public void update(){
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		object.rotation += 30f*Gdx.graphics.getDeltaTime();
		
		UCore.clearScreen(Color.BLACK);
		
		batch.begin();
		LayeredRenderer.instance().render(batch);
		batch.end();
	}
	
	public void resize(int width, int height){
		camera.setToOrtho(false, width/scale, height/scale);
		camera.update();
		object.setPosition(width/2/scale, height/2/scale);
	}

}
