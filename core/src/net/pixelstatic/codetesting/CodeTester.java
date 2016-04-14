package net.pixelstatic.codetesting;

import java.util.HashMap;

import net.pixelstatic.codetesting.entities.Entity;
import net.pixelstatic.codetesting.modules.Module;
import net.pixelstatic.codetesting.modules.ModuleGroup;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.utils.Array;

public class CodeTester extends ApplicationAdapter{
	ModuleGroup type = ModuleGroup.ENTITY_TESTER;
	Array<Module> moduleArray = new Array<Module>();
	HashMap<Class<? extends Module>, Module> modules = new HashMap<Class<? extends Module>, Module>();

	@Override
	public void create(){
		Entity.tester = this;
		Module[] array = type.modules();
		for(Module module : array){
			moduleArray.add(module);
			modules.put(module.getClass(), module);
		}
		
		for(Module module : moduleArray){
			module.tester = this;
			module.init();
		}
	}

	@Override
	public void render(){
		for(Module module : moduleArray)
			module.update();
	}
	
	@Override
	public void resize(int width, int height){
		for(Module module : moduleArray)
			module.resize(width, height);
	}
	
	public <T extends Module> T getModule(Class<T> c) {
		return c.cast(modules.get(c));
	}
}
