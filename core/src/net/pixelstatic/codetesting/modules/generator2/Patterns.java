package net.pixelstatic.codetesting.modules.generator2;

import com.badlogic.gdx.graphics.Color;
import com.jhlabs.image.CellularFilter;
import com.jhlabs.image.CrystallizeFilter;

public class Patterns{
	static CrystallizeFilter filter;
	
	static{
	
		filter = new CrystallizeFilter();
		filter.setEdgeThickness(0.5f);
		filter.setEdgeColor(Color.rgba8888(Color.MAGENTA));
		filter.setRandomness(0.6f);
		filter.setGridType(CellularFilter.HEXAGONAL);
		filter.setScale(7);
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
