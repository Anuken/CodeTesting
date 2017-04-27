package io.anuke.codetesting.examplesetup.entities;

import io.anuke.ucore.core.Draw;
import io.anuke.ucore.entities.BulletEntity;
import io.anuke.ucore.entities.Entity;
import io.anuke.ucore.entities.SolidEntity;

public class ExampleBullet extends BulletEntity{
	
	public ExampleBullet(Entity owner, float angle){
		super(owner, 5f, angle);
	}
	
	@Override
	public void collision(SolidEntity other){
		remove();
	}
	
	@Override
	public void draw(){
		Draw.color("red");
		Draw.thickness(5f);
		Draw.circle(x, y, 5);
		Draw.clear();
	}

	@Override
	public int getDamage(){
		return 1;
	}

}
