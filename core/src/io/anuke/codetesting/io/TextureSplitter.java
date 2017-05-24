package io.anuke.codetesting.io;

import io.anuke.ucore.graphics.PixmapUtils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;

public class TextureSplitter{
	private FileHandle file;
	private int size;

	public TextureSplitter(FileHandle file, int size){
		this.size = size;
		this.file = file;
	}

	public void split(FileHandle folder, int scale, int zoom){
		Pixmap pixmap = new Pixmap(file);
		int gwidth = pixmap.getWidth() / size;
		int gheight = pixmap.getHeight() / size;
		
		for(int gx = 0; gx < gwidth; gx ++){
			for(int gy = 0; gy < gheight; gy ++){
				Pixmap clip = new Pixmap(size, size, Format.RGBA8888);
				for(int x = gx*size; x < gx*size + size; x ++){
					for(int y = gy*size; y < gy*size + size; y ++){
						clip.drawPixel(x - gx*size, y - gy*size, pixmap.getPixel(x, y));
					}
				}
				Pixmap scaled = PixmapUtils.scale(clip, scale);
				Pixmap zoomed = PixmapUtils.zoom(scaled, zoom);
				
				PixmapIO.writePNG(folder.child("patch-" + gx + "," +gy + ".png"), zoomed);
				
				clip.dispose();
				scaled.dispose();
				zoomed.dispose();
			}
		}
	}
}
