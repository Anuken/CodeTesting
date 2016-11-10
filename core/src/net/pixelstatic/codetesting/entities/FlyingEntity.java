package net.pixelstatic.codetesting.entities;

import net.pixelstatic.codetesting.modules.weaponphysics.WeaponWorld;

import com.badlogic.gdx.math.Vector2;

public abstract class FlyingEntity extends SolidEntity{
	public Vector2 velocity = new Vector2();
	
	//updates velocity and position
	void UpdateVelocity(){
		velocity.limit(material.maxvelocity);
		if(tester.getModule(WeaponWorld.class).solid(x + velocity.x*delta(), y)){ 
			velocity.x = 0;
			velocity.y *= (1f-material.friction);
		}
		if(tester.getModule(WeaponWorld.class).solid(x,y + velocity.y*delta())){ 
			velocity.y = 0;
			velocity.x *= (1f-material.friction);
		}
		x += velocity.x*delta();
		y += velocity.y*delta();
		velocity.scl((float)Math.pow(1f - material.drag, delta()));
	}
	
	public FlyingEntity setVelocity(float x, float y){
		velocity.set(x,y);
		return this;
	}
}
