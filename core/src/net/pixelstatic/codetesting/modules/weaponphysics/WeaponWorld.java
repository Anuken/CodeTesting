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

	@Override
	public void update(){
		Block[][] worldclone = cloneWorld();
		for(int x = 0;x < size;x ++){
			for(int y = 0;y < size;y ++){
				if( !world[x][y].empty()) world[x][y].update(x, y, worldclone);
			}
		}
		world = worldclone;

	}

	void set(Block[][] clone){
		for(int x = 0;x < size;x ++){
			for(int y = 0;y < size;y ++){
				world[x][y] = clone[x][y];
			}
		}
	}

	Block[][] cloneWorld(){
		Block[][] temp = new Block[size][size];
		for(int x = 0;x < size;x ++){
			for(int y = 0;y < size;y ++){
				temp[x][y] = new Block(this.world[x][y]);
			}
		}
		return temp;
	}

}
