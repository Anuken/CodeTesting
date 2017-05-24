package io.anuke.codetesting.layervoxel;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class VoxelConverter{
	
	public static TextureRegion[] convert(int[][][] voxels){
		int w = voxels.length;
		int h = voxels[0].length;
		int l = voxels[0][0].length;
		
		TextureRegion[] regions = new TextureRegion[h];
		Pixmap pixmap = new Pixmap(w*h, l, Format.RGBA8888);
		
		for(int y = 0; y < h; y ++){
			for(int x = 0; x < w; x ++){
				for(int z = 0; z < l; z ++){
					int color = voxels[x][y][z];
					if(color != 0)
					pixmap.drawPixel(y*w+x, z, color);
				}
			}
		}
		
		Texture texture = new Texture(pixmap);
		
		for(int y = 0; y < h; y ++){
			regions[y] = new TextureRegion(texture, y*w, 0, w, l);
		}
		
		return regions;
	}
}
