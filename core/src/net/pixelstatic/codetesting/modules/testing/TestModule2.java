package net.pixelstatic.codetesting.modules.testing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.PixmapIO;

import io.anuke.ucore.graphics.Hue;
import io.anuke.ucore.graphics.PixmapUtils;
import net.pixelstatic.codetesting.modules.Module;

public class TestModule2 extends Module{
	
	public void init(){
		FileHandle directory = Gdx.files.absolute("/home/cobalt/Documents/ANT-sprites/input");
		Pixmap.setBlending(Blending.None);
		for(FileHandle file : directory.list()){
			System.out.println("Processing file: " + file.name() + "...");
			Pixmap pixmap = new Pixmap(file);
			Pixmap out = process(pixmap);
			PixmapIO.writePNG(Gdx.files.absolute("/home/cobalt/Documents/ANT-sprites/output").child(file.name()), out);
			pixmap.dispose();
			out.dispose();
		}
		System.out.println("Done.");
		Gdx.app.exit();
	}

	public Pixmap process(Pixmap input){
		int first = 0;
		
		Pixmap pixmap = PixmapUtils.copy(input);
		out:
		for(int x= 0; x < pixmap.getWidth(); x ++){
			for(int y= 0; y < pixmap.getHeight(); y ++){
				int c = input.getPixel(x, y);
				if(c != 0){
					first = c;
					break out;
				}
			}
		}
		//int seed = MathUtils.random(9999999);
		
		Color color = new Color(first);
		float[] values = Hue.RGBtoHSB(color, new float[3]);
		values[0] = 0.4f;
		//if(values[2] < 0.2f) values[2] = 0.2f;
		//if(values[1] < 0.5f) values[1] = 0.5f;
		color = Hue.fromHSB(values[0], values[1], values[2]);
		first = Color.rgba8888(color);
		float nsum = Hue.sum(color.set(first));
		Color o = new Color();
		
		for(int x= 0; x < pixmap.getWidth(); x ++){
			for(int y= 0; y < pixmap.getHeight(); y ++){
				int c = input.getPixel(x, y);
				if(c != 0){
					color.set(first);
					o.set(c);
					float sum = Hue.sum(o);
					float isum = (sum - nsum);
					isum += ((x+y)%2)/2f;
					isum = (int)(isum/0.5f)*0.5f;
					
					color.add(isum, isum, isum, 0f);
					color.clamp();
					pixmap.setColor(color);
					pixmap.drawPixel(x, y);
				}
			}
		}

		return pixmap;
	}

	@Override
	public void update(){
	}
}
