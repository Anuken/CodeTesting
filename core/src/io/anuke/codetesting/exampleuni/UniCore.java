package io.anuke.codetesting.exampleuni;

import io.anuke.ucore.modules.ModuleCore;

public class UniCore extends ModuleCore{
	
	public void init(){
		add(Vars.control = new UniControl());
		add(Vars.ui = new UniUI());
	}
}
