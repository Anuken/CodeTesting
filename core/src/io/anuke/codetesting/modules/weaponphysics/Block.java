package io.anuke.codetesting.modules.weaponphysics;

import java.util.concurrent.CopyOnWriteArrayList;

public class Block{
	final int x,y;
	int rotation;
	float lifetime;
	CopyOnWriteArrayList<Block> sources = new CopyOnWriteArrayList<Block>();
	Material material;
	boolean powered, sourcepower, active;
	
	public Block(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public boolean empty(){
		return material == null;
	}
	
	public void update(int x, int y, Block[][] world){
		checkPower(x, y, world);
		material.update(x, y, world, this);
	}
	
	public void checkPower(int x, int y, Block[][] world){
		for(Block block : sources){
			if(block.sourcepower){
				if(!powered)material.powerEvent(x, y, world, this);
				powered = true;
				return;
			}
		}
		powered = false;
	}
	//returns whether or not the block is facing in an X direction
	boolean xRotation(){
		return rotationX() != 0;
	}
	//returns the facing X direction
	int rotationX(){
		int i = rotation;
		return (i == 0 ? 1 : 0) +(i == 2 ? -1 : 0); 
	}
	//returns the facing Y direction
	int rotationY(){
		int i = rotation;
		return (i == 1 ? 1 : 0) +(i == 3 ? -1 : 0);
	}
	
	public boolean opposite(Block other){
		return other.rotationX() == -this.rotationX() && other.rotationY() == -this.rotationY();
	}
	
	public String toString(){
		return "Block: " + material + " (" + x + ", " + y + ")"; 
	}
	
}
