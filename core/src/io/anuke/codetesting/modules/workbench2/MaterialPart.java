package io.anuke.codetesting.modules.workbench2;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Texture;

import io.anuke.ucore.graphics.Textures;

public class MaterialPart{
	public Texture texture;
	public Pixmap pixmap;
	String type;
	float x, y, hitwidth, hitheight;
	
	public MaterialPart(String type){
		this.type = type;
		
		texture = Textures.get(type);
		texture.getTextureData().prepare();
		pixmap = texture.getTextureData().consumePixmap();
		this.texture = new Texture(pixmap);
		
		hitwidth = texture.getWidth()/2.5f;
		hitheight = texture.getHeight()/2.5f;
	}
	
	public void toolUsed(int x, int y){
		System.out.println("tool use: " + x + ", " + y);
		Pixmap.setBlending(Blending.None);
		pixmap.setColor(Color.CLEAR);
		pixmap.fillCircle(x, pixmap.getHeight()-y, 2);
		texture.draw(pixmap, 0, 0);

		updateTexture();
	}
	
	public boolean collides(MaterialPart other){
		float bx = x - hitwidth;
		float by = y - hitheight;
		float tx = x + hitwidth;
		float ty = y + hitheight;
		
		float obx = other.x - other.hitwidth;
		float oby = other.y - other.hitheight;
		float otx = other.x + other.hitwidth;
		float oty = other.y + other.hitheight;
		
		return bx < otx && tx > obx && by < oty && ty > oby;
	}
	
	public float getWorldX(){
		return x;
	}
	
	public float getWorldY(){
		return y;
	}
	
	public void updateTexture(){
		texture.draw(pixmap, 0, 0);
	}
	
	public MaterialPart set(float x, float y){
		this.x = x;
		this.y = y;
		return this;
	}
	
	boolean blank(int x, int y){
		return (pixmap.getPixel(x, y) & 0x000000ff) == 0;
	}
}
