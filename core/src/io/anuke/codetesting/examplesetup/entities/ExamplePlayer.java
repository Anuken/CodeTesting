package io.anuke.codetesting.examplesetup.entities;

import io.anuke.ucore.core.Draw;
import io.anuke.ucore.core.UInput;
import io.anuke.ucore.entities.DestructibleEntity;

public class ExamplePlayer extends DestructibleEntity{
	float speed = 4f;
	
	@Override
	public void update(){
		if(UInput.keyDown("up"))
			y += speed;
		
		if(UInput.keyDown("down"))
			y -= speed;
		
		if(UInput.keyDown("left"))
			x -= speed;
		
		if(UInput.keyDown("right"))
			x += speed;
	}
	
	@Override
	public void draw(){
		Draw.thickness(4);
		Draw.color("royal");
		Draw.circle(x, y, 10);
		Draw.clear();
	}
}
