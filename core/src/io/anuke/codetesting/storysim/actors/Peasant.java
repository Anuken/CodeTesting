package io.anuke.codetesting.storysim.actors;

import io.anuke.codetesting.storysim.Actor;
import io.anuke.codetesting.storysim.Place;
import io.anuke.codetesting.storysim.State;
import io.anuke.ucore.util.Mathf;

public class Peasant extends Actor{
	static String[] peasantNames = {"Joe", "Bob", "John", "Johnibob"};
	
	public int wheat;
	
	public Peasant(){
		name = Mathf.select(peasantNames);
		state = PeasantState.farming;
		globalState = PeasantState.global;
	}
	
	enum PeasantState implements State<Peasant>{
		farming{
			public void update(Peasant p){
				p.log("Farming some wheat.");
				p.wheat ++;
				
				if(p.wheat > 0){
					p.setState(sleeping);
				}
			}
			
			public void exited(Peasant p){
				p.log("Done with farming.");
			}
		},
		sleeping{
			public void entered(Peasant p){
				p.log("Now sleeping.");
			}
			
			public void update(Peasant p){
				
			}
		},
		global{
			
			public void enteredPlace(Peasant p, Place l){
				p.log("entered " + l.name);
			}
		}
	}
}
