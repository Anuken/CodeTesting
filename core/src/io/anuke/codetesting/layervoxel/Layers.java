package io.anuke.codetesting.layervoxel;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import io.anuke.ucore.core.Core;

public class Layers{
	private static final int MAX_LAYERS = 100;
	private static final float e = 0.001f;

	public static float spacing = 1f;
	public static int steps = 1;
	public static float camrotation = 0f;
	
	private static int toplayer = 0;
	private static Array<TextureLayer>[] layers = new Array[MAX_LAYERS];
	
	static{
		for(int i = 0; i < layers.length; i++){
			layers[i] = new Array<TextureLayer>();
		}
	}

	public static void render(){
		OrthographicCamera camera = Core.camera;
		Batch batch = Core.batch;
		
		for(int l = 0; l < toplayer; l++){
			for(TextureLayer layer : layers[l]){
				float x = 0, y = layer.object.z + l * spacing;
				float rotation = layer.object.rotation + camrotation;
				TextureRegion region = layer.region;

				float oy = layer.object.y;
				float ox = layer.object.x;
				ox -= camera.position.x;
				oy -= camera.position.y;

				float cos = (float) Math.cos(camrotation * MathUtils.degRad);
				float sin = (float) Math.sin(camrotation * MathUtils.degRad);

				float newX = ox * cos - oy * sin;
				float newY = ox * sin + oy * cos;

				ox = newX;
				oy = newY;

				x += ox + camera.position.x;
				y += oy + camera.position.y;

				for(int i = 0; i <= steps; i++){
					batch.setColor(layer.object.color);
					batch.draw(region, x - region.getRegionWidth() / 2 - e, y - region.getRegionHeight() / 2 - e, region.getRegionWidth() / 2, region.getRegionHeight() / 2, region.getRegionWidth() + e * 2, region.getRegionHeight() + e * 2, 1, 1, rotation);
					y += spacing / steps;
				}
			}
		}
	}
	
	public static void add(LayerObject object){
		for(int i = object.offset; i < object.regions.length; i++){
			layers[i].add(new TextureLayer(object, object.regions[i]));
		}

		if(object.regions.length > toplayer){
			toplayer = object.regions.length;
		}
	}
	
	public static void remove(LayerObject object){
		for(int i = toplayer; i >= 0; i--){
			Array<TextureLayer> array = layers[i];

			for(int j = object.offset; j < layers[i].size; j++){

				if(array.get(j).object == object){

					array.removeIndex(j);

					if(array.size == 0 && i >= toplayer){
						toplayer--;
					}

					break;
				}
			}
		}
	}

	static class TextureLayer{
		float x, y;
		LayerObject object;
		TextureRegion region;

		public TextureLayer(LayerObject object, TextureRegion region) {
			this.object = object;
			this.x = object.x;
			this.y = object.y;
			this.region = region;
		}
	}

}
