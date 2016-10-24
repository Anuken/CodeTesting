package net.pixelstatic.codetesting.modules.testing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.anuke.layer3d.LayeredObject;
import io.anuke.layer3d.LayeredRenderer;
import io.anuke.ucore.UCore;
import net.pixelstatic.codetesting.modules.Module;

public class TestModule extends Module{
	SpriteBatch batch = new SpriteBatch();
	float screenScale = 1/2f;

	public void init(){
		/*
		 * TextureRegion[] regions = new TextureRegion[10];
		 * 
		 * for(int i = 0; i < 10; i ++) regions[i] = new TextureRegion(new
		 * Texture("layers/layer" + (i+1) + ".png"));
		 */
		LayeredRenderer.instance().worldScale = screenScale;
		
		TextureRegion region = new TextureRegion(new Texture("layers/monu.png"));
		TextureRegion[] regions = region.split(126, 126)[0];
		
		new LayeredObject(regions).setPosition(Gdx.graphics.getWidth() / 2 * screenScale,
				Gdx.graphics.getHeight() / 2  * screenScale).add();
		

		
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
		
		batch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth() * screenScale, Gdx.graphics.getHeight() * screenScale);
	}

	@Override
	public void update(){
		LayeredRenderer.instance().baserotation += 1f;
		UCore.clearScreen(Color.BLACK);
		batch.begin();
		LayeredRenderer.instance().render(batch);
		batch.end();
	}
}
