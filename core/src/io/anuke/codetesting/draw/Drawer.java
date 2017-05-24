package io.anuke.codetesting.draw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import io.anuke.codetesting.CodeTester;
import io.anuke.ucore.UCore;
import io.anuke.ucore.modules.Module;

public class Drawer extends Module<CodeTester>{
	String[] children = {"drawable-hdpi", "drawable-mdpi", "drawable-xhdpi", "drawable-xxhdpi"};
	int[] sizes = {72, 48, 96, 144};
	String filename = "ic_launcher.png";
	FileHandle dir = Gdx.files.absolute("/home/anuke/Projects/Novix/android/res");
	FileHandle img = Gdx.files.absolute("/home/anuke/Documents/NovixIcons/pixelicon-out.png");
	
	public void init(){
		long max = 2000;
		
		for(int a = 1; a < max; a ++){
			for(int b = 1; b < max; b ++){
				for(int c = 1; c < max; c ++){
					double i = (double)a/(b+c) + (double)b/(a+c) + (double)c/(a+b);
					if(Math.abs(i-4) < 0.0000001){
						UCore.log("values", a, b, c);
						UCore.log("out", i);
						Gdx.app.exit();
						return;
					}
				}
			}
			log("Done with permutation  " + a);
		}
		
		log("Failed.");
		Gdx.app.exit();
		/*
		
		Pixmap pixmap = new Pixmap(img);
		
		for(int i = 0; i < children.length; i ++){
			
			FileHandle out = dir.child(children[i]).child(filename);
			
			log("Processing " + out);
			//int size = sizes[i];
			
			Pixmap res = new Pixmap(out);
			res.setFilter(Filter.NearestNeighbour);
			
			res.drawPixmap(pixmap, 0, 0, pixmap.getWidth(), pixmap.getHeight(), 0, 0, res.getWidth(), res.getHeight());
			PixmapIO.writePNG(out, res);
		}
		
		log("Done.");
		Gdx.app.exit();
		*/
	}
}
