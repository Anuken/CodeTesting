package io.anuke.codetesting.ecs;

import com.badlogic.gdx.graphics.Color;

import io.anuke.ucore.core.Draw;
import io.anuke.ucore.core.Effects;
import io.anuke.ucore.ecs.Prototype;
import io.anuke.ucore.ecs.TraitList;
import io.anuke.ucore.ecs.extend.Events.*;
import io.anuke.ucore.ecs.extend.traits.*;

public class TestEnemy extends Prototype{
	
	public TestEnemy(){
		event(Collision.class, (a, b)->{
			//UCore.log("(TestEnemy) Collision between entities " + a.getID() + " and " + b.getID() + "!");
		});
		
		event(CollisionFilter.class, (a, b)->{
			return !(b.getType() instanceof TestEnemy);
		});
		
		event(Damaged.class, (a, b, damage)->{
			//UCore.log(a.getID() + " getting damaged: " + damage + " health: " + a.get(HealthTrait.class).health);
			Effects.effect("test", a.pos().x, a.pos().y);
		});
		
		event(Death.class, spark->{
			spark.remove();
		});
	}
	
	@Override
	public TraitList traits(){
		return new TraitList(
			new PosTrait(), 
			new ColliderTrait(), 
			new HealthTrait(100),
			new ProjectileTrait(),
			new DrawTrait(s->{
				Draw.color(Color.RED);
				Draw.thick(3f);
				Draw.circle(s.pos().x, s.pos().y, 20);
			})
		);
	}

}
