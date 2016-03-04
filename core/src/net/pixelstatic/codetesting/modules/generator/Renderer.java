package net.pixelstatic.codetesting.modules.generator;

import net.pixelstatic.codetesting.modules.Module;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class Renderer extends Module{
	SpriteBatch batch;
	Pixmap pixmap;
	Texture texture;
	int size = 300;
	int[][] grid;
	Integer[] gridrandom;
	int scale = 2, branches, maxbranches = 40;
	boolean done = false;

	public void init(){
		batch = new SpriteBatch();
		grid = new int[size][size];
		gridrandom = new Integer[size * size];
		randomize();
		pixmap = new Pixmap(size, size * 2, Format.RGBA8888);
		texture = new Texture(pixmap);
		Pixmap.setBlending(Blending.None);
		seed(size / 2, 0);
	}

	void randomize(){
		Array<Integer> ints = new Array<Integer>();
		for(int i = 0;i < size * size;i ++){
			ints.add(i);
		}
		ints.shuffle();
		gridrandom = ints.toArray(Integer.class);
	}

	@Override
	public void update(){
		input();
		if( !done) for(int i = 0;i < 10;i ++)
			generate();
		draw();
	}

	void input(){
		if(Gdx.input.isKeyJustPressed(Keys.R)){
			for(int x = 0;x < size;x ++){
				for(int y = 0;y < size;y ++){
					grid[x][y] = 0;
					pixmap.drawPixel(x, y, 0);
				}
				seed(size / 2, 0);
				branches = 0;
				done = false;
			}
		}
		if(Gdx.input.isKeyJustPressed(Keys.Q)){
			System.out.println("Exporting...");
			PixmapIO.writePNG(Gdx.files.local("tree.png"), pixmap);
			System.out.println("Exported.");
		}
	}

	void generate(){

		for(int i = 0;i < size * size;i ++){
			int c = gridrandom[i];
			int x = c % size, y = c / size;
			int p = grid[x][y];
			if(p == 2 && y < size - 1){
				grow(x, y);
				continue;
			}
			if(done) return;
			if(p == 1) grid[x][y] = 2;
		}
	}

	void finish(){
		for(int x = 0;x < size;x ++){
			for(int y = 0;y < size;y ++){
				if((grid[x][y] != 2 && grid[x][y] != 1) && !(y == size - 1 && grid[x][y] == 3)) continue;
				pixmap.setColor(Color.FOREST.cpy().sub(new Color(0.4f, 0.4f, 0.4f, 0f)).add(y / 800f, y / 800f, y / 800f, 0f));
				pixmap.fillCircle(x, y, y / 20 + 3 + MathUtils.random(0, 5));
			}
		}
	}

	void grow(int x, int y){
		if( !bounds(x, y)) return;
		int add = 0;
		int spacing = 20;
		for(int i = 1;i < spacing;i ++){
			if(occupied(x + spacing, y) && occupied(x - spacing, y)) break;
			if(occupied(x + i, y)){
				add = -1;
				break;
			}else if(occupied(x - i, y)){
				add = 1;
				break;
			}
		}
		//if(add == 0 && Math.random() < 0.3) add = MathUtils.randomSign();
		/*
		if(Math.random() < 0.05){
			int leftr = 0, rightr = 0;
			for(int i = 1;i < spacing;i ++){
				if(leftr != 0 && rightr != 0) break;
				if(leftr != 0 && occupied( -i + x, y)){
					leftr = i;
				}
				if(rightr != 0 && occupied(i + x, y)){
					rightr = i;
				}
			}
			if(leftr == 0 && rightr == 0){
				seed(x + MathUtils.randomSign(), y);
			}else if(leftr == rightr){
				//do nothing
			}else{
				seed(x + leftr < rightr ? leftr : ri2ghtr, y);
			}
		}
		*/
		int tallness = size / 2;
		if(y + 2 == tallness){
			for(int ix = 0;ix < size;ix ++){
				for(int iy = 5;iy < tallness - 5;iy ++){
					if(grid[ix][iy] == 3 && Math.random() < 0.05 / branches){

						int leftr = 0, rightr = 0;
						for(int i = 1;i < 200;i ++){
							if(leftr != 0 && rightr != 0) break;
							if(leftr != 0 && occupied( -i + ix, iy)){
								leftr = i;
							}
							if(rightr != 0 && occupied(i + ix, iy)){
								rightr = i;
							}
						}
						if(leftr == 199 && leftr == 199){
							continue;
						}else if(leftr == 0 && rightr == 0){
							seed(ix + MathUtils.randomSign(), iy);
						}else{
							seed(ix + leftr < rightr ? -1 : 1, iy);
						}

						branches ++;
						if(branches > maxbranches){
							done = true;
							finish();
							return;
						}
					}
				}
			}
		}
		seed(x + add, y + 1);
		grid[x][y] = 3;
	}

	boolean occupied(int x, int y){
		if( !bounds(x, y)) return false;
		return grid[x][y] > 0;
	}

	void seed(int x, int y){
		if( !bounds(x, y) || grid[x][y] == 3) return;
		grid[x][y] = 1;
		pixel(x, y);
	}

	boolean bounds(int x, int y){
		return !(x < 0 || y < 0 || x >= size || y >= size);
	}

	void pixel(int x, int y){
		pixmap.drawPixel(x, y, Color.rgba8888(Color.BROWN.cpy().add(y / 800f - 0.2f, y / 800f - 0.2f, y / 800f - 0.2f, 0f)));
	}

	public void resize(int width, int height){
		batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
	}

	void draw(){
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		texture.draw(pixmap, 0, 0);
		batch.begin();
		batch.draw(texture, Gdx.graphics.getWidth() / 2 - size * scale / 2, Gdx.graphics.getHeight() / 2 - size * scale / 2 + size * 2 * scale, size * scale, -size * scale * 2);
		batch.end();
	}

}
