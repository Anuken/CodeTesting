package net.pixelstatic.codetesting.modules.testing;

import io.anuke.gdxutils.graphics.Hue;
import io.anuke.utils.io.GifRecorder;
import net.pixelstatic.codetesting.modules.Module;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ShortArray;

public class TestModule extends Module{
	Texture texture = new Texture("sprites/sprite.png");
	TextureRegion tregion = new TextureRegion(texture);
	SpriteBatch batch = new SpriteBatch();
	PolygonSpriteBatch polybatch = new PolygonSpriteBatch();
	ShapeRenderer shape = new ShapeRenderer();
	int scale = 8;
	PolygonRegion region;
	EarClippingTriangulator triangulator = new EarClippingTriangulator();
	GifRecorder recorder = new GifRecorder(batch, 1f){{setSkipAlpha(true);}};
	int vw = 20, vh = 10;
	float vertices[];
	
	public void init(){
		tregion = new TextureRegion(texture);
		resetVertices(defaultVertices());
	}
	
	void resetVertices(float[] vertices){
		this.vertices = vertices;
		
		ShortArray triangles = triangulator.computeTriangles(vertices);
		region = new PolygonRegion(tregion, vertices, triangles.toArray());
		setVerticeUVs();
		
	//	for(int i = 0; i < vertices.length; i ++)
		//	this.vertices[i] *= -1f;
	}
	
	float[] defaultVertices(){
		FloatArray v = new FloatArray();
		//float[] v = new float[10];
		float xw = texture.getWidth()*scale;
		float yw = texture.getHeight()*scale;
		
		//bottom
		for(int x = 0; x < vw; x ++){
			v.add(((float)x/vw)*xw);
			v.add(0);
		}
		
		//right
		for(int y = 0; y < vh; y ++){
			v.add(xw);
			v.add(((float)y/vh)*yw);
		}
		
		//top
		for(int x = vw-1; x >= 0; x --){
			v.add(((float)x/vw)*xw);
			v.add(yw);
		}
		
		//left
		for(int y = vh; y >= 0; y --){
			v.add(0);
			v.add(((float)y/vh)*yw);
		}
				
		return v.toArray();
	}
	
	void setVerticeUVs(){
		float[] v = region.getTextureCoords();
		
		int i = 0;
		
		//bottom
		for(int x = 0; x < vw; x ++){
			v[i ++] = ((float)x/vw);
			v[i ++] = 0;
		}
		
		//right
		for(int y = 0; y < vh; y ++){
			v[i ++] = (1f);
			v[i ++] = ((float)y/vh);
		}
		
		//top
		for(int x = vw-1; x >= 0; x --){
			v[i ++] = ((float)x/vw);
			v[i ++] = (1f);
		}
		
		//left
		for(int y = vh; y >= 0; y --){
			v[i ++] = (0);
			v[i ++] = ((float)y/vh);
		}
		
	}
	
	public void update(){
		

		
		if(Gdx.input.isKeyPressed(Keys.R)){
			float[] vertices = defaultVertices();
			for(int i = 0; i < vertices.length/2; i ++){
				vertices[i*2+1] += Math.sin(vertices[i*2]/24f + (int)Gdx.graphics.getFrameId()/15f)*30;
			}
			resetVertices(vertices);
		}
		
		Hue.clearScreen(Color.BLACK);
		
		
		polybatch.begin();
		polybatch.draw(region, Gdx.graphics.getWidth()/2 - texture.getWidth()/2*scale, Gdx.graphics.getHeight()/2 + texture.getHeight()/2*scale, texture.getWidth(), -texture.getHeight());
		polybatch.end();
		
		batch.begin();
		recorder.update(tregion, Gdx.graphics.getDeltaTime());
		batch.end();
		
		
		shape.setColor(Color.RED);
		shape.begin(ShapeType.Line);
		shape.polygon(vertices);
		shape.end();
	
	}
	
	public void resize(int width, int height){
		batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
		polybatch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
		shape.getProjectionMatrix().setToOrtho2D(-Gdx.graphics.getWidth()/2 + texture.getWidth()/2*scale, -Gdx.graphics.getHeight()/2 + texture.getHeight()/2*scale, width, height);
	}
}
