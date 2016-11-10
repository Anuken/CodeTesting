package net.pixelstatic.codetesting.entities;

import net.pixelstatic.codetesting.modules.weaponphysics.WeaponPhysics;

import com.badlogic.gdx.graphics.Color;

public class IronCube extends DestructibleEntity{
	final static int lifetime = 3000;
	float life;
	{
		material.drag = 0.03f;
	}

	@Override
	public void Update(){
		UpdateVelocity();
		life += delta();
		if(life > lifetime){
			RemoveSelf();
		}
		if(heat >= 1f) collisionEvent((int)x,(int)y);
		if(heat > 0) heat -= 0.003f;
	}

	@Override
	public void Draw(){
		tester.getModule(WeaponPhysics.class).draw("ironcube", x, y);
		tester.getModule(WeaponPhysics.class).batch.setColor(new Color(1,0.4f,0.05f,heat/1.5f));
		tester.getModule(WeaponPhysics.class).draw("ironcubewhite", x, y);
		tester.getModule(WeaponPhysics.class).batch.setColor(Color.WHITE);
	}

}
