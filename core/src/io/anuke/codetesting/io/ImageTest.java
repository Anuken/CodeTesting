package io.anuke.codetesting.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.anuke.ucore.core.Draw;
import io.anuke.ucore.modules.RendererModule;

public class ImageTest extends RendererModule{
	Texture texture;
	int size = 2048;

	@Override
	public void init(){
		Pixmap p = new Pixmap(size, size, Format.RGBA8888);
		
		int step = Integer.MAX_VALUE/(size*size);
		
		for(int i = 0; i < size*size; i ++){
			p.drawPixel(i % size, i / size, (step*i) | 0x000000FF);
		}
		
		PixmapIO.writePNG(Gdx.files.local("images.png"), p);
		
		texture = new Texture(p);
	}
	
	public void update(){
		drawDefault();
	}
	
	public void draw(){
		Draw.rect(new TextureRegion(texture), Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
	}
}
