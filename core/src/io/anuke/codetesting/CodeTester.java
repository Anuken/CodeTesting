package io.anuke.codetesting;

import io.anuke.codetesting.raytest.RayTest;
import io.anuke.ucore.modules.ModuleCore;

public class CodeTester extends ModuleCore<CodeTester>{
	
	@Override
	public void init(){
		addModule(new RayTest());
	}
}
