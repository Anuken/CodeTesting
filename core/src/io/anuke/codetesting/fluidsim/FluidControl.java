package io.anuke.codetesting.fluidsim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import io.anuke.gif.GifRecorder;
import io.anuke.ucore.core.*;
import io.anuke.ucore.modules.RendererModule;
import io.anuke.ucore.util.Mathf;

public class FluidControl extends RendererModule{
	int size = 160;
	
	Color light = Color.valueOf("88acde");
	Color dark = Color.valueOf("3c407b");
	float drawsize = 1f;
	
	GifRecorder recorder = new GifRecorder(Core.batch);
	
	FluidProvider prov = new GridFluidProvider(size, size);
	Fluidsim sim = new Fluidsim(prov);

	public FluidControl() {
		Core.cameraScale = 4;
		
		clearColor = Color.WHITE;
		Core.font = new BitmapFont();
		
		//pixelate();
	}

	public void update(){
		sim.simulate();
		
		for(int i = 1; i < size-1; i ++){
			int rad = 1;
			if(Mathf.chance(0.005)){
				for(int cx = -rad; cx <= rad; cx ++){
					for(int cy = -rad; cy <= rad; cy ++){
						prov.setLiquid(i+cx, size-2+cy, 1f);
					}
				}
			}
		}
		
		int x = (int)(Graphics.mouseWorld().x/drawsize);
		int y = (int)(Graphics.mouseWorld().y/drawsize);
		
		if(Inputs.buttonDown(Buttons.LEFT)){
			int rad = 2;
			
			if(Mathf.inBounds(x, y, size, size)){
				for(int cx = -rad; cx <= rad; cx ++){
					for(int cy = -rad; cy <= rad; cy ++){
						if(!Mathf.inBounds(x+cx, y+cy, size, size)) continue;
						prov.setLiquid(x+cx, y+cy, 1f);
						prov.setSettled(cx, cy, false);
					}
				}
			}
		}
		
		drawDefault();
		
		recorder.update();
	}

	public void draw(){
		for(int x = 0; x < size; x++){
			for(int y = 0; y < size; y++){
				if(prov.isSolid(x, y)){
					Draw.color(Color.BLACK);
					Draw.fillcrect(x*drawsize, y*drawsize, drawsize, drawsize);
				}else if(!Mathf.zero(prov.getLiquid(x, y))){
					float height = prov.getLiquid(x, y)*drawsize;
					if(y < size-1 && prov.getLiquid(x, y+1) > 0.0001f){
						height = drawsize;
					}
					Draw.color(light, dark, Mathf.clamp((prov.getLiquid(x, y)-1f)/1.5f));
					Draw.fillcrect(x*drawsize, y*drawsize, drawsize, height);
				}
			}
		}
		
		Draw.tcolor(Color.BLACK);
		Draw.text(Gdx.graphics.getFramesPerSecond() + " FPS", 0, 0);
	}
	
	public void resize(){
		setCamera(size*drawsize/2f, size*drawsize/2f);
		Core.camera.update();
	}
}
