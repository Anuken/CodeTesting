package net.pixelstatic.codetesting.modules.weaponphysics;

import com.badlogic.gdx.math.MathUtils;

public enum BlockType{
	solid, gas{
		public void update(int x, int y, Block[][] world){
			//for(int i = 0;i < 5;i ++){
				Material mat = world[x][y].material;
				boolean movex = half();
				int mx = movex ? (half() ? 1 : -1) : 0;
				int my = !movex ? (half() ? 1 : -1) : 0;
				//if(!inBounds(x+mx, y+my, world.length)) continue;
				tryPlace( x + mx, y + my, world, mat);
				//x += mx;
				//y += my;
			//}
		}
	};
	

	public boolean half(){
		return MathUtils.randomBoolean();
	}

	public void update(int x, int y, Block[][] world){

	}

	public void tryPlace(int x, int y, Block[][] world, Material material){
		if( !inBounds(x, y, world.length)) return;
		if(world[x][y].empty()) world[x][y].material = material;
	}

	public void move(int ox, int oy, int x, int y, Block[][] world, Material material){
		if( !inBounds(x, y, world.length) || world[ox][oy].empty()) return;
		if(world[x][y].empty()){
			world[x][y].material = material;
			world[ox][oy].material = null;
		}

	}

	public boolean inBounds(int x, int y, int size){
		return !(x < 0 || y < 0 || x >= size || y >= size);
	}
}
