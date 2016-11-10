package net.pixelstatic.codetesting.entities;

import net.pixelstatic.codetesting.modules.weaponphysics.WeaponPhysics;

import com.badlogic.gdx.graphics.Color;

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
