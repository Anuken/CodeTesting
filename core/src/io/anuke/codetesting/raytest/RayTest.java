package io.anuke.codetesting.raytest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import io.anuke.gif.GifRecorder;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.core.DrawContext;
import io.anuke.ucore.core.Graphics;
import io.anuke.ucore.graphics.Atlas;
import io.anuke.ucore.lights.PointLight;
import io.anuke.ucore.lights.RayHandler;
import io.anuke.ucore.modules.RendererModule;

public class RayTest extends RendererModule{
	RayHandler rays;
	Rectangle rect;
	GifRecorder recorder = new GifRecorder(batch);
	
	public RayTest(){
		
		cameraScale = 4;
		
		atlas = new Atlas("codetesting.pack");
		DrawContext.font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
		
		clearColor = Color.WHITE;
		
		RayHandler.useDiffuseLight(true);
		rays = new RayHandler();
		//rays.pixelate();
		
		//rays.setTint(Hue.rgb(0.65, 0.5, 0.3).mul(1.3f));
		
		MathUtils.random.setSeed(0);
		
		//for(int i = 0; i < 160; i ++)
		//	rays.addRect(new Rectangle().setSize(Mathf.random(10)).setCenter(Mathf.range(200), Mathf.range(200)));
		
		PointLight light = new PointLight(rays, 400, Color.WHITE, 500, 0, 0);
		light.setNoise(1, 0, 0);
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
		//Draw.text(Gdx.graphics.getFramesPerSecond()+"FPS", 0, 0);
		
	}
	
	public void resize(){
		rays.resizeFBO((int)(screen.x/4), (int)(screen.y/4));
		//rays.pixelate();
	}
}
