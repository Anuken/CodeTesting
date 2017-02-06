package io.anuke.codetesting.modules.voxel;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import io.anuke.ucore.UCore;

public class ImmediateRenderer{
	private final static ImmediateModeRenderer20 i = new ImmediateModeRenderer20(true, true, 0);
	private final static Color color = new Color(1, 0, 0, 1);
	private final static Vector3[] vectors = new Vector3[8];
	private final static MeshBuilder builder = new MeshBuilder();
	private final static ModelBuilder modelBuilder = new ModelBuilder();
	private final static VertexInfo vertTmp1 = new VertexInfo();
	private final static VertexInfo vertTmp2 = new VertexInfo();
	private final static VertexInfo vertTmp3 = new VertexInfo();
	private final static VertexInfo vertTmp4 = new VertexInfo();
	private static int[][][] voxels;
	private static Array<Mesh> meshes = new Array<Mesh>();

	static{
		for(int i = 0; i < vectors.length; i++)
			vectors[i] = new Vector3();
	}

	public static void getModel(int[][][] voxels, Camera cam, float offsetx, float offsetz, float scl){
		int size = voxels.length;
		ImmediateRenderer.voxels = voxels;

		i.begin(cam.combined, GL20.GL_TRIANGLES);
		
		for(int x = 0; x < size; x++){
			for(int y = 0; y < voxels[x].length; y++){
				for(int z = 0; z < size; z++){
					if(voxels[x][y][z] != 0){
						color.set(voxels[x][y][z]);
						i.color(color);

						//System.out.println("building " + x +" " + y + " " + z);

						if(voxels[x][y][z] != 0)
							cube(offsetx + x * scl, y * scl, offsetz + z * scl, scl, !exists(x, y + 1, z), //top
									!exists(x, y - 1, z), //bottom
									!exists(x - 1, y, z), //left
									!exists(x + 1, y, z), //right
									!exists(x, y, z + 1), //front
									!exists(x, y, z - 1));//back
					}

				}
			}
		}

		i.end();
	}

	private static boolean exists(int x, int y, int z){
		return UCore.inBounds(x, y, z, voxels) && voxels[x][y][z] != 0;
	}

	private static void checkMesh(){
		if(builder.getAttributes() == null){

			builder.begin(Usage.Position | Usage.Normal | Usage.ColorPacked, GL20.GL_TRIANGLES);
			//System.out.println("Beginning first mesh build");

		}else if(builder.getNumIndices() >= Short.MAX_VALUE
				+ 16000 /* if the vertices will exceed max vertices soon */){
			endMesh();

			//System.out.println("Adding new mesh.");

			builder.begin(Usage.Position | Usage.Normal | Usage.ColorPacked, GL20.GL_TRIANGLES);
		}
	}

	private static void endMesh(){
		Mesh mesh = builder.end();
		meshes.add(mesh);
		//System.out.println("End mesh #"+meshes.size+". Adding to array.");
	}

	private static void cube(float x, float y, float z, float size, boolean top, boolean bottom, boolean left, boolean right, boolean front, boolean back){
		checkMesh();

		vectors[0].set(x, y, z);
		vectors[1].set(x, y, z + size);
		vectors[2].set(x + size, y, z + size);
		vectors[3].set(x + size, y, z);

		vectors[4].set(x, y + size, z);
		vectors[5].set(x, y + size, z + size);
		vectors[6].set(x + size, y + size, z + size);
		vectors[7].set(x + size, y + size, z);

		//System.out.println(top + " " + bottom + " " + left + " " + top + " " + front + " " + back);

		if(top)
			rect(vectors[4], vectors[5], vectors[6], vectors[7], Normals.up); //top
		if(bottom)
			rect(vectors[3], vectors[2], vectors[1], vectors[0], Normals.down); //bottom

		if(left)
			rect(vectors[5], vectors[4], vectors[0], vectors[1], Normals.left); //left
		if(right)
			rect(vectors[2], vectors[3], vectors[7], vectors[6], Normals.right); //right

		if(front)
			rect(vectors[6], vectors[5], vectors[1], vectors[2], Normals.front); //front
		if(back)
			rect(vectors[3], vectors[0], vectors[4], vectors[7], Normals.back); //back
	}

	static private void rect(Vector3 a, Vector3 b, Vector3 c, Vector3 d, Vector3 normal){
		//if(Math.random() < 0.01){
		//	r(a);
		//	r(b);
		//	r(c);
		//	r(d);
		//}
		
		i.vertex(a.x, a.y, a.z);
		i.color(color);
		i.normal(normal.x, normal.y, normal.z);
		i.vertex(b.x, b.y, b.z);
		i.color(color);
		i.normal(normal.x, normal.y, normal.z);
		i.vertex(c.x, c.y, c.z);
		i.color(color);
		i.normal(normal.x, normal.y, normal.z);

		i.vertex(c.x, c.y, c.z);
		i.color(color);
		i.normal(normal.x, normal.y, normal.z);
		i.vertex(d.x, d.y, d.z);
		i.color(color);
		i.normal(normal.x, normal.y, normal.z);
		i.vertex(a.x, a.y, a.z);
		i.color(color);
		i.normal(normal.x, normal.y, normal.z);
		
		//builder.rect(vertTmp1.set(a, normal, color, null).setUV(0f, 1f), vertTmp2.set(b, normal, color, null).setUV(1f, 1f), vertTmp3.set(c, normal, color, null).setUV(1f, 0f), vertTmp4.set(d, normal, color, null).setUV(0f, 0f));
	}

	static void r(Vector3 v){
		float r = 50;
		v.add(MathUtils.random(-r, r), MathUtils.random(-r, r), MathUtils.random(-r, r));
	}

}
