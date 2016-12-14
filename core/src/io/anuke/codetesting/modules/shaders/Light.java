package io.anuke.codetesting.modules.shaders;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

public class Light{
	Color color;
	Vector3 pos;
	
	public Light(Color color, Vector3 pos){
		this.color = color;
		this.pos = pos;
	}
}
