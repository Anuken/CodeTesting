package io.anuke.codetesting.modules.voxel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.math.MathUtils;

import io.anuke.codetesting.modules.Module;
import io.anuke.ucore.UCore;
import io.anuke.ucore.noise.Noise;

@SuppressWarnings("deprecation")
public class PlaneRenderer extends Module{
	Environment environment;
	DirectionalShadowLight shadowLight;
	PerspectiveCamera cam;
	Controller controller;
	WorldMeshes meshes;
	ModelBatch batch;
	Model model;
	ModelInstance instance;
	Color color = new Color();

	public PlaneRenderer() {
		Noise.seed = MathUtils.random(100000);
		
		int size = 500;
		
		float[][] heights = new float[size][size];
		
		for(int x = 0; x < size; x ++){
			for(int z = 0; z < size; z ++){
				heights[x][z] = (float)Noise.nnoise(x, z, 80f, 100f) + 
						Noise.nnoise(x, z, 7f, 8f) + 
						Noise.nnoise(x, z, 4f, 3f) + 
						Noise.nnoise(x, z, 100f, 200f) +
						Noise.nnoise(x, z, 40, 40f);
			}
		}
		
		
		//float offsetx = 0, offsetz = 0;
		float scl = 3;

		meshes = MeshManager.getMeshes(heights, scl);
		
		batch = new ModelBatch();
		
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		
		shadowLight = new DirectionalShadowLight(1440 * 10, 900 * 10, 400f, 400f, 1f, 300f);
		
		environment.add(shadowLight.set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		environment.shadowMap = shadowLight;

		size = 100;

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		cam.position.set(size *scl, size * 3, size *scl);
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
