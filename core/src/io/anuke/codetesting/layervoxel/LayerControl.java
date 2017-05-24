package io.anuke.codetesting.layervoxel;

import com.badlogic.gdx.Input.Keys;

import io.anuke.ucore.core.Inputs;
import io.anuke.ucore.modules.RendererModule;

public class LayerControl extends RendererModule{
	VoxelPacker packer = new VoxelPacker();
	
	public void init(){
		packer.add("tree1", Generator.tree.generate(21, 30, 21));
		packer.pack();
		
		packer.newObject("tree1").add();
		
		cameraScale = 3;
		pixelate();
	}
	
	public void update(){
		float speed = 1f;
		
		if(Inputs.keyDown(Keys.Q))
			Layers.camrotation -= speed;
		
		if(Inputs.keyDown(Keys.E))
			Layers.camrotation += speed;
		
		setCamera(0, 0);
		drawDefault();
	}
	
	public void draw(){
		Layers.render();
	}
}
