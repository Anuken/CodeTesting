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
import io.anuke.ucore.graphics.Hue;
import io.anuke.ucore.lights.PointLight;
import io.anuke.ucore.lights.RayHandler;
import io.anuke.ucore.modules.RendererModule;
import io.anuke.ucore.util.Mathf;

public class RayTest extends RendererModule{
	RayHandler rays;
	Rectangle rect;
	GifRecorder recorder = new GifRecorder(batch);
	
	public RayTest(){
		
		cameraScale = 4f;
		
		atlas = new Atlas("codetesting.pack");
		DrawContext.font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
		
		clearColor = Color.WHITE;
		
		RayHandler.useDiffuseLight(true);
		rays = new RayHandler();
		rays.pixelate();
		rays.setBlur(true);
		
		rays.setTint(Hue.rgb(0.65, 0.5, 0.3).mul(1.3f));
		
		MathUtils.random.setSeed(0);
	
		//for(int i = 0; i < 1; i ++)
		//	new PointLight(rays, 200, Color.WHITE, 500, Mathf.range(700), Mathf.range(700));
		
		for(int i = 0; i < 160; i ++)
			rays.addRect(new Rectangle().setSize(Mathf.random(10)).setCenter(Mathf.range(200), Mathf.range(200)));
		
		new PointLight(rays, 200, Color.WHITE, 500, 0, 0);
		
		rect = new Rectangle().setSize(20).setCenter(0, 0);
		rays.addRect(rect);
		
		setPixelation();
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
		rays.resizeFBO(gwidth()/4, gheight()/4);
		rays.pixelate();
	}
}
