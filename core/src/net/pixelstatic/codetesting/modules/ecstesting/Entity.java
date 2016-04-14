package net.pixelstatic.codetesting.modules.ecstesting;

public class Entity{
	private static long nextid;
	private long id;
	
	public Entity(){
		id = nextid++;
	}
	
	public long getID(){
		return id;
	}
}
