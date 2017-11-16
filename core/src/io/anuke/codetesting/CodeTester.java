package io.anuke.codetesting;

import io.anuke.codetesting.io.PolygonGenerator;
import io.anuke.ucore.modules.ModuleCore;

public class CodeTester extends ModuleCore{
	
	@Override
	public void init(){
		module(new PolygonGenerator());
	}
}
