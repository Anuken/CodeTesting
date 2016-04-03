package net.pixelstatic.codetesting.modules.vertex;

import net.pixelstatic.codetesting.modules.Module;


public class VertexGUI extends Module{
	VertexEditor editor;
	
	@Override
	public void update(){
		
	}
	

	public void init(){
		editor = tester.getModule(VertexEditor.class);
	}

}
