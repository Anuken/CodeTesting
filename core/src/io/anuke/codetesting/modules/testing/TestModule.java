package io.anuke.codetesting.modules.testing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.anuke.codetesting.modules.Module;
import io.anuke.codetesting.utils.BloomShader;
import io.anuke.layer3d.LayeredObject;
import io.anuke.layer3d.LayeredRenderer;
import io.anuke.ucore.UCore;
import io.anuke.utils.io.ShadowGenerator;

public class TestModule extends Module{
	SpriteBatch batch;
	float scale = 12;
	OrthographicCamera camera;
	BloomShader bloom;
	LayeredObject object;

	public void init(){
		
		ShadowGenerator gen = new ShadowGenerator();
		gen.generateImages();
		
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Gdx.graphics.getWidth()/scale, Gdx.graphics.getHeight()/scale);
		bloom = new BloomShader();
		/*
		 * TextureRegion[] regions = new TextureRegion[10];
		 * 
		 * for(int i = 0; i < 10; i ++) regions[i] = new TextureRegion(new
		 * Texture("layers/layer" + (i+1) + ".png"));
		 */
		
		TextureRegion region = new TextureRegion(new Texture("layers/char.png"));
		TextureRegion[] regions = region.split(20, 20)[0];
		
		(object = new LayeredObject(regions)).setPosition(0, 0, 0).add();
		LayeredRenderer.instance().steps = 8;
		LayeredRenderer.instance().drawShadows = true;
		LayeredRenderer.instance().spacing = 1f;
		
		LayeredRenderer.instance().camera = camera;
		bloom.setTreshold(0.7f);
		
		/*
		TextureRegion region = new TextureRegion(new Texture("layers/cube.png"));
		TextureRegion[] regions = region.split(8, 8)[0];
		for(int x = -5; x <= 5; x++){
			for(int y = -5; y <= 5; y++){
				new LayeredObject(regions).setPosition(Gdx.graphics.getWidth() / 8 + x*8,
						Gdx.graphics.getHeight() / 8 + y*8).add();
			}
		}
		*/
		// object.rotation = 45;
		
	}

	@Override
	public void update(){
		if(!Gdx.input.isKeyPressed(Keys.W))
		LayeredRenderer.instance().baserotation += 60f*Gdx.graphics.getDeltaTime();
		UCore.clearScreen(Color.BLACK);
		
		batch.setProjectionMatrix(camera.combined);
		
		bloom.capture();
		batch.begin();
		LayeredRenderer.instance().render(batch);
		batch.end();
		bloom.render();
	}
	
	public void resize(int width, int height){
		camera.setToOrtho(false, width/scale, height/scale);
		camera.update();
		object.setPosition(width/12/2, height/12/2);
		bloom.dispose();
		bloom = new BloomShader();
	}
}
