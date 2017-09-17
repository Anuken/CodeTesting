package io.anuke.codetesting.ecs;

import com.badlogic.gdx.graphics.Color;

import io.anuke.ucore.core.Draw;
import io.anuke.ucore.ecs.TraitList;
import io.anuke.ucore.ecs.extend.Events.Collision;
import io.anuke.ucore.ecs.extend.traits.DrawTrait;

public class TestEnemyOverride extends TestEnemy{
	
	public TestEnemyOverride(){
		event(Collision.class, (a, b)->{
			
			callSuper(Collision.class, a, b);
			
			//UCore.log("(Extra overriden code stuff)");
		});
	}
	
	@Override
	public TraitList traits(){
		return super.traits()
		.override(DrawTrait.class, new DrawTrait(spark->{
			Draw.color(Color.PURPLE);
			Draw.thick(3f);
			Draw.square(spark.pos().x, spark.pos().y, 20);
		}));
	}
}
