package io.anuke.codetesting.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;

public class ShadowGenerator{
	static final float ratio = 0.6f;
	
	public void generateImages(){
		float ratio = ShadowGenerator.ratio;
		for(int i = 3; i < 70; i ++){
			PixmapIO.writePNG(Gdx.files.local("shadows/shadow" + i*2 + ".png"), generateShadow(i*2, ratio *=0.985f));
		}
		System.out.println("Done generating shadows.");
	}
	
	public Pixmap generateShadow(int width, float ratio){
		if(width == 8) ratio = 0.5f;
		int height = Math.round(width*ratio);
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);

		for(int x = 0; x < width; x ++){
			for(int y = 0; y < height; y ++){
				float range = sqr(x-width/2f+0.5f)/sqr(width/2f) + sqr(y-height/2f+0.5f)/sqr(height/2f);
				if(range <= 1f){
					pixmap.drawPixel(x, y, Color.rgba8888(Color.BLACK));
				}
			}
		}
		return pixmap;
	}
	
	float sqr(float a){
		return (float)Math.pow(a, 2);
	}
}
