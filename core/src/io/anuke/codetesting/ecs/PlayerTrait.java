package io.anuke.codetesting.ecs;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

import io.anuke.ucore.core.Inputs;
import io.anuke.ucore.core.Timers;
import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.ecs.Trait;
import io.anuke.ucore.ecs.extend.traits.PosTrait;

public class PlayerTrait extends Trait{
	
	@Override
	public void update(Spark spark){
		PosTrait pos = spark.pos();
		Vector2 vector = new Vector2();
		
		float speed = 4f*Timers.delta();
		
		if(Inputs.keyDown(Keys.W)){
			vector.y += speed;
		}
		
		if(Inputs.keyDown(Keys.A)){
			vector.x -= speed;
		}

		if(Inputs.keyDown(Keys.S)){
			vector.y -= speed;
		}

		if(Inputs.keyDown(Keys.D)){
			vector.x += speed;
		}
		
		vector.limit(speed);
		
		pos.translate(vector);
	}

}
