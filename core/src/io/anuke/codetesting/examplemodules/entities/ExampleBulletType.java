package io.anuke.codetesting.examplemodules.entities;

import io.anuke.ucore.core.Draw;
import io.anuke.ucore.entities.BaseBulletType;

public abstract class ExampleBulletType extends BaseBulletType<ExampleBullet>{
	
	public static final ExampleBulletType
	
	test = new ExampleBulletType(){
		{
			speed = 4f;
			damage = 1;
		}
		
		public void draw(ExampleBullet b){
			Draw.color("red");
			Draw.thickness(5f);
			Draw.circle(b.x, b.y, 5);
			Draw.reset();
		}
	};
}
