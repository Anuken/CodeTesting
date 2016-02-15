package net.pixelstatic.codetesting.modules.weaponphysics;

import net.pixelstatic.codetesting.modules.Module;

public class WeaponWorld extends Module{
	public int[][] world;
	public final int size = 200;
	
	@Override
	public void init(){
		world = new int[size][size];
	}

	@Override
	public void update(){
		
	}

}
