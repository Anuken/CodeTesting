package io.anuke.codetesting.examplemodules.entities;

import io.anuke.ucore.entities.BulletEntity;
import io.anuke.ucore.entities.Entity;

public class ExampleBullet extends BulletEntity{
	
	public ExampleBullet(ExampleBulletType type, Entity owner, float angle){
		super(type, owner, angle);
	}
}
