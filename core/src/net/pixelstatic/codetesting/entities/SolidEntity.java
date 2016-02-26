package net.pixelstatic.codetesting.entities;

import net.pixelstatic.codetesting.utils.MaterialData;

//a solid entity is an entity that collides with things
//this class does not have velocity; see FlyingEntity
public abstract class SolidEntity extends Entity{
	transient public MaterialData material = new MaterialData(this, 6,6);
	
	//returns whether this entity collides with the other solid entity
	public boolean collides(SolidEntity other){
		material.updateHitbox();
		other.material.updateHitbox();
		return material.collides(other.material);
	}
	
	//called when this entity hit a solid block
	public void collisionEvent(int x, int y){
	//	new ExplosionEffect().setPosition(this.x, this.y).AddSelf();
	//	RemoveSelf();
	}
}
