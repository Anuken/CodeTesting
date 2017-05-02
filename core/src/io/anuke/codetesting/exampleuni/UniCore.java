package io.anuke.codetesting.exampleuni;

import io.anuke.ucore.modules.Core;

public class UniCore extends Core{
	
	public void init(){
		add(Vars.control = new UniControl());
		add(Vars.ui = new UniUI());
	}
}
