package io.anuke.codetesting.examplesetup;

import io.anuke.ucore.modules.SceneModule;
import io.anuke.ucore.scene.builders.build;
import io.anuke.ucore.scene.builders.button;
import io.anuke.ucore.scene.builders.table;
import io.anuke.ucore.scene.style.Styles;
import io.anuke.ucore.scene.ui.KeybindDialog;

public class ExampleUI extends SceneModule<ExampleMain>{
	
	public ExampleUI(){
		Styles.styles.font().setUseIntegerPositions(false);
	}
	
	@Override
	public void init(){
		build.begin(scene);
		new table(){{
			aleft();
			atop();
			new button("Press me.", ()->{
				new KeybindDialog().show(scene);
			});
		}};
		build.end();
	}
}
