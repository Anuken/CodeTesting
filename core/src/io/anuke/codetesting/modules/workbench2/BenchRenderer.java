package io.anuke.codetesting.modules.workbench2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import io.anuke.codetesting.modules.Module;
import io.anuke.ucore.UCore;
import io.anuke.ucore.graphics.Textures;

public class BenchRenderer extends Module{
	SpriteBatch batch;
	Array<MaterialPart> parts = new Array<MaterialPart>();
	MaterialPart selected;
	float sx, sy;
	float scl = 5f;
	
	public void init(){
		Gdx.input.setInputProcessor(this);
		
		batch = new SpriteBatch();
		Textures.load("materials/");
		
		parts.add(new MaterialPart("wood").set(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2));
		
		parts.add(new MaterialPart("wood").set(600, 600));
	}
	
	@Override
	public void update(){
		if(Gdx.input.isKeyJustPressed(Keys.E)){
			toolClick(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY());
		}
		
		UCore.clearScreen(Color.BLACK);
		batch.begin();
		for(MaterialPart part : parts){
			Texture texture = part.texture;
			batch.draw(texture, part.x - texture.getWidth()/2*scl,part.y-texture.getHeight()/2*scl, texture.getWidth()*scl, texture.getHeight()*scl);
		}
		batch.end();
	}
	
	void toolClick(float x, float y){
		for(MaterialPart part : parts){
			float rx = (x-(part.getWorldX()));
			float ry = (y-(part.getWorldY()));
			rx += part.texture.getWidth()/2*scl;
			ry += part.texture.getHeight()/2*scl;
			part.toolUsed(UCore.scl(rx, scl), UCore.scl(ry, scl));
		}
	}
	
	public void resize(int width, int height){
		batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
	}
	
	public boolean touchDragged (int screenX, int screenY, int pointer) {
		if(selected != null){
			selected.x = UCore.round(Gdx.input.getX() + sx, scl);
			selected.y = Gdx.graphics.getHeight()-UCore.round(Gdx.input.getY() - sy, scl);
		}
		return false;
	}
	
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		for(MaterialPart part : parts){
			Texture texture =part.texture;
			
			if(UCore.inRect(screenX, Gdx.graphics.getHeight()-screenY,
					part.getWorldX() - texture.getWidth()/2*scl, part.getWorldY()-texture.getHeight()/2*scl, 
					part.getWorldX() + texture.getWidth()/2*scl, part.getWorldY()+texture.getHeight()/2*scl)){
				
				selected = part;
				parts.removeValue(part, true);
				parts.add(part);
				
				sx = part.getWorldX() - screenX;
				sy = part.getWorldY() - (Gdx.graphics.getHeight()-screenY);
				return true;
			}
		}
		return false;
	}
	
	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		selected = null;
		return false;
	}

}
