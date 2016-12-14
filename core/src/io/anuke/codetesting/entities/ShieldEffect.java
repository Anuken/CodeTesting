package io.anuke.codetesting.entities;

import com.badlogic.gdx.graphics.Color;

import io.anuke.codetesting.modules.weaponphysics.WeaponPhysics;

public class ShieldEffect extends Effect{
	{
		lifetime = 15;
	}
	@Override
	public void Draw(){
		tester.getModule(WeaponPhysics.class).batch.setColor(new Color(1,1,1, 1f-life/lifetime));
		tester.getModule(WeaponPhysics.class).draw("shield", x, y);
		tester.getModule(WeaponPhysics.class).batch.setColor(Color.WHITE);
	}

}
