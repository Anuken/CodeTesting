package io.anuke.codetesting.io;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.ObjectMap;

import io.anuke.ucore.graphics.Pixmaps;

public class SimpleAtlasUnpacker{
	
	public static void unpack(TextureAtlas atlas, FileHandle directory){
		ObjectMap<Texture, Pixmap> pixmaps = new ObjectMap<Texture, Pixmap>();
		
		for(Texture texture : atlas.getTextures()){
			texture.getTextureData().prepare();
			Pixmap pixmap = texture.getTextureData().consumePixmap();
			
			pixmaps.put(texture, pixmap);
		}
		
		for(AtlasRegion region : atlas.getRegions()){
			Pixmap pixmap = pixmaps.get(region.getTexture());
			Pixmap output = Pixmaps.crop(pixmap, region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight());
			PixmapIO.writePNG(directory.child(region.name), output);
			output.dispose();
		}
		
		for(Pixmap pixmap : pixmaps.values()){
			pixmap.dispose();
		}
	}
}
