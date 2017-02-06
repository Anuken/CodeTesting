package io.anuke.codetesting.modules.voxel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import io.anuke.ucore.UCore;
import io.anuke.ucore.graphics.Hue;

public class MeshManager{
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
		for(int i = 0;i < vectors.length;i ++)
			vectors[i] = new Vector3();
	}
	
	private static Model endBuild(){
		if(builder.getAttributes() != null)
			endMesh();
		
		modelBuilder.begin();
		for(int i = 0; i < meshes.size; i ++)
			modelBuilder.part("mesh" + i, meshes.get(i), GL20.GL_TRIANGLES, new Material());
		
		meshes.clear();
		
		System.out.println("Done.");
		
		return modelBuilder.end();
	}
	
	public static Array<Mesh> getMeshes(float[][] heights, float scl){
		
		long begin = TimeUtils.millis();
		
		builder.begin(Usage.Position | Usage.Normal | Usage.ColorPacked, GL20.GL_TRIANGLES);
		
		for(int x = 0; x < heights.length-1; x ++){
			for(int z = 0; z < heights.length-1; z ++){
				color.set(Hue.mix(Color.FOREST, Color.WHITE, heights[x][z] / 40f, color));
				
				checkMesh();
				
				builder.ensureVertices(4);
				builder.ensureRectangleIndices(1);
				
				rect(vectors[0].set(x*scl, heights[x][z]*scl, z*scl),
					 vectors[1].set(x*scl, heights[x][z+1]*scl, (z+1)*scl),
					 vectors[2].set((x+1)*scl, heights[x+1][z+1]*scl, (z+1)*scl),
					 vectors[3].set((x+1)*scl, heights[x+1][z]*scl, z*scl));
			}
		}
		
		if(builder.getAttributes() != null)
			endMesh();
		
		long end = TimeUtils.timeSinceMillis(begin);
		
		System.out.println("Model build time: " + end);
		
		return meshes;
	}
	
	public static Model getModel(int[][][] voxels, float offsetx, float offsetz, float scl){
		int size = voxels.length;
		MeshManager.voxels = voxels;
		
		builder.begin(Usage.Position | Usage.Normal | Usage.ColorPacked, GL20.GL_TRIANGLES);
		
		for(int x = 0; x < size; x ++){
			for(int y = 0; y < voxels[x].length; y ++){
				for(int z = 0; z < size; z ++){
					if(voxels[x][y][z] != 0){
						color.set(voxels[x][y][z]);
						
						//System.out.println("building " + x +" " + y + " " + z);
						
						if(voxels[x][y][z] != 0) cube(offsetx + x * scl, y * scl, offsetz + z * scl, scl,
								!exists(x, y + 1, z), //top
								!exists(x, y - 1, z), //bottom
								!exists(x - 1, y, z), //left
								!exists(x + 1, y, z), //right
								!exists(x, y, z + 1), //front
								!exists(x, y, z - 1));//back
					}

				}
			}
		}
		
		if(builder.getAttributes() != null)
			endMesh();
		
		modelBuilder.begin();
		for(int i = 0; i < meshes.size; i ++)
			modelBuilder.part("mesh" + i, meshes.get(i), GL20.GL_TRIANGLES, new Material());
		
		meshes.clear();
		
		System.out.println("Done.");
		
		return modelBuilder.end();
	}
	
	private static boolean exists(int x, int y, int z){
		return UCore.inBounds(x, y, z, voxels) && voxels[x][y][z] != 0;
	}
	
	private static void checkMesh(){
		if(builder.getAttributes() == null){

			builder.begin(Usage.Position | Usage.Normal | Usage.ColorPacked, GL20.GL_TRIANGLES);
			//System.out.println("Beginning first mesh build");

		}else if(builder.getNumIndices() >= Short.MAX_VALUE + 16000 /*if the vertices will exceed max vertices soon*/){
			endMesh();

			//System.out.println("Adding new mesh.");

			builder.begin(Usage.Position | Usage.Normal | Usage.ColorPacked, GL20.GL_TRIANGLES);
		}
	}
	
	private static void endMesh(){
		Mesh mesh = builder.end();
		meshes.add(mesh);
		System.out.println("End mesh #"+meshes.size+". Adding to array.");
	}
	
	private static void cube(float x, float y, float z, float size, boolean top, boolean bottom, boolean left, boolean right, boolean front, boolean back){
		checkMesh();
		
		builder.ensureVertices(4 * 6);
		builder.ensureRectangleIndices(6);

		vectors[0].set(x, y, z);
		vectors[1].set(x, y, z + size);
		vectors[2].set(x + size, y, z + size);
		vectors[3].set(x + size, y, z);

		vectors[4].set(x, y + size, z);
		vectors[5].set(x, y + size, z + size);
		vectors[6].set(x + size, y + size, z + size);
		vectors[7].set(x + size, y + size, z);
		
		//System.out.println(top + " " + bottom + " " + left + " " + top + " " + front + " " + back);

		if(top) rect(vectors[4], vectors[5], vectors[6], vectors[7], Normals.up); //top
		if(bottom) rect(vectors[3], vectors[2], vectors[1], vectors[0], Normals.down); //bottom

		if(left) rect(vectors[5], vectors[4], vectors[0], vectors[1], Normals.left); //left
		if(right) rect(vectors[2], vectors[3], vectors[7], vectors[6], Normals.right); //right

		if(front) rect(vectors[6], vectors[5], vectors[1], vectors[2], Normals.front); //front
		if(back) rect(vectors[3], vectors[0], vectors[4], vectors[7], Normals.back); //back
	}
	
	private static Vector3 tempa = new Vector3();
	private static Vector3 tempb = new Vector3();
	
	static private void rect(Vector3 a, Vector3 b, Vector3 c, Vector3 d){
		tempa.set(c).sub(b);
		tempb.set(a).sub(b);
		
		rect(a, b, c, d, tempa.crs(tempb));
	}
	
	static private void rect(Vector3 a, Vector3 b, Vector3 c, Vector3 d, Vector3 normal){

		builder.rect(vertTmp1.set(a, normal, color, null).setUV(0f, 1f), vertTmp2.set(b, normal, color, null).setUV(1f, 1f), vertTmp3.set(c, normal, color, null).setUV(1f, 0f), vertTmp4.set(d, normal, color, null).setUV(0f, 0f));
	}
	
	static void r(Vector3 v){
		float r = 50;
		v.add(MathUtils.random(-r, r), MathUtils.random(-r, r), MathUtils.random(-r, r));
	}
}
