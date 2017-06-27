package io.anuke.codetesting.fluidsim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import io.anuke.codetesting.fluidsim.Cell.CellType;
import io.anuke.gif.GifRecorder;
import io.anuke.ucore.core.*;
import io.anuke.ucore.modules.RendererModule;
import io.anuke.ucore.util.Angles;
import io.anuke.ucore.util.Mathf;

public class FluidControl extends RendererModule{
	int size = 160;
	Cell[][] grid = new Cell[size][size];
	float[][] write = new float[size][size];
	int iterations = 3;
	
	Color light = Color.valueOf("88acde");
	Color dark = Color.valueOf("3c407b");
	float drawsize = 5f;
	
	GifRecorder recorder = new GifRecorder(batch);
	
	float maxValue = 1.0f;
	float minValue = 0.005f;

	float maxCompression = 0.25f;

	float minFlow = 0.005f;
	float maxFlow = 4f;

	float flowSpeed = 1f;
	
	FluidProvider prov = new GridFluidProvider(size, size);
	Fluidsim sim = new Fluidsim(prov);
	

	public FluidControl() {
		clearColor = Color.WHITE;
		DrawContext.font = new BitmapFont();
		
		for(int x = 0; x < size; x++){
			for(int y = 0; y < size; y++){
				grid[x][y] = new Cell();
			}
		}
		
		for(int x = 0; x < size; x++){
			for(int y = 0; y < size; y++){
				Cell cell = grid[x][y];
				
				if(Mathf.inBounds(x, y+1, grid))
					cell.top = grid[x][y+1];
				
				if(Mathf.inBounds(x, y-1, grid))
					cell.bottom = grid[x][y-1];
				
				if(Mathf.inBounds(x+1, y, grid))
					cell.right = grid[x+1][y];
				
				if(Mathf.inBounds(x-1, y, grid))
					cell.left = grid[x-1][y];
				
			}
		}
		
		int pad = 3;
		
		
		Angles.translation(45, 10f);
		for(int x = pad; x < size-pad; x ++){
			grid[x][size/2].type = CellType.solid;
		}
	}

	public void update(){
		//for(int i = 0; i < iterations; i++){
		//	Simulate();
		//}
		sim.simulate();
		
		int x = (int)(Graphics.mouseWorld().x/drawsize);
		int y = (int)(Graphics.mouseWorld().y/drawsize);
		
		if(Inputs.buttonDown(Buttons.LEFT)){
			int rad = 2;
			
			if(Mathf.inBounds(x, y, grid)){
				for(int cx = -rad; cx <= rad; cx ++){
					for(int cy = -rad; cy <= rad; cy ++){
						if(!Mathf.inBounds(x+cx, y+cy, grid)) continue;
						prov.setLiquid(x+cx, y+cy, 1f);
						prov.setSettled(cx, cy, false);
					}
				}
			}
		}
		
		if(Inputs.buttonDown(Buttons.RIGHT)){
			
			
			if(Mathf.inBounds(x, y, grid)){
				for(int cx = -1; cx <= 1; cx ++){
					for(int cy = -1; cy <= 1; cy ++){
						if(!Mathf.inBounds(x+cx, y+cy, grid)) continue;
						grid[x+cx][y+cy].liquid = 0f;
					}
				}
			}
		}
		
		if(Inputs.buttonDown(Buttons.MIDDLE)){
			if(Mathf.inBounds(x, y, grid)){
				grid[x][y].liquid = 0f;
				grid[x][y].type = CellType.solid;
			}
		}
		
		drawDefault();
		
		recorder.update();
	}

	public void draw(){
		for(int x = 0; x < size; x++){
			for(int y = 0; y < size; y++){
				if(prov.isSolid(x, y)){
					Draw.color(Color.BLACK);
					Draw.fillcrect(x*drawsize, y*drawsize, drawsize, drawsize);
				}else/* if(grid[x][y].Type == CellType.Liquid)*/{
					float height = prov.getLiquid(x, y)*drawsize;
					if(y < size-1 && prov.getLiquid(x, y+1) > minValue){
						height = drawsize;
					}
					Draw.color(light, dark, Mathf.clamp((prov.getLiquid(x, y)-1f)/1.5f));
					Draw.fillcrect(x*drawsize, y*drawsize, drawsize, height);
				}
			}
		}
		
		Draw.tcolor(Color.BLACK);
		Draw.text(Gdx.graphics.getFramesPerSecond() + " FPS", 0, 0);
		/*
		int x = (int)(Graphics.mouseWorld().x/drawsize);
		int y = (int)(Graphics.mouseWorld().y/drawsize);
		
		
		if(Mathf.inBounds(x, y, grid)){
			Cell cell = grid[x][y];
			
			float dx = 0, dy = 20;
			
			Draw.tcolor(Color.BLACK);
			Draw.text(cell.Type.name(), dx, dy, Align.left);
			Draw.text(cell.Liquid +"", dx, dy-10, Align.left);
			Draw.text(cell.Settled+"", dx, dy-20, Align.left);
		}
		*/
	}
	
	float CalculateVerticalFlowValue(float remainingLiquid, Cell destination){
		float sum = remainingLiquid + destination.liquid;
		float value = 0;

		if (sum <= maxValue) {
			value = maxValue;
		} else if (sum < 2 * maxValue + maxCompression) {
			value = (maxValue * maxValue + sum * maxCompression) / (maxValue + maxCompression);
		} else {
			value = (sum + maxCompression) / 2f;
		}

		return value;
	}

	// Run one simulation step
	public void Simulate() {

		float flow = 0;

		// Reset the diffs array
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				write[x][y] = 0;
			}
		}

		// Main loop
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {

				// Get reference to Cell and reset flow
				Cell cell = grid[x][y];
				//cell.ResetFlowDirections ();
				
				// Validate cell
				if (cell.type == CellType.solid) {
					cell.liquid = 0;
					continue;
				}
				
				if (cell.liquid == 0)
					continue;
				//if (cell.Settled) 
				//	continue;
				if (cell.liquid < minValue) {
					cell.liquid = 0;
					continue;
				}
				

				// Keep track of how much liquid this cell started off with
				float startValue = cell.liquid;
				float remainingValue = cell.liquid;
				flow = 0;

				// Flow to bottom cell
				if (cell.bottom != null && cell.bottom.type != CellType.solid) {

					// Determine rate of flow
					flow = CalculateVerticalFlowValue(cell.liquid, cell.bottom) - cell.bottom.liquid;
					if (cell.bottom.liquid > 0 && flow > minFlow)
						flow *= flowSpeed; 

					// Constrain flow
					flow = Math.max (flow, 0);
					if (flow > Math.min(maxFlow, cell.liquid)) 
						flow = Math.min(maxFlow, cell.liquid);

					// Update temp values
					if (flow != 0) {
						remainingValue -= flow;
						write[x][y] -= flow;
						write[x][y - 1] += flow;
						cell.bottom.settled = false;
						cell.settletime = 0;
					} 
				}

				// Check to ensure we still have liquid in this cell
				if (remainingValue < minValue) {
					write[x][y] -= remainingValue;
					continue;
				}

				// Flow to left cell
				if (cell.left != null && cell.left.type != CellType.solid) {

					// Calculate flow rate
					flow = (remainingValue - cell.left.liquid) / 4f;
					if (flow > minFlow)
						flow *= flowSpeed;

					// constrain flow
					flow = Math.max (flow, 0);
					if (flow > Math.min(maxFlow, remainingValue)) 
						flow = Math.min(maxFlow, remainingValue);

					// Adjust temp values
					if (flow != 0) {
						remainingValue -= flow;
						write[x][y] -= flow;
						write[x - 1][y] += flow;
						cell.left.settled = false;
						cell.settletime = 0;
					} 
				}

				// Check to ensure we still have liquid in this cell
				if (remainingValue < minValue) {
					write[x][y] -= remainingValue;
					continue;
				}
				
				// Flow to right cell
				if (cell.right != null && cell.right.type != CellType.solid) {

					// calc flow rate
					flow = (remainingValue - cell.right.liquid) / 3f;										
					if (flow > minFlow)
						flow *= flowSpeed; 

					// constrain flow
					flow = Math.max (flow, 0);
					if (flow > Math.min(maxFlow, remainingValue)) 
						flow = Math.min(maxFlow, remainingValue);
					
					// Adjust temp values
					if (flow != 0) {
						remainingValue -= flow;
						write[x][y] -= flow;
						write[x + 1][y] += flow;
						cell.right.settled = false;
						cell.settletime = 0;
					} 
				}

				// Check to ensure we still have liquid in this cell
				if (remainingValue < minValue) {
					write[x][y] -= remainingValue;
					continue;
				}
				
				// Flow to Top cell
				if (cell.top != null && cell.top.type != CellType.solid) {

					flow = remainingValue - CalculateVerticalFlowValue (remainingValue, cell.top); 
					if (flow > minFlow)
						flow *= flowSpeed; 

					// constrain flow
					flow = Math.max (flow, 0);
					if (flow > Math.min(maxFlow, remainingValue)) 
						flow = Math.min(maxFlow, remainingValue);

					// Adjust values
					if (flow != 0) {
						remainingValue -= flow;
						write[x][y] -= flow;
						write[x][y + 1] += flow;
						cell.top.settled = false;
						cell.settletime = 0;
					} 
				}

				// Check to ensure we still have liquid in this cell
				if (remainingValue < minValue) {
					write[x][y] -= remainingValue;
					continue;
				}

				// Check if cell is settled
				if (startValue == remainingValue) {
					cell.settletime++;
					if (cell.settletime >= 10) {
						cell.settled = true;
					}
				} else {
					cell.UnsettleNeighbors ();
				}
			}
		}
			
		// Update Cell values
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				grid[x][y].liquid += write[x][y];
				if (grid[x][y].liquid < minValue) {
					grid[x][y].liquid = 0;
					grid[x][y].settled = false;	//default empty cell to unsettled
				}				
			}
		}			
	}
	
	public void resize(){
		setCamera(size*drawsize/2f, size*drawsize/2f);
		camera.update();
	}
}
