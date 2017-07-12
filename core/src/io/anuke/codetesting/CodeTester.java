package io.anuke.codetesting;

import io.anuke.codetesting.ecs.EControl;
import io.anuke.ucore.modules.ModuleController;

public class CodeTester extends ModuleController<CodeTester>{
	
	@Override
	public void init(){
		addModule(new EControl());
	}
}
