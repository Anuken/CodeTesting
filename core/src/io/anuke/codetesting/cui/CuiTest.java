package io.anuke.codetesting.cui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.anuke.ucore.core.Core;
import io.anuke.ucore.core.Graphics;
import io.anuke.ucore.cui.Canvas;
import io.anuke.ucore.cui.Stylesheet;
import io.anuke.ucore.cui.layout.HorizontalLayout;
import io.anuke.ucore.cui.layout.VerticalLayout;
import io.anuke.ucore.cui.section.Button;
import io.anuke.ucore.cui.section.Label;
import io.anuke.ucore.modules.Module;

public class CuiTest extends Module{
	Canvas canvas;
	
	@Override
	public void init(){
		Core.batch = new SpriteBatch();
		Core.font = new BitmapFont(Gdx.files.internal("vui/prose.fnt"));
		
		canvas = new Canvas(new Stylesheet(Gdx.files.internal("ui/uiskin.atlas"), Gdx.files.internal("cui/sheet.json")));
		canvas.addLayout(new HorizontalLayout(){{
			Button button = new Button();
			add(button);
			
			button.add(new Label("text"));
			
			Button b2 = new Button();
			b2.add(new Label("more text?"));
			add(b2);
			
			VerticalLayout v = new VerticalLayout();
			
			add(v);
			
			v.add(new Label("a"));
			v.add(new Label("b"));
			v.add(new Label("c"));
			v.add(new Label("d"));
			v.add(new Label("e"));
			v.add(new Label("f"));
			v.add(new Label("g"));
			
			Button b3 = new Button();
			b3.add(new Label("more text"));
			add(b3);
		}});
	}
	
	@Override
	public void update(){
		Graphics.clear(Color.BLACK);
		
		canvas.update();
		
		Core.batch.begin();
		canvas.draw();
		Core.batch.end();
	}
	
	@Override
	public void resize(int width, int height){
		Core.batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
	}
}
