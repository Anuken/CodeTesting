package net.pixelstatic.codetesting.modules.vertex;

import net.pixelstatic.codetesting.modules.Module;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public class VertexGUI extends Module{
	final Color verticeColor = Color.RED;
	final Color selectColor = Color.PURPLE;
	final Color nodeColor = Color.GREEN;
	final float grabrange = 20;
	ShapeRenderer shape = new ShapeRenderer();
	Array<VertexCanvas> canvases = new Array<VertexCanvas>();
	VertexCanvas canvas = new VertexCanvas();
	//VertexObject object = new VertexObject();
	Vector2 vertice;
	VertexEditor editor;
	boolean symmetry;
	boolean drawMode;

	@Override
	public void update(){
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		ActorAlign.updateAll();
		input();
		if(symmetry) updateSymmetry();
		shape.begin(ShapeType.Line);
		draw();
		shape.end();
	}
	
	void draw(){
		drawVertices(canvas.vertices, false);
		
		if( symmetry) drawVertices(mirror(canvas.vertices), true);
		
		
		shape.set(ShapeType.Line);
		shape.setColor(Color.MAGENTA);
		if(symmetry) shape.line(Gdx.graphics.getWidth()/2, 0, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());
	
	}

	void drawVertices(Array<Vector2> vertices, boolean mirror){
		shape.set(ShapeType.Line);
		shape.setColor(verticeColor);
		Gdx.gl.glLineWidth(4);
		shape.setAutoShapeType(true);
		for(int i = 0;i < vertices.size;i ++){
			Vector2 current = vertices.get(i);
			Vector2 next = (i == vertices.size - 1 ? (drawMode ? null : vertices.get(0)) : vertices.get(i + 1));
			if(next != null) shape.line(current.cpy().add(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2), next.cpy().add(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2));

		}

		if(drawMode && vertices.size != 0) shape.line(vertices.peek().cpy().add(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2), mouseVector().scl( (mirror ? -1 : 1), 1f).add(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2));

		Gdx.gl.glLineWidth(2);
		shape.setColor(selectColor);

		Vector2 selected = selectedVertice();

		if(vertice != null){
			shape.setColor(selectColor);
			selected = vertice;
		}else if(selected != null){
			shape.setColor(Color.BLUE);
		}
		
		
		shape.setColor(selectColor);

		if(selected != null || vertice != null) shape.circle(Gdx.graphics.getWidth() / 2 + selected.x, Gdx.graphics.getHeight() / 2 + selected.y, grabrange);

		shape.set(ShapeType.Filled);
		for(int i = 0;i < vertices.size;i ++){
			Vector2 current = vertices.get(i);
			if(current == vertice){
				shape.setColor(Color.YELLOW);
			}else{
				shape.setColor(nodeColor);
			}
			shape.circle(Gdx.graphics.getWidth() / 2 + current.x, Gdx.graphics.getHeight() / 2 + current.y, 10);
		}
		
		shape.setColor(Color.CYAN);
		if(drawMode) shape.circle(Gdx.graphics.getWidth() / 2 + mouseVector().x * (mirror ? -1 : 1), Gdx.graphics.getHeight() / 2 + mouseVector().y, 10);

	}
	
	Array<Vector2> mirror(Array<Vector2> vertices){
		Array<Vector2> copy = new Array<Vector2>();
		for(Vector2 vector : vertices){
			copy.add(vector.cpy().scl(-1,1));
		}
		return copy;
	}

	void finishDrawMode(){
		if(!symmetry) return;
		Array<Vector2> mirror = mirror(canvas.vertices);
		for(int i= mirror.size-1; i >= 0 ; i --){
			canvas.vertices.add(mirror.get(i));
		}
	}

	void activateSymmetry(){
		symmetry = !symmetry;
	}

	void updateSymmetry(){
		/*
		for(int i = 0;i < object.vectors.size / 2;i ++){

			Vector2 vector = object.vectors.get(i);
			Vector2 other = object.vectors.get(object.vectors.size - 1 - i);
			if(other == vertice){
				Vector2 temp = vector;
				vector = other;
				other = temp;
			}
			other.set( -vector.x, vector.y);
		}
		*/
	}

	Vector2 mouseVector(){
		return new Vector2(Gdx.input.getX() - Gdx.graphics.getWidth() / 2, (Gdx.graphics.getHeight() - Gdx.input.getY()) - Gdx.graphics.getHeight() / 2);
	}

	Vector2 selectedVertice(){
		for(Vector2 vector : canvas.vertices){
			if(vector.dst(Gdx.input.getX() - Gdx.graphics.getWidth() / 2, (Gdx.graphics.getHeight() - Gdx.input.getY()) - Gdx.graphics.getHeight() / 2) < grabrange){
				return vector;
			}
		}
		return null;
	}

	void input(){
		if(vertice != null){
			vertice.set(Gdx.input.getX() - Gdx.graphics.getWidth() / 2, (Gdx.graphics.getHeight() - Gdx.input.getY()) - Gdx.graphics.getHeight() / 2);
		}
	}

	public void init(){
		//Gdx.graphics.set
		editor = tester.getModule(VertexEditor.class);
		shape = new ShapeRenderer();

		TextField field = new TextField("name", editor.skin);
		field.setWidth(130);
		align(field, Align.topRight, 1f, 1f, 0, 0);

		Button symmetry = new TextButton("Symmetry", editor.skin, "toggle");
		symmetry.setSize(field.getWidth(), 30);
		symmetry.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				activateSymmetry();
			}
		});

		align(symmetry, Align.topRight, 1f, 1f, 0, -field.getHeight());

		Button clear = new TextButton("Clear", editor.skin);
		clear.setSize(field.getWidth(), 30);
		clear.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				canvas.clear();
			}
		});

		align(clear, Align.topRight, 1f, 1f, 0, -field.getHeight() * 2);

		Button button = new TextButton("Export", editor.skin);
		button.setSize(field.getWidth(), 30);

		align(button, Align.topRight, 1f, 1f, 0, -field.getHeight() * 3);

		Button button2 = new TextButton("Import", editor.skin);
		button2.setSize(field.getWidth(), 30);

		align(button2, Align.topRight, 1f, 1f, 0, -field.getHeight() * 4);

	}

	void align(Actor actor, int align, float wscl, float hscl, float xoffset, float yoffset){
		new ActorAlign(actor, align, wscl, hscl, xoffset, yoffset);
	}

	@Override
	public void resize(int width, int height){
		shape.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
		shape.updateMatrices();
	}

}
