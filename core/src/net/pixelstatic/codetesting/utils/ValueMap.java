package net.pixelstatic.codetesting.utils;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Keys;

public class ValueMap{
	private ObjectMap<String, Object> values;
	
	public ValueMap(){
		values = new ObjectMap<String, Object>();
	}
	
	public <T> T get(String name, Class<T> c){
		return c.cast(values.get(name));
	}
	
	public void add(String name, Object object){
		values.put(name, object);
	}
	
	public Keys<String> valueNames(){
		return values.keys();
	}
}
