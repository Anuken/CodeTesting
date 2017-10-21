package io.anuke.codetesting.lsystems;

import com.badlogic.gdx.utils.Array;

import io.anuke.codetesting.lsystems.LProcessor.Line;

public enum Evaluator{
	surface {
		@Override
		public float getScore(Array<Line> lines){
			float surface = 0f;
			float scaleback = 0f;
			int volume = lines.size;
			
			for(Line line : lines){
				surface += Math.abs(line.x1 - line.x2);
				
				if(line.y1 < 0 || line.y2 < 0 || line.y1 > 300f){
					scaleback += 2f;
				}
			}
			
			return surface*3f - volume - scaleback * 400f;
		}
	};
	
	public abstract float getScore(Array<Line> lines);
}
