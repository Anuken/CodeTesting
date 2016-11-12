package net.pixelstatic.codetesting.modules.workbench;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntSet;

import io.anuke.ucore.graphics.PixmapUtils;

public class MaterialObject{
	public Texture texture;
	public Pixmap pixmap;

	public MaterialObject(Texture texture) {
		texture.getTextureData().prepare();
		pixmap = texture.getTextureData().consumePixmap();

		this.texture = new Texture(pixmap);
	}

	public void toolUsed(int x, int y){
		System.out.println("tool use: " + x + ", " + y);
		Pixmap.setBlending(Blending.None);
		pixmap.setColor(Color.CLEAR);
		pixmap.fillCircle(x, y, 4);
		texture.draw(pixmap, 0, 0);

		update();
	}

	void update(){
		IntSet all = new IntSet();
		Array<IntSet> groups = new Array<IntSet>();

		PixmapUtils.traverse(pixmap, (x, y) -> {
			if(blank(x, y))
				return;
			int i = index(x, y);
			if(!all.contains(i)){
				IntSet set = new IntSet();
				fill(set, x, y);
				groups.add(set);
				all.addAll(set);
				System.out.println("Got a set with size " + set.size);

			}
		});
		
		/*
		System.out.println(groups.size + " chunks.");

		int max = -1, maxi = 0;
		{
			int i = 0;
			for(IntSet set : groups){
				if(set.size > max){
					max = set.size;
					maxi = i;
				}
				i++;
			}
		}
		
		
		pixmap.setColor(Color.CLEAR);
		pixmap.fill();
		
		int i = 0;
		for(IntSetIterator it = groups.get(maxi).iterator(); it.hasNext; i = it.next()){
			int x = i % pixmap.getWidth();
			int y = i / pixmap.getWidth();
			pixmap.drawPixel(x, y);
		

		}
		*/
	}

	void fill(IntSet set, int sx, int sy){
		IntArray points = new IntArray();

		points.add(index(sx, sy));
		while(points.size != 0){
			int pos = points.pop();
			int x = pos % pixmap.getWidth();
			int y = pos / pixmap.getWidth();

			if(!blank(x, y)){
				set.add(pos);

				int u = index(x, y + 1), d = index(x, y - 1), l = index(x - 1, y), r = index(x + 1, y);

				if(x > 0 && !set.contains(l))
					points.add(l);
				if(y > 0 && !set.contains(d))
					points.add(d);
				if(x < width() - 1 && !set.contains(r))
					points.add(r);
				if(y < height() - 1 && !set.contains(u))
					points.add(u);
			}
		}
	}

	int width(){
		return pixmap.getWidth();
	}

	int height(){
		return pixmap.getHeight();
	}

	int index(int x, int y){
		return y * pixmap.getWidth() + x;
	}

	boolean blank(int x, int y){
		return (pixmap.getPixel(x, y) & 0x000000ff) == 0;
	}
}
