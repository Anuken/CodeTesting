package net.pixelstatic.codetesting;

import java.util.HashMap;

import net.pixelstatic.codetesting.entities.Entity;
import net.pixelstatic.codetesting.modules.*;

import com.badlogic.gdx.ApplicationAdapter;

public class CodeTester extends ApplicationAdapter{
	ModuleGroup type = ModuleGroup.WEAPON_PHYSICS;
	HashMap<Class<? extends Module>, Module> modules;

	@Override
	public void create(){
		Entity.tester = this;
		modules = type.getModules();
		for(Module module : modules.values()){
			module.tester = this;
			module.init();
		}
	}

	@Override
	public void render(){
		for(Module module : modules.values())
			module.update();
	}
	
	@Override
	public void resize(int width, int height){
		for(Module module : modules.values())
			module.resize(width, height);
	}
	
	public <T extends Module> T getModule(Class<T> c) {
		return c.cast(modules.get(c));
	}
}
