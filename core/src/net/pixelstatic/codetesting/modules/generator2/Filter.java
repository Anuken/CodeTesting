package net.pixelstatic.codetesting.modules.generator2;

import net.pixelstatic.codetesting.modules.generator2.TreeGenerator.Pixel;
import net.pixelstatic.codetesting.utils.ValueMap;
import net.pixelstatic.codetesting.utils.values.Value;
import net.pixelstatic.codetesting.utils.values.Value.FloatValue;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.ObjectMap.Keys;

public enum Filter{
	light(false){
		{
			//values.add("sourcex", new FloatValue(0, 99, 99));
			//values.add("sourcey", new FloatValue(0, 99, 99));
			values.add("polylightscale", new FloatValue(-1, 1, 0.14f));
			values.add("lightscale", new FloatValue(-1, 1, 0.5f));
	
		}
	//	private Vector2 lightsource = new Vector2();

		public float applyBrightness(){
			float gscl = -0.1f;

			float selflightscale = values.get("polylightscale").getValue(Float.class);//0.4f; //default is 0.7f
			float globallightscale = values.get("lightscale").getValue(Float.class); //default is 0.6f;

			float dist = pixel.polygon.height() / 3f + gscl + selflightscale * (((1.5f) - pixel.polygon.lightVertice.dst(project(x - width / 2), project(y))));
			float lightdist = gscl + globallightscale * ((1f - tree.lightSource().dst(project(x - width / 2), project(y))));
			return dist + lightdist;
		}
	},
	distlight(false){
		{
			values.add("intensity", new FloatValue(0, 20, 1f));
		}
		public float applyBrightness(){
			float dist = pixel.polygon.distance(project(x - width / 2), project(y));
			return dist * values.get("intensity").getValue(Float.class) / (pixel.polygon.dimensions() * 3.4f);
		}
	},
	noise(false){
		{
			values.add("scale", new FloatValue(0, 15, 4f));
			values.add("magnitude", new FloatValue(-2, 2, 0.2f));
		}
		public float applyBrightness(){
			float scl = values.get("scale").getValue(Float.class), mag = values.get("magnitude").getValue(Float.class);
			return Patterns.noise(x, y, scl, mag);
		}
	},
	needles(false){
		{
			values.add("intensity", new FloatValue(-1f, 1f, 0.3f));
		}
		public float applyBrightness(){
			float intensity = values.get("intensity").getValue(Float.class);
			return intensity*Patterns.leaves(x + (int)(pixel.polygon.center.x * 1 / 60f), y + (int)(pixel.polygon.center.y * 1 / 60f));
		}
	},
	bark{
		public void apply(){
			Color color = new Color(pixmap.getPixel(x, cy));
			float m = 0f;
			m = (Patterns.nMod(x, y, 0.2f));
			float dist = pixel.polygon.distance(project(x - width / 2), project(y));
			m += dist * 10f / (pixel.polygon.dimensions() * 3.4f);
			if(m > 1.4f) m /= 2f;
			m = round(m, 0.4f);
			m += 1f;
			color.mul(m, m, m, 1f);
			pixmap.drawPixel(x, cy, Color.rgba8888(color));
		}
	},
	lines(false){
		{
			values.add("intensity", new FloatValue(-1f, 1f, 0.03f));
		}
		public float applyBrightness(){
			float intensity = values.get("intensity").getValue(Float.class);
			return Patterns.mod(x, y, intensity);
		}
	},
	barklines(false){
		{
			values.add("intensity", new FloatValue(-5f, 5f, 0.05f));
		}
		public float applyBrightness(){
			float intensity = values.get("intensity").getValue(Float.class);
			return Patterns.nMod(x, y, intensity);
		}
	},
	shadows(true){
		{
			values.add("intensity", new FloatValue(-1f, 1f, 0.33f));
		}
	
		public void apply(){
			if(y == 0) return;
			int offsetx = x - 2;
			int offsety = y + 3;

			GeneratorPolygon poly = getPixelPolygon(offsetx, offsety);
			GeneratorPolygon other = getPixelPolygon(x, y);
			if(poly == null) return;

			if( !(poly.above(other))) return;
			
			float i =  values.getFloat("intensity");
			pixmap.drawPixel(x, cy, Color.rgba8888(new Color(0,0,0,i)));
		}
	},
	outline{
		{
			values.add("intensity", new FloatValue(0f, 1f, 0.2f));
		}
		
		public void apply(){
			int y = height - 1 - cy;
			Pixel pixel = materials[x][y];
			if(pixel.material == null) return;
			int color = Color.rgba8888(new Color(0, 0, 0, values.getFloat("intensity")));

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
	},
	round(false){
		{
			values.add("amount", new FloatValue(0.01f, 1f, 0.1f));
		}
		public float change(float input){
			return round(input, values.getFloat("amount"));
		}
	},
	shading(){
		{
			alwaysEnabled = true;
			values.add("rscale", new FloatValue(-3f, 3f, 1f));
			values.add("gscale", new FloatValue(-3f, 3f, 1f));
			values.add("bscale", new FloatValue(-3f, 3f, -2f));
		}
		
		public void change(Color color, float amount){
			color.add(amount * values.getFloat("rscale"),amount * values.getFloat("gscale"), amount * values.getFloat("bscale"), 0f);
		}
	};
	protected ValueMap values = new ValueMap();
	protected Pixmap pixmap;
	protected Pixel[][] materials;
	protected Pixel pixel;
	protected int x, y, cy, width, height;
	protected TreeGenerator tree;
	protected boolean isApplied = true;
	protected boolean alwaysEnabled = false;
	
	private Filter(){
		
	}
	
	private Filter(boolean applied){
		this.isApplied = applied;
	}

	protected float applyBrightness(){
		return 0f;
	}

	protected void apply(){

	}
	
	public boolean alwaysEnabled(){
		return alwaysEnabled;
	}
	
	public void change(Color color, float amount){
		
	}

	public boolean isApplied(){
		return isApplied;
	}
	
	public float change(float input){
		return input;
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
	
	public ValueMap valueMap(){
		return values;
	}
	
	
	protected void value(String name, Value<?> value){
		values.add(name, value);
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
	
	float round(float a, float b){
		return b * (int)(a / b);
	}
}
