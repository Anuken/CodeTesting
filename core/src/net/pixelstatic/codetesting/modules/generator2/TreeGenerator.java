package net.pixelstatic.codetesting.modules.generator2;

import net.pixelstatic.codetesting.modules.Module;
import net.pixelstatic.codetesting.modules.vertex.VertexCanvas.PolygonType;
import net.pixelstatic.codetesting.modules.vertex.VertexLoader;
import net.pixelstatic.codetesting.modules.vertex.VertexObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.jhlabs.image.CellularFilter;

public class TreeGenerator extends Module{
	final int width = 100, height = 100, scl = 5;
	SpriteBatch batch;
	Pixel[][] materials;
	Pixmap materialPixmap;
	Texture materialTexture;

	class Pixel{
		Material material;
		GeneratorPolygon polygon;
	}

	public enum Material{
		bone(Color.MAGENTA), wood(Color.BROWN.cpy().sub(0.1f, 0.1f, 0.1f, 0f), CellularFilter.HEXAGONAL), 
		leaves(new Color(0.0f, 0.4f, 0.3f, 1f),-1);

		public Color color;
		public int type = CellularFilter.TRIANGULAR;

		private Material(Color color){
			this.color = color;
		}
		
		private Material(Color color, int type){
			this.color = color;
			this.type = type;
		}

		public Color getColor(){
			return color;
		}
	}

	public float project(int i){
		float scl = 1f / 60f;
		return i * scl;
	}

	void generate(){
		//System.out.println(new Color(899795322));
		VertexObject object = VertexLoader.read(Gdx.files.internal("vertexobjects/tree.vto"));
		object.normalize();
		object.alignBottom();
		object.alignSides();
		ObjectMap<String, Polygon> rawpolygons = object.getPolygons();

		Array<GeneratorPolygon> polygons = new Array<GeneratorPolygon>();

		for(String key : rawpolygons.keys())
			polygons.add(new GeneratorPolygon(key, object.lists.get(key), rawpolygons.get(key)));

		for(int x = 0;x < width;x ++){
			for(int y = 0;y < height;y ++){
				float scl = 1f / 60f;
				float rx = project(x - width / 2), ry = project(y);
				for(GeneratorPolygon poly : polygons){
					if(poly.list.type == PolygonType.line) continue;
					if(poly.polygon.contains(rx, ry)) set(x, y, poly.list.material(), poly);
				}
			}
		}
		finish();
	}

	void applyShading(){
		
		
		int[] colors = new int[width*height];
		//store color array
		for(int x = 0;x < width;x ++){
			for(int y = 0;y < height;y ++){
				colors[y * width + x] = toARGB(materialPixmap.getPixel(x, y));
			}
		}
		
		//crystallization...
		for(int x = 0;x < width;x ++){
			for(int cy = 0;cy < height;cy ++){
				int y = height - 1 - cy;
				Pixel pixel = materials[x][height-1-y];
				if(pixel.material == null) continue;
				
				if(pixel.material.type == -1) continue;
				int color = Patterns.leafPattern(x, y, width, height, colors, pixel.material.type);
				Color cc = new Color(color);
				if(cc.a < 0.5f){
					cc.a = 0f;
				}else{
					cc.a = 1f;
					cc.set(pixel.material.getColor().cpy().mul(0.8f, 0.8f, 0.8f,1f));
				}
				materialPixmap.setColor(cc);
				if(new Color(materialPixmap.getPixel(x, y)).a > 0.001f)
				materialPixmap.drawPixel(x, y);
			}
		}
		
		float round = 0.1f;
		//add brightness
		for(int x = 0;x < width;x ++){
			for(int cy = 0;cy < height;cy ++){
				int y = height - 1 - cy;
				Pixel pixel = materials[x][y];
				if(pixel.polygon == null || pixel.polygon.material() != Material.leaves) continue;

				float dist = 0.7f * ((1.5f - pixel.polygon.lightVertice.dst(project(x - width / 2), project(y))));
				float lightdist = 0.6f * ((1f - GeneratorPolygon.lightsource.dst(project(x - width / 2), project(y))));

				float scl = /*(project(y) - pixel.polygon.bottom()) / pixel.polygon.height()+*/dist + lightdist;// + (float)Noise.NormalNoise(x, y, 4f, 0.3f, 1f);
				materialPixmap.drawPixel(x, cy, Color.rgba8888(brighter(new Color(materialPixmap.getPixel(x, cy)), round * (int)(((scl * 0.5f) / round)))));

				if(pixel.polygon.center.dst(project(x - width / 2), project(y)) < 0.1){
					//	materialPixmap.drawPixel(x, cy,Color.rgba8888(Color.WHITE));
				}
			}
		}
		
		//add shadows
		for(int x = 0;x < width;x ++){
			for(int cy = 0;cy < height;cy ++){
				int y = height - 1 - cy;
				if(y == 0) continue;
				int offsetx = x - 3;
				int offsety = y + 3;

				GeneratorPolygon poly = getPixelPolygon(offsetx, offsety);
				GeneratorPolygon other = getPixelPolygon(x, y);
				if(poly == null) continue;

				if(!(poly.above(other))) continue;
			//	float scl = 1f -poly.center.dst(project(offsetx - width / 2), project(offsety)) / (poly.height());
				
				materialPixmap.setColor(new Color(0, 0, 0, 0.3f));
				materialPixmap.drawPixel(x, cy);

			}
		}
		
		//add outlines...
		for(int x = 0;x < width;x ++){
			for(int cy = 0;cy < height;cy ++){
				int y = height - 1 - cy;
				Pixel pixel = materials[x][y];
				if(pixel.material == null) continue;
				
			}
		}
		
	}
	Color temp = new Color();
	public int toARGB(int rgb){
		temp.set(rgb);
		java.awt.Color color = new java.awt.Color(temp.r, temp.g, temp.b, temp.a);
		return color.getRGB();
	}
	
	public static int fromARGB(int rgb){
		java.awt.Color color = new java.awt.Color(rgb);
		return Color.rgba8888(color.getRed(), color.getGreen(), color.getBlue(), 1f);
	}

	Color brighter(Color color, float a){
		//float a = 0.07f;
		color.add(a, a, -a * 2, 0f);
		//color.a = 1f;
		return color;
	}

	void set(int x, int y, Material material, GeneratorPolygon polygon){
		if(x < 0 || y < 0 || x >= width || y >= height) return;
		if( !(materials[x][y].material == null || materials[x][y].material.ordinal() < material.ordinal() || polygon.above(getPixelPolygon(x, y)))) return;
		materials[x][y].material = material;
		materials[x][y].polygon = polygon;
	}

	GeneratorPolygon getPixelPolygon(int x, int y){
		if(x < 0 || y < 0 || x >= width || y >= height) return null;
		return materials[x][y].polygon;
	}

	@Override
	public void update(){
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)) Gdx.app.exit();
		if(Gdx.input.isKeyJustPressed(Keys.R)) reset();
		draw();

	}

	void draw(){
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(materialTexture, Gdx.graphics.getWidth() / 2 - width * scl / 2, Gdx.graphics.getHeight() / 2 - height * scl / 2, width * scl, height * scl);
		batch.end();
	}

	void generatePixmaps(){
		for(int x = 0;x < width;x ++){
			for(int y = 0;y < height;y ++){
			//	 materialPixmap.drawPixel(x, height - 1 - y, Color.rgba8888(Color.WHITE));
				if(materials[x][y] != null && materials[x][y].material != null) materialPixmap.drawPixel(x, height - 1 - y, Color.rgba8888(materials[x][y].material.getColor()));
			}
		}
		print("Applying shading...");
		applyShading();
		print("Finished applying shading.");
		materialTexture.draw(materialPixmap, 0, 0);
	}

	@Override
	public void init(){
		batch = new SpriteBatch();
		materials = new Pixel[width][height];
		for(int x = 0;x < width;x ++){
			for(int y = 0;y < height;y ++){
				materials[x][y] = new Pixel();
			}
		}
		//	materials[width / 2][0].material = Material.wood;
		materialPixmap = new Pixmap(width, height, Format.RGBA8888);
		materialTexture = new Texture(materialPixmap);

		generate();
		generatePixmaps();
	}

	public void resize(int width, int height){
		batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
	}

	void reset(){
		Pixmap.setBlending(Blending.None);
		for(int x = 0;x < width;x ++){
			for(int y = 0;y < height;y ++){
				materials[x][y] = new Pixel();
				materialPixmap.drawPixel(x, y, Color.rgba8888(Color.CLEAR));
			}
		}
		//materials[width / 2][0].material = Material.wood;
		Pixmap.setBlending(Blending.SourceOver);

		generate();
		generatePixmaps();
	}

	void finish(){
		print("Finished generation.");
	}

	void print(Object o){
		System.out.println(o);
	}
}
