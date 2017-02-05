package io.anuke.codetesting.modules.weaponphysics;

import java.util.*;

import com.badlogic.gdx.math.Vector2;

import io.anuke.codetesting.entities.Entity;
import io.anuke.codetesting.entities.FlyingEntity;
import io.anuke.codetesting.entities.ShieldEffect;
import io.anuke.codetesting.modules.Module;

public class WeaponWorld extends Module{
	private Vector2 v = new Vector2();
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
	
	public void repulseForce(int blockx, int blocky, float range, float force){
		float x = blockx * physics.pixsize+ physics.pixsize/2, y =  blocky* physics.pixsize + physics.pixsize/2;
		for(Entity entity : Entity.entities.values()){
			if(!(entity instanceof FlyingEntity)) continue;
			FlyingEntity fly = (FlyingEntity)entity;
			v.set(entity.x - x, entity.y - y);
			float dist = v.len();
			if(dist < range){
				v.setLength(force * (1f-dist/range));
				fly.velocity.add(v);
				new ShieldEffect().setPosition(x, y).AddSelf();
			}
		}
	}
	
	public boolean lineForce(int x, int y, Block block, int range, float force){
		boolean any = false;
		int ray = 100;
		for(int i = 1; i <= range; i ++){
			if(solid(x + block.rotationX()*i, y + block.rotationY()*i)){ ray = i;break;}
		}
		for(Entity entity : Entity.entities.values()){
			if(!(entity instanceof FlyingEntity)) continue;
			FlyingEntity fly = (FlyingEntity)entity;
			if((block.xRotation() && entity.blockY() == y && signe(worldPos(x) - entity.x, block.rotationX()) && Math.abs(x - entity.blockX()) < range) 
					|| (!block.xRotation() && entity.blockX() == x && signe(worldPos(y) - entity.y, block.rotationY()) && Math.abs(y - entity.blockY()) < range)){
				v.set((worldPos(x) - entity.x), (worldPos(y) - entity.y));
				if(Math.abs(x- entity.blockX()) > ray || Math.abs(y- entity.blockY()) > ray){
					return false;
				}
				v.setAngle(block.rotation * 90 + (force < 0 ? 0 : 180));
				float dist = v.len();
				v.setLength((force*1.3f) / (0.5f-dist/range));
				
				fly.velocity.add(v);
				any = true;
			}
		}
		return any;
	}
	
	public boolean isType(int x, int y, Material mat){
		if(!inBounds(x,y)) return false;
		return world[x][y].material == mat;
	}
	
	public boolean signe(float a, float b){
		return !((a < 0 && b < 0) || (a > 0 && b > 0)); 
	}
	
	public float worldPos(int gridpos){
		return gridpos * physics.pixsize + physics.pixsize/2;
	}

	public boolean inBounds(int x, int y){
		return !(x < 0 || y < 0 || x >= size || y >= size);
	}
	
	public Block block(int x, int y){
		if(!inBounds(x,y)) return world[0][0];
		return world[x][y];
	}

	public boolean solid(int x, int y){
		if(!inBounds(x,y)) return false;
		return !(world[x][y] == null || world[x][y].empty());
	}
	
	public boolean solid(float x, float y){
		return solid((int)(x/physics.pixsize), (int)(y/physics.pixsize));
	}

	@Override
	public void update(){
		for(int x = 0;x < size;x ++){
			for(int y = 0;y < size;y ++){
				if( !world[x][y].empty()){
					world[x][y].update(x, y, world);
				}
			}
		}
	}
}
