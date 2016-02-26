package net.pixelstatic.codetesting.entities;

public abstract class Effect extends Entity{
	int lifetime = 12;
	float life;

	@Override
	public final void Update(){
		life += delta();
		if(life > lifetime) RemoveSelf();
	}
}
