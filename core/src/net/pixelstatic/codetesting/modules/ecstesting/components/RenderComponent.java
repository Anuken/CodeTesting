package net.pixelstatic.codetesting.modules.ecstesting.components;

import net.pixelstatic.codetesting.modules.ecstesting.Entity;
import net.pixelstatic.codetesting.modules.ecstesting.EntityTester;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

public class RenderComponent implements Component{
	float x, y;
	
	public void update(Entity entity, EntityTester tester){
		tester.batch.draw(tester.atlas.findRegion("repulsemagnet"), (float)entity.getValue("x"), (float)entity.getValue("y"));
		entity.incrementValue("x", MathUtils.sin((float)entity.getValue("y")/10f));
		entity.incrementValue("y", MathUtils.sin((float)entity.getValue("x")/10f));
	}
	
	public void init(Entity entity){
		entity.addValue("x", (float)MathUtils.random(Gdx.graphics.getWidth()));
		entity.addValue("y",  (float)MathUtils.random(Gdx.graphics.getHeight()));
		entity.addValue("speed", MathUtils.random());
	}
}
