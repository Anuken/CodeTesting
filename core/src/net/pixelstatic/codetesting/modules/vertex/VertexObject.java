package net.pixelstatic.codetesting.modules.vertex;

import net.pixelstatic.codetesting.modules.vertex.VertexCanvas.PolygonType;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class VertexObject{
	public int flag;
	public ObjectMap<String, VertexList> polygons = new ObjectMap<String, VertexList>();
	
	class VertexList{
		int flag;
		Array<Vector2> vertices;
		PolygonType type;
		
		public VertexList(Array<Vector2> vertices, PolygonType type, int flag){
			this.vertices = vertices;
		}
		
		public VertexList(){
			
		}
	}

	public VertexObject(){

	}

	public VertexObject(Array<VertexCanvas> canvases){
		for(VertexCanvas canvas : canvases){
			polygons.put(canvas.name, new VertexList(canvas.vertices, canvas.type, Color.rgba8888(canvas.color)));
		}
		//normalize();
	}

	public void normalize(){
		float max = 1;
		for(VertexList poly : polygons.values())
			for(Vector2 vertice : poly.vertices){
				if(Math.abs(vertice.x) > max) max = Math.abs(vertice.x);
				if(Math.abs(vertice.y) > max) max = Math.abs(vertice.y);
			}

		for(VertexList poly : polygons.values())
			for(Vector2 vertice : poly.vertices){
				
				vertice.scl(1f / max);
			}
	}

}
