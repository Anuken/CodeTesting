package net.pixelstatic.codetesting.modules.vertex;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class VertexInput implements InputProcessor{
	private VertexGUI gui;

	public VertexInput(VertexGUI gui){
		this.gui = gui;
	}

	@Override
	public boolean keyDown(int keycode){
		if(keycode == Keys.SHIFT_LEFT){
			gui.drawMode = true;
			gui.canvas.clear();
			return true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode){
		if(keycode == Keys.SHIFT_LEFT) if(gui.drawMode){
			gui.finishDrawMode();
			gui.drawMode = false;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character){
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button){
		if(button == Buttons.LEFT){
			if(gui.drawMode){
				gui.canvas.vertices.add(gui.mouseVector());
				return false;
			}

			Vector2 selected = gui.selectedVertice();
			if(selected != null){
				gui.vertice = selected;
				return true;
			}
		}else if(button == Buttons.RIGHT){
			if(gui.canvas.vertices.size > 3){
				gui.canvas.vertices.removeValue(gui.vertice, true);
				gui.vertice = null;
			}
			/*
			Vector2 closest = gui.closestVertice();
			gui.object.vectors.insert(gui.object.vectors.indexOf(closest, true) + closest.x < 0 ? 1 : 0, closest.cpy());
			gui.vertice = closest;
			if(!Gdx.input.isButtonPressed(Buttons.LEFT)){
				gui.input();

				gui.vertice = null;
			}
			*/
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button){
		if(button == Buttons.LEFT){

			gui.vertice = null;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer){
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY){
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount){
		gui.canvas.scale(amount > 0 ? 0.9f : 1.1f);

		return false;
	}

}
