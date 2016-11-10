package net.pixelstatic.codetesting.modules.testing;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.utils.ShortArray;

public class TerrainPolygon{
	PolygonRegion region;
	EarClippingTriangulator tri = new EarClippingTriangulator();
	float x, y;
	
	public TerrainPolygon(Texture tex, float[] vertices){
		ShortArray shorts = tri.computeTriangles(vertices);
		region = new PolygonRegion(new TextureRegion(tex), vertices, shorts.toArray());
	}
	
	public void setVertices(float[] vertices){
		ShortArray shorts = tri.computeTriangles(vertices);
		region = new PolygonRegion(new TextureRegion(this.region.getRegion()), vertices, shorts.toArray());
	}
	
	public void setRegion(TextureRegion region){
		this.region = new PolygonRegion(new TextureRegion(region), this.region.getVertices(), this.region.getTriangles());
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
