package io.anuke.codetesting.layervoxel;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class VoxelPacker{
	private Array<Texture> textures = new Array<>();
	private ObjectMap<String, TextureRegion[]> regions = new ObjectMap<>();
	private Array<PackData> packing = new Array<>();
	
	public TextureRegion[] getRegions(String name){
		return regions.get(name);
	}
	
	public LayerObject newObject(String name){
		return new LayerObject(regions.get(name));
	}
	
	public void add(String name, int[][][] voxels){
		packing.add(new PackData(name, voxels));
	}
	
	public void pack(){
		for(PackData data : packing){
			TextureRegion[] out = VoxelConverter.convert(data.voxels);
			
			regions.put(data.name, out);
			textures.add(out[0].getTexture());
		}
		
		packing.clear();
	}
	
	private class PackData{
		String name;
		int[][][] voxels;
		
		PackData(String name, int[][][] voxels){
			this.name = name;
			this.voxels = voxels;
		}
	}
}
