package io.anuke.codetesting.exampleuni;

import com.badlogic.gdx.graphics.Color;

import io.anuke.ucore.core.Draw;
import io.anuke.ucore.core.Effects;
import io.anuke.ucore.graphics.Hue;

public class EffectLoader{
	
	public static void load(){
		
		Effects.create("explosion", 10, e->{
			Draw.color(Color.YELLOW);
			Draw.circle(e.x, e.y, 20*e.ifract());
			Draw.reset();
		});
		
		Effects.create("hit", 10, e->{
			Draw.thickness(3f);
			Draw.color(Hue.mix(Color.WHITE, Color.ORANGE, e.ifract()));
			Draw.spikes(e.x, e.y, 5+e.ifract()*40f, 10, 8);
			Draw.reset();
		});
		
	}
	
}
