package io.anuke.codetesting.modules.voxel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;

import io.anuke.codetesting.modules.Module;
import io.anuke.ucore.UCore;
import io.anuke.ucore.graphics.Hue;
import io.anuke.ucore.noise.Noise;
import io.anuke.ucore.noise.RidgedPerlin;

public class VoxelRenderer extends Module{
	Environment environment;
	PerspectiveCamera cam;
	Controller controller;
	ModelBatch batch;
	Model model;
	ModelInstance instance;
	int[][][] voxels;
	Color color = new Color();
	Color[] elcolors = {Color.FOREST, new Color(0.3f, 0.5f, 0.3f, 1f), Color.GRAY, Color.WHITE};
	

	public VoxelRenderer() {
		int size = 600;
		int height = 100;

		voxels = new int[size][height][size];
		
		RidgedPerlin rid = new RidgedPerlin(1, 1, 0.005f);

		for(int x = 0; x < size; x++){
			for(int z = 0; z < size; z++){
				
				
				float theight = 70 
						+ Noise.normalNoise(x, z, 60, 40) 
						+ Noise.normalNoise(x, z, 10, 5) 
						+ Noise.normalNoise(x, z, 5, 4) 
						+ rid.getValue(x, z, 0.005f)*45;
				
				for(int y = 0; y < theight && y < height; y++){
					voxels[x][y][z] = Color.rgba8888(Hue.blend(elcolors, color, UCore.clamp((theight-10)/90f)));
				}
			}
		}
		
		Hue.blend(elcolors, color, 0.67f);
		System.out.println(color);
		float offsetx = 0, offsetz = 0;
		float scl = 5;

		model = MeshManager.getModel(voxels, offsetx, offsetz, scl);
		instance = new ModelInstance(model);

		batch = new ModelBatch();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

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
