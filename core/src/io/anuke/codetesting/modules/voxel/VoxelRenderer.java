package io.anuke.codetesting.modules.voxel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

import io.anuke.codetesting.modules.Module;
import io.anuke.ucore.UCore;
import io.anuke.ucore.noise.Noise;
import io.anuke.ucore.noise.RidgedPerlin;

@SuppressWarnings("deprecation")
public class VoxelRenderer extends Module{
	Environment environment;
	DirectionalShadowLight shadowLight;
	PerspectiveCamera cam;
	Controller controller;
	ModelBatch batch;
	WorldMeshes meshes;
	int[][][] voxels;
	Color color = new Color();
	Color[] elcolors = {Color.FOREST, new Color(0.3f, 0.5f, 0.3f, 1f), Color.GRAY, Color.WHITE};
	RidgedPerlin ridges = new RidgedPerlin(MathUtils.random(100000), 1, 0.4f);
	RidgedPerlin cave = new RidgedPerlin(MathUtils.random(100000), 2, 0.4f);
	RidgedPerlin cave2 = new RidgedPerlin(MathUtils.random(100000), 1, 0.4f);

	public VoxelRenderer() {
		int size = 512;
		int height = 256;
		voxels = new int[size][height][size];
		
		/*
		if (cave.getValue(wx, y, wz, 0.003f) / 1.1f + cave2.getValue(wx, y, wz, 0.006f) / 2.2f
				+ Noise.normalNoise(wx, y, wz, 60f, 0.6f) + Noise.normalNoise(wx, y, wz, 25f, 0.2f)
				+ Noise.normalNoise(wx, y, wz, 11f, 0.2f)
				 >= 1.3f)
			voxels[wx][y][wz] = 0;
			*/
		
		Noise.seed = MathUtils.random(100000);
		
		long begin = TimeUtils.millis();
		
		for(int wx = 0; wx < size; wx++){
			for(int wz = 0; wz < size; wz++){
				double theight = 150
						+ Noise.normalNoise(wx, wz, 70, 50) 
						+ Noise.normalNoise(wx, wz, 13, 7) 
						+ Noise.normalNoise(wx, wz, 5, 6) 
						+ ridges.getValue(wx, wz, 0.003f)*45;
				
				for(int y = 0; y < theight && y < height; y++){
					
					Resource res = Resource.rock;
					
					voxels[wx][y][wz] = res.color;
					
					//voxels[wx][y][wz] = theight-y < 10 ? Color.rgba8888(Hue.mix(elcolors, color, UCore.clamp((theight/256)))) : Color.rgba8888(Color.DARK_GRAY);
						
				}
			}
		}
		
		float offsetx = 0, offsetz = 0;
		float scl = 3;

		meshes = MeshManager.getModel(voxels, offsetx, offsetz, scl);

		batch = new ModelBatch();
		
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		
		shadowLight = new DirectionalShadowLight(1440 * 10, 900 * 10, 400f, 400f, 1f, 300f);
		
		environment.add(shadowLight.set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		environment.shadowMap = shadowLight;

		size = 100;

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		cam.position.set(size * 4*scl, size * 3, size * 4*scl);
		cam.lookAt(-size * 2, size * 2, -size * 2);
		//cam.
		cam.near = 1f;
		cam.far = 10000f;
		cam.update();

		controller = new Controller(cam);
		Gdx.input.setInputProcessor(controller);
		
		long end = TimeUtils.timeSinceMillis(begin);
		
		System.out.println("time taken: " + end);
	}

	@Override
	public void update(){
		UCore.clearScreen(Color.SKY);
		
		batch.begin(cam);
		batch.render(meshes, environment);
		batch.end();

		controller.update();
	}

	public void resize(int width, int height){
		cam.viewportWidth = width;
		cam.viewportHeight = height;
		cam.update(true);
	}
}
