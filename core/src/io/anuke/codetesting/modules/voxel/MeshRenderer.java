package io.anuke.codetesting.modules.voxel;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;

public class MeshRenderer{
	public static ImmediateModeRenderer20 i = new ImmediateModeRenderer20(true, true, 0);
	
	public static void render(int[][][] voxels, Camera cam){
		i.begin(cam.combined, GL20.GL_TRIANGLES);
		
		i.end();
	}
}
