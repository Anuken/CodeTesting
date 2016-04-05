package net.pixelstatic.codetesting.modules.vertex;

import net.pixelstatic.codetesting.modules.Module;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public class VertexGUI extends Module{
	final Color otherVerticeColor = Color.BLUE;
	final Color verticeColor = Color.RED;
	final Color selectColor = Color.PURPLE;
	final Color nodeColor = Color.GREEN;
	final float grabrange = 20;
	ShapeRenderer shape = new ShapeRenderer();
	Array<VertexCanvas> canvases = new Array<VertexCanvas>();
	VertexCanvas canvas = new VertexCanvas("canvas", 0);
	Vector2 vertice;
	VertexEditor editor;
	TextButton symmetry, overwrite, add, clear;
	SelectBox<Color> box;
	TextField field;
	boolean drawing, drawMode = true;
	float offsetx, offsety;

	@Override
	public void update(){
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		updateCanvases();
		input();
		shape.begin(ShapeType.Line);
		draw();
		shape.end();
		ActorAlign.updateAll();
	}
	
	void updateCanvases(){
		for(VertexCanvas canvas : canvases)
			canvas.update(this.canvas);
		symmetry.setChecked(canvas.symmetry);
		overwrite.setChecked(drawMode);
		overwrite.setText(drawMode ? "Draw Mode" : "Edit Mode");
		clear.setColor(drawMode ? Color.LIGHT_GRAY: Color.WHITE);
		symmetry.setColor(drawMode ? Color.LIGHT_GRAY : Color.WHITE);
	}
	
	void draw(){
		for(VertexCanvas canvas : canvases){
			drawVertices(canvas, canvas.vertices, false);
			if(canvas.symmetry) drawVertices(canvas, mirror(canvas.vertices), true);
		}
		
		add.setPosition(0, Gdx.graphics.getHeight() - add.getHeight()*(canvases.size+1));
		
		shape.set(ShapeType.Line);
		shape.setColor(Color.MAGENTA);
		if(canvas.symmetry) shape.line(centerx(), 0, centerx(), Gdx.graphics.getHeight());
	}
	
	float centerx(){
		return Gdx.graphics.getWidth()/2 + offsetx;
	}
	
	float centery(){
		return Gdx.graphics.getHeight()/2 + offsety;
	}

	void drawVertices(VertexCanvas canvas, Array<Vector2> vertices, boolean mirror){
		shape.set(ShapeType.Line);
		shape.setColor(canvas.color);
		Gdx.gl.glLineWidth(4);
		shape.setAutoShapeType(true);
		for(int i = 0;i < vertices.size;i ++){
			Vector2 current = vertices.get(i);
			Vector2 next = (i == vertices.size - 1 ? ((drawing && canvas == this.canvas) ? null : vertices.get(0)) : vertices.get(i + 1));
			if(next != null) shape.line(current.cpy().add(centerx(), centery()), next.cpy().add(centerx(), centery()));

		}

		if(drawing && vertices.size != 0 && canvas == this.canvas) shape.line(vertices.peek().cpy().add(centerx(), centery()), mouseVector().scl( (mirror ? -1 : 1), 1f).add(centerx(), centery()));

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

		if((selected != null || vertice != null )&& !drawing && canvas == this.canvas) shape.circle(centerx() + selected.x, centery() + selected.y, grabrange);

		shape.set(ShapeType.Filled);
		if(canvas == this.canvas)
		for(int i = 0;i < vertices.size;i ++){
			Vector2 current = vertices.get(i);
			if(current == vertice){
				shape.setColor(Color.YELLOW);
			}else{
				shape.setColor(nodeColor);
			}
			shape.circle(centerx()+ current.x, centery() + current.y, 10);
		}
		
		shape.setColor(Color.CYAN);
		if(drawing && canvas == this.canvas) shape.circle(centerx() + mouseVector().x * (mirror ? -1 : 1), centery() + mouseVector().y, 10);

	}
	
	Array<Vector2> mirror(Array<Vector2> vertices){
		Array<Vector2> copy = new Array<Vector2>();
		for(Vector2 vector : vertices){
			copy.add(vector.cpy().scl(-1,1));
		}
		return copy;
	}

	void finishDrawMode(){
		if(!canvas.symmetry) return;
		Array<Vector2> mirror = mirror(canvas.vertices);
		for(int i= mirror.size-1; i >= 0 ; i --){
			canvas.vertices.add(mirror.get(i));
		}
	}

	Vector2 mouseVector(){
		return new Vector2(Gdx.input.getX() - (centerx()), (Gdx.graphics.getHeight() - Gdx.input.getY()) -(centery()));
	}

	Vector2 selectedVertice(){
		for(Vector2 vector : canvas.vertices){
			if(vector.dst(Gdx.input.getX() - centerx(), (Gdx.graphics.getHeight() - Gdx.input.getY()) - centery()) < grabrange){
				return vector;
			}
		}
		return null;
	}

	void input(){
		if(vertice != null){
			vertice.set(Gdx.input.getX() -  centerx(), (Gdx.graphics.getHeight() - Gdx.input.getY()) -centery());
		}
		
		if(Gdx.input.isButtonPressed(Buttons.LEFT) && (Gdx.input.getX() < Gdx.graphics.getWidth()-130 || Gdx.input.getY() > 30)){
			editor.stage.setKeyboardFocus(null);
		}
		float speed  = 6f;
		float offsetx=0, offsety=0;
		if(Gdx.input.isKeyPressed(Keys.W))offsety += speed;
		if(Gdx.input.isKeyPressed(Keys.D))offsetx += speed;
		if(Gdx.input.isKeyPressed(Keys.S))offsety -= speed;
		if(Gdx.input.isKeyPressed(Keys.A))offsetx -= speed;
		
		if(Gdx.input.isKeyPressed(VertexInput.alt_key)){
			//for(VertexCanvas canvas : canvases)
			//	canvas.translate(offsetx, offsety);
			this.offsetx += offsetx;
			this.offsety += offsety;
		}else{
			canvas.translate(offsetx, offsety);
		}
	}

	public void init(){
		//Gdx.graphics.set
		VertexCanvas.gui = this;
		editor = tester.getModule(VertexEditor.class);
		shape = new ShapeRenderer();

		field = new TextField("canvas", editor.skin);
		field.setWidth(130);
		field.setTextFieldListener(new TextFieldListener(){
			@Override
			public void keyTyped (TextField textField, char c){
				canvas.name = field.getText();
				canvas.button.setText(canvas.name);
			}
		});
		
		align(field, Align.topRight, 1f, 1f, 0, 0);
		
		box = new SelectBox<Color>(editor.skin);
		box.setSize(field.getWidth(), field.getHeight());
		box.setItems(Colors.getColors().values().toArray());
		box.setSelectedIndex(0);
		
		box.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor){
				canvas.color = box.getSelected();
			}
		}); 
		
		align(box, Align.topRight, 1f, 1f, 0, -field.getHeight());
		
	

		symmetry = new TextButton("Symmetry", editor.skin, "toggle");
		symmetry.setSize(field.getWidth(), 30);
		symmetry.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				if(drawMode)canvas.activateSymmetry();
			}
		});

		align(symmetry, Align.topRight, 1f, 1f, 0, -field.getHeight()*2);
		
		overwrite = new TextButton("Draw Mode", editor.skin, "toggle");
		overwrite.setSize(field.getWidth(), 30);
		overwrite.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				drawMode = !drawMode;
			}
		});

		align(overwrite, Align.topRight, 1f, 1f, 0, -field.getHeight() * 3);

		
		clear = new TextButton("Clear", editor.skin);
		clear.setSize(field.getWidth(), 30);
		clear.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				if(drawMode)canvas.clear();
			}
		});

		align(clear, Align.topRight, 1f, 1f, 0, -field.getHeight() * 4);
		
		

		Button button = new TextButton("Export", editor.skin);
		button.setSize(field.getWidth(), 30);

		align(button, Align.topRight, 1f, 1f, 0, -field.getHeight() * 5);

		Button button2 = new TextButton("Import", editor.skin);
		button2.setSize(field.getWidth(), 30);

		align(button2, Align.topRight, 1f, 1f, 0, -field.getHeight() * 6);
		
		add = new TextButton("New Canvas", editor.skin);
		add.setSize(100, 30);
		add.align(Align.topLeft);
		add.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				addCanvas("canvas" + (canvases.size+1));
			}
		});
		
		editor.stage.addActor(add);
		
		canvases.add(canvas);
		
		addCanvas("canvas2");
	}
	
	public void addCanvas(String name){
		VertexCanvas canvas = new VertexCanvas(name, canvases.size);
		canvases.add(canvas);
	}

	ActorAlign align(Actor actor, int align, float wscl, float hscl, float xoffset, float yoffset){
		return new ActorAlign(actor, align, wscl, hscl, xoffset, yoffset);
	}

	@Override
	public void resize(int width, int height){
		shape.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
		shape.updateMatrices();
	}

}
