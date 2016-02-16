package net.pixelstatic.codetesting.modules.weaponphysics;

import com.badlogic.gdx.graphics.Color;


public enum Material{
	iron{
		public void draw(WeaponPhysics render, int x, int y){
			render.batch.setColor(Color.GRAY);
			render.draw("pixel", x * render.pixsize, y * render.pixsize, render.pixsize, render.pixsize);
		}
	}, 
	hydrogen{
	
		
		public BlockType getType(){
			return BlockType.gas;
		}
		
		public void draw(WeaponPhysics render, int x, int y){
			render.batch.setColor(Color.SKY);
			render.draw("pixel", x * render.pixsize, y * render.pixsize, render.pixsize, render.pixsize);
		}
	};
	
	public void draw(WeaponPhysics render, int x, int y){
		render.batch.setColor(Color.WHITE);
		render.draw("pixel", x * render.pixsize, y * render.pixsize, render.pixsize, render.pixsize);
	}
	
	public BlockType getType(){
		return BlockType.solid;
	}
}
