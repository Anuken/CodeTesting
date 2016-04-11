package net.pixelstatic.codetesting.utils.values;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;

public abstract class Value<T>{
	protected T object, defobject;
	
	public Value(T t){
		this.object = t;
		this.defobject = t;
	}
	
	public abstract Actor getActor(Skin skin);
	public abstract void onChange(Actor actor);
	
	public String toString(){
		return object.toString();
	}
	
	public void reset(Actor actor){
		this.object = defobject;
	}
	
	public T getValue(){
		return object;
	}
	
	public static class FloatValue extends Value<Float>{
		private float min, max, def;
		
		public FloatValue(float min, float max, float def){
			super(def);
			this.min = min;
			this.max = max;
			this.def = def;
		}
		
		@Override
		public Actor getActor(Skin skin){
			Slider slider = new Slider(min,max,0.05f,false,skin);
			slider.setValue(def);
			return slider;
		}

		@Override
		public void onChange(Actor actor){
			object = ((Slider)actor).getValue();
		}
		
		public void reset(Actor actor){
			this.object = defobject;
			((Slider)actor).setValue(object);
		}
		
		public String toString(){
			String string = object.toString();
			return string.length() > 3 ? string.substring(0, 4) : string;
		}
		
	}
}
