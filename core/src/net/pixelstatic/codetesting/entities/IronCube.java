package net.pixelstatic.codetesting.entities;

import net.pixelstatic.codetesting.modules.weaponphysics.WeaponPhysics;

public class IronCube extends DestructibleEntity{
	final static int lifetime = 3000;
	float life;
	{
		material.drag = 0;
	}

	@Override
	public void Update(){
		UpdateVelocity();
		life += delta();
		if(life > lifetime){
			RemoveSelf();
		}
	}

	@Override
	public void Draw(){
		tester.getModule(WeaponPhysics.class).draw("ironcube", x, y);
	}

}
