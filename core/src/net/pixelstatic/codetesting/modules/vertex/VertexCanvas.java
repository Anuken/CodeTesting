package net.pixelstatic.codetesting.modules.vertex;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class VertexCanvas{
	Array<Vector2> vertices = new Array<Vector2>();
	
	public VertexObject toObject(){
		return new VertexObject(vertices);
	}
	
	public void scale(float amount){
		for(Vector2 vertice : vertices)
			vertice.scl(amount);
	}
	
	public void clear(){
		vertices.clear();
	}
}
