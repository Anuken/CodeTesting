package io.anuke.codetesting.raytest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;

import io.anuke.gif.GifRecorder;
import io.anuke.ucore.core.Core;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.core.Graphics;
import io.anuke.ucore.graphics.Atlas;
import io.anuke.ucore.graphics.Hue;
import io.anuke.ucore.lights.PointLight;
import io.anuke.ucore.lights.RayHandler;
import io.anuke.ucore.modules.ControlModule;
import io.anuke.ucore.util.Mathf;

public class RayTest extends ControlModule{
	RayHandler rays;
	Rectangle rect;
	GifRecorder recorder = new GifRecorder(batch);
	
	public RayTest(){
		
		Core.cameraScale = 4;
		
		atlas = new Atlas("codetesting.pack");
		Core.font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
		
		clearColor = Color.WHITE;
		
		RayHandler.useDiffuseLight(true);
		rays = new RayHandler();
		//rays.pixelate();
		
		//rays.setTint(Hue.rgb(0.65, 0.5, 0.3).mul(1.3f));
		
		Mathf.random().setSeed(0);
		
		for(int i = 0; i < 160; i ++)
			rays.addRect(new Rectangle().setSize(Mathf.random(10)).setCenter(Mathf.range(200), Mathf.range(200)));
		
		for(int i = 0; i < 20; i ++){
			PointLight light = new PointLight(rays, 400, Hue.random(), 500, 0, 0);
			light.setNoise(1, 0, 0);
			light.setPosition(Mathf.range(200), Mathf.range(200));
		}
		
		//light.setSoftnessLength(0f);
		//light.setSoft(false);
		
		rect = new Rectangle().setSize(10).setCenter(0, 0);
		rays.addRect(rect);
		
		pixelate();
	}
	
	public void update(){
		setCamera(0, 0);
		rays.updateRects();
		
		drawDefault();
		
		rays.setCombinedMatrix(camera);
		rays.updateAndRender();
		
		recorder.update();
		
		/*
		batch.begin();
		Draw.color(Color.PURPLE);
		Draw.linerect(rect.x, rect.y, rect.width, rect.height);
		batch.end();
		*/
	}
	
	public void draw(){
		float x = Graphics.mouseWorld().x;
		float y = Graphics.mouseWorld().y;
		
		rect.setCenter(x, y);
		
		Draw.color();
		for(Rectangle rect : rays.getRects())
			Draw.rect("heater", rect.x+rect.width/2, rect.y+rect.height/2, rect.width, rect.height);
		
		Draw.tcolor(Color.PURPLE);
		Draw.text(Gdx.graphics.getFramesPerSecond()+"FPS", 0, 0);
		
	}
	
	public void resize(){
		rays.resizeFBO((int)(screen.x/4), (int)(screen.y/4));
		//rays.pixelate();
	}
}
