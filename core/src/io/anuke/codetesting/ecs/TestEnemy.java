package io.anuke.codetesting.ecs;

import com.badlogic.gdx.graphics.Color;

import io.anuke.ucore.core.Draw;
import io.anuke.ucore.ecs.Prototype;
import io.anuke.ucore.ecs.TraitList;
import io.anuke.ucore.ecs.extend.DrawTrait;
import io.anuke.ucore.ecs.extend.PosTrait;

public class TestEnemy extends Prototype{
	
	@Override
	public TraitList traits(){
		return new TraitList(new PosTrait(), 
			new DrawTrait(s->{
				Draw.color(Color.RED);
				Draw.thick(3f);
				Draw.circle(s.pos().x, s.pos().y, 20);
			})
		);
	}

}
