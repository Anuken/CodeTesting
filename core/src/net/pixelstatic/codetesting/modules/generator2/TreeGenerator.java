package net.pixelstatic.codetesting.modules.generator2;

import net.pixelstatic.codetesting.modules.Module;
import net.pixelstatic.codetesting.modules.vertex.VertexLoader;
import net.pixelstatic.codetesting.modules.vertex.VertexObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class TreeGenerator extends Module{
	final int width = 40, height = 56, scl = 5;
	boolean generated;
	SpriteBatch batch;
	GrowMaterial[][] materials;
	Pixmap materialPixmap;
	Texture materialTexture;
	int iterations = 0;
	
	class GrowMaterial{
		//final int maxgrowths = MathUtils.random(1, 20);
		//int growths;
		Material material;
		Material grow;
		
		void grow(){
			if(grow != null){
				//growths ++;
				material = grow;
				grow = null;
			}
		}
	}

	enum Material{
		wood(Color.BROWN), leaves(Color.FOREST);

		public Color color;

		private Material(Color color){
			this.color = color;
		}

		public Color getColor(){
			return color;
		}
	}

	void generate(){
		int maxiterations = 30;
		for(int x = 0;x < width;x ++){
			for(int y = 0;y < height;y ++){
				if(materials[x][y].material== Material.wood){
					
					if(Math.random() < 0.3 && y == 0){
						set(x+MathUtils.randomSign(),y,Material.wood);
					}else{
						set(x,y+1,Material.wood);
					}
						
				}
				materials[x][y].grow();
			}
		}
		if(++ iterations > maxiterations) finish();
	}
	
	void set(int x, int y, Material material){
		if(x < 0 || y < 0 || x >= width || y >= height) return;
			
		materials[x][y].grow = material;
	}

	@Override
	public void update(){
		if(Gdx.input.isKeyPressed(Keys.ESCAPE))Gdx.app.exit();
		if(Gdx.input.isKeyJustPressed(Keys.R))reset();
		if( !generated){
			generate();
			generatePixmaps();
		}
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
		materialTexture.draw(materialPixmap, 0, 0);
	}

	@Override
	public void init(){
		batch = new SpriteBatch();
		materials = new GrowMaterial[width][height];
		for(int x = 0;x < width;x ++){
			for(int y = 0;y < height;y ++){
				materials[x][y] = new GrowMaterial();
			}
		}
		materials[width / 2][0].material = Material.wood;
		materialPixmap = new Pixmap(width, height, Format.RGBA8888);
		materialTexture = new Texture(materialPixmap);
		
		VertexObject object = new VertexObject();
		
		VertexLoader.write(object, Gdx.files.local("pls.vert"));
	}

	public void resize(int width, int height){
		batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
	}
	
	void reset(){
		Pixmap.setBlending(Blending.None);
		for(int x = 0;x < width;x ++){
			for(int y = 0;y < height;y ++){
				materials[x][y] = new GrowMaterial();
				materialPixmap.drawPixel(x,y, Color.rgba8888(Color.CLEAR));
			}
		}
		materials[width / 2][0].material = Material.wood;
		generated = false;
		Pixmap.setBlending(Blending.SourceOver);
		iterations = 0;
	}

	void finish(){
		generated = true;
		print("Finished generation.");
	}

	void print(Object o){
		System.out.println(o);
	}
}
