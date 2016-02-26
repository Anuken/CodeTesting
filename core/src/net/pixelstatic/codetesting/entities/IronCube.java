package net.pixelstatic.codetesting.entities;

import net.pixelstatic.codetesting.modules.weaponphysics.WeaponPhysics;

public class IronCube extends DestructibleEntity{
	{
		material.drag = 0;
	}

	@Override
	public void Update(){
		UpdateVelocity();
	}

	@Override
	public void Draw(){
		tester.getModule(WeaponPhysics.class).draw("ironcube", x, y);
	}

}
