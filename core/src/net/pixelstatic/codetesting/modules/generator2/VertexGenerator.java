package net.pixelstatic.codetesting.modules.generator2;



import net.pixelstatic.codetesting.modules.generator2.GeneratorRenderer.Material;
import net.pixelstatic.codetesting.modules.vertex.VertexList;
import net.pixelstatic.codetesting.modules.vertex.VertexObject;
import net.pixelstatic.codetesting.modules.vertex.VertexObject.PolygonType;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class VertexGenerator{
	static float rot = 0;
	

	/** Creates a pine tree by stacking segment polygons on top of each other.*
	 * @param part the polygon segment to be stacked.*/
	public static void generatePineTree(VertexObject part){
				
		VertexList list = part.lists.get("leafsegment");
		//list.scale(0.01f);
		part.lists.remove("leafsegment");
		//part.lists.remove("trunk");
		
		list.alignBottom();
		float height = list.height();
		
		float scl = 1f;
		float seg = 0.1f;
		float offsety = 0, lastx = 0f, lastrotation = 0;
		float compactness = 0.86f;
		float rotatecompactness = 0.7f;
		for(int i = 0; i < 10; i ++){
			VertexList newlist = new VertexList(new Array<Vector2>(), PolygonType.polygon, Material.leaves.ordinal());
			float rotation = lastrotation*0.9f + rot;
			Vector2 rotatevector = new Vector2(0,scl*height*compactness*rotatecompactness).rotate(rotation);
			for(Vector2 vector : list.vertices)
				newlist.vertices.add(vector.cpy().scl(scl));
			
			lastx += rotatevector.x;
			newlist.rotate(rotation);
			newlist.translate(lastx, offsety);
			part.lists.put("leafsegment" + i, newlist);
			lastrotation = rotation;
			offsety += rotatevector.y*1f/rotatecompactness;
			scl -= seg;
		}
		
		part.lists.get("trunk").translate(0, 185f);
	
	}
}
