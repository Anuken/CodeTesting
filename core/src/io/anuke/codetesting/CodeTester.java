package io.anuke.codetesting;

import io.anuke.codetesting.raytest.RayTest;
import io.anuke.ucore.modules.ModuleController;

public class CodeTester extends ModuleController<CodeTester>{
	
	@Override
	public void init(){
		addModule(new RayTest());
	}
}
