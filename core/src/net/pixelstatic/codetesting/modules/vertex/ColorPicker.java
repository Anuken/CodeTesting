package net.pixelstatic.codetesting.modules.vertex;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

public class ColorPicker extends Widget{
	private Skin skin;
	
	public ColorPicker(Skin skin){
		this.skin = skin;
	}
	
	@Override
	public void draw (Batch batch, float parentAlpha) {
		batch.draw(skin.getAtlas().findRegion(""), this.getX(), this.getY(), this.getPrefWidth(), this.getPrefHeight());
		validate();
	}
	
	@Override
	public float getPrefWidth(){
		return 100;
	}
	
	@Override
	public float getPrefHeight(){
		return 100;
	}
}
