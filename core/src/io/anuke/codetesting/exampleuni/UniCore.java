package io.anuke.codetesting.exampleuni;

import io.anuke.ucore.modules.ModuleCore;

public class UniCore extends ModuleCore{
	
	public void init(){
		module(Vars.control = new UniControl());
		module(Vars.ui = new UniUI());
	}
}
