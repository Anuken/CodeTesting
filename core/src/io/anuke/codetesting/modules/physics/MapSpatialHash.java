package io.anuke.codetesting.modules.physics;

import static io.anuke.ucore.UCore.scl;

import java.util.function.Consumer;

import com.badlogic.gdx.utils.ObjectSet;

import io.anuke.ucore.util.GridMap;

public class MapSpatialHash<T extends Spatial> {
	private GridMap<ObjectSet<T>> map = new GridMap<ObjectSet<T>>();
	private float gridsize = 100;

	void addEntity(T entity){
		int x = (int) (entity.getX() / gridsize), y = (int) (entity.getY() / gridsize);

		ObjectSet<T> set = map.get(x, y);

		if(set == null){
			map.put(x, y, (set = new ObjectSet<T>()));
		}

		set.add(entity);
	}

	public void clear(){
		for(ObjectSet<T> set : map.values()){
			set.clear();
		}
	}

	public void getNearbyEntities(float cx, float cy, float range, Consumer<T> con){

		if(range < 1 || range < 1)
			throw new IllegalArgumentException("rangex and rangey cannot be negative.");

		int maxx = scl(cx + range, gridsize), maxy = scl(cy + range, gridsize), minx = scl(cx - range, gridsize),
				miny = scl(cy - range, gridsize);

		for(int x = minx; x < maxx + 1; x++){
			for(int y = miny; y < maxy + 1; y++){
				ObjectSet<T> set = map.get(x, y);
				if(set != null){
					for(T e : set){
						if(Math.abs(e.getX() - cx) < range && Math.abs(e.getY() - cy) < range){
							con.accept(e);
						}
					}
				}
			}
		}
	}
}
