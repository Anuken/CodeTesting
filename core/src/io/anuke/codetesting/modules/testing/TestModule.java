package io.anuke.codetesting.modules.testing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import io.anuke.codetesting.modules.Module;
import io.anuke.codetesting.utils.BloomShader;
import io.anuke.layer3d.ArrayRenderer;
import io.anuke.layer3d.LayeredObject;
import io.anuke.ucore.UCore;
import io.anuke.utools.io.ShadowGenerator;

/**Testing bloom shader and layered rendering*/
public class TestModule extends Module{
	SpriteBatch batch;
	float scale = 2;
	OrthographicCamera camera;
	BloomShader bloom;
	LayeredObject object;

	public void init(){
		
		ShadowGenerator gen = new ShadowGenerator();
		gen.generateImages();
		
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Gdx.graphics.getWidth()/scale, Gdx.graphics.getHeight()/scale);
		bloom = new BloomShader();
		
		TextureRegion region = new TextureRegion(new Texture("layers/monu2.png"));
		TextureRegion[] regions = region.split(97, 97)[0];
		
		(object = new LayeredObject(regions)).setPosition(0, 0, 0);
		ArrayRenderer.instance().add(object);
		
		ArrayRenderer.instance().steps = 1;
		//LayeredRenderer.instance().drawShadows = true;
		ArrayRenderer.instance().spacing = 0.5f;
		
		ArrayRenderer.instance().camera = camera;
		bloom.setTreshold(0.7f);
		float o = 550;
		
		for(int i = 0; i < 10; i ++){
			 LayeredObject ob = new LayeredObject(regions).setPosition(o+MathUtils.random(-400, 400),o+MathUtils.random(-400, 400), 0);
			 ArrayRenderer.instance().add(ob);
		}
	}

	@Override
	public void update(){
		if(!Gdx.input.isKeyPressed(Keys.W))
			ArrayRenderer.instance().camrotation += 60f*Gdx.graphics.getDeltaTime()*0.5f;
		UCore.clearScreen(Color.BLACK);
		
		batch.setProjectionMatrix(camera.combined);
		
		if(Gdx.input.isKeyJustPressed(Keys.M))
			ArrayRenderer.instance().remove(object);
		
		//bloom.capture();
		batch.begin();
		ArrayRenderer.instance().render(batch);
		batch.end();
		//bloom.render();
		if(Gdx.graphics.getFrameId()%100==0) System.out.println(Gdx.graphics.getFramesPerSecond());
	}
	
	public void resize(int width, int height){
		camera.setToOrtho(false, width/scale, height/scale);
		camera.update();
		object.setPosition(width/scale/2, height/scale/2);
		//bloom.dispose();
		//bloom = new BloomShader();
	}
}
