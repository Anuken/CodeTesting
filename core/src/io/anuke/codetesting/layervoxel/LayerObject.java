package io.anuke.codetesting.layervoxel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool.Poolable;


public class LayerObject implements Poolable{
	public TextureRegion[] regions;
	public float x, y, z, rotation;
	public Color color = new Color(1,1,1,1);
	public int offset = 0;
	
	public LayerObject(TextureRegion... regions){
		this.regions = regions;
	}
	
	public LayerObject(Texture... textures){
		regions = new TextureRegion[textures.length];
		for(int i = 0; i < textures.length; i ++) regions[i] = new TextureRegion(textures[i]);
	}
	
	public LayerObject setPosition(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	public LayerObject setPosition(float x, float y){
		return setPosition(x,y,z);
	}
	
	public LayerObject setColor(float r, float g, float b, float a){
		color.set(r, g, b, a);
		return this;
	}
	
	public LayerObject setColor(float r, float g, float b){
		color.set(r, g, b, color.a);
		return this;
	}
	
	public LayerObject add(){
		Layers.add(this);
		return this;
	}
	
	public LayerObject remove(){
		Layers.remove(this);
		return this;
	}

	@Override
	public void reset(){
		x = y = z = rotation = 0;
		regions = null;
	}
}
