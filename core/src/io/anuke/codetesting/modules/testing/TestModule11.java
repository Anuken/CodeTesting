package io.anuke.codetesting.modules.testing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.IntArray;

import io.anuke.ucore.graphics.Hue;
import io.anuke.ucore.modules.Module;

public class TestModule11 extends Module<TestModule9>{
	static int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
	String filename = "wait";
	
	public TestModule11(){
		Pixmap pix = new Pixmap(Gdx.files.absolute("/home/anuke/Pictures/input/"+filename+".jpg"));
		process(pix);
		PixmapIO.writePNG(Gdx.files.absolute("/home/anuke/Pictures/input/out.png"), pix);
		Gdx.app.exit();
	}
	
	void process(Pixmap pix){
		Color color = new Color();
		Color other = new Color();
		
		IntArray coords = new IntArray();
		
		for(int x = 0; x < pix.getWidth(); x ++){
			for(int y = 0; y < pix.getHeight(); y ++){
				coords.add(x+y*pix.getWidth());
			}
		}
		
		coords.shuffle();
		
		for(int i = 0; i < coords.size; i ++){
			int c = coords.get(i);
			int x = c % pix.getWidth();
			int y = c / pix.getWidth();
			color.set(pix.getPixel(x, y));
			
			for(int[] dir : directions){
				int mx = dir[0]+x, my = dir[1]+y;
				other.set(pix.getPixel(mx, my));
				if(Hue.diff(color, other) < 2f){
					pix.setColor(color);
					pix.drawPixel(mx, my);
				}
			}
		}
		
		/*
		for(int x = 0; x < pix.getWidth(); x ++){
			for(int y = 0; y < pix.getHeight(); y ++){
				color.set(pix.getPixel(x, y));
				
				for(int[] dir : directions){
					int mx = dir[0]+x, my = dir[1]+y;
					other.set(pix.getPixel(mx, my));
					if(Hue.diff(color, other) < 0.6f){
						pix.setColor(color);
						pix.drawPixel(mx, my);
					}
				}
			}
		}
		*/
	}
}
