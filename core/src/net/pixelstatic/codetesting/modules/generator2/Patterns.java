package net.pixelstatic.codetesting.modules.generator2;

import net.pixelstatic.codetesting.utils.Noise;

import com.badlogic.gdx.graphics.Color;
import com.jhlabs.image.CellularFilter;
import com.jhlabs.image.CrystallizeFilter;

public class Patterns{
	static CrystallizeFilter filter;
	static float b = 0.15f;
	static float[][] bark = 
		{
		{0, b, 0, b, 0},
		{0, b, 0, b, 0},
		{0, b, b, b, 0},
		{0, 0, b, 0, 0},
		{0, 0, b, 0, 0}
		};
	static{
	
		filter = new CrystallizeFilter();
		filter.setEdgeThickness(0.5f);
		filter.setEdgeColor(Color.rgba8888(Color.MAGENTA));
		filter.setRandomness(0.6f);
		filter.setGridType(CellularFilter.HEXAGONAL);
		filter.setScale(7);
	}
	
	
	public static float leaves(int x, int y){
		int nscl = 6; // noise scale
		int mag = 5; //noise magnitutde
		return bark[bark.length-1-(y+(int)noise(x,y,nscl,mag)) % bark.length][(x+(int)noise(x,y,nscl,mag)) % bark.length];
	}
	
	public static float nMod(int x, int y, float scl){
		int spc = 5; // spacing between lines
		int nscl = 6; // noise scale
		int mag = 8; //noise magnitutde
		float yscl=-1f, xscl=1f; // scale of x/y coords
		if((x*xscl+y*yscl+1+(int)noise(x,y,nscl,mag))%spc == 0){
			return scl*2;
	
		}else if((x*xscl+y*yscl+(int)noise(x,y,nscl,mag))%spc == 0){
			return scl;
		}else if((x*xscl+y*yscl+(int)noise(x,y,nscl,mag)-1)%spc == 0){
			return -scl;
		}
		return 0;
	}
	
	public static float smod(int x, int y, float scl, float xscl, float yscl){
		return (int)((x*xscl)+(y*yscl))%10 == 0? scl : 0;
	}
	
	public static float mod(int x, int y, float scl){
		return (x-y)%5 == 0 ? scl : 0;
	}
	
	public static float noise(int x, int y, float scl, float mag){
		return (float)Noise.NormalNoise(x, y, scl, mag, 1f);
	}
	
	public static int leafPattern(int x, int y, int width, int height, int[] colors, int type){
		filter.setGridType(type);
		return filter.getPixel(x, y, colors, width, height);
	}
	
	public static int fromARGB(int rgb){
		java.awt.Color color = new java.awt.Color(rgb);
		return Color.rgba8888(color.getRed(), color.getGreen(), color.getBlue(), 1f);
	}
}
