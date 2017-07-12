package io.anuke.codetesting.ecs;

import io.anuke.ucore.ecs.Basis;
import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.ecs.extend.DrawProcessor;
import io.anuke.ucore.modules.RendererModule;

public class EControl extends RendererModule{
	Basis basis = new Basis();
	Spark player;
	
	public EControl(){
		player = new Spark(Prototypes.player).add();
		new Spark(Prototypes.enemy).add();
		
		basis.addProcessor(new DrawProcessor());
	}
	
	@Override
	public void init(){
		
	}
	
	@Override
	public void update(){
		clearScreen();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		basis.update();
		batch.end();
	}
}
