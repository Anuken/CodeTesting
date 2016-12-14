package io.anuke.codetesting.modules.workbench;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.utils.Array;

import io.anuke.codetesting.modules.Module;
import io.anuke.ucore.UCore;
import io.anuke.ucore.graphics.Textures;

public class CraftRenderer extends Module{
	World world;
	Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	OrthographicCamera camera;
	float scale = 10f;
	float clickrad = 0.01f, toolrad = 3;
	//Body moving = null;
	Body ground;
	Vector3 mouse = new Vector3();
	float dragspeed = 100;
	private MouseJoint mouseJoint = null;
	SpriteBatch batch;
	Array<Body> bodies = new Array<Body>();
	float accumulator;

	public void init(){
		world = new World(new Vector2(0, 0), true);
		camera = new OrthographicCamera(Gdx.graphics.getWidth() / scale, Gdx.graphics.getHeight() / scale);
		batch = new SpriteBatch();
		Textures.load("materials/");
		
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.type = BodyType.StaticBody;
		ground = world.createBody(groundBodyDef);
		
		Body edgebody = world.createBody(groundBodyDef);
		
		float vw = camera.viewportWidth/2, vh = camera.viewportHeight/2;
		
		addEdge(edgebody, -vw, -vh, vw, -vh);
		addEdge(edgebody, vw, -vh, vw, vh);
		addEdge(edgebody, vw, vh, -vw, vh);
		addEdge(edgebody, -vw, vh, -vw, -vh);
		
		
		addMaterial("wood");
		

		Gdx.input.setInputProcessor(this);
	}
	
	void addEdge(Body body, float x1, float y1, float x2, float y2){
		EdgeShape edge = new EdgeShape();
		edge.set(new Vector2(x1, y1), new Vector2(x2,  y2));
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = edge;
		
		body.createFixture(fixture);

		edge.dispose();
	}
	
	void addMaterial(String name){
		Texture texture = Textures.get(name);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(0, 0);
		bodyDef.linearDamping = 4f;
		bodyDef.angularDamping = 4f;

		Body body = world.createBody(bodyDef);
		
		PolygonShape box = new PolygonShape();  
		box.setAsBox(texture.getWidth()/2, texture.getHeight()/2);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = box;
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f;

		body.createFixture(fixtureDef);
		body.setUserData(new MaterialObject(texture));
		box.dispose();
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button){

		world.QueryAABB(new QueryCallback(){
			@Override
			public boolean reportFixture(Fixture fixture){
				MouseJointDef def = new MouseJointDef();
				def.bodyA = ground;
				def.bodyB = fixture.getBody();
				def.collideConnected = true;
				def.target.set(mouse.x,  mouse.y);
				def.maxForce = 1000.0f * fixture.getBody().getMass();

				mouseJoint = (MouseJoint)world.createJoint(def);
				fixture.getBody().setAwake(true);
				
				return false;
			}
		}, mouse.x - clickrad, mouse.y - clickrad, mouse.x + clickrad, mouse.y + clickrad);
		return false;
	}

	public boolean touchUp(int screenX, int screenY, int pointer, int button){
		if (mouseJoint != null) {
			world.destroyJoint(mouseJoint);
			mouseJoint = null;
		}
		return false;
	}

	public boolean touchDragged(int screenX, int screenY, int pointer){
		if(mouseJoint != null)
		mouseJoint.setTarget(mouseJoint.getTarget().set(mouse.x, mouse.y));
		return false;
	}
	
	public void useTool(){
		world.QueryAABB(new QueryCallback(){
			@Override
			public boolean reportFixture(Fixture fixture){
				Body body = fixture.getBody();
				Vector2 vector = new Vector2(mouse.x, mouse.y).sub(body.getPosition());
				vector.rotateRad(-body.getAngle());
				MaterialObject mat = (MaterialObject)body.getUserData();
				mat.toolUsed((int)(vector.x +mat.texture.getWidth()/2), mat.texture.getHeight() - (int)(vector.y + mat.texture.getHeight()/2));
				return true;
			}
		}, mouse.x - toolrad, mouse.y - toolrad, mouse.x + toolrad, mouse.y + toolrad);
	}

	@Override
	public void update(){
		mouse = camera.unproject(mouse.set(Gdx.input.getX(), Gdx.input.getY(), 0));
		if(Gdx.input.isKeyJustPressed(Keys.E)) addMaterial("wood");
		if(Gdx.input.isKeyJustPressed(Keys.Q)) useTool();

		camera.position.set(0, 0, 0);
		camera.update();

		UCore.clearScreen(Color.BLACK);

		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		bodies.clear();
		world.getBodies(bodies);
		for(Body body : bodies){
			if(body.getUserData() == null) continue;
			Texture tex = ((MaterialObject)body.getUserData()).texture;
			batch.draw(new TextureRegion(tex), body.getPosition().x - tex.getWidth()/2, body.getPosition().y - tex.getHeight()/2, 
					tex.getWidth()/2, tex.getHeight()/2, tex.getWidth(), tex.getHeight(), 1f, 1f, body.getAngle()*MathUtils.radDeg);
			//batch.draw(tex, body.getPosition().x - tex.getWidth()/2, body.getPosition().y - tex.getHeight()/2, tex.getWidth(), tex.getWidth(), 1, 1, 1, 1, 1, 1, 1);
		}
		batch.end();
		
		debugRenderer.render(world, camera.combined);
		
		stepWorld(Gdx.graphics.getDeltaTime());
	}
	
	private void stepWorld(float deltaTime) {
	    // fixed time step
	    // max frame time to avoid spiral of death (on slow devices)
		float step = 1/300f;
	    float frameTime = Math.min(deltaTime, 0.25f);
	    accumulator += frameTime;
	    while (accumulator >= step) {
	        world.step(step, 6, 4);
	        accumulator -= step;
	    }
	}

	public void resize(int width, int height){
		camera.setToOrtho(false, width/scale, height/scale);
	}

}
