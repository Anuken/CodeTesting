package net.pixelstatic.codetesting.modules.vertex;

import net.pixelstatic.codetesting.modules.Module;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.utils.Align;

public class VertexGUI extends Module{
	VertexEditor editor;

	@Override
	public void update(){
		ActorAlign.updateAll();
	}

	public void init(){
		editor = tester.getModule(VertexEditor.class);

		Button button = new Button(editor.skin);
		align(button, Align.top, 0.5f, 1f, 0, 0);
	}

	void align(Actor actor, int align, float wscl, float hscl, float xoffset, float yoffset){
		new ActorAlign(actor, align, wscl, hscl, xoffset, yoffset);
	}

}
