package net.pixelstatic.codetesting.modules.ecstesting;

import net.pixelstatic.codetesting.modules.Module;
import net.pixelstatic.codetesting.utils.Atlas;
import net.pixelstatic.utils.GifRecorder;

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
				recorder.writeGIF(Gdx.files.local("gifimages"), Gdx.files.local("gifexport"));
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
