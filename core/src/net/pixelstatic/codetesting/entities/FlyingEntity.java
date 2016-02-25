package net.pixelstatic.codetesting.entities;

import com.badlogic.gdx.math.Vector2;

public abstract class FlyingEntity extends SolidEntity{
	public Vector2 velocity = new Vector2();
	
	//updates velocity and position
	void UpdateVelocity(){
		x += velocity.x*delta();
		y += velocity.y*delta();
		velocity.scl((float)Math.pow(1f - material.drag, delta()));
	}
}
