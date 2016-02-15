package net.pixelstatic.codetesting.modules.weaponphysics;

import net.pixelstatic.codetesting.modules.Module;

public class WeaponWorld extends Module{
	public int[][] world;
	public final int size = 200;
	
	@Override
	public void init(){
		world = new int[size][size];
		for(int x = 0;x < size;x ++){
			for(int y = 0;y < size;y ++){
				if(Math.random() < 0.1){
				//	world[x][y] = 1;
				}
			}
		}
	}

	@Override
	public void update(){
		
	}

}
