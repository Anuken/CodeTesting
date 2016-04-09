package net.pixelstatic.codetesting.modules.vertex;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import net.pixelstatic.codetesting.modules.Module;
import net.pixelstatic.codetesting.modules.generator2.Material;
import net.pixelstatic.codetesting.modules.vertex.VertexObject.PolygonType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class VertexGUI extends Module{
	VertexEditor editor;
	Skin skin;
	Stage stage;
	Table table;
	Dialog infodialog;
	TextButton symmetry, overwrite, add, clear, delete, smooth;
	SelectBox<Material> box;
	SelectBox<PolygonType> typebox;
	Texture colorbox;
	TextField field;
	float uiwidth = 130, uiheight = 30;

	@Override
	public void update(){
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)) Gdx.app.exit();
		updateButtons();
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	public void updateButtons(){
		boolean drawMode = editor.drawMode;
		symmetry.setChecked(editor.selectedCanvas.symmetry);
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

	public void init(){
		InputMultiplexer plex = new InputMultiplexer();

		plex.addProcessor(new VertexInput(tester.getModule(VertexEditor.class)));
		plex.addProcessor(stage);
		Gdx.input.setInputProcessor(plex);
	}

	public VertexGUI(){
		skin = new Skin(Gdx.files.internal("gui/uiskin.json"));
		stage = new Stage();
		stage.setViewport(new ScreenViewport());
		ActorAlign.stage = stage;
		setupGUI();
	}

	public void setupGUI(){
		table = new Table();
		table.setFillParent(true);
		stage.addActor(table);
		float width = uiwidth, height = uiheight;

		field = new TextField("canvas", skin);
		field.setSize(width, height);
		field.setTouchable(Touchable.disabled);
		field.setTextFieldListener(new TextFieldListener(){
			@Override
			public void keyTyped(TextField textField, char c){
				editor.selectedCanvas.name = field.getText();
				editor.selectedCanvas.button.setText(editor.selectedCanvas.name);
			}
		});
		table.top().right();
		table.add(field).size(width, height);

		box = new SelectBox<Material>(skin);
		box.setItems(Material.values());
		box.setSelectedIndex(0);
		box.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor){
				editor.selectedCanvas.list.material = box.getSelected();
			}
		});

		table.row().top().right();
		table.add(box).size(width, height);

		typebox = new SelectBox<PolygonType>(skin);
		typebox.setItems(PolygonType.values());
		typebox.setSelectedIndex(0);

		typebox.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor){
				editor.selectedCanvas.list.type = typebox.getSelected();
			}
		});

		table.row().top().right();
		table.add(typebox).size(width, height);

		symmetry = new TextButton("Symmetry", skin, "toggle");
		symmetry.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				if(editor.drawMode){
					editor.selectedCanvas.symmetry = !editor.selectedCanvas.symmetry;
					if(editor.selectedCanvas.symmetry) editor.selectedCanvas.clear();
				}
			}
		});

		table.row().top().right();
		table.add(symmetry).size(width, height);

		overwrite = new TextButton("Draw Mode", skin, "toggle");
		overwrite.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				editor.drawMode = !editor.drawMode;
			}
		});

		table.row().top().right();
		table.add(overwrite).size(width, height);

		smooth = new TextButton("Smooth", skin);
		smooth.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				if( !editor.selectedCanvas.list.smooth()) showInfo("Calm down m8");
			}
		});

		table.row().top().right();
		table.add(smooth).size(width, height);

		clear = new TextButton("Clear", skin);
		clear.setSize(field.getWidth(), 30);
		clear.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				if(editor.drawMode) editor.selectedCanvas.clear();
			}
		});

		table.row().top().right();
		table.add(clear).size(width, height);

		delete = new TextButton("Delete Canvas", skin);
		delete.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				/*
				if(editor.drawMode && editor.canvases.size > 1){
					editor.selectedCanvas.delete();
					editor.canvases.removeValue(editor.selectedCanvas, true);
					editor.selectedCanvas = editor.canvases.get(0);
					editor.fixCanvases();
				}
				*/
			}
		});

		table.row().top().right();
		table.add(delete).size(width, height);

		Button exportbutton = new TextButton("Export", skin);
		exportbutton.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				JFileChooser chooser = new JFileChooser();
				int option = chooser.showSaveDialog(null);
				if(option == JFileChooser.APPROVE_OPTION){
					try{
						VertexLoader.write(new VertexObject(editor.canvases), Gdx.files.absolute(chooser.getSelectedFile().getAbsolutePath()));
					}catch(Exception e){
						JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
				}
			}
		});

		table.row().top().right();
		table.add(exportbutton).size(width, height);

		Button importbutton = new TextButton("Import", skin);
		importbutton.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				JFileChooser chooser = new JFileChooser();
				int option = chooser.showOpenDialog(null);
				if(option == JFileChooser.APPROVE_OPTION){
					try{
						VertexObject object = VertexLoader.read(Gdx.files.absolute(chooser.getSelectedFile().getAbsolutePath()));
						editor.loadObject(object);
					}catch(Exception e){
						JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
				}
			}
		});

		table.row().top().right();
		table.add(importbutton).size(width, height);

		add = new TextButton("New Canvas", skin);
		add.align(Align.topLeft);
		add.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				//	addCanvas("canvas" + (editor.canvases.size + 1));
			}
		});

		text("Segments: 10").setName("segmenttext");;

		Slider segmentslider = new Slider(1, 40, 1, false, skin);
		segmentslider.setValue(10);
		segmentslider.addListener(new ChangeListener(){
			public void changed(ChangeEvent event, Actor actor){
				int value = (int)segmentslider.getValue();
				((Label)table.findActor("segmenttext")).setText("Segments: " + value);
				editor.tree.getVertexGenerator().segments = value;
			}
		});

		add(segmentslider);
		text("Compactness: 0.8").setName("compacttext");;

		Slider compactslider = new Slider(0.1f, 2f, 0.05f, false, skin);
		compactslider.setValue(0.8f);
		compactslider.addListener(new ChangeListener(){
			public void changed(ChangeEvent event, Actor actor){
				float value = (compactslider.getValue());
				((Label)table.findActor("compacttext")).setText("Compactness: " + (value + "").substring(0, 3));
				editor.tree.getVertexGenerator().segmentCompactness = value;
			}
		});

		add(compactslider);

		text("Rotation: 0").setName("rotationtext");;

		Slider rotationslider = new Slider(-10, 10, 0.1f, false, skin);
		rotationslider.setValue(0f);
		rotationslider.addListener(new ChangeListener(){
			public void changed(ChangeEvent event, Actor actor){
				float value = (rotationslider.getValue());
				((Label)table.findActor("rotationtext")).setText("rotation: " + (value + "").substring(0, 3));
				editor.tree.getVertexGenerator().segmentRotation = value;
			}
		});

		add(rotationslider);
		
		text("Scale: 1/1000").setName("scaletext");;

		Slider scaleslider = new Slider(1, 3000, 10f, false, skin);
		scaleslider.setValue(1000);
		scaleslider.addListener(new ChangeListener(){
			public void changed(ChangeEvent event, Actor actor){
				float value = (scaleslider.getValue());
				((Label)table.findActor("scaletext")).setText("Scale: 1/" + (int)value);
				editor.tree.setCanvasScale(1f/value);
			}
		});

		add(scaleslider);
		
		CheckBox scalebox = new CheckBox("Autoscale", skin);
		scalebox.setChecked(true);
		scalebox.addListener(new ChangeListener(){
			public void changed(ChangeEvent event, Actor actor){
				if(editor != null)editor.tree.setAutoScale(scalebox.isChecked());
				scaleslider.setTouchable(scalebox.isChecked() ? Touchable.disabled : Touchable.enabled);
				scaleslider.setColor(scalebox.isChecked() ? Color.GRAY : Color.WHITE);
			}
		});
		scalebox.fire(new ChangeListener.ChangeEvent());
		
		add(scalebox);

		infodialog = new Dialog("Info", skin, "dialog").text("").button("Ok", true).key(Keys.ENTER, true).key(Keys.ESCAPE, false);

	}

	void add(Actor actor){
		table.row().top().right();
		table.add(actor).size(uiwidth, uiheight);

	}

	Label text(String text){
		Label label = new Label(text, skin);
		table.row().top().right();
		table.add(label).align(Align.center);
		return label;
	}

	public void showInfo(String info){
		((Label)infodialog.getContentTable().getChildren().get(0)).setText(info);
		infodialog.show(stage);
	}

	public void resize(int width, int height){
		stage.getViewport().update(width, height, true);
	}
}
