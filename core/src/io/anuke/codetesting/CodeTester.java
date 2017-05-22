package io.anuke.codetesting;

import io.anuke.codetesting.draw.Drawer;
import io.anuke.ucore.modules.ModuleController;

public class CodeTester extends ModuleController<CodeTester>{
	
	@Override
	public void init(){
		addModule(new Drawer());
	}
}
