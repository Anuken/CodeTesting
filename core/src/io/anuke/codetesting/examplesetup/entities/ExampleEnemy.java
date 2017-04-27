package io.anuke.codetesting.examplesetup.entities;

import io.anuke.codetesting.examplesetup.ExampleMain;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.entities.DestructibleEntity;
import io.anuke.ucore.util.Mathf;

public class ExampleEnemy extends DestructibleEntity{
	
	@Override
	public void update(){
		if(Mathf.chance(0.05))
			new ExampleBullet(this, angleTo(ExampleMain.i.player)).add();
	}
	
	@Override
	public void draw(){
		Draw.thickness(4);
		Draw.color("orange");
		Draw.circle(x, y, 10);
		Draw.clear();
	}
}
