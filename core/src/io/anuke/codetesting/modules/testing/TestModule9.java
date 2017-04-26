package io.anuke.codetesting.modules.testing;

import io.anuke.ucore.modules.ModuleController;

public class TestModule9 extends ModuleController<TestModule9>{

	@Override
	public void init(){
		addModule(TestModule10.class);
	}
}
