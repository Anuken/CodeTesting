package io.anuke.codetesting.examplemodules.entities;

import io.anuke.codetesting.examplemodules.ExampleMain;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.core.Effects;
import io.anuke.ucore.entities.DestructibleEntity;
import io.anuke.ucore.util.Mathf;

public class ExampleEnemy extends DestructibleEntity{
	
	@Override
	public void update(){
		if(Mathf.chance(0.05))
			new ExampleBullet(ExampleBulletType.test, this, angleTo(ExampleMain.i.player)).add();
	}
	
	@Override
	public void draw(){
		Draw.thickness(4);
		Draw.color("orange");
		Draw.circle(x, y, 10);
		Draw.reset();
	}
	
	public void doSomething(){
		Effects.effect("hit", this);
	}
}
