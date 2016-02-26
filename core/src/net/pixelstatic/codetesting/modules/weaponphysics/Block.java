package net.pixelstatic.codetesting.modules.weaponphysics;

import java.util.concurrent.CopyOnWriteArrayList;

public class Block{
	final int x,y;
	CopyOnWriteArrayList<Block> sources = new CopyOnWriteArrayList<Block>();
	Material material;
	boolean powered, sourcepower;
	
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
	
	public String toString(){
		return "Block: " + material + " (" + x + ", " + y + ")"; 
	}
	
}
