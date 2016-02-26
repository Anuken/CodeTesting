package net.pixelstatic.codetesting.entities;

import java.util.concurrent.ConcurrentHashMap;

import net.pixelstatic.codetesting.CodeTester;
import net.pixelstatic.codetesting.modules.weaponphysics.WeaponPhysics;

import com.badlogic.gdx.Gdx;

public abstract class Entity{
    static private long lastid;
    public static ConcurrentHashMap<Long, Entity> entities = new ConcurrentHashMap<Long, Entity>();
    public static CodeTester tester;
    private long id;
    public float x,y;
    
    abstract public void Update();
    abstract public void Draw();
    
    public Entity setPosition(float x, float y){
    	this.x = x;
    	this.y = y;
    	return this;
    }
    
    public Entity setBlockPosition(int x, int y){
    	this.x = tester.getModule(WeaponPhysics.class).pixsize * x + tester.getModule(WeaponPhysics.class).pixsize/2;
    	this.y = tester.getModule(WeaponPhysics.class).pixsize * y + tester.getModule(WeaponPhysics.class).pixsize/2;
    	return this;
    }
    
    
    public Entity AddSelf(){
    	entities.put(id, this);
    	return this;
    }
    
    public void RemoveSelf(){
    	entities.remove(this.id);
    }
    
    public void resetID(long newid){
    	RemoveSelf();
    	this.id = newid;
    	AddSelf();
    	lastid = id+1;
    }
    
    public long GetID(){
    	return id;
    }
    
    public static Entity getEntity(long id){
    	return entities.get(id);
    }
    
    public static boolean entityExists(long id){
    	return entities.get(id) != null;
    }
    
    public void serverUpdate(){
    	//do nothing
    }
    
    public float delta(){
    	return Gdx.graphics.getDeltaTime() * 60f;
    }
    
    public Entity(){
    	id = lastid++;
    }
}
