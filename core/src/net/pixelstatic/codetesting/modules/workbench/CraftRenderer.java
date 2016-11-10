package net.pixelstatic.codetesting.modules.workbench;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;

import io.anuke.ucore.UCore;
import net.pixelstatic.codetesting.modules.Module;

public class CraftRenderer extends Module{
	World world;
	Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	OrthographicCamera camera;
	float scale = 10f;
	float clickrad = 0.01f;
	//Body moving = null;
	Body ground;
	Vector3 mouse = new Vector3();
	float dragspeed = 80;
	private MouseJoint mouseJoint = null;


	public void init(){
		world = new World(new Vector2(0, 0), true);
		camera = new OrthographicCamera(Gdx.graphics.getWidth() / scale, Gdx.graphics.getHeight() / scale);

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(0, 0);
		bodyDef.linearDamping = 4f;

		Body body = world.createBody(bodyDef);
		

		CircleShape circle = new CircleShape();
		circle.setRadius(6f);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f;

		body.createFixture(fixtureDef);

		circle.dispose();
		
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.type = BodyType.StaticBody;
		ground = world.createBody(groundBodyDef);


		Gdx.input.setInputProcessor(this);
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
				def.maxForce = 2000.0f * fixture.getBody().getMass();

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

	@Override
	public void update(){
		mouse = camera.unproject(mouse.set(Gdx.input.getX(), Gdx.input.getY(), 0));


		if(Gdx.graphics.getFrameId() % 200 == 0)
			debugRenderer.SHAPE_NOT_AWAKE.set(Color.DARK_GRAY);

		// System.out.println("begin loop");
		world.step(1 / 60f, 6, 2);

		camera.position.set(0, 0, 0);
		camera.update();

		UCore.clearScreen(Color.BLACK);

		debugRenderer.render(world, camera.combined);
	}

	public void resize(int width, int height){
		camera.setToOrtho(false, width/scale, height/scale);
	}

}
