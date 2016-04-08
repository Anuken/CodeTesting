package net.pixelstatic.codetesting.modules.vertex;

import net.pixelstatic.codetesting.modules.generator2.GeneratorRenderer.Material;
import net.pixelstatic.codetesting.modules.vertex.VertexObject.PolygonType;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class VertexList{
	private int flag; //used for storing material type; a bit redundant
	public Material material;
	public Array<Vector2> vertices;
	public PolygonType type;

	public VertexList(Array<Vector2> vertices, PolygonType type, int flag){
		this.vertices = vertices;
		this.type = type;
		this.flag = flag;
		this.material = flagMaterial();
	}

	public float height(){
		float max = 0;
		for(Vector2 vector : vertices){
			max = Math.max(max, vector.y);
		}
		return max;
	}
	
	public void tscl(float scl){
		//float min = alignBottom();
	//	for(Vector2 vertice : vertices)
			//vertice.scl(0, scl);
			
		//translate(0, min);
	}
	
	public void scale(float x, float y){
		for(Vector2 vertice : vertices)
			vertice.scl(x, y);
	}

	public void translate(float x, float y){
		for(Vector2 vertice : vertices)
			vertice.add(x, y);
	}
	
	public void rotate(float rotation){
		float min = alignBottom();
		for(Vector2 vector : vertices){
			vector.rotate(rotation);
		}
		translate(0, min);
	}
	
	public float min(){
		float min = 0;
		for(Vector2 vector : vertices){
			if(Math.abs(vector.y) > min){
				min = vector.y;
			}
		}
		return min;
	}

	public float alignBottom(){
		float min = 0;
		for(Vector2 vector : vertices){
			if(Math.abs(vector.y) > min){
				min = vector.y;
			}
		}

		for(Vector2 vector : vertices)
			vector.y -= min;
		
		return min;
	}

	public void mirrorVertices(){
		Array<Vector2> copy = new Array<Vector2>();
		for(Vector2 vector : vertices){
			copy.add(vector.cpy().scl( -1, 1));
		}
		for(int i = copy.size - 1;i >= 0;i --){
			vertices.add(copy.get(i));
		}
	}

	public boolean smooth(){
		if(vertices.size > 100) return false;

		Vector2[] array = new Vector2[vertices.size * 2 - (type == PolygonType.line ? 1 : 0)];
		for(int i = 0;i < vertices.size;i ++){
			array[i * 2] = vertices.get(i);
			Vector2 next = (i == vertices.size - 1 ? vertices.first() : vertices.get(i + 1));
			if( !(type == PolygonType.line && i == vertices.size - 1)) array[i * 2 + 1] = next.cpy().add(vertices.get(i)).scl(0.5f);
		}

		for(int i = 0;i < array.length;i += 2){
			if(type == PolygonType.line && i == array.length - 1) break;

			Vector2 next = (i == array.length - 1 ? array[0] : array[i + 1]);

			Vector2 last = (i == 0 ? array[array.length - 1] : array[i - 1]);

			if( !((i == 0 || i == array.length - 1) && type == PolygonType.line)) array[i] = next.cpy().add(last).scl(0.5f);
		}

		vertices = new Array<Vector2>(array);

		return true;
	}

	public void scale(float amount){
		Vector2 avg = Vector2.Zero.cpy();
		for(Vector2 vertice : vertices){
			if(avg.isZero()){
				avg = vertice.cpy();
			}else{
				avg.add(vertice);
			}
		}

		avg.scl(1f / vertices.size);

		for(Vector2 vertice : vertices)
			vertice.sub(avg);

		for(Vector2 vertice : vertices){
			vertice.scl(amount);
			vertice.add(avg);
		}

	}

	public Material flagMaterial(){
		return Material.values()[flag];
	}

	public Material material(){
		return Material.values()[flag];
	}

	@SuppressWarnings("unused")
	private VertexList(){

	}
}
