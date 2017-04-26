package io.anuke.codetesting.modules.testing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;

import io.anuke.scene.SceneModule;
import io.anuke.scene.builders.*;
import io.anuke.scene.style.Styles;
import io.anuke.ucore.core.UInput;

public class TestModule10 extends SceneModule<TestModule9>{
	
	public TestModule10(){
		loadStyles(new Styles(Gdx.files.internal("vui/uiskin.json")));
		
		//begin scene building with a specified Scene object
		//you can call this with a Table instead of a Scene to begin building inside that table
		SceneBuild.begin(scene);
		
		//this creates a new table within the Scene, adds it as a child and sets fillParent to true
		new table(){{
			
			//align the table contents left
			aleft();
			
			//add a label
			new label("text to the side");
			
			row();
			
			//add a button with a listener function
			new button("a button", ()->{
				//this adds a new row of text to the table each time it's clicked
				row();
				get().add("line of text");
			});
			
			row();
			
			//create an imagebutton, set its image size to be 40
			new imagebutton("icon-arrow-right").imageSize(40);
			
			row();
			
			//create a nested table
			new table(){{
				//grow the table's cell in the parent table
				grow();
				
				aright();
				atop();
				
				new label("some text").padLeft(60);
				
				row();
				
				new label("new row text");
				
				//constructor a checkbox
				checkbox box = new checkbox("check me");
				
				//add a change listener
				box.changed(checked->{
					//calling get() here returns the actual CheckBox object in the scene
					box.get().setText(checked ? "now checked " : "now unchecked");
				});
				
			}};
			
			new table(){{
				grow();
				
				new label("some more text goes here");
			}};
			
		}};
		
		//end layout building
		SceneBuild.end();
		
		scene.setDebugAll(true);
		/*
		Table table = fill();
		
		table.top().left().addButton("Wow!", ()->{
			Label text = new Label("Text!");
			
			table.addCheck("Check me.", b->{
				text.setText(b ? "Checked!" : "Unchecked!");
			});
			table.add(text).minWidth(100).padLeft(10f).row();
		}).left().colspan(2).row();
		
		fill().addButton("Open a window.", ()->{
			Dialog d = new Dialog("Wow!");
			d.text("Some text!");
			d.show(scene);
		}).size(150, 60);
		
		fill().top().right().addField("", s->{
			log("Textfield changed: " + s);
		});
		*/
	}
	
	@Override
	public void update(){
		clearScreen(Color.BLACK);
		
		if(UInput.keyDown(Keys.ESCAPE))
			Gdx.app.exit();
		
		act();
	}
}
