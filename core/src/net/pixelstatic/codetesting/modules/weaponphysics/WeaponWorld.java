package net.pixelstatic.codetesting.modules.weaponphysics;

import java.util.ArrayList;

import net.pixelstatic.codetesting.entities.Entity;
import net.pixelstatic.codetesting.modules.Module;

public class WeaponWorld extends Module{
	public WeaponPhysics physics;
	public Block[][] world;
	public Block[][] temp;
	public final int size = 200;
	public boolean other;

	@Override
	public void init(){
		Material.world = this;
		physics = getModule(WeaponPhysics.class);
		world = new Block[size][size];
		temp = new Block[size][size];
		for(int x = 0;x < size;x ++){
			for(int y = 0;y < size;y ++){
				world[x][y] = new Block();
			}
		}
	}
	
	public ArrayList<Entity> getEntities(int x, int y){
		ArrayList<Entity> list = new ArrayList<Entity>();
		for(Entity entity : Entity.entities.values()){
			if((int)(entity.x / physics.pixsize) == x && (int)(entity.y / physics.pixsize) == y){
				list.add(entity);
			}
		}
		return list;
	}
	
	public boolean noEntities(int x, int y){
		return getEntities(x,y).isEmpty();
	}
	
	public void updateBlocks(){
		
	}
	
	public boolean solid(int x, int y){
		return !(world[x][y] == null  ||world[x][y].empty());
	}

	@Override
	public void update(){
		for(int x = 0;x < size;x ++){
			for(int y = 0;y < size;y ++){
				if( !world[x][y].empty()) world[x][y].update(x, y, world);
			}
		}
	}
}
