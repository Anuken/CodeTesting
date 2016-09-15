package net.pixelstatic.codetesting.modules.ecstesting;

import net.pixelstatic.codetesting.modules.Module;
import net.pixelstatic.gdxutils.graphics.Atlas;
import net.pixelstatic.utils.io.GifRecorder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ObjectMap;

public class EntityTester extends Module{
	ObjectMap<Long, Entity> entities = new ObjectMap<Long, Entity>();
	GifRecorder recorder;
	public SpriteBatch batch;
	public Atlas atlas;
	
	
	public void init(){
		//TextureSplitter splitter = new TextureSplitter(Gdx.files.absolute("/home/cobalt/Downloads/kag-emotes-transparent.png"), 32);
		//splitter.split(Gdx.files.absolute("/home/cobalt/Downloads/kagemotes"), 16, 2);
		
		/*
		FileHandle directory = Gdx.files.absolute("/home/cobalt/Documents/removal");
		
		Color color = new Color();
		Pixmap.setBlending(Blending.None);
		for(FileHandle file : directory.list()){
			System.out.println("Processing file: " + file.name() + "...");
			Pixmap pixmap = new Pixmap(file);
			for(int x = 0; x < pixmap.getWidth(); x ++){
				for(int y = 0; y < pixmap.getHeight(); y ++){
					color.set(pixmap.getPixel(x, y));
					
					if(color.a <= 0.99f)
					pixmap.drawPixel(x,y, 0);
				}
			}
			PixmapIO.writePNG(file, pixmap);
			pixmap.dispose();
		}
		
		System.out.println("Done.");
		*/
		batch = new SpriteBatch();
		atlas = new Atlas(Gdx.files.internal("sprites/codetesting.pack"));
		recorder = new GifRecorder(batch);
		recorder.setFPS(30);
		for(int i = 0; i < 9999*3; i ++)
		entities.put((long)i, new TestEntity());
		
	}

	@Override
	public void update(){
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)) Gdx.app.exit();
		if(Gdx.input.isKeyJustPressed(Keys.R)){
			if(!recorder.isRecording()){
				recorder.startRecording();
			}else{
				recorder.finishRecording();
				recorder.writeGIF();
			}
		}
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		for(Entity entity : entities.values()){
			entity.updateComponents(this);
		}
		recorder.update(atlas.findRegion("pixel"), Gdx.graphics.getDeltaTime()*60f);
		batch.end();
	}
	
	public void resize(int width, int height){
		batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
	}
}
