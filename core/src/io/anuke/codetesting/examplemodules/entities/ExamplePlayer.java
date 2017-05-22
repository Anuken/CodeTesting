package io.anuke.codetesting.examplemodules.entities;

import io.anuke.codetesting.examplemodules.ExampleMain;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.core.Effects;
import io.anuke.ucore.core.Inputs;
import io.anuke.ucore.entities.DestructibleEntity;
import io.anuke.ucore.entities.SolidEntity;

public class ExamplePlayer extends DestructibleEntity{
	float speed = 4f;
	
	@Override
	public void update(){
		if(Inputs.keyDown("up"))
			y += speed;
		
		if(Inputs.keyDown("down"))
			y -= speed;
		
		if(Inputs.keyDown("left"))
			x -= speed;
		
		if(Inputs.keyDown("right"))
			x += speed;
		
	}
	
	@Override
	public void draw(){
		Draw.thickness(4);
		Draw.color("royal");
		Draw.circle(x, y, 10);
		Draw.reset();
	}
	
	@Override
	public void collision(SolidEntity other){
		super.collision(other);
		
		ExampleMain.i.enemy.doSomething();
		
		Effects.effect("hit", other);
		Effects.shake(5f, 10f);
	}
}
