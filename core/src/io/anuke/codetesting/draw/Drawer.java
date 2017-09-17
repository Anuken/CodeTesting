package io.anuke.codetesting.draw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Filter;
import com.badlogic.gdx.graphics.PixmapIO;

import io.anuke.codetesting.CodeTester;
import io.anuke.ucore.modules.Module;

public class Drawer extends Module<CodeTester>{
	String[] children = {"drawable-hdpi", "drawable-mdpi", "drawable-xhdpi", "drawable-xxhdpi", "drawable-xxxhdpi"};
	int[] sizes = {72, 48, 96, 144};
	String filename = "ic_launcher.png";
	FileHandle dir = Gdx.files.absolute("/home/anuke/Projects/Mindustry/android/res");
	FileHandle img = Gdx.files.absolute("/home/anuke/Pictures/screens-desktop/mindustry-icon.png");
	Filter filter = Filter.BiLinear;
	
	public void init(){
		
		Pixmap pixmap = new Pixmap(img);
		
		for(int i = 0; i < children.length; i ++){
			
			FileHandle out = dir.child(children[i]).child(filename);
			
			log("Processing " + out);
			//int size = sizes[i];
			
			Pixmap res = new Pixmap(out);
			res.setFilter(filter);
			
			res.setBlending(Blending.None);
			res.drawPixmap(pixmap, 0, 0, pixmap.getWidth(), pixmap.getHeight(), 0, 0, res.getWidth(), res.getHeight());
			PixmapIO.writePNG(out, res);
		}
		
		log("Done.");
		Gdx.app.exit();
		
	}
	
}
