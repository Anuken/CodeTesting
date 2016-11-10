package net.pixelstatic.codetesting.modules;

import com.badlogic.gdx.InputAdapter;

import net.pixelstatic.codetesting.CodeTester;


public abstract class Module extends InputAdapter{
	public CodeTester tester;

	public void init(){}
	public abstract void update();
	public void resize(int width, int height){};
	
	public <T extends Module> T getModule(Class<T> c) {
		return tester.getModule(c);
	}
}
