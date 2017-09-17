package io.anuke.codetesting.ecs;


import com.badlogic.gdx.graphics.Color;

import io.anuke.ucore.core.Draw;
import io.anuke.ucore.core.Effects;
import io.anuke.ucore.ecs.Basis;
import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.ecs.extend.processors.CollisionProcessor;
import io.anuke.ucore.ecs.extend.processors.DrawProcessor;
import io.anuke.ucore.entities.Entities;
import io.anuke.ucore.modules.ControlModule;

public class EControl extends ControlModule{
	Basis basis = new Basis();
	Spark player;
	
	public EControl(){
		player = new Spark(Prototypes.player).add();
		
		new Spark(Prototypes.overenemy).add().pos().set(100, 200);
		new Spark(Prototypes.enemy).add().pos().set(100, 100);
		
		basis.addProcessor(new DrawProcessor());
		basis.addProcessor(new CollisionProcessor());
		
		basis.getProcessor(CollisionProcessor.class).resizeTree(-1000, -1000, 10000, 10000);
		
		Effects.create("test", 10, e->{
			Draw.color(Color.ORANGE);
			Draw.circle(e.x, e.y, e.ifract()*20f);
		});
	}
	
	@Override
	public void init(){
		
	}
	
	@Override
	public void update(){
		Entities.update();
		clearScreen();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		Entities.draw();
		basis.update();
		batch.end();
	}
}
