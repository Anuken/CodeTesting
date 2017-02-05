package io.anuke.codetesting.modules.benchmark;

import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.utils.TimeUtils;

public class EntityTest{
	
	public static void test(){
		for(int j = 0; j < 10; j ++){
			ConcurrentHashMap<Integer, Trash> map = new ConcurrentHashMap<Integer, Trash>();
			//ArrayList<Trash> list = new ArrayList<Trash>();
			
			for(int i = 0; i < 999999; i ++){
				//list.add(new Trash());
				map.put(i, new Trash());
			}
			
			long start = TimeUtils.millis();
			
			for(int l = 0; l < 100; l ++){
				map.forEachValue(0, trash ->{
					trash.garbage = 1;
				});
			}
			
			long timemap = TimeUtils.timeSinceMillis(start);
			
			start = TimeUtils.millis();
			
			for(int l = 0; l < 100; l ++){
				for(Trash trash : map.values()){
					trash.garbage = 1;
				}
			}
			
			long timelist = TimeUtils.timeSinceMillis(start);
			
			System.out.println("Time foreach: " + timemap);
			System.out.println("Time for: " + timelist);
		}
	}
	
	static class Trash{
		int garbage;
		float thing1, thing2;
	}
}
