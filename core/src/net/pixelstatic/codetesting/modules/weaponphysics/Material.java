package net.pixelstatic.codetesting.modules.weaponphysics;

import java.util.ArrayList;

import net.pixelstatic.codetesting.entities.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;


public enum Material{
	iron{
		public void draw(WeaponPhysics render, int x, int y){
			render.draw("ironblock", x,y);
		}
	},
	pulsewire{
		public void draw(WeaponPhysics render, int x, int y, Block block){
			render.draw(!block.powered ? "pulsewire" : "pulsewireglow", x,y);
		}
		
		public PowerType getPowerType(){
			return PowerType.conductor;
		}
	},
	pulsetimer{
		public void draw(WeaponPhysics render, int x, int y, Block block){
			render.draw(!block.sourcepower ? "pulsetimer" : "pulsetimerglow", x,y);
		}
		
		public void update(int x, int y, Block[][] blocks, Block block){
			if(Gdx.graphics.getFrameId() % 100 < 50){
				block.sourcepower = true;
			}else{
				block.sourcepower = false;
			}
		}
		
		public PowerType getPowerType(){
			return PowerType.source;
		}
	},
	detector{
		public void draw(WeaponPhysics render, int x, int y, Block block){
			render.draw(!block.sourcepower ? "detector" : "detectorglow", x,y);
		}
		
		public void update(int x, int y, Block[][] blocks, Block block){
			for(int i = 0; i < 4; i ++){
				if(!world.noEntities(x +(i == 0 ? 1 : 0) +(i == 1 ? -1 : 0), y  +(i == 2 ? 1 : 0) +(i == 3 ? -1 : 0))){
					block.sourcepower = true;
					return;
				}
			}
			block.sourcepower = false;
		}
		
		public PowerType getPowerType(){
			return PowerType.source;
		}
	},
	massmaker{
		public void draw(WeaponPhysics render, int x, int y){
			render.draw("massmaker", x,y);
		}
		
		public void powerEvent(int x, int y, Block[][] blocks, Block block){
			if(world.vacant(x, y+1)){
				new IronCube().setVelocity(0, 0).setBlockPosition(x, y+1).AddSelf();
			}
		}
		
		public PowerType getPowerType(){
			return PowerType.acceptor;
		}
	},
	repulsor{
		public void draw(WeaponPhysics render, int x, int y){
			render.draw("repulsor", x,y);
		}
		
		public void powerEvent(int x, int y, Block[][] blocks, Block block){
			for(int i = 0; i < 4; i ++){
				ArrayList<Entity> list = world.getEntities(x +(i == 0 ? 1 : 0) +(i == 1 ? -1 : 0), y  +(i == 2 ? 1 : 0) +(i == 3 ? -1 : 0));
				for(Entity e1 : list){
					if(e1 instanceof FlyingEntity) ((FlyingEntity)e1).setVelocity((i == 0 ? 1 : 0) +(i == 1 ? -1 : 0), (i == 2 ? 1 : 0) +(i == 3 ? -1 : 0));
				}
			}
		}
		
		public PowerType getPowerType(){
			return PowerType.acceptor;
		}
	},
	autorepulsor{
		Vector2 v = new Vector2();
		public void update(int x, int y, Block[][] blocks, Block block){
			for(Entity entity : Entity.entities.values()){
				if(!(entity instanceof FlyingEntity)) continue;
				FlyingEntity fly = (FlyingEntity)entity;
				v.set(entity.x - world.worldPos(x), entity.y - world.worldPos(y));
				if(v.len() < 10f){
					v.setLength(2f);
					fly.velocity.set(v);
					new ShieldEffect().setPosition(world.worldPos(x), world.worldPos(y)).AddSelf();
				}
			}
		}
		
		public PowerType getPowerType(){
			return PowerType.acceptor;
		}
	};
	public static WeaponWorld world;
	
	public void update(int x, int y, Block[][] blocks, Block block){

	}
	
	public void draw(WeaponPhysics render, int x, int y, Block block){
		draw(render,x,y);
	}
	
	public void draw(WeaponPhysics render, int x, int y){
		render.draw(name(), x,y);
	}
	
	public void powerEvent(int x, int y, Block[][] blocks, Block block){
		
	}
	
	public PowerType getPowerType(){
		return PowerType.neutral;
	}
	
	public final boolean isPowerSource(){
		return getPowerType().equals(PowerType.source);
	}
	
	public final boolean isConducting(){
		return getPowerType().equals(PowerType.conductor);
	}
	
	public final boolean isAcceptor(){
		return getPowerType().equals(PowerType.acceptor);
	}
	
	public final boolean isNeutral(){
		return getPowerType().equals(PowerType.neutral);
	}
	
	public final boolean isPowerable(){
		return isConducting() || isAcceptor();
	}
}
