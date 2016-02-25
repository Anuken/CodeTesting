package net.pixelstatic.codetesting.modules.weaponphysics;

import com.badlogic.gdx.graphics.Color;


public enum Material{
	iron{
		public void draw(WeaponPhysics render, int x, int y){
			render.batch.setColor(Color.GRAY);
			render.draw("ironblock", x,y);
		}
	};
	
	public void update(int x, int y, Block[][] world){

	}
	
	public void draw(WeaponPhysics render, int x, int y){
		render.batch.setColor(Color.WHITE);
		render.draw("pixel", x * render.pixsize, y * render.pixsize, render.pixsize, render.pixsize);
	}
	
	public BlockType getType(){
		return BlockType.solid;
	}
}
