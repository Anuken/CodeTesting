package net.pixelstatic.codetesting.entities;

import net.pixelstatic.codetesting.modules.weaponphysics.WeaponPhysics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class ShieldBitEffect extends Effect{
	float rotation;
	{
		lifetime = 15;
	}
	
	@Override
	public void Draw(){
		Sprite sprite = new Sprite(tester.getModule(WeaponPhysics.class).atlas.findRegion("shieldbit"));
		sprite.setOrigin(0, 10);
		sprite.setPosition(x, y - 10);
		sprite.setRotation(rotation);
		sprite.setColor(new Color(1,1,1,1f-life/lifetime));
		sprite.draw(tester.getModule(WeaponPhysics.class).batch);
	}
	
	public ShieldBitEffect setRotation(float rotation){
		this.rotation = rotation;
		return this;
	}

}
