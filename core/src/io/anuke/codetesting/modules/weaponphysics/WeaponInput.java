package io.anuke.codetesting.modules.weaponphysics;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.*;

import io.anuke.codetesting.entities.ShieldBitEffect;
import io.anuke.codetesting.modules.Module;

public class WeaponInput extends Module implements InputProcessor{
	WeaponPhysics weapon;
	public float speed = 10f;

	@Override
	public void update(){
		float speedtemp = speed * getModule(WeaponPhysics.class).camera.zoom;
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)) Gdx.app.exit();
		if(Gdx.input.isKeyPressed(Keys.W)) weapon.camera.position.y += speedtemp;
		if(Gdx.input.isKeyPressed(Keys.A)) weapon.camera.position.x -= speedtemp;
		if(Gdx.input.isKeyPressed(Keys.S)) weapon.camera.position.y -= speedtemp;
		if(Gdx.input.isKeyPressed(Keys.D)) weapon.camera.position.x += speedtemp;
		if(Gdx.input.isButtonPressed(Buttons.LEFT)) tryPlace();
		if(Gdx.input.isButtonPressed(Buttons.RIGHT)) tryRemove();
		if(Gdx.input.isKeyJustPressed(Keys.R)) tryRotate();
		if(Gdx.input.isButtonPressed(Buttons.LEFT)){
			new ShieldBitEffect().setRotation(MathUtils.random(360f)).AddSelf();;
		}
	}
	
	@Override
	public void init(){
		Gdx.input.setInputProcessor(this);
		weapon = getModule(WeaponPhysics.class);
	}
	
	@Override
	public boolean keyDown(int keycode){
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode){
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character){
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button){
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button){
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer){
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY){
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount){
		if(!Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)){
			getModule(WeaponPhysics.class).scroll(amount);;
		}else{
			amount = -amount;
			int next = weapon.block.ordinal() + amount;
			if(next < 0 || next >= Material.values().length) return false;
			weapon.block = Material.values()[next];
		}
		return false;
	}
	
	void tryRotate(){
		Vector3 v = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f);
		Vector3 unproject = weapon.camera.unproject(v);
		weapon.rotateBlock((int)(unproject.x / weapon.pixsize),(int)(unproject.y / weapon.pixsize));
	}
	
	void tryPlace(){
		Vector3 v = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f);
		Vector3 unproject = weapon.camera.unproject(v);
		weapon.placeBlock((int)(unproject.x / weapon.pixsize),(int)(unproject.y / weapon.pixsize));
	}
	
	void tryRemove(){
		Vector3 v = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f);
		Vector3 unproject = weapon.camera.unproject(v);
		weapon.removeBlock((int)(unproject.x / weapon.pixsize),(int)(unproject.y / weapon.pixsize));
	}
	
}
