package net.pixelstatic.codetesting.modules.generator2;



import net.pixelstatic.codetesting.modules.generator2.GeneratorRenderer.Material;
import net.pixelstatic.codetesting.modules.vertex.VertexList;
import net.pixelstatic.codetesting.modules.vertex.VertexObject;
import net.pixelstatic.codetesting.modules.vertex.VertexObject.PolygonType;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class VertexGenerator{
	
	public static void generatePineTree(VertexObject part){
				
		VertexList list = part.lists.get("leafsegment");
		
		list.alignBottom();
		float height = list.height();
		
		float scl = 1f;
		float seg = 0.1f;
		float offset = 0f;
		float compactness = 0.86f;
		for(int i = 0; i < 10; i ++){
			
			VertexList newlist = new VertexList(new Array<Vector2>(), PolygonType.polygon, Material.leaves.ordinal());
			for(Vector2 vector : list.vertices){
				newlist.vertices.add(vector.cpy().scl(scl).add(0, offset));
			}
			part.lists.put("leafsegment" + i, newlist);
			offset += scl*height*compactness;
			scl -= seg;
		}
		
		part.lists.get("trunk").translate(0, 185f);
	
	}
}
