package io.anuke.codetesting.modules.voxel;

import com.badlogic.gdx.graphics.Color;

public enum Resource{
	rock(Color.FOREST.cpy().mul(0.8f, 0.8f, 0.8f, 1f));
	
	int color;
	
	private Resource(Color color){
		this.color = Color.rgba8888(color);
	}
}
