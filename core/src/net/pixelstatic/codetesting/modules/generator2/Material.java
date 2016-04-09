package net.pixelstatic.codetesting.modules.generator2;

import com.badlogic.gdx.graphics.Color;
import com.jhlabs.image.CellularFilter;

public enum Material{
	wood(Color.BROWN.cpy().sub(0.1f, 0.1f, 0.1f, 0f), -1), 
	leaves(new Color(0.0f, 0.4f, 0.3f, 1f), -1);

	public Color color;
	public int type = CellularFilter.TRIANGULAR;

	private Material(Color color){
		this.color = color;
	}

	private Material(Color color, int type){
		this.color = color;
		this.type = type;
	}

	public Color getColor(){
		return color;
	}
}
