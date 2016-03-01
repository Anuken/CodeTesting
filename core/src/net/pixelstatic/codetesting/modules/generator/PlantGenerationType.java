package net.pixelstatic.codetesting.modules.generator;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Pixmap.Format;

public enum PlantGenerationType{
	grass, tree{
		public Pixmap generate(){
			int size = 100;
			Pixmap pixmap = new Pixmap(size, size, Format.RGBA8888);
			Branch trunk = new Branch(null);
			return pixmap;
		}
	};
	public Pixmap generate(){
		return null;
	}
}
