package io.anuke.codetesting.modules.testing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ShortArray;

import io.anuke.codetesting.modules.Module;
import io.anuke.codetesting.utils.BloomShader;
import io.anuke.ucore.UCore;
import io.anuke.ucore.graphics.Textures;

public class TestModule3 extends Module{
	PolygonSpriteBatch batch = new PolygonSpriteBatch();
	PolyTerrain terrain1;
	PolyTerrain terrain2;
	float scl = 5;
	BloomShader bloom;
	
	public void init(){
		Textures.load("textures/");
		Textures.repeatWrap("grass", "lava");
		float[] vertices = nGon(10, 60);
		terrain1 = new PolyTerrain(Textures.get("grass"), vertices);
		terrain1.x = Gdx.graphics.getWidth()/2/scl;
		terrain1.y = Gdx.graphics.getHeight()/2/scl;
		

		float[] vertices2 = nGon(3, 30);
		terrain2 = new PolyTerrain(Textures.get("lava"), vertices2);
		terrain2.x = Gdx.graphics.getWidth()/2/scl;
		terrain2.y = Gdx.graphics.getHeight()/2/scl;
		
		bloom = new BloomShader();
	}
	
	@Override
	public void update(){
		UCore.clearScreen(Color.BLACK);
		
		batch.begin();
		terrain1.draw(batch);
		
		bloom.capture();
		
		terrain2.draw(batch);
		batch.end();
		
		bloom.render();
	}
	
	public void resize(int width, int height){
		batch.getProjectionMatrix().setToOrtho2D(0, 0, width/scl, height/scl);
	}
	
	public float[] nGon(int amount, float size){
		float[] v = new float[amount*2];
		Vector2 vec = new Vector2(1,1);
		vec.setLength(size);
		for(int i = 0; i < amount; i ++){
			vec.setAngle((360f/amount) * i + 90);
			v[i*2] = vec.x;
			v[i*2+1] = vec.y;
		}
		return v;
	}
	
	static class PolyTerrain{
		PolygonRegion region;
		EarClippingTriangulator tri = new EarClippingTriangulator();
		float x, y;
		
		public PolyTerrain(Texture tex, float[] vertices){
			
			FloatArray floats = new FloatArray();
			
			Array<GridPoint2> points = new Array<GridPoint2>();
			for(int i = 0; i < vertices.length/2; i ++){
				float x = vertices[i*2+0];
				float y = vertices[i*2+1];
				float x2 = 0;
				float y2 =0;
				if(i == vertices.length/2-1){
					x2 = vertices[0];
					y2 = vertices[1];
				}else{
					x2 = vertices[i*2+2];
					y2 = vertices[i*2+3];
				}
				line((int)x, (int)y, (int)x2, (int)y2, points);
				for(GridPoint2 point : points){
					floats.add(point.x);
					floats.add(point.y);
				}
				
				points.clear();
			}
			vertices = floats.toArray();
			ShortArray shorts = tri.computeTriangles(vertices);
			region = new PolygonRegion(new TextureRegion(tex), vertices, shorts.toArray());
		}
		
		public void draw(PolygonSpriteBatch batch){
			
			batch.setColor(1,1,1,0.6f);
			batch.draw(region, x, y, 10, 10);
			batch.setColor(1,1,1,0.2f);
			batch.draw(region, x, y);
			batch.setColor(1,1,1,1);
			batch.draw(region, x, y);
		}
	}
	
	public static void line(int x0, int y0, int x1, int y1, Array<GridPoint2> points) {
	    int xDist =  Math.abs(x1 - x0);
	    int yDist = -Math.abs(y1 - y0);
	    int xStep = (x0 < x1 ? +1 : -1);
	    int yStep = (y0 < y1 ? +1 : -1);
	    int error = xDist + yDist;
	    
	    points.add(new GridPoint2(x0, y0));
	   
	    while (x0 != x1 || y0 != y1) {
	        if (2*error - yDist > xDist - 2*error) {
	            // horizontal step
	            error += yDist;
	            x0 += xStep;
	        } else {
	            // vertical step
	            error += xDist;
	            y0 += yStep;
	        }

	        points.add(new GridPoint2(x0, y0));
	 	   
	    }
	}

}
