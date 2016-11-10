package net.pixelstatic.codetesting.modules.ecstesting;

import java.lang.reflect.Method;

import com.badlogic.gdx.utils.ObjectMap;

public abstract class Entity{
	private static long nextid;
	private long id;
	private ObjectMap<String, Object> values = new ObjectMap<String, Object>();
	private Method[] updatemethods;

	public Entity(){
		id = nextid ++;

		Class<?> c = this.getClass();
		Class<?>[] components = c.getInterfaces();
		updatemethods = new Method[components.length];
		int i = 0;
		for(Class<?> component : components){
			try{
				updatemethods[i] = component.getMethod("update", Entity.class, EntityTester.class);
				component.getMethod("init", Entity.class).invoke(this, this);
			}catch(Exception e){
				e.printStackTrace();
			}

			i ++;
		}
	}

	public final void updateComponents(EntityTester tester){
		for(Method method : updatemethods){
			try{
				method.invoke(this, this, tester);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void incrementValue(String name, float amount){
		values.put(name, (float)(Float)values.get(name) + amount);
	}
	
	public <T> T getValue(String name, Class<T> c){
		return c.cast(values.get(name));
	}
	
	public Object getValue(String name){
		return values.get(name);
	}

	public void addValue(String name, Object object){
		values.put(name, object);
	}

	public long getID(){
		return id;
	}
}
