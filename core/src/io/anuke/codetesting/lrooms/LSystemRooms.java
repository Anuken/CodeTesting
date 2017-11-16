package io.anuke.codetesting.lrooms;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.TimeUtils;

import io.anuke.home.world.MapIO;
import io.anuke.home.world.Tile;
import io.anuke.home.world.World;
import io.anuke.home.world.blocks.Blocks;
import io.anuke.lsystem.evolution.Evolver;
import io.anuke.lsystem.evolution.LProcessor;
import io.anuke.lsystem.evolution.LProcessor.Line;
import io.anuke.lsystem.evolution.LTree;
import io.anuke.ucore.UCore;
import io.anuke.ucore.lsystem.LSystemData;
import io.anuke.ucore.noise.Simplex;
import io.anuke.ucore.util.Geometry;
import io.anuke.ucore.util.Mathf;

public class LSystemRooms{
	static Simplex sim = new Simplex();
	static float roomSize = 0.4f;

	public static void main(String[] args) throws Exception{
		sim.setSeed(TimeUtils.millis());

		Evolver evol = new Evolver();
		evol.generations = 50;
		evol.maxMutations += 3;
		evol.variants += 2;
		evol.iterations = 3;

		float bounds = 30;

		LSystemData data = evol.evolve(tree -> {
			int score = 0;

			for(int i = 0; i < tree.lines.size; i++){
				Line l1 = tree.lines.get(i);

				if(!Mathf.in(l1.x1, 0, bounds / 1.9f) || !Mathf.in(l1.y1, bounds / 2, bounds / 1.9f)){
					return -1;
				}

				for(int j = i + 1; j < tree.lines.size; j++){
					Line l2 = tree.lines.get(j);

					if(Mathf.intersect(l1.x2, l1.y2, roomSize, l2.x2, l2.y2, roomSize)){
						score -= 12;
					}
				}
			}

			if(tree.lines.size == 0){
				return -1;
			}

			return tree.lines.size * 4 + score + tree.branches * 2;
		});

		UCore.log("Resulting data: " + data.rules);

		LTree tree = LProcessor.getTree(data.axiom, data.rules, data.iterations, data.space);
		String out = new Json().toJson(data);
		Files.write(Paths.get(System.getProperty("user.home"), "LSystemExport", "room.json"), out.getBytes());

		UCore.log("Wrote file.");

		float minX = Float.MAX_VALUE, maxX = Float.MIN_VALUE, maxY = Float.MIN_VALUE;

		for(Line line : tree.lines){
			minX = Math.min(line.x2, minX);
			maxX = Math.max(line.x2, maxX);
			maxY = Math.max(line.y2, maxY);
		}

		int roomScale = 30;
		int roomSize = roomScale - 14;

		for(Line line : tree.lines){
			line.x2 -= minX;
			line.x2 += 1f;
			line.y2 += 1f;

			line.x1 -= minX;
			line.x1 += 1f;
			line.y1 += 1f;
		}

		int width = (int) (roomScale * (maxX - minX + 2f)), height = (int) (roomScale * (maxY + 2f));

		UCore.log("Map size: " + width + "x" + height);

		UCore.log("Generating tiles.");

		Tile[][] tiles = new Tile[width][height];
		Tile[][] write = new Tile[width][height];

		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				tiles[x][y] = new Tile(x, y);
				tiles[x][y].wall = Blocks.brickwall;
				tiles[x][y].floor = Blocks.stonefloor;
			}
		}

		for(Line line : tree.lines){
			int x = (int) (line.x2 * roomScale);
			int y = (int) (line.y2 * roomScale);

			for(int dx = -roomSize / 2; dx <= roomSize / 2; dx++){
				for(int dy = -roomSize / 2; dy <= roomSize / 2; dy++){
					int worldx = x + dx;
					int worldy = y + dy;

					if(Mathf.inBounds(worldx, worldy, tiles)){
						tiles[worldx][worldy].wall = Blocks.air;
					}
				}
			}
		}

		//add corridors
		for(Line line : tree.lines){
			int x1 = (int) (line.x1 * roomScale);
			int y1 = (int) (line.y1 * roomScale);

			int x2 = (int) (line.x2 * roomScale);
			int y2 = (int) (line.y2 * roomScale);

			Geometry.iterateLine(0f, x1, y1, x2, y2, 1, (x, y) -> {
				int wx = (int) x;
				int wy = (int) y;
				int rad = 2;

				for(int dx = -rad; dx <= rad; dx++){
					for(int dy = -rad; dy <= rad; dy++){
						int worldx = wx + dx;
						int worldy = wy + dy;

						if(Mathf.inBounds(worldx, worldy, tiles) && Vector2.dst(dx, dy, 0, 0) < rad){
							tiles[worldx][worldy].wall = Blocks.air;
						}
					}
				}
			});
		}

		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				write[x][y] = new Tile(x, y, tiles[x][y].floor, tiles[x][y].wall);
				write[x][y].decal = tiles[x][y].decal;
			}
		}

		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				Tile tile = tiles[x][y];

				int rad = 2;

				if(tile.wall.solid){
					boolean nearAir = false;

					outer: for(int dx = -rad; dx <= rad; dx++){
						for(int dy = -rad; dy <= rad; dy++){
							int worldx = x + dx;
							int worldy = y + dy;

							if(Mathf.inBounds(worldx, worldy, tiles) && tiles[worldx][worldy].wall == Blocks.air){
								nearAir = true;
								break outer;
							}
						}
					}

					if(!nearAir){
						write[x][y].floor = Blocks.air;
						write[x][y].wall = Blocks.air;
					}
				}
			}
		}

		Line start = tree.lines.first();
		write[(int) (start.x1 * roomScale)][(int) (start.y1 * roomScale)].wall = Blocks.startcheckpoint;

		decorateDungeon(write);

		for(Line line : tree.lines){
			int x = (int) (line.x2 * roomScale);
			int y = (int) (line.y2 * roomScale);

			decorateRoom(x, y, roomSize, write);
		}

		UCore.log("Done generating. Writing...");

		World.data().dark = true;
		World.data().sky = false;

		MapIO.save(write, new FileOutputStream(new File("procmap.hsv")));

		UCore.log("Done.");
	}

	static void decorateDungeon(Tile[][] tiles){
		for(int x = 0; x < tiles.length; x++){
			for(int y = 0; y < tiles[0].length; y++){

				//moss
				if(sim.octaveNoise2D(3, 0.8, 1 / 20.0, x, y) > 0.23 && tiles[x][y].floor != Blocks.air){
					tiles[x][y].decal = Blocks.thickmoss;
				}

				//candles
				if(sim.octaveNoise2D(4, 0.9, 1 / 25.0, x, y) > 0.54 && tiles[x][y].wall == Blocks.air && tiles[x][y].floor != Blocks.air){
					tiles[x][y].wall = Blocks.candles;
				}

				//wall moss
				if(Mathf.chance(0.1) && y > 0 && tiles[x][y].wall == Blocks.brickwall && tiles[x][y - 1].wall == Blocks.air){
					tiles[x][y - 1].wall = Blocks.wallmoss;
				}
			}
		}
	}

	static void decorateRoom(int x, int y, int size, Tile[][] tiles){
		boolean library = Mathf.chance(0.08);

		for(int dx = -size / 2 - 1; dx <= size / 2 + 1; dx++){
			for(int dy = -size / 2 - 1; dy <= size / 2 + 1; dy++){
				int worldx = x + dx;
				int worldy = y + dy;

				if(Mathf.inBounds(worldx, worldy, tiles)){
					if(library){
						if(tiles[worldx][worldy].wall == Blocks.brickwall){
							tiles[worldx][worldy].wall = Blocks.smallshelf;
						}

						if(tiles[worldx][worldy].wall == Blocks.air && !Mathf.intersect(x, y, size / 2 - 3, worldx, worldy, 1) && Mathf.chance(0.3)){
							tiles[worldx][worldy].wall = Mathf.choose(Blocks.books, Blocks.books, Blocks.books, Blocks.books, Blocks.books, Blocks.books, Blocks.booktable);
						}
					}else{
						if(tiles[worldx][worldy].wall == Blocks.air && !Mathf.intersect(x, y, size / 2 - 4, worldx, worldy, 1) && Mathf.chance(0.05)){
							tiles[worldx][worldy].wall = Mathf.choose(Blocks.barrel, Blocks.barrel, Blocks.barrel, Blocks.bottles, Blocks.bottles, Blocks.bottles, Blocks.table);
						}
					}
				}
			}
		}

		int trad = size - 4;

		//torches
		if(Mathf.chance(0.1)){
			tiles[x + trad / 2][y + trad / 2].wall = Blocks.torchunlit;
			tiles[x - trad / 2][y + trad / 2].wall = Blocks.torchunlit;
			tiles[x + trad / 2][y - trad / 2].wall = Blocks.torchunlit;
			tiles[x - trad / 2][y - trad / 2].wall = Blocks.torchunlit;
		}
	}
}
