package net.pixelstatic.codetesting.modules.vertex;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

public class VertexLoader{
	static Json json = new Json();
	
	static public void write(VertexObject object, FileHandle file){
		String string = json.toJson(object);
		file.writeString(string, false);
	}
	
	static public VertexObject read(FileHandle file){
		VertexObject object = json.fromJson(VertexObject.class, file);
		return object;
	}
}