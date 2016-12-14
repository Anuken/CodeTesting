package io.anuke.codetesting.entities;

import com.badlogic.gdx.graphics.Color;

import io.anuke.codetesting.modules.weaponphysics.WeaponPhysics;

public class ExplosionEffect extends Effect{
	{
		lifetime = 12;
	}

	@Override
	public void Draw(){
		tester.getModule(WeaponPhysics.class).batch.setColor(life >= lifetime /2 ? Color.DARK_GRAY : Color.WHITE);
		tester.getModule(WeaponPhysics.class).draw("explosion", x, y);
		tester.getModule(WeaponPhysics.class).batch.setColor(Color.WHITE);
	}

}
