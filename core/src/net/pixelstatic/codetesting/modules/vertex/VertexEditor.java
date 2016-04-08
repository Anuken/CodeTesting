package net.pixelstatic.codetesting.modules.vertex;

import net.pixelstatic.codetesting.modules.Module;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class VertexEditor extends Module{
	Skin skin;
	Stage stage;

	@Override
	public void update(){
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)) Gdx.app.exit();
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}
	
	public void init(){
		InputMultiplexer plex = new InputMultiplexer();
	
		plex.addProcessor(new VertexInput(tester.getModule(VertexGUI.class)));
		plex.addProcessor(stage);
		Gdx.input.setInputProcessor(plex);
	}

	public VertexEditor(){
		skin = new Skin(Gdx.files.internal("gui/uiskin.json"));
		stage = new Stage();
		stage.setViewport(new ScreenViewport());
		ActorAlign.stage = stage;
	}

	public void resize(int width, int height){
		stage.getViewport().update(width, height, true);
	}
}
