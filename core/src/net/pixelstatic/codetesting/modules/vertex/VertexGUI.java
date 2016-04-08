package net.pixelstatic.codetesting.modules.vertex;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import net.pixelstatic.codetesting.modules.Module;
import net.pixelstatic.codetesting.modules.generator2.GeneratorRenderer.Material;
import net.pixelstatic.codetesting.modules.generator2.TreeGenerator;
import net.pixelstatic.codetesting.modules.vertex.VertexObject.PolygonType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
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
	Dialog infodialog;
	Array<VertexCanvas> canvases = new Array<VertexCanvas>();
	VertexCanvas selectedCanvas;
	Vector2 vertice;
	VertexEditor editor;
	TextButton symmetry, overwrite, add, clear, delete, smooth;
	SelectBox<Material> box;
	SelectBox<PolygonType> typebox;
	TextField field;
	TreeGenerator tree;
	boolean drawing, drawMode = true;
	float offsetx, offsety, treeScale = 4f;

	@Override
	public void update(){
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		updateCanvases();
		input();
		shape.begin(ShapeType.Line);
		draw();
		shape.end();
		drawTree();
		ActorAlign.updateAll();
	}

	void updateCanvases(){
		for(VertexCanvas canvas : canvases)
			canvas.update(this.selectedCanvas);
		symmetry.setChecked(selectedCanvas.symmetry);
		overwrite.setChecked(drawMode);
		overwrite.setText(drawMode ? "Draw Mode" : "Edit Mode");
		clear.setColor( !drawMode ? Color.LIGHT_GRAY : Color.WHITE);
		clear.setTouchable( !drawMode ? Touchable.disabled : Touchable.enabled);
		symmetry.setColor( !drawMode ? Color.LIGHT_GRAY : Color.WHITE);
		symmetry.setTouchable( !drawMode ? Touchable.disabled : Touchable.enabled);
		delete.setColor( !drawMode ? Color.LIGHT_GRAY : Color.WHITE);
		delete.setTouchable( !drawMode ? Touchable.disabled : Touchable.enabled);
		smooth.setColor( !drawMode ? Color.LIGHT_GRAY : Color.WHITE);
		smooth.setTouchable( !drawMode ? Touchable.disabled : Touchable.enabled);

	}

	void drawTree(){
		editor.stage.getBatch().setColor(Color.WHITE);
		editor.stage.getBatch().begin();
		editor.stage.getBatch().draw(tree.getTexture(), 0, 0, tree.getTexture().getWidth() * treeScale, tree.getTexture().getHeight() * treeScale);
		editor.stage.getBatch().end();
	}

	void draw(){
		//draw center of coords
		shape.set(ShapeType.Line);
		shape.setColor(Color.BLUE);
		shape.line(Gdx.graphics.getWidth() / 2 + offsetx, 0, Gdx.graphics.getWidth() / 2 + offsetx, Gdx.graphics.getHeight());
		shape.line(0, Gdx.graphics.getHeight() / 2 + offsety, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2 + offsety);

		for(VertexCanvas canvas : canvases){
			if(canvas == this.selectedCanvas) continue;
			drawVertices(canvas, canvas.vertices(), false);
		}

		drawVertices(selectedCanvas, selectedCanvas.vertices(), false);
		if(selectedCanvas.symmetry && drawing) drawVertices(selectedCanvas, mirror(selectedCanvas.vertices()), true);

		add.setPosition(0, Gdx.graphics.getHeight() - add.getHeight() * (canvases.size + 1));

		shape.set(ShapeType.Line);
		shape.setColor(Color.MAGENTA);
		if(selectedCanvas.symmetry && drawing) shape.line(centerx(), 0, centerx(), Gdx.graphics.getHeight());

		float lineoffset = 1f;
		shape.setColor(Color.RED);
		shape.rect(lineoffset, lineoffset, tree.width * treeScale, tree.height * treeScale);
	}

	float centerx(){
		return Gdx.graphics.getWidth() / 2 + offsetx;
	}

	float centery(){
		return Gdx.graphics.getHeight() / 2 + offsety;
	}

	void drawVertices(VertexCanvas canvas, Array<Vector2> vertices, boolean mirror){
		shape.set(ShapeType.Line);
		shape.setColor(canvas.list.material.getColor());
		Gdx.gl.glLineWidth(4);
		shape.setAutoShapeType(true);
		for(int i = 0;i < vertices.size;i ++){
			Vector2 current = vertices.get(i);
			Vector2 next = (i == vertices.size - 1 ? (((drawing && canvas == this.selectedCanvas) || canvas.list.type == PolygonType.line) ? null : vertices.get(0)) : vertices.get(i + 1));
			if(next != null) shape.line(current.cpy().add(centerx(), centery()), next.cpy().add(centerx(), centery()));

		}

		if(drawing && vertices.size != 0 && canvas == this.selectedCanvas) shape.line(vertices.peek().cpy().add(centerx(), centery()), mouseVector().scl((mirror ? -1 : 1), 1f).add(centerx(), centery()));

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

		if((selected != null || vertice != null) && !drawing && canvas == this.selectedCanvas) shape.circle(centerx() + selected.x, centery() + selected.y, grabrange);

		shape.set(ShapeType.Filled);
		if(canvas == this.selectedCanvas) for(int i = 0;i < vertices.size;i ++){
			Vector2 current = vertices.get(i);
			if(current == vertice){
				shape.setColor(Color.YELLOW);
			}else{
				shape.setColor(nodeColor);
			}
			shape.circle(centerx() + current.x, centery() + current.y, 10);
		}

		shape.setColor(Color.CYAN);
		if(drawing && canvas == this.selectedCanvas) shape.circle(centerx() + mouseVector().x * (mirror ? -1 : 1), centery() + mouseVector().y, 10);

	}

	Array<Vector2> mirror(Array<Vector2> vertices){
		Array<Vector2> copy = new Array<Vector2>();
		for(Vector2 vector : vertices){
			copy.add(vector.cpy().scl( -1, 1));
		}
		return copy;
	}

	void finishDrawMode(){
		if( !selectedCanvas.symmetry) return;
		selectedCanvas.list.mirrorVertices();
	}

	Vector2 mouseVector(){
		return new Vector2(Gdx.input.getX() - (centerx()), (Gdx.graphics.getHeight() - Gdx.input.getY()) - (centery()));
	}

	Vector2 selectedVertice(){
		for(Vector2 vector : selectedCanvas.vertices()){
			if(vector.dst(Gdx.input.getX() - centerx(), (Gdx.graphics.getHeight() - Gdx.input.getY()) - centery()) < grabrange){
				return vector;
			}
		}
		return null;
	}

	void input(){
		if(vertice != null){
			vertice.set(Gdx.input.getX() - centerx(), (Gdx.graphics.getHeight() - Gdx.input.getY()) - centery());
		}

		if(Gdx.input.isButtonPressed(Buttons.LEFT) && (Gdx.input.getX() < Gdx.graphics.getWidth() - 130 || Gdx.input.getY() > 30)){
			//	editor.stage.setKeyboardFocus(null);
		}
		if(editor.stage.getKeyboardFocus() != null) return;
		float speed = 6f;
		float offsetx = 0, offsety = 0;

		if(Gdx.input.isKeyJustPressed(Keys.R)){
			int minvertices = Integer.MAX_VALUE;
			for(VertexCanvas canvas : canvases)
				minvertices = Math.min(canvas.list.vertices.size, minvertices);
			
			if(minvertices >= 3){
				tree.setVertexObject(new VertexObject(canvases));
				tree.generate();
			}else{
				showInfo("Each polygon must have at least 3 vertices\nfor the tree to generate!");
			}
		}

		if(Gdx.input.isKeyPressed(Keys.W)) offsety += speed;
		if(Gdx.input.isKeyPressed(Keys.D)) offsetx += speed;
		if(Gdx.input.isKeyPressed(Keys.S)) offsety -= speed;
		if(Gdx.input.isKeyPressed(Keys.A)) offsetx -= speed;

		if( !Gdx.input.isKeyPressed(VertexInput.alt_key)){
			this.offsetx -= offsetx;
			this.offsety -= offsety;
		}else{
			selectedCanvas.list.translate(offsetx, offsety);
		}
		if(Gdx.input.isKeyJustPressed(Keys.SPACE)) drawMode = !drawMode;
	}

	public void init(){
		VertexCanvas.gui = this;
		editor = tester.getModule(VertexEditor.class);
		shape = new ShapeRenderer();
		tree = new TreeGenerator(VertexLoader.read(Gdx.files.internal("vertexobjects/pinetreepart.vto")));
		tree.generate();

		field = new TextField("canvas", editor.skin);
		field.setWidth(130);
		field.setTextFieldListener(new TextFieldListener(){
			@Override
			public void keyTyped(TextField textField, char c){
				selectedCanvas.name = field.getText();
				selectedCanvas.button.setText(selectedCanvas.name);
			}
		});

		align(field, Align.topRight, 1f, 1f, 0, 0);

		box = new SelectBox<Material>(editor.skin);
		box.setSize(field.getWidth(), field.getHeight());
		box.setItems(Material.values());
		box.setSelectedIndex(0);

		box.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor){
				selectedCanvas.list.material = box.getSelected();
			}
		});

		align(box, Align.topRight, 1f, 1f, 0, -field.getHeight());

		typebox = new SelectBox<PolygonType>(editor.skin);
		typebox.setSize(field.getWidth(), field.getHeight());
		typebox.setItems(PolygonType.values());
		typebox.setSelectedIndex(0);

		typebox.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor){
				selectedCanvas.list.type = typebox.getSelected();
			}
		});

		align(typebox, Align.topRight, 1f, 1f, 0, -field.getHeight() * 2);

		symmetry = new TextButton("Symmetry", editor.skin, "toggle");
		symmetry.setSize(field.getWidth(), 30);
		symmetry.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				if(drawMode){
					selectedCanvas.symmetry = !selectedCanvas.symmetry;
					if(selectedCanvas.symmetry) selectedCanvas.clear();
				}
			}
		});

		align(symmetry, Align.topRight, 1f, 1f, 0, -field.getHeight() * 3);

		overwrite = new TextButton("Draw Mode", editor.skin, "toggle");
		overwrite.setSize(field.getWidth(), 30);
		overwrite.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				drawMode = !drawMode;
			}
		});

		align(overwrite, Align.topRight, 1f, 1f, 0, -field.getHeight() * 4);

		smooth = new TextButton("Smooth", editor.skin);
		smooth.setSize(field.getWidth(), 30);
		smooth.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				if(!selectedCanvas.list.smooth())showInfo("Calm down m8");
			}
		});

		align(smooth, Align.topRight, 1f, 1f, 0, -field.getHeight() * 5);

		clear = new TextButton("Clear", editor.skin);
		clear.setSize(field.getWidth(), 30);
		clear.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				if(drawMode) selectedCanvas.clear();
			}
		});

		align(clear, Align.topRight, 1f, 1f, 0, -field.getHeight() * 6);

		delete = new TextButton("Delete Canvas", editor.skin);
		delete.setSize(field.getWidth(), 30);
		delete.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				if(drawMode && canvases.size > 1){
					selectedCanvas.delete();
					canvases.removeValue(selectedCanvas, true);
					selectedCanvas = canvases.get(0);
					fixCanvases();
				}
			}
		});

		align(delete, Align.topRight, 1f, 1f, 0, -field.getHeight() * 7);

		Button button = new TextButton("Export", editor.skin);
		button.setSize(field.getWidth(), 30);
		button.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				JFileChooser chooser = new JFileChooser();
				int option = chooser.showSaveDialog(null);
				if(option == JFileChooser.APPROVE_OPTION){
					try{
						VertexLoader.write(new VertexObject(canvases), Gdx.files.absolute(chooser.getSelectedFile().getAbsolutePath()));
					}catch(Exception e){
						JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
				}
			}
		});

		align(button, Align.topRight, 1f, 1f, 0, -field.getHeight() * 8);

		Button button2 = new TextButton("Import", editor.skin);
		button2.setSize(field.getWidth(), 30);
		button2.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				JFileChooser chooser = new JFileChooser();
				int option = chooser.showOpenDialog(null);
				if(option == JFileChooser.APPROVE_OPTION){
					try{
						VertexObject object = VertexLoader.read(Gdx.files.absolute(chooser.getSelectedFile().getAbsolutePath()));
						loadObject(object);
					}catch(Exception e){
						JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
				}
			}
		});

		align(button2, Align.topRight, 1f, 1f, 0, -field.getHeight() * 9);

		add = new TextButton("New Canvas", editor.skin);
		add.setSize(100, 30);
		add.align(Align.topLeft);
		add.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				addCanvas("canvas" + (canvases.size + 1));
			}
		});

		editor.stage.addActor(add);
		
		infodialog = new Dialog("Info", editor.skin, "dialog").text("").button("Ok", true).key(Keys.ENTER, true).key(Keys.ESCAPE, false);

		selectedCanvas = new VertexCanvas("leafsegment", 0);
		selectedCanvas.list.material = Material.leaves;
		canvases.add(selectedCanvas);
		selectedCanvas.updateBoxes();

		VertexCanvas trunk = addCanvas("trunk");
		trunk.list.material = Material.wood;
	}

	void loadObject(VertexObject object){
		for(VertexCanvas canvas : canvases)
			canvas.delete();
		canvases.clear();
		for(String string : object.lists.keys()){
			VertexCanvas canvas = addCanvas(string);
			canvas.list.vertices = object.lists.get(string).vertices;
			canvas.list.material = object.lists.get(string).flagMaterial();
			canvas.list.type = object.lists.get(string).type;
		}
		selectedCanvas = canvases.first();
		selectedCanvas.updateBoxes();
	}

	void fixCanvases(){
		for(int i = 0;i < canvases.size;i ++){
			VertexCanvas canvas = canvases.get(i);
			canvas.index = i;
			canvas.update(canvas);
		}
	}

	public VertexCanvas addCanvas(String name){
		VertexCanvas canvas = new VertexCanvas(name, canvases.size);
		canvases.add(canvas);
		return canvas;
	}

	ActorAlign align(Actor actor, int align, float wscl, float hscl, float xoffset, float yoffset){
		return new ActorAlign(actor, align, wscl, hscl, xoffset, yoffset);
	}

	public void showInfo(String info){
		((Label)infodialog.getContentTable().getChildren().get(0)).setText(info);
		infodialog.show(editor.stage);
	}

	@Override
	public void resize(int width, int height){
		shape.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
		shape.updateMatrices();
	}

}
