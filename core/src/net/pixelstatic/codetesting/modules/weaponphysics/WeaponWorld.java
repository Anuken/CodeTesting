package net.pixelstatic.codetesting.modules.weaponphysics;

import java.util.*;

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
				world[x][y] = new Block(x, y);
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
		return getEntities(x, y).isEmpty();
	}

	public boolean vacant(int x, int y){
		return noEntities(x, y) && world[x][y].empty();
	}

	public void updateBlocks(){
		for(int x = 0;x < size;x ++){
			for(int y = 0;y < size;y ++){
				if(world[x][y].empty()) continue;
				world[x][y].sources.clear();
				world[x][y].sourcepower = false;
			}
		}
		for(int x = 0;x < size;x ++){
			for(int y = 0;y < size;y ++){
				if(world[x][y].empty()) continue;
				if(world[x][y].material.isPowerSource()){
					connectPower(x, y); //update blocks that are connected to power sources
				}
			}
		}
	}

	void connectPower(int fx, int fy){
		HashSet<Block> checked = new HashSet<Block>();
		Queue<Block> queue = new LinkedList<Block>();
		queue.add(world[fx][fy]);
		while( !queue.isEmpty()){
			Block block = queue.poll();
			if(checked.contains(block)) continue;
			int x = block.x;
			int y = block.y;
			if(block.material.isConducting() || block.material.isPowerSource()){
				if(conductor(x, y + 1)) queue.add(world[x][y + 1]);
				if(conductor(x, y - 1)) queue.add(world[x][y - 1]);
				if(conductor(x + 1, y)) queue.add(world[x + 1][y]);
				if(conductor(x - 1, y)) queue.add(world[x - 1][y]);
			}
			checked.add(block);
			if(x == fx && y == fy) continue;
			block.sources.add(world[fx][fy]);
		}
	}

	public boolean conductor(int x, int y){
		return inBounds(x, y) && !world[x][y].empty() && world[x][y].material.isPowerable();
	}
	
	public float worldPos(int gridpos){
		return gridpos * physics.pixsize + physics.pixsize/2;
	}

	public boolean inBounds(int x, int y){
		return !(x < 0 || y < 0 || x >= size || y >= size);
	}

	public boolean solid(int x, int y){
		return !(world[x][y] == null || world[x][y].empty());
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
