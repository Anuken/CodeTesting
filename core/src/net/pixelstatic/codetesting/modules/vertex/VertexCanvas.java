package net.pixelstatic.codetesting.modules.vertex;

import javax.swing.JOptionPane;

import net.pixelstatic.codetesting.entities.Entity;
import net.pixelstatic.codetesting.modules.generator2.TreeGenerator.Material;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public class VertexCanvas{
	static VertexGUI gui;
	int index;
	PolygonType type = PolygonType.polygon;
	TextButton button;
	Material material = Material.bone;
	String name;
	ActorAlign align;
	Array<Vector2> vertices = new Array<Vector2>();
	boolean symmetry;

	public enum PolygonType{
		polygon, line
	}

	public VertexCanvas(String name, int index){
		this.index = index;
		this.name = name;
	}

	void activateSymmetry(){
		symmetry = !symmetry;
		if(symmetry) clear();
	}

	/*
	public Array<Vector2> normalize(){
		Array<Vector2> vertices = new Array<Vector2>();
		for(Vector2 vector : this.vertices) vertices.add(vector.cpy());
		
		float max = 0;
		for(Vector2 vertice : vertices){
			if(vertice.x > max) max = vertice.x;
			if(vertice.y > max) max = vertice.y;
		}
		
		for(Vector2 vertice : vertices)
			vertice.scl(1f/max);
		return vertices;
	}
	*/

	public void scale(float amount){
		Vector2 avg = Vector2.Zero.cpy();
		for(Vector2 vertice : vertices){
			if(avg.isZero()){
				avg = vertice.cpy();
			}else{
				avg.add(vertice);
			}
		}

		avg.scl(1f / vertices.size);

		for(Vector2 vertice : vertices)
			vertice.sub(avg);

		for(Vector2 vertice : vertices){
			vertice.scl(amount);
			vertice.add(avg);
		}

	}

	public void translate(float x, float y){
		for(Vector2 vertice : vertices)
			vertice.add(x, y);
	}

	//public void

	public void update(VertexCanvas selected){
		if(button == null){
			button = new TextButton(name, Entity.tester.getModule(VertexEditor.class).skin, "toggle");
			button.setSize(100, 30);
			align = new ActorAlign(button, Align.topLeft, 0, 1, 0, -index * button.getHeight());
			VertexCanvas canvas = this;
			button.addListener(new ClickListener(){
				public void clicked(InputEvent event, float x, float y){
					gui.canvas = canvas;
					updateBoxes();
				}
			});

		}
		align.set(Align.topLeft, 0, 1, 0, -index * button.getHeight());
		button.setChecked(selected == this);
	}
	
	public void smooth(){
		if(vertices.size > 100){
			JOptionPane.showMessageDialog(null, "calm down m8", "pls", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		Vector2[] array = new Vector2[vertices.size*2 - (type == PolygonType.line ? 1 : 0)];
		for(int i = 0; i < vertices.size; i ++){
			array[i*2] = vertices.get(i);
			Vector2 next = (i == vertices.size - 1 ? vertices.first() : vertices.get(i+1));
			if(!(type == PolygonType.line && i == vertices.size-1))array[i*2+1] = next.cpy().add(vertices.get(i)).scl(0.5f);
		}
		
		for(int i = 0; i < array.length; i += 2){
			if(type == PolygonType.line && i == array.length-1) break;
			
			Vector2 next = (i == array.length -1 ? array [0] : array[i+1]);
			
			Vector2 last = (i == 0 ? array [array.length-1] : array[i-1]);
			
			if(!((i == 0 || i == array.length -1) && type == PolygonType.line))
			array[i] = next.cpy().add(last).scl(0.5f);
		}
		
		vertices = new Array<Vector2>(array);
	}

	public void updateBoxes(){
		gui.field.setText(name);
		gui.box.setSelected(material);
		gui.typebox.setSelected(type);
	}

	public void delete(){
		button.remove();
		ActorAlign.removeAlign(align);
	}

	public void clear(){
		vertices.clear();
	}
}
