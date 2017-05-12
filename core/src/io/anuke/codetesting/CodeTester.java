package io.anuke.codetesting;

import io.anuke.codetesting.lsystems.LRenderer;
import io.anuke.codetesting.lsystems.LUI;
import io.anuke.ucore.modules.ModuleController;

public class CodeTester extends ModuleController<CodeTester>{
	
	@Override
	public void init(){
		addModule(new LRenderer());
		addModule(new LUI());
	}
}
