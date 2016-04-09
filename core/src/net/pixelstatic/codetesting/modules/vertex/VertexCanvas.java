package net.pixelstatic.codetesting.modules.vertex;

import net.pixelstatic.codetesting.entities.Entity;
import net.pixelstatic.codetesting.modules.generator2.Material;
import net.pixelstatic.codetesting.modules.vertex.VertexObject.PolygonType;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public class VertexCanvas{
	int index;
	VertexList list;
	TextButton button;
	String name;
	ActorAlign align;
	boolean symmetry;

	public VertexCanvas(String name, int index){
		this.index = index;
		this.name = name;
		list = new VertexList(new Array<Vector2>(), PolygonType.polygon, Material.leaves);
	}
	
	public Array<Vector2> vertices(){
		return list.vertices;
	}

	public void update(VertexCanvas selected, VertexEditor editor, VertexGUI gui){
		if(button == null){
			button = new TextButton(name, Entity.tester.getModule(VertexGUI.class).skin, "toggle");
			button.setSize(100, 30);
			align = new ActorAlign(button, Align.topLeft, 0, 1, 0, -index * button.getHeight());
			VertexCanvas canvas = this;
			button.addListener(new ClickListener(){
				public void clicked(InputEvent event, float x, float y){
					editor.selectedCanvas = canvas;
					updateBoxes(gui);
				}
			});

		}
		align.set(Align.topLeft, 0, 1, 0, -index * button.getHeight());
		button.setChecked(selected == this);
	}

	public void updateBoxes(VertexGUI gui){
		gui.field.setText(name);
		gui.box.setSelected(list.material);
		gui.typebox.setSelected(list.type);
	}

	public void delete(){
		button.remove();
		ActorAlign.removeAlign(align);
	}

	public void clear(){
		list.vertices.clear();
	}
}
