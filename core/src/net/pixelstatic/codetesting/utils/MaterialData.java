package net.pixelstatic.codetesting.utils;

import net.pixelstatic.codetesting.entities.SolidEntity;

import com.badlogic.gdx.math.Rectangle;

//stores mass, hitbox size, calculating collisions..
public class MaterialData{
	public float mass = 1f;
	public float drag = 0.08f;
	public float maxvelocity = 5f;
	public float friction = 0.5f;
	private SolidEntity entity;
	private Rectangle rectangle;
	
	public boolean collides(MaterialData other){
		return rectangle.overlaps(other.rectangle);
	}
	
	public Rectangle getRectangle(){
		return rectangle;
	}
	
	public MaterialData(SolidEntity entity, int hitwidth, int hitheight){
		this.entity = entity;
		rectangle = new Rectangle(0, 0, hitwidth, hitheight);
	}
	
	public void updateHitbox(){
		rectangle.setCenter(entity.x, entity.y);
	}
}
