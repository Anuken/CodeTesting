package net.pixelstatic.codetesting.modules.vertex;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

public class ActorAlign{
	private static Array<ActorAlign> aligns = new Array<ActorAlign>();
	public static Stage stage;
	private float wscl, hscl;
	private float x, y;
	private int align;
	public Actor actor;
	
	public ActorAlign(Actor actor, int align, float wscl, float hscl, float xoffset, float yoffset){
		this.wscl = wscl;
		this.hscl = hscl;
		this.actor = actor;
		this.x = xoffset;
		this.y = yoffset;
		this.align = align;
		stage.addActor(actor);
		aligns.add(this);
	}
	
	public void update(){
		actor.setPosition(Gdx.graphics.getWidth() * wscl + x, Gdx.graphics.getHeight() * hscl + y, align);
	}
	
	public static void updateAll(){
		for(ActorAlign align : aligns){
			align.update();
		}
	}
}
