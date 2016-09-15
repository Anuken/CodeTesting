package net.pixelstatic.codetesting.modules.testing;

import net.pixelstatic.codetesting.modules.Module;
import net.pixelstatic.gdxutils.graphics.Hue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TestModule extends Module{
	Texture texture = new Texture("sprites/sprite.png");
	SpriteBatch batch = new SpriteBatch();
	int scale = 5;
	//PolygonRegion region = new PolygonRegion();
	
	public void update(){
		Hue.clearScreen(Color.WHITE);
		batch.begin();
		batch.draw(texture, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, texture.getWidth()*scale, texture.getHeight()*scale);
		batch.end();
	}

	public void init(){
		/*
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
		*/
	}
}
