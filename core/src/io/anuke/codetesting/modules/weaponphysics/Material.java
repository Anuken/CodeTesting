package io.anuke.codetesting.modules.weaponphysics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

import io.anuke.codetesting.entities.*;

public enum Material{
	iron{
		public void draw(WeaponPhysics render, int x, int y){
			render.draw("ironblock", x, y);
		}
	},
	pulsewire{
		public void draw(WeaponPhysics render, int x, int y, Block block){
			render.draw( !block.powered ? "pulsewire" : "pulsewireglow", x, y);
		}

		public PowerType getPowerType(){
			return PowerType.conductor;
		}
	},
	pulsedelayer{

	},
	pulsetimer{
		public void draw(WeaponPhysics render, int x, int y, Block block){
			render.draw( !block.sourcepower ? "pulsetimer" : "pulsetimerglow", x, y);
		}

		public void update(int x, int y, Block[][] blocks, Block block){
			block.lifetime += Entity.delta();
			int time = 100;
			for(int i = 0;i < 4;i ++){
				block.rotation = i;
				if(world.isType(x + block.rotationX(), y + block.rotationY(), Material.pulsedelayer)) time += 100;
			}
			if(((int)block.lifetime) % time < time / 2){
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
			render.draw( !block.sourcepower ? "detector" : "detectorglow", x, y);
		}

		public void update(int x, int y, Block[][] blocks, Block block){
			for(int i = 0;i < 4;i ++){
				if( !world.noEntities(x + (i == 0 ? 1 : 0) + (i == 1 ? -1 : 0), y + (i == 2 ? 1 : 0) + (i == 3 ? -1 : 0))){
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
			render.draw("massmaker", x, y, world.world[x][y].rotation - 1);
		}

		public void powerEvent(int x, int y, Block[][] blocks, Block block){
			if(world.vacant(x + block.rotationX(), y + block.rotationY())){
				new IronCube().setVelocity(0, 0).setBlockPosition(x + block.rotationX(), y + block.rotationY()).AddSelf();
			}
		}

		public PowerType getPowerType(){
			return PowerType.acceptor;
		}

		public boolean isRotateable(){
			return true;
		}
	},
	heater{
		
	},
	gasemitter{
		int contained(int x, int y, Block block){
			for(int i = 1; i < 7; i ++){
				Block detect = world.block(x, y + i);
				if(detect.material == Material.shieldmagnet && detect.rotationY() == -1){
					for(int r = 2; r < 5; r ++){
						int num = r % 4;
						Block check = world.block(rotationX(num) * i + x, rotationY(num) * i + y);
						if(check.rotation == (num+2)%4 && check.material == Material.shieldmagnet){
							
						}else{
						//	System.out.println("checking " + check + " returning false " + check.rotation + " " + num);
							return 0;
						}
					}
					return i;
				}
			}
			return 0;
		}
		
		int heat(int x, int y, Block block, int radius){
			int heat = 0;
			for(int scanx = - radius; scanx < radius; scanx ++){
				for(int scany = - radius; scany < radius; scany ++){
					if(Math.sqrt(scanx*scanx + scany*scany) < radius){
						if(heat > 4) return 4;
						if(world.isType(x + scanx, y + scany, Material.heater)){
							heat ++;
						}
					}
				}
			}
			return heat;
		}
		
		public void draw(WeaponPhysics render, int x, int y, Block block){
			int contained = contained(x,y,block);
			
			int radius = (int)((contained != 0 ? (contained / 5f) * 100 : 200f) / 20f);
			
			int heat = heat(x,y,block,radius);
			
			float drad = radius*10f;
			if(heat > 0)
			for(Entity entity : Entity.entities.values()){
				if(!(entity instanceof DestructibleEntity)) continue;
				DestructibleEntity d = (DestructibleEntity)entity;
				float dist = (float)Math.sqrt(Math.pow(x*10+5-entity.x,2) + Math.pow(y*10+5-entity.y,2));
				if(dist < drad){
					d.heat += 0.006f * (drad-dist)/(drad) * heat/4f * (contained != 0 ? 28f/contained : 1f);
				}
			}
			
			render.batch.setColor(contained == 0 ? new Color(1,1,1,(float)Math.abs(Math.sin(Gdx.graphics.getFrameId() / 200f)) / 4f + 0.05f)
			: new Color(1f,1f,1f,(float)Math.abs(Math.sin(Gdx.graphics.getFrameId() / 200f)) / 2f+0.2f));
			
			render.draw(contained != 0 ? "gascontained" : "gas", x*10+5, y*10+5, radius*20f);
			
			render.batch.setColor(new Color(1,0.5f,0f,heat / 20f * (contained != 0 ? 2f : 1f)));
			render.draw("gascontained", x*10+5, y*10+5, radius*20f);
			
			render.batch.setColor(Color.WHITE);
			render.draw("gasemitter", x, y);
		}
	},
	magnet{

		public void draw(WeaponPhysics render, int x, int y){
			render.draw( !world.world[x][y].active ? "magnet" : "magnetlight", x, y, world.world[x][y].rotation - 1);
		}

		public void update(int x, int y, Block[][] blocks, Block block){
			block.active = world.lineForce(x, y, block, 5, 0.2f);
		}

		public boolean isRotateable(){
			return true;
		}
	},
	repulsemagnet{

		public void draw(WeaponPhysics render, int x, int y){
			render.draw( !world.world[x][y].active ? "repulsemagnet" : "repulsemagnetlight", x, y, world.world[x][y].rotation - 1);
		}

		public void update(int x, int y, Block[][] blocks, Block block){
			block.active = world.lineForce(x, y, block, 5, -0.2f);
		}

		public boolean isRotateable(){
			return true;
		}
	},
	shieldmagnet{
		public boolean isRotateable(){
			return true;
		}

		public float addscl(int x, int y, Block block){
			for(int i = 1;i < 13;i ++){ //scan 10 blocks
				//new scan coord
				int nx = block.rotationX() * i + x, ny = block.rotationY() * i + y;
				if(world.isType(nx, ny, Material.shieldmagnet) && block.opposite(world.block(nx, ny))){
					return (i - 4) / 4f;
				}else if(world.isType(nx, ny, Material.shieldmagnet)){
					return 0f;
				}
			}
			return 0f;
		}

		public void draw(WeaponPhysics render, int x, int y, Block block){
			render.draw("shieldmagnet", x, y, world.world[x][y].rotation - 1);
			float scl = 0.7f;
			//scan blocks..
			scl += addscl(x, y, block);
			Sprite sprite = new Sprite(world.getModule(WeaponPhysics.class).atlas.findRegion("shieldpart"));
			sprite.setPosition(x * 10 - 15 + block.rotationX() * 20 * scl + block.rotationX() * 5, y * 10 - 15 + block.rotationY() * 20 * scl + block.rotationY() * 5);
			sprite.setOriginCenter();
			sprite.setRotation((block.rotation - 1) * 90);
			sprite.setScale(scl);
			sprite.setColor(new Color(1, 1, 1, (float)Math.abs(Math.sin(Gdx.graphics.getFrameId() / 60f))));
			sprite.draw(world.getModule(WeaponPhysics.class).batch);
		}
	}
	/*
	repulsor{
		public void draw(WeaponPhysics render, int x, int y){
			render.draw("repulsor", x,y);
		}
		
		public void powerEvent(int x, int y, Block[][] blocks, Block block){
			for(int i = 0; i < 4; i ++){
				ArrayList<Entity> list = world.getEntities(x +(i == 0 ? 1 : 0) +(i == 2 ? -1 : 0), y  +(i == 1 ? 1 : 0) +(i == 3 ? -1 : 0));
				for(Entity e1 : list){
					if(e1 instanceof FlyingEntity) ((FlyingEntity)e1).setVelocity((i == 0 ? 1 : 0) +(i == 2 ? -1 : 0), (i == 1 ? 1 : 0) +(i == 3 ? -1 : 0));
				}
				if(!list.isEmpty())new ShieldBitEffect().setRotation(i*90).setPosition(world.worldPos(x), world.worldPos(y)).AddSelf();
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
				if(v.len() < 12f){
					v.setLength(2f);
					fly.velocity.set(v);
					new ShieldEffect().setPosition(world.worldPos(x), world.worldPos(y)).AddSelf();
				}
			}
		}
		
		public PowerType getPowerType(){
			return PowerType.acceptor;
		}
	}
	*/;
	public static WeaponWorld world;
	
	int rotationX(int i){
		return (i == 0 ? 1 : 0) +(i == 2 ? -1 : 0); 
	}
	int rotationY(int i){
		return (i == 1 ? 1 : 0) +(i == 3 ? -1 : 0);
	}

	public boolean signe(float a, float b){
		return !((a < 0 && b < 0) || (a > 0 && b > 0));
	}

	public void update(int x, int y, Block[][] blocks, Block block){

	}

	public void draw(WeaponPhysics render, int x, int y, Block block){
		draw(render, x, y);
	}

	public void draw(WeaponPhysics render, int x, int y){
		render.draw(name(), x, y);
	}

	public void powerEvent(int x, int y, Block[][] blocks, Block block){

	}

	public boolean isRotateable(){
		return false;
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
