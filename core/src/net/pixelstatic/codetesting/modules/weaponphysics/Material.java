package net.pixelstatic.codetesting.modules.weaponphysics;

import net.pixelstatic.codetesting.entities.IronCube;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;


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
	massmaker{
		public void draw(WeaponPhysics render, int x, int y){
			render.draw("massmaker", x,y);
		}
		
		public void update(int x, int y, Block[][] blocks, Block block){
			if(Gdx.graphics.getFrameId() % 100 == 0 && world.noEntities(x, y+1)){
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
		render.batch.setColor(Color.WHITE);
		render.draw("pixel", x * render.pixsize, y * render.pixsize, render.pixsize, render.pixsize);
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
	
	public final boolean isPowerable(){
		return isConducting() || isAcceptor();
	}
	
	public BlockType getType(){
		return BlockType.solid;
	}
}
