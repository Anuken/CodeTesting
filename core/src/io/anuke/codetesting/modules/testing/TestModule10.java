package io.anuke.codetesting.modules.testing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import io.anuke.SceneModule;
import io.anuke.scene.style.Styles;
import io.anuke.scene.ui.Dialog;
import io.anuke.scene.ui.Label;
import io.anuke.scene.ui.layout.Table;
import io.anuke.ucore.core.UInput;

//import static io.anuke.engine.DrawContext.skin;
public class TestModule10 extends SceneModule<TestModule9>{
	
	public TestModule10(){
		loadStyles(new Styles(Gdx.files.internal("vui/uiskin.json")));
		
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
	}
	
	@Override
	public void update(){
		if(UInput.keyDown(Keys.ESCAPE))
			Gdx.app.exit();
		
		act();
	}
}
