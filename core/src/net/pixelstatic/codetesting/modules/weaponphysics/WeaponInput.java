package net.pixelstatic.codetesting.modules.weaponphysics;

import net.pixelstatic.codetesting.modules.Module;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector3;

public class WeaponInput extends Module implements InputProcessor{
	WeaponPhysics weapon;
	public float speed = 10f;

	@Override
	public void update(){
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)) Gdx.app.exit();
		if(Gdx.input.isKeyPressed(Keys.W)) weapon.camera.position.y += speed;
		if(Gdx.input.isKeyPressed(Keys.A)) weapon.camera.position.x -= speed;
		if(Gdx.input.isKeyPressed(Keys.S)) weapon.camera.position.y -= speed;
		if(Gdx.input.isKeyPressed(Keys.D)) weapon.camera.position.x += speed;
		if(Gdx.input.isButtonPressed(Buttons.LEFT))tryPlace();
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
		getModule(WeaponPhysics.class).scroll(amount);;
		return false;
	}
	
	void tryPlace(){
		Vector3 v = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f);
		Vector3 unproject = weapon.camera.unproject(v);
		weapon.placeBlock(unproject.x, unproject.y);

	}
	
}
