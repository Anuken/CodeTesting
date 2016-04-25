package net.pixelstatic.codetesting.modules.vertex;

import net.pixelstatic.codetesting.modules.generator2.Filter;
import net.pixelstatic.codetesting.modules.generator2.Material;
import net.pixelstatic.codetesting.utils.ValueMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;

public class EditorState{
	static Json json = new Json();
	public VertexObject vertexobject;
	public ObjectMap<Filter, ObjectMap<Material, ValueMap>> filtervalues = new ObjectMap<Filter, ObjectMap<Material, ValueMap>>();
	public ObjectMap<Material, Color> colors = new ObjectMap<Material, Color>();
	public ObjectMap<String, ObjectMap<String, Boolean>> filters = new ObjectMap<String, ObjectMap<String, Boolean>>();


	static public void writeObject(VertexObject object, FileHandle file){
		String string = json.toJson(object);
		if( !file.file().getAbsolutePath().endsWith(".vto")) file = Gdx.files.absolute(file.file().getAbsolutePath() + ".vto");
		file.writeString(string, false);
	}

	static public VertexObject readObject(FileHandle file){
		VertexObject object = json.fromJson(VertexObject.class, file);
		return object;
	}

	static public void writeState(EditorState save, FileHandle file){
		String string = json.toJson(save);
		if( !file.file().getAbsolutePath().endsWith(".est")) file = Gdx.files.absolute(file.file().getAbsolutePath() + ".est");
		file.writeString(string, false);
	}

	static public EditorState readState(FileHandle file){
		EditorState object = json.fromJson(EditorState.class, file);
		return object;
	}

}
