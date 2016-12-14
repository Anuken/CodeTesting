package io.anuke.codetesting.modules;

import java.util.HashMap;


public enum ModuleGroup{
	/*
	WEAPON_PHYSICS(new WeaponPhysics(),  new WeaponWorld(), new WeaponInput()),
	GENERATOR(new Generator(),new PlantGenerator(),new GeneratorInput(),new Renderer()),	
	WORKBENCH(new net.pixelstatic.codetesting.modules.workbench.Renderer()),
	ENTITY_TESTER(new EntityTester()),
	TESTING(new TestModule()),
	TESTING2(new TestModule2()),
	SHADERS(new LightTest());*/
	;
	private Module[] modules;
	
	private ModuleGroup(Module...modules ){
		this.modules = modules;
	}
	
	public Module[] modules(){
		return modules;
	}
	
	public final HashMap<Class<? extends Module>, Module> getModules(){
		HashMap<Class<? extends Module>, Module> map = new HashMap<Class<? extends Module>, Module>();
		for(Module m : modules){
			map.put(m.getClass(), m);
		}
		return map;
	}
}
