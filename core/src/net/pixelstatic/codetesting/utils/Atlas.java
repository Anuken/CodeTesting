package net.pixelstatic.codetesting.utils;

import java.util.HashMap;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

//utility class that improves on TextureAtlas - faster texture lookups, and automatic error textures.
public class Atlas extends TextureAtlas{
	HashMap<String, AtlasRegion> regionmap = new HashMap<String, AtlasRegion>();
	AtlasRegion error;
	
	public Atlas(FileHandle file){
		super(file);
		for(AtlasRegion r : super.getRegions()){
			String[] split = r.name.split("/");
			if(split.length > 1){
				if(regionmap.containsKey(split[1])) System.out.println("--- WARNING: TEXTURE CONFLICT! --- " + "(" + split[1] + ")");
				regionmap.put(split[1], r);
			}else{
				if(regionmap.containsKey(split[0])) System.out.println("--- WARNING: TEXTURE CONFLICT! --- " + "(" + split[0] + ")");
				regionmap.put(split[0], r);
			}
			r.name = new String(r.name);
		}
		error = findRegion("error");
	}
	//returns error texture if region not found
	@Override
	public AtlasRegion findRegion(String name){
		AtlasRegion r = regionmap.get(name);
		if(r == null) return error;
		return r;
	}

	public float RegionHeight(String name){
		return findRegion(name).getRegionHeight();
	}

	public float RegionWidth(String name){
		return findRegion(name).getRegionWidth();
	}
	//if a texture is in the atlas
	public boolean IsBlank(String s){
		return findRegion(s).equals(error);
	}
}
