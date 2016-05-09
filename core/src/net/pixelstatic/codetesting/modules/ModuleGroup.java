package net.pixelstatic.codetesting.modules;

import java.util.HashMap;

import net.pixelstatic.codetesting.modules.ecstesting.EntityTester;
import net.pixelstatic.codetesting.modules.generator.*;
import net.pixelstatic.codetesting.modules.weaponphysics.WeaponInput;
import net.pixelstatic.codetesting.modules.weaponphysics.WeaponPhysics;
import net.pixelstatic.codetesting.modules.weaponphysics.WeaponWorld;


public enum ModuleGroup{
	WEAPON_PHYSICS{
		@Override
		public Module[] modules(){
			return new Module[]{
				new WeaponPhysics(),
				new WeaponWorld(),
				new WeaponInput()
			};
		}
	},
	GENERATOR{
		@Override
		public Module[] modules(){
			return new Module[]{
				new Generator(),
				new PlantGenerator(),
				new GeneratorInput(),
				new Renderer()
			};
		}
	},	
	ENTITY_TESTER{
		@Override
		public Module[] modules(){
			return new Module[]{
				new EntityTester(),
			};
		}
	};
	
	abstract public Module[] modules();
		
	
	
	public final HashMap<Class<? extends Module>, Module> getModules(){
		Module[] modules = modules();
		HashMap<Class<? extends Module>, Module> map = new HashMap<Class<? extends Module>, Module>();
		for(Module m : modules){
			map.put(m.getClass(), m);
		}
		return map;
	}
}
