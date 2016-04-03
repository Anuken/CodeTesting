package net.pixelstatic.codetesting.modules.vertex;

import net.pixelstatic.codetesting.modules.Module;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class VertexEditor extends Module{
	Skin skin;
	FitViewport viewport;
	Stage stage;
	
	@Override
	public void update(){
		if(Gdx.input.isKeyPressed(Keys.ESCAPE))Gdx.app.exit();
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}
	
	public VertexEditor(){
		skin = new Skin(Gdx.files.internal("gui/uiskin.json"));
		viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage = new Stage();
		stage.setViewport(viewport);
		Button button = new Button(skin);
		button.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		stage.addActor(button);
		Gdx.input.setInputProcessor(stage);
	}
	
	public void resize(int width, int height){
		stage.getViewport().update(width, height, true);
	}
}
