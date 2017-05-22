package io.anuke.codetesting.exampleuni.entities;

import io.anuke.codetesting.examplemodules.entities.ExampleBullet;
import io.anuke.codetesting.examplemodules.entities.ExampleBulletType;
import io.anuke.codetesting.exampleuni.Vars;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.core.Effects;
import io.anuke.ucore.entities.DestructibleEntity;
import io.anuke.ucore.util.Mathf;

public class UniEnemy extends DestructibleEntity{

	@Override
	public void update(){
		if(Mathf.chance(0.05))
			new ExampleBullet(ExampleBulletType.test, this, angleTo(Vars.player)).add();
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
