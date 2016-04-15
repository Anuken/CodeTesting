package net.pixelstatic.codetesting.modules.ecstesting.components;
import net.pixelstatic.codetesting.modules.ecstesting.Entity;
import net.pixelstatic.codetesting.modules.ecstesting.EntityTester;

public interface Component{
	void update(Entity entity, EntityTester tester);
	void init(Entity entity);
}
