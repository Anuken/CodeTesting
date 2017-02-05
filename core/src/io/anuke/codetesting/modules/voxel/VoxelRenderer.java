package io.anuke.codetesting.modules.voxel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;

import io.anuke.codetesting.modules.Module;
import io.anuke.ucore.UCore;
import io.anuke.ucore.graphics.Hue;
import io.anuke.ucore.noise.Noise;
import io.anuke.ucore.noise.RidgedPerlin;

@SuppressWarnings("deprecation")
public class VoxelRenderer extends Module{
	Environment environment;
	DirectionalShadowLight shadowLight;
	PerspectiveCamera cam;
	Controller controller;
	ModelBatch batch;
	Model model;
	ModelInstance instance;
	int[][][] voxels;
	Color color = new Color();
	Color[] elcolors = {Color.FOREST, new Color(0.3f, 0.5f, 0.3f, 1f), Color.GRAY, Color.WHITE};
	RidgedPerlin ridges = new RidgedPerlin(2, 1, 0.4f);
	RidgedPerlin cave = new RidgedPerlin(3, 2, 0.4f);
	RidgedPerlin cave2 = new RidgedPerlin(4, 1, 0.4f);

	public VoxelRenderer() {
		int size = 400;
		int height = 256;
		voxels = new int[size][height][size];
		
		RidgedPerlin rid = new RidgedPerlin(1, 1, 0.005f);
		
		float iterations = size*size*height;
		float to = 0;
		
		for(int x = 0; x < size; x++){
			for(int z = 0; z < size; z++){
				double theight = 150
						+ Noise.normalNoise(x, z, 60, 40) 
						+ Noise.normalNoise(x, z, 10, 5) 
						+ Noise.normalNoise(x, z, 5, 4) 
						+ rid.getValue(x, z, 0.005f)*45;;
				
				for(int y = 0; y < theight && y < height; y++){
					
					voxels[x][y][z] = theight-y < 10 ? Color.rgba8888(Hue.mix(elcolors, color, UCore.clamp((theight-220)/150f))) : Color.rgba8888(Color.DARK_GRAY);
					
					
					if (cave.getValue(x, y, z, 0.009f) / 1.3f + cave2.getValue(x, y, z, 0.01f) / 2.3f
							+ Noise.normalNoise(x, y, z, 60f, 0.7f) + Noise.normalNoise(x, y, z, 25f, 0.25f)
							+ Noise.normalNoise(x, y, z, 11f, 0.25f)
							 >= 0.78f)
						voxels[x][y][z] = 0;
						
					
					to ++;
				}
				to += (height-theight);
				if((int)to % 3000 == 0){
					System.out.println("Generating: " + (int)(200*(float)to/iterations) + "%");
				}
			}
		}
		
		Hue.mix(elcolors, color, 0.67f);
		System.out.println(color);
		float offsetx = 0, offsetz = 0;
		float scl = 3;

		model = MeshManager.getModel(voxels, offsetx, offsetz, scl);
		instance = new ModelInstance(model);

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
	}

	@Override
	public void update(){
		UCore.clearScreen(Color.BLACK);
		
		/*
		shadowLight.begin(Vector3.Zero, cam.direction);
		batch.begin(shadowLight.getCamera());
		Gdx.gl.glClearColor(0, 0, 0, 0);
		batch.render(instance, environment);
		batch.end();
		shadowLight.end();
		*/

		batch.begin(cam);
		batch.render(instance, environment);
		batch.end();

		controller.update();
	}

	public void resize(int width, int height){
		cam.viewportWidth = width;
		cam.viewportHeight = height;
		cam.update(true);
	}
}
