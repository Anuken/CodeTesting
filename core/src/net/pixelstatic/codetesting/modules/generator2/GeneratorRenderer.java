package net.pixelstatic.codetesting.modules.generator2;

import net.pixelstatic.codetesting.modules.Module;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jhlabs.image.CellularFilter;

public class GeneratorRenderer extends Module{
	final int scl = 5;
	SpriteBatch batch;
	TreeGenerator tree;

	static class Pixel{
		Material material;
		GeneratorPolygon polygon;
	}

	public enum Material{
		bone(Color.MAGENTA), wood(Color.BROWN.cpy().sub(0.1f, 0.1f, 0.1f, 0f), -1), leaves(new Color(0.0f, 0.4f, 0.3f, 1f), -1);

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

	@Override
	public void update(){
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)) Gdx.app.exit();
		if(Gdx.input.isKeyJustPressed(Keys.R)) tree.reset();
		draw();
	}

	void draw(){
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(tree.texture, Gdx.graphics.getWidth() / 2 - tree.width * scl / 2, Gdx.graphics.getHeight() / 2 - tree.height * scl / 2, tree.width * scl, tree.height * scl);
		batch.end();
	}

	@Override
	public void init(){
		batch = new SpriteBatch();
		tree = new TreeGenerator();
		tree.reset();
	}

	public void resize(int width, int height){
		batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
	}

	void print(Object o){
		System.out.println(o);
	}
}
