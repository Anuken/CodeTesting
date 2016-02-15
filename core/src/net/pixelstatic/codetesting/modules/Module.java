package net.pixelstatic.codetesting.modules;

import net.pixelstatic.codetesting.CodeTester;


public abstract class Module{
	public CodeTester tester;

	public void init(){}
	public abstract void update();
	
	public <T extends Module> T getModule(Class<T> c) {
		return tester.getModule(c);
	}
}
