package net.pixelstatic.codetesting;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import io.anuke.ucore.UCore;
import net.pixelstatic.codetesting.entities.Entity;
import net.pixelstatic.codetesting.modules.Module;
import net.pixelstatic.codetesting.modules.testing.LayerModule;

public class CodeTester extends ApplicationAdapter{
	//ModuleGroup type = ModuleGroup.WORKBENCH;
	Array<Module> moduleArray = new Array<Module>();
	ObjectMap<Class<? extends Module>, Module> modules = new ObjectMap<Class<? extends Module>, Module>();

	@Override
	public void create(){
		UCore.maximizeWindow();
		Entity.tester = this;
		Module[] array = {new LayerModule()};
		
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
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) Gdx.app.exit();
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
