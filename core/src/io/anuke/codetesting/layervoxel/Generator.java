package io.anuke.codetesting.layervoxel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import io.anuke.ucore.graphics.Hue;
import io.anuke.ucore.util.Mathf;

public enum Generator{
	tree{
		void gen(){
			int trunk = 7;
			for(int y = 0; y < trunk; y ++){
				disc(Color.BROWN, y, 4);
			}
			for(int y = trunk; y < h; y ++){
				disc(Color.FOREST, y, w/2-y/3);
			}
		}
	};
	
	protected int w, h, l;
	protected int[][][] blocks;
	
	abstract void gen();
	
	void disc(Color color, int y, int rad){
		disc(color, w/2, y, l/2, rad);
	}
	
	void disc(Color color, int x, int y, int z, int rad){
		
		for(int dx = -rad; dx <= rad; dx ++){
			for(int dz = -rad; dz <= rad; dz ++){
				if(Vector2.dst(dx, dz, 0f, 0f) <= rad){
					place(color, x+dx, y, z+dz);
				}
			}
		}
	}
	
	void place(Color color, int x, int y, int z){
		if(!Mathf.inBounds(x, y, z, blocks))
			return;
		
		blocks[x][y][z] = Hue.rgb(color);
	}
	
	public int[][][] generate(int w, int h, int l){
		this.w = w;
		this.h = h;
		this.l = l;
		this.blocks = new int[w][h][l];
		
		gen();
		
		return blocks;
	}
}
