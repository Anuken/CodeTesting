package net.pixelstatic.codetesting.entities;

//an entity that can hit blocks
public abstract class DestructibleEntity extends FlyingEntity{
	public int health = 100;
	public float heat;
	
	//when the entity dies
	public void deathEvent(){
		
	}
	
	//whether to remove the entity when it dies
	public boolean removeOnDeath(){
		return true;
	}
}
