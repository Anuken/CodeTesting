package net.pixelstatic.codetesting.modules.weaponphysics;

import java.util.concurrent.CopyOnWriteArrayList;

public class Block{
	CopyOnWriteArrayList<Block> sources = new CopyOnWriteArrayList<Block>();
	Material material;
	boolean powered, sourcepower;
	
	public boolean empty(){
		return material == null;
	}
	
	public void update(int x, int y, Block[][] world){
		powered = false;
		for(Block block : sources){
			if(!block.material.isPowerSource()){
				sources.remove(block);
				continue;
			}
			powered = block.sourcepower;
			if(powered) break;
		}
		material.update(x, y, world, this);
	}
}
