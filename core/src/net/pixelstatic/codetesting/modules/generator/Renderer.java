package net.pixelstatic.codetesting.modules.generator;

import net.pixelstatic.codetesting.modules.Module;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;

public class Renderer extends Module{
	SpriteBatch batch;
	Pixmap pixmap;
	Texture texture;
	int size = 300;
	int[][] grid;
	Integer[] gridrandom;
	int scale = 2, branches, maxbranches = 20;
	boolean done = false;
	float extrasize = 1.3f;
	boolean square = true;

	public void init(){
		batch = new SpriteBatch();
		grid = new int[size][size];
		gridrandom = new Integer[size * size];
		randomize();
		pixmap = new Pixmap(size, (int)(size * extrasize), Format.RGBA8888);
		texture = new Texture(pixmap);
		Pixmap.setBlending(Blending.None);
		seed(size / 2, 0);
	}

	int psize(){
		return (int)(size * extrasize);
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

	void clear(){
		for(int x = 0;x < size;x ++){
			for(int y = 0;y < psize();y ++){
				if(y < size) grid[x][y] = 0;
				pixmap.drawPixel(x, y, 0);
			}
			seed(size / 2, 0);
			branches = 0;
			done = false;
		}
	}

	void input(){
		if(Gdx.input.isKeyJustPressed(Keys.P)){
			done = !done;
		}
		if(Gdx.input.isKeyJustPressed(Keys.R)){
			clear();
		}
		if(Gdx.input.isKeyJustPressed(Keys.Q)){
			System.out.println("Exporting...");
			Pixmap flipped = new Pixmap(size * 2, (int)(size * extrasize) * 2, Format.RGBA8888);
			int scale = 2;
			for(int x = 0;x < size * scale;x ++){
				for(int y = 0;y < (int)(size * extrasize) * scale;y ++){
					flipped.drawPixel(x, y, pixmap.getPixel(x / scale, ((int)(size * extrasize - 1) - y / scale)));
				}
			}
			PixmapIO.writePNG(Gdx.files.local("tree.png"), flipped);
			flipped.dispose();
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

	//returns whether or not there is a pixel at x,y
	boolean pe(int x, int y){
		if(x < 0 || y < 0 || x >= size || y >= psize()) return false;
		return pixmap.getPixel(x, y) != 0;
	}

	void finish(){
		int trunkwidth = 1;
		//thicker trunk
		for(int x = 0;x < size;x ++){
			for(int y = 0;y < size;y ++){
				//trunkwidth = (size-y)/75+1;
				if(grid[x][y] == 3){
					for(int i = -trunkwidth;i <= trunkwidth;i ++){
						pixel(x + i, y);
						pixel(x, y + i);
					}
				}
			}
		}
		//fancier trunk
		for(int x = 0;x < size;x ++){
			for(int y = 0;y < size;y ++){
				//edgier corners
				if(((pe(x - 1, y) || pe(x + 1, y)) && !pe(x, y - 1) && pe(x, y)) || ( !pe(x - 1, y) && pe(x + 1, y) && pe(x, y))){
					pixmap.drawPixel(x, y, Color.rgba8888(barkcolor(y).mul(0.7f, 0.7f, 0.7f, 1f)));
				}

				if(((pe(x - 1, y) || pe(x + 1, y)) && !pe(x, y + 1) && pe(x, y)) || ( !pe(x + 1, y) && pe(x - 1, y) && pe(x, y))){
					pixmap.drawPixel(x, y, Color.rgba8888(barkcolor(y).mul(1.3f)));
				}

			}
		}

		//add leaves
		for(int x = 0;x < size;x ++){
			for(int y = 0;y < size;y ++){
				if((grid[x][y] != 2 && grid[x][y] != 1) && !(y == size - 1 && grid[x][y] == 3)) continue;
				int size = y / 20 + 3 + MathUtils.random(0, 5) * 4;
				//shadows
				Pixmap.setBlending(Blending.SourceOver);
				pixmap.setColor(new Color(0, 0, 0, 0.05f));
				int add = 6;
				int shadowsize = size + add;
				for(int i = 0;i < 6;i ++){
					if(square){
						pixmap.fillRectangle(x - shadowsize / 2, y - shadowsize / 2, shadowsize, shadowsize);
					}else{
						circle(x,y,shadowsize/2);
					}
					shadowsize -= add / 6;
				}
				
				//pixmap.fillCircle(x, y, shadowsize / 2);
				
				Pixmap.setBlending(Blending.None);

				//'leaves'
				Color color = leafcolor(y);
				pixmap.setColor(color);
				if(square){
					pixmap.fillRectangle(x - size / 2, y - size / 2, size, size);
					pixmap.setColor(color.cpy().sub(0.1f, 0.1f, 0.1f, 0f));
					pixmap.drawLine(x - size / 2, y - size / 2, x - size / 2, y + size / 2);
					pixmap.drawLine(x - size / 2 + 1, y - size / 2, x + size / 2, y - size / 2);
					pixmap.setColor(color.cpy().add(0.1f, 0.1f, 0.1f, 0f));
					pixmap.drawLine(x + size / 2, y + size / 2, x - size / 2, y + size / 2);
					pixmap.drawLine(x + size / 2, y + size / 2 - 1, x + size / 2, y - size / 2);
				}else{
					pixmap.fillCircle(x , y , size/2);
				}
			}
		}
	}
	
	void circle(int x, int y, int rad){
		for(int rx = -rad; rx <= rad; rx ++){
			for(int ry = -rad; ry <= rad; ry ++){
				if(!pbounds(x+rx,y+ry)) continue;
				if(Vector2.dst(x, y, x+rx, y+ry) < rad){
					pixmap.drawPixel(rx+x, ry+y);
				}
			}
		}
	}

	void grow(int x, int y){
		if( !bounds(x, y)) return;
		int add = 0;
		int spacing = 20;
		y ++;
		for(int i = 1;i < spacing;i ++){
			if((occupied(x + spacing, y) && occupied(x - spacing, y))) break;
			if((occupied(x + i, y) && occupied(x - i, y)) || (occupied(x + i + 1, y) && occupied(x - i, y)) || (occupied(x + i, y) && occupied(x - i - 1, y))) break;
			if(occupied(x + i, y)){
				add = -1;
				break;
			}else if(occupied(x - i, y)){
				add = 1;
				break;
			}
		}
		y --;
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
		float tallness = size / 1.5f;
		float offset = size / 4;
		if(Math.random() < 0.01){
			for(int ix = 0;ix < size;ix ++){
				for(int iy = (int)offset;iy < tallness - 5;iy ++){
					if(grid[ix][iy] == 3 && Math.random() < 0.05 / branches){

						int leftr = 0, rightr = 0;
						for(int i = 1;i <= 50;i ++){

							if(leftr != 0 && rightr != 0) break;
							if(leftr != 0 && occupied( -i + ix, iy)){
								leftr = i;
							}
							if(rightr != 0 && occupied(i + ix, iy)){
								rightr = i;
							}

							if(i == 50){
								if(rightr == 0 && leftr == 0){
									seed(ix + MathUtils.randomSign(), iy);
								}else if(leftr == 0){
									seed(ix + 1, iy);
								}else if(rightr == 0){
									seed(ix - 1, iy);
								}else{

								}
							}
						}
						/*
						if(leftr == 199 && leftr == 199){
							continue;
						}else if(leftr == 0 && rightr == 0){
							seed(ix + MathUtils.randomSign(), iy);
						}else{
							seed(ix + leftr < rightr ? -1 : 1, iy);
						}
						*/
						//seed(ix + (ix < size/2 ? -1 : 1), iy);

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
		seed(x + add, y + (add == 0 ? 1 : 0));
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
	
	boolean pbounds(int x, int y){
		return !(x < 0 || y < 0 || x >= size || y >= psize());
	}

	Color barkcolor(int y){
		float offset = 0.5f;
		float scl = offset + y / (size * (1f / offset));
		return (Color.BROWN.cpy().mul(scl, scl, scl, 1f));
	}

	Color leafcolor(int y){
		float offset = 0.4f;
		float scl = offset + y / (size * (1f / offset));
		return (Color.OLIVE.cpy().mul(scl, scl, scl, 1f));
	}

	void pixel(int x, int y){
		pixmap.drawPixel(x, y, Color.rgba8888(barkcolor(y)));
	}

	public void resize(int width, int height){
		batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
	}

	void draw(){
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		texture.draw(pixmap, 0, 0);
		batch.begin();
		batch.draw(texture, Gdx.graphics.getWidth() / 2 - size * scale / 2, Gdx.graphics.getHeight() / 2 - size * scale / 2 + size * extrasize * scale, size * scale, -size * scale * extrasize);
		batch.end();
	}

}
