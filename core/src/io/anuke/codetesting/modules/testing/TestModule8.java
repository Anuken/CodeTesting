package io.anuke.codetesting.modules.testing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

import io.anuke.codetesting.modules.Module;
import io.anuke.vector.Canvas;
import io.anuke.vector.CanvasFlags.CanvasFlag;
import io.anuke.vector.DrawingStyle;
import io.anuke.vector.LineCap;

/** uVector test. */
public class TestModule8 extends Module{
	private Canvas canvas;

	public TestModule8() {
		canvas = Canvas.obtain(CanvasFlag.antiAlias, CanvasFlag.stencilStrokes);
	}

	@Override
	public void update(){
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT);
		canvas.clear();
		canvas.setDrawingStyle(DrawingStyle.stroke);
		//canvas.setFillColor(0, 0, 1f, 1f);
		canvas.setStrokeLineCap(LineCap.round);
		canvas.setStrokeWidth(10f);
		canvas.setStrokeColor(Color.RED);
		
		//canvas.drawRectangle(300, 300, 100, 100);
		canvas.drawLines(0, 0, 500, 400);
		//canvas.drawPoints(0, 0, 500, 500);
		canvas.render();
		
	}
	
	public void resize(int width, int height){
		canvas.free();
		canvas = Canvas.obtain(CanvasFlag.antiAlias, CanvasFlag.stencilStrokes);
	}
}
