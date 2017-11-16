package io.anuke.codetesting.storysim;

import com.badlogic.gdx.utils.Array;

import io.anuke.codetesting.storysim.actors.Peasant;

public class Story{
	Array<Actor> actors = new Array<>();
	Array<Place> places = new Array<>();
	
	public Story(){
		Place place = new Place();
		places.add(place);
		
		for(int i = 0; i < 1; i ++){
			Peasant p = new Peasant();
			p.setPlace(place);
			actors.add(p);
		}
		
		for(int i = 0; i < 100; i ++){
			update();
		}
	}
	
	void update(){
		for(Actor actor : actors){
			actor.update();
		}
	}
	
	public static void log(Actor actor, String message){
		String text = actor.typeName() + " \"" + actor.name + "\": " + message;
		System.out.println(text);
	}
}
