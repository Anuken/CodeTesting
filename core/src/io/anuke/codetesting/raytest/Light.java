package io.anuke.codetesting.raytest;

import com.badlogic.gdx.graphics.Color;

public class Light{
	Raycast[] raycasts = new Raycast[50];
	float x, y, radius = 100;
	Color color = new Color(0xffff00ff);
	
	public Light(){
		for(int i = 0; i < raycasts.length; i++){
			raycasts[i] = new Raycast();
		}
	}
	
	void set(float x, float y){
		this.x = x;
		this.y = y;
	}
}
