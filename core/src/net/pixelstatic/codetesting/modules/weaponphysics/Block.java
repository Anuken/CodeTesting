package net.pixelstatic.codetesting.modules.weaponphysics;

public class Block{
	Material material;
	
	public Block(){
		
	}
	
	public Block(Block other){
		material = other.material;
	}
	
	public boolean empty(){
		return material == null;
	}
	
	public void update(int x, int y, Block[][] world){
		material.update(x, y, world);
	}
}
