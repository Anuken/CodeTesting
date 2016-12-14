package io.anuke.codetesting.modules.benchmark;

import java.util.HashMap;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.TimeUtils;

public class CollectionBenchmark{
	public static void testObjectMap(int iterations){
		ObjectMap<Integer, Integer> omap = new ObjectMap<Integer, Integer>();
		HashMap<Integer, Integer> hmap = new HashMap<Integer, Integer>();
		long tests = 10;
		long ostime = 0;
		long hstime = 0;
		for(int c = 0;c < tests;c ++){
			long otime = TimeUtils.millis();
			for(int i = 0;i < iterations;i ++){
				omap.put(i, i + 1);
			}
			for(int i = 0;i < iterations;i ++){
				omap.remove(i);
			}
			long oelapsed = TimeUtils.timeSinceMillis(otime);
			System.out.println("ObjectMap speed: " + oelapsed);

			long htime = TimeUtils.millis();
			for(int i = 0;i < iterations;i ++){
				hmap.put(i, i + 1);
			}
			for(int i = 0;i < iterations;i ++){
				hmap.remove(i);
			}
			long helapsed = TimeUtils.timeSinceMillis(htime);
			System.out.println("HashMap speed: " + helapsed);
			
			ostime += oelapsed;
			hstime += helapsed;
		}
		System.out.println("Final results: objectmap: "+ (ostime/tests) + ", hashmap: " +(hstime/tests));

	}
}
