package io.anuke.codetesting.io;

import java.awt.Polygon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;

import io.anuke.ucore.UCore;
import io.anuke.ucore.core.Timers;
import io.anuke.ucore.modules.Module;
import io.anuke.ucore.util.Angles;

public class PolygonGenerator extends Module{
	
	public PolygonGenerator(){
		int imagesize = 63;
		Timers.mark();
		UCore.log("Starting...");
		for(int i = 3; i <= 7; i ++){
			generate(imagesize, i, Gdx.files.local("shapes/shape-" + i + ".png"));
		}
		generate(imagesize, 100, Gdx.files.local("shapes/circle.png"));
		UCore.log("Done. Time taken: " + Timers.elapsed() + " ms.");
		Gdx.app.exit();
	}
	
	static void generate(int imagesize, int sides, FileHandle file){
		Polygon poly = new Polygon();
		
		for(int i = 0; i < sides; i ++){
			Angles.translation(i * ( 360f / sides) - 90, imagesize/2f - 1);
			poly.addPoint((int)Angles.x(), (int)Angles.y());
		}
		
		Pixmap pix = new Pixmap(imagesize, imagesize, Format.RGBA8888);
		pix.setColor(Color.WHITE);
		for(int x = 0; x < imagesize; x ++){
			for(int y = 0; y < imagesize; y ++){
				if(poly.contains(x - imagesize/2, y - imagesize/2)){
					pix.drawPixel(x, y);
				}
			}
		}
		PixmapIO.writePNG(file, pix);
	}
}
