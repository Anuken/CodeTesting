package io.anuke.codetesting.storysim;

public class Actor<S extends State>{
	public String name = "no name";
	protected S state;
	protected S globalState;
	protected Place place;
	
	public void update(){
		state.update(this);
		if(globalState != null)
			globalState.update(this);
	}
	
	public S getState(){
		return state;
	}
	
	public void setPlace(Place place){
		state.exitedPlace(this, this.place);
		globalState.exitedPlace(this, this.place);
		this.place = place;
		state.enteredPlace(this, place);
		globalState.enteredPlace(this, place);
	}
	
	public void setState(S newstate){
		state.exited(this);
		state = newstate;
		newstate.entered(this);
	}
	
	public void log(String message){
		Story.log(this, message);
	}
	
	public String typeName(){
		return getClass().getSimpleName();
	}
}
