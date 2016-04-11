package net.pixelstatic.codetesting.modules.generator2;

import net.pixelstatic.codetesting.modules.generator2.TreeGenerator.Pixel;
import net.pixelstatic.codetesting.utils.ValueMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap.Keys;

public enum Filter{
	light{
		{
			values.add("source", new Vector2());
		}
		private Vector2 lightsource = new Vector2();

		public float applyBrightness(){
			float gscl = -0.1f;

			float selflightscale = 0.4f; //default is 0.7f
			float globallightscale = 0.8f; //default is 0.6f;

			float dist = pixel.polygon.height() / 3f + gscl + selflightscale * (((1.5f) - pixel.polygon.lightVertice.dst(project(x - width / 2), project(y))));
			float lightdist = gscl + globallightscale * ((1f - lightsource.dst(project(x - width / 2), project(y))));
			return dist + lightdist;
		}
	},
	noise{
		{
			values.add("scale", 1f);
			values.add("magnitude", 1f);
		}
		public float applyBrightness(){
			float scl = 1f, mag = 1f;
			return Patterns.noise(x, y, scl, mag);
		}
	},
	needles{
		{
			values.add("intensity", 0.1f);
		}
		public float applyBrightness(){
			return Patterns.leaves(x + (int)(pixel.polygon.center.x * 1 / 60f), y + (int)(pixel.polygon.center.y * 1 / 60f));
		}
	},

	shadows{
		{
			values.add("intensity", 0.3f);
		}
		public void apply(){
			if(y == 0) return;
			int offsetx = x - 2;
			int offsety = y + 3;

			GeneratorPolygon poly = getPixelPolygon(offsetx, offsety);
			GeneratorPolygon other = getPixelPolygon(x, y);
			if(poly == null) return;

			if( !(poly.above(other))) return;

			pixmap.setColor(new Color(0, 0, 0, 0.3f));
			pixmap.drawPixel(x, cy);
		}
	},
	outline{
		public void apply(){
			{
				values.add("intensity", 0.2f);
			}
			int y = height - 1 - cy;
			Pixel pixel = materials[x][y];
			if(pixel.material == null) return;
			int color = Color.rgba8888(new Color(0, 0, 0, 0.2f));

			if( !same(pixel, x, y - 1)){
				pixmap.drawPixel(x, cy, color);
			}else if( !same(pixel, x, y + 1)){
				pixmap.drawPixel(x, cy, color);
			}else if( !same(pixel, x + 1, y)){
				pixmap.drawPixel(x, cy, color);
			}else if( !same(pixel, x - 1, y)){
				pixmap.drawPixel(x, cy, color);
			}
		}
	};
	protected ValueMap values = new ValueMap();
	protected Pixmap pixmap;
	protected Pixel[][] materials;
	protected Pixel pixel;
	protected int x, y, cy, width, height;
	protected TreeGenerator tree;

	protected float applyBrightness(){
		return 0f;
	}

	protected void apply(){

	}

	public boolean isApplied(){
		return true;
	}

	public final float apply(TreeGenerator tree, Pixmap pixmap, Pixel[][] materials, Pixel pixel, int x, int y, int cy, int width, int height){
		this.pixel = pixel;
		this.tree = tree;
		this.pixmap = pixmap;
		this.materials = materials;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.cy = cy;
		if(isApplied()){
			apply();
			return 0f;
		}else{
			return applyBrightness();
		}
	}

	public boolean editable(){
		return true;
	}

	public String getName(){
		String name = name();
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}
	
	public Keys<String> valueNames(){
		return values.valueNames();
	}

	protected float project(int i){
		return tree.project(i);
	}

	protected boolean same(Pixel pixel, int x, int y){
		if(x < 0 || y < 0 || x >= width || y >= height) return true;
		return !pixel.polygon.above(materials[x][y].polygon);
	}

	protected GeneratorPolygon getPixelPolygon(int x, int y){
		if(x < 0 || y < 0 || x >= width || y >= height) return null;
		return materials[x][y].polygon;
	}
}
