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
		bone(Color.MAGENTA),
		wood(Color.BROWN.cpy().sub(0.1f,0.1f,0.1f,0f)), 
		leaves(Color.FOREST.cpy().sub(0.1f,0.1f,0.1f,0f));

		public Color color;

		private Material(Color color){
			this.color = color;
		}

		public Color getColor(){
			return color;
		}
	}
	
	public float project(int i){
		float scl = 1f / 60f;
		return i*scl;
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
				float rx = project(x - width/2), ry = project(y);
				for(GeneratorPolygon poly : polygons){
					if(poly.list.type == PolygonType.line) continue;
					if(poly.polygon.contains(rx, ry))
						set(x,y, poly.list.material(), poly);
				}
			}
		}
		finish();
	}
	
	void applyShading(){
		float round = 0.07f;
		//add brightness
		for(int x = 0;x < width;x ++){
			for(int cy = 0;cy < height;cy ++){
				int y = height-1-cy;
				Pixel pixel = materials[x][y];
				if(pixel.polygon == null || pixel.polygon.material() != Material.leaves) continue; 
				float scl = (project(y) - pixel.polygon.bottom()) / pixel.polygon.height();
				materialPixmap.drawPixel(x, cy,
				Color.rgba8888(brighter(
				new Color(materialPixmap.getPixel(x, cy)), round * (int)(((scl*0.5f)/ round) ))));
				
			}
		}
		//add shadows
		for(int x = 0;x < width;x ++){
			for(int cy = 0;cy < height;cy ++){
				int y = height-1-cy;
			}
		}
	}
	
	Color brighter(Color color, float a){
		//float a = 0.07f;
		color.add(a, a, -a, 0f);
		//color.a = 1f;
		return color;
	}
	
	void set(int x, int y, Material material, GeneratorPolygon polygon){
		if(x < 0 || y < 0 || x >= width || y >= height) return;
		if(!(materials[x][y].material == null || materials[x][y].material.ordinal() < material.ordinal() || polygon.above(getPixelPolygon(x,y)))) return;
		materials[x][y].material = material;
		materials[x][y].polygon = polygon;
	}
	
	GeneratorPolygon getPixelPolygon(int x, int y){
		if(x < 0 || y < 0 || x >= width || y >= height) return null;
		return materials[x][y].polygon;
	}

	@Override
	public void update(){
		if(Gdx.input.isKeyPressed(Keys.ESCAPE))Gdx.app.exit();
		if(Gdx.input.isKeyJustPressed(Keys.R))reset();
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
				if(materials[x][y] != null && materials[x][y].material != null)materialPixmap.drawPixel(x, height - 1 - y, Color.rgba8888(materials[x][y].material.getColor()));
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
				materialPixmap.drawPixel(x,y, Color.rgba8888(Color.CLEAR));
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
