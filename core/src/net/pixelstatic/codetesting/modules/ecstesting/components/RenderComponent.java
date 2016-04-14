package net.pixelstatic.codetesting.modules.ecstesting.components;

import net.pixelstatic.codetesting.entities.Entity;
import net.pixelstatic.codetesting.modules.ecstesting.EntityTester;

public interface RenderComponent extends Component{

	public default void update(EntityTester tester){
		//tester.ba
	}
	
	public default void init(Entity entity){
		
	}
}
