package net.pixelstatic.codetesting.modules.weaponphysics;

import net.pixelstatic.codetesting.modules.Module;

public class WeaponWorld extends Module{
	public Block[][] world;
	public Block[][] temp;
	public final int size = 200;
	public boolean other;

	@Override
	public void init(){
		world = new Block[size][size];
		temp = new Block[size][size];
		for(int x = 0;x < size;x ++){
			for(int y = 0;y < size;y ++){
				world[x][y] = new Block();
			}
		}
	}
	
	public boolean solid(int x, int y){
		return !(world[x][y] == null  ||world[x][y].empty());
	}

	@Override
	public void update(){
		for(int x = 0;x < size;x ++){
			for(int y = 0;y < size;y ++){
				if( !world[x][y].empty()) world[x][y].update(x, y, world);
			}
		}
	}
}
