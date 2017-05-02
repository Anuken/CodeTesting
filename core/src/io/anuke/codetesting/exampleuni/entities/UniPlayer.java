package io.anuke.codetesting.exampleuni.entities;

import io.anuke.codetesting.exampleuni.Vars;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.core.Effects;
import io.anuke.ucore.core.Inputs;
import io.anuke.ucore.entities.DestructibleEntity;
import io.anuke.ucore.entities.SolidEntity;

public class UniPlayer extends DestructibleEntity{
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
		Draw.clear();
	}
	
	@Override
	public void collision(SolidEntity other){
		super.collision(other);
		
		Vars.enemy.doSomething();
		
		//Effects.sound("shoot");
		Effects.effect("hit", other);
		Effects.shake(5f, 10f);
	}
}
