package io.anuke.codetesting.modules.voxel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

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
		
		long begin = TimeUtils.millis();
		
		int size = 80;
		
		float[][] heights = new float[size][size];
		
		for(int x = 0; x < size; x ++){
			for(int z = 0; z < size; z ++){
				heights[x][z] = (float)Noise.nnoise(x, z, 30f, 70f) + Noise.nnoise(x, z, 5f, 8f);
			}
		}
		
		
		//float offsetx = 0, offsetz = 0;
		float scl = 10;

		meshes = new WorldMeshes(MeshManager.getMeshes(heights, scl));
		
		long end = TimeUtils.timeSinceMillis(begin);
		
		System.out.println("time taken: " + end);
		

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
