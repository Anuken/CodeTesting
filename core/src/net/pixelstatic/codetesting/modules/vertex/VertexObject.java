package net.pixelstatic.codetesting.modules.vertex;

import net.pixelstatic.codetesting.modules.vertex.VertexCanvas.PolygonType;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class VertexObject{
	public int flag;
	public ObjectMap<String, VertexList> polygons = new ObjectMap<String, VertexList>();
	
	public static class VertexList{
		public int flag;
		public Array<Vector2> vertices;
		public PolygonType type;
		
		public VertexList(Array<Vector2> vertices, PolygonType type, int flag){
			this.vertices = vertices;
			this.type = type;
			this.flag = flag;
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
	
	public ObjectMap<String, Polygon> getPolygons(){
		ObjectMap<String, Polygon> polygons = new ObjectMap<String, Polygon>();
		for(String key : this.polygons.keys()){
			VertexList list = this.polygons.get(key);
			if(list.type == PolygonType.polygon){
				polygons.put(key, toPolygon(list.vertices));
			}
		}
		return polygons;
	}
	
	public Polygon toPolygon(Array<Vector2> vertices){
		float[] array = new float[vertices.size*2];
		for(int i =0; i < vertices.size; i ++){
			array[i*2] = vertices.get(i).x;
			array[i*2+1] = vertices.get(i).y;
		}
		return new Polygon(array);
		//Polygon polygon = 
	}
	
	public void alignBottom(){
		float min = 0;
		
		for(VertexList poly : polygons.values())
			for(Vector2 vertice : poly.vertices)
				if(vertice.y < min) min = vertice.y;
		for(VertexList poly : polygons.values())
			for(Vector2 vertice : poly.vertices)
				vertice.y -= min; 
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
