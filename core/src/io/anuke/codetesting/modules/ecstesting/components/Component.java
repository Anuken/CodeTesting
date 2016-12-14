package io.anuke.codetesting.modules.ecstesting.components;
import io.anuke.codetesting.modules.ecstesting.Entity;
import io.anuke.codetesting.modules.ecstesting.EntityTester;

public interface Component{
	void update(Entity entity, EntityTester tester);
	void init(Entity entity);
}
