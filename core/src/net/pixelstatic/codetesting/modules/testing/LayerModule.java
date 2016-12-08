package net.pixelstatic.codetesting.modules.testing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.anuke.ucore.UCore;
import io.anuke.ucore.graphics.Atlas;
import io.anuke.ucore.spritesystem.RenderableHandler;
import io.anuke.ucore.spritesystem.SpriteRenderable;
import net.pixelstatic.codetesting.modules.Module;

public class LayerModule extends Module{
	SpriteBatch batch;
	OrthographicCamera camera;
	Atlas atlas;
	float scale = 4;
	
	public LayerModule(){
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		atlas = new Atlas(Gdx.files.absolute("/home/anuke/Projects/Koru/core/assets/sprites/koru.atlas"));
		
		SpriteRenderable sprite = new SpriteRenderable(atlas.findRegion("stoneblock"));
		sprite.add();
	}
	
	@Override
	public void update(){
		
		batch.setProjectionMatrix(camera.combined);
		
		UCore.clearScreen(Color.BLACK);
		
		batch.begin();
		RenderableHandler.getInstance().renderAll(batch);
		batch.end();
	}
	
	public void resize(int width, int height){
		camera.setToOrtho(false, width/scale, height/scale);
	}

}
