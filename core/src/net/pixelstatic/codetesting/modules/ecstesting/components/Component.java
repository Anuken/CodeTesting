package net.pixelstatic.codetesting.modules.ecstesting.components;

import net.pixelstatic.codetesting.entities.Entity;
import net.pixelstatic.codetesting.modules.ecstesting.EntityTester;

public interface Component{
	void update(EntityTester tester);
	void init(Entity entity);
}
