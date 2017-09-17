package io.anuke.codetesting.ecs;

import com.badlogic.gdx.graphics.Color;

import io.anuke.ucore.core.Draw;
import io.anuke.ucore.ecs.Prototype;
import io.anuke.ucore.ecs.TraitList;
import io.anuke.ucore.ecs.extend.traits.ColliderTrait;
import io.anuke.ucore.ecs.extend.traits.DrawTrait;
import io.anuke.ucore.ecs.extend.traits.PosTrait;

public class TestPlayer extends Prototype{

	@Override
	public TraitList traits(){
		return new TraitList(
			new PlayerTrait(), 
			new PosTrait(),
			new ColliderTrait(),
			new DrawTrait(s->{
				Draw.color(Color.GREEN);
				Draw.thick(3f);
				Draw.circle(s.pos().x, s.pos().y, 20);
			})
		);
	}

}
