package net.pixelstatic.codetesting.modules;

import java.util.HashMap;

import net.pixelstatic.codetesting.modules.weaponphysics.*;


public enum ModuleGroup{
	WEAPON_PHYSICS{
		@Override
		Module[] modules(){
			return new Module[]{
				new WeaponPhysics(),
				new WeaponWorld()
			};
		}
	};
	
	abstract Module[] modules();
		
	
	
	public final HashMap<Class<? extends Module>, Module> getModules(){
		Module[] modules = modules();
		HashMap<Class<? extends Module>, Module> map = new HashMap<Class<? extends Module>, Module>();
		for(Module m : modules){
			map.put(m.getClass(), m);
		}
		return map;
	}
}
