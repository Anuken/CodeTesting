package io.anuke.codetesting.modules.testing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;

import io.anuke.codetesting.modules.DrawModule;
import io.anuke.ucore.UCore;
import io.anuke.ucore.drawpointers.DrawHandler;
import io.anuke.ucore.drawpointers.DrawLayer;
import io.anuke.ucore.drawpointers.DrawPointerList;
import io.anuke.ucore.graphics.Textures;

public class TestModule6 extends DrawModule{
	DrawPointerList list = new DrawPointerList();
	float playerx, playery;

	public void init(){
		Textures.load("textures/");

		scale = 4f;

		for(int x = -10; x <= 10; x++){
			for(int y = -10; y <= 10; y++){
				final int zx = x, zy = y;
				list.add(0f, DrawLayer.tile, (p) -> {
					batch.draw(Textures.get("grass"), w / 2 + zx*12, h / 2 + zy*12);
				});
			}
		}
		

		list.add(playery, DrawLayer.object, (p) -> {
			batch.draw(Textures.get("magmarock"), playerx, playery);
			p.layer = playery;
		});
		
		list.add(h/2, DrawLayer.object, (p) -> {
			batch.draw(Textures.get("lava"), w/2, h/2);
			p.layer = h/2;
		});
		
	}

	public void update(){
		UCore.clearScreen(Color.BLACK);
		batch.begin();
		DrawHandler.instance().draw();
		batch.end();
		
		float speed = 2f;
		
		if(Gdx.input.isKeyPressed(Keys.W)) playery += speed;
		if(Gdx.input.isKeyPressed(Keys.A)) playerx -= speed;
		if(Gdx.input.isKeyPressed(Keys.S)) playery -= speed;
		if(Gdx.input.isKeyPressed(Keys.D)) playerx += speed;
	}

}
