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
	Pixmap pixmap;
	Texture materialTexture;

	class Pixel{
		Material material;
		GeneratorPolygon polygon;
	}

	public enum Material{
		bone(Color.MAGENTA), wood(Color.BROWN.cpy().sub(0.1f, 0.1f, 0.1f, 0f), -1), 
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
				colors[y * width + x] = toARGB(pixmap.getPixel(x, y));
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
				float mscl = 0.8f;
				if(cc.a < 0.5f){
					cc.a = 0f;
				}else{
					cc.a = 1f;
					cc.set(pixel.material.getColor().cpy().mul(mscl, mscl, mscl,1f));
				}
				pixmap.setColor(cc);
				if(new Color(pixmap.getPixel(x, y)).a > 0.001f)
				pixmap.drawPixel(x, y);
			}
		}
		
		//add brightness, leaf patterns
		for(int x = 0;x < width;x ++){
			for(int cy = 0;cy < height;cy ++){
				int y = height - 1 - cy;
				Pixel pixel = materials[x][y];
				if(pixel.polygon == null) continue;
				if(pixel.polygon.material() == Material.leaves){
					generateLeafPattern(x,y,cy,pixel);
				}else if(pixel.polygon.material() == Material.wood){
					generateLogPattern(x,y,cy,pixel);
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
				
				pixmap.setColor(new Color(0, 0, 0, 0.3f));
				pixmap.drawPixel(x, cy);

			}
		}
		
		//add outlines...
		//if(false)
		for(int x = 0;x < width;x ++){
			for(int cy = 0;cy < height;cy ++){
				int y = height - 1 - cy;
				Pixel pixel = materials[x][y];
				if(pixel.material == null) continue;
				int color = Color.rgba8888(new Color(0,0,0,0.2f));
				
				if(!same(pixel, x, y-1)){
					pixmap.drawPixel(x, cy, color);
				}else if(!same(pixel, x, y+1)){
					pixmap.drawPixel(x, cy, color);
				}else if(!same(pixel, x+1, y)){
					pixmap.drawPixel(x, cy, color);
				}else if(!same(pixel, x-1, y)){
					pixmap.drawPixel(x, cy, color);
				}
			}
		}
	}
	
	public void generateLogPattern(int x, int y, int cy, Pixel pixel){
		Color color = new Color(pixmap.getPixel(x, cy));

		
		//if(project(x-width/2) < pixel.polygon.center.x){
			float m = 0f;
			m = (Patterns.nMod(x,y, 0.2f));
			m+=1f;
			//m += (Patterns.mod(x,-y, 0.1f));
			//color.add(m,m,m,0f);
			color.mul(m,m,m,1f);
		//}
		pixmap.drawPixel(x, cy, Color.rgba8888(color));
	}
	
	
	public void generateLeafPattern(int x, int y, int cy, Pixel pixel){
		float round = 0.1f;
		float gscl = -0.1f;
		
		float dist = gscl +0.7f * ((1.5f - pixel.polygon.lightVertice.dst(project(x - width / 2), project(y))));
		float lightdist = gscl +0.6f * ((1f - GeneratorPolygon.lightsource.dst(project(x - width / 2), project(y))));

		float scl = /*(project(y) - pixel.polygon.bottom()) / pixel.polygon.height()+*/dist + lightdist 
				+ Patterns.noise(x, y, 4f, 0.2f)
				+ Patterns.mod(x, y, 0.07f);
		pixmap.drawPixel(x, cy, Color.rgba8888(brighter(new Color(pixmap.getPixel(x, cy)), round * (int)(((scl * 0.5f) / round)))));

		if(pixel.polygon.center.dst(project(x - width / 2), project(y)) < 0.1){
			//	materialPixmap.drawPixel(x, cy,Color.rgba8888(Color.WHITE));
		}
	}
	
	public float alim(float a){
		if(a < 1f){
			return -1f;
		}else if( a > 1f){
			return 1f;
		}
		return a;
	}
	
	public float round(float a, float b){
		return b *(int)(a/b);
	}
	
	public boolean same(Pixel pixel, int x, int y){
		if(x < 0 || y < 0 || x >= width || y >= height) return true;
		return !pixel.polygon.above(materials[x][y].polygon);
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
				if(materials[x][y] != null && materials[x][y].material != null) pixmap.drawPixel(x, height - 1 - y, Color.rgba8888(materials[x][y].material.getColor()));
			}
		}
		print("Applying shading...");
		applyShading();
		print("Finished applying shading.");
		materialTexture.draw(pixmap, 0, 0);
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
		pixmap = new Pixmap(width, height, Format.RGBA8888);
		materialTexture = new Texture(pixmap);

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
				pixmap.drawPixel(x, y, Color.rgba8888(Color.CLEAR));
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
