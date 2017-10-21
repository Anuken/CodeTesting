package io.anuke.codetesting;

import io.anuke.codetesting.cui.CuiTest;
import io.anuke.ucore.modules.ModuleCore;

public class CodeTester extends ModuleCore{
	
	@Override
	public void init(){
		module(new CuiTest());
	}
}
