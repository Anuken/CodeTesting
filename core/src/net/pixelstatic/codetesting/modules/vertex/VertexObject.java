package net.pixelstatic.codetesting.modules.vertex;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class VertexObject{
	public int flag;
	public Array<Vector2> vertices = new Array<Vector2>();
	
	public VertexObject(){
		
	}
	
	public VertexObject(Array<Vector2> vertices){
		this.vertices = vertices;
	}

}
