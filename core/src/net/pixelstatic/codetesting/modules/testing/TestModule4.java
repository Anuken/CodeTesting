package net.pixelstatic.codetesting.modules.testing;

import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.tan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import io.anuke.ucore.UCore;
import io.anuke.ucore.graphics.Hue;
import io.anuke.utils.io.GifRecorder;
import net.pixelstatic.codetesting.modules.Module;

public class TestModule4 extends Module{
	SpriteBatch batch;
	ShapeRenderer shape;
	GifRecorder recorder;
	double s = 0.01;
	double max = Math.PI*10;
	double scale = 100;
	float offsetx = Gdx.graphics.getWidth()/2, offsety = Gdx.graphics.getHeight()/2;
	float[] rgb = new float[3];
	float frameid = 0;
	
	public void init(){
		batch = new SpriteBatch();
		shape = new ShapeRenderer();
		recorder = new GifRecorder(batch);
	}
	
	@Override
	public void update(){
		
		frameid += Gdx.graphics.getDeltaTime()*10;
		
		UCore.clearScreen(Color.BLACK);
		shape.begin(ShapeType.Line);
		shape.setColor(Color.GREEN);
		
		double last = -999999999;
		for(double a = 0; a < max; a += s){
			double r = formula(a);
			
			if(last < -99999){
				last = r;
				continue;
			}
			
			draw(r,last,a);
			
			last = r;
		}
		
		shape.end();
		
		
		batch.begin();
		recorder.update();
		batch.end();
	}
	
	void draw(double r1, double r2, double a){
		shape.setColor(Hue.fromHSB(((float)sin(a/500f)*360f+180f), 1f, 1f));
		shape.setColor(Color.WHITE);
		
		float x1 = (float)(r2*scale*cos(a-s))+offsetx;
		float y1 = (float)(r2*scale*sin(a-s))+offsety;
		float x2 = (float)(r1*scale*cos(a))+offsetx;
		float y2 = (float)(r1*scale*sin(a))+offsety;
		
		shape.line(x1, y1, x2, y2);
	}
	
	double formula(double a){
		tan(0);
		a += pow(sin(1.2*a+(frameid/10)), 1.2);
		return pow(sin(1.2*a), 2) + pow(cos(6*a), 3);
	}
	
	public void resize(int width, int height){
		batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
		shape.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
		shape.updateMatrices();
		
		offsetx = Gdx.graphics.getWidth()/2;
		offsety = Gdx.graphics.getHeight()/2;
	}
}
