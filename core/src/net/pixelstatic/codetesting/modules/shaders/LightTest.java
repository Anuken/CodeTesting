package net.pixelstatic.codetesting.modules.shaders;

import net.pixelstatic.codetesting.modules.Module;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class LightTest extends Module{
	public static final float DEFAULT_LIGHT_Z = 0.075f;
	public static final float AMBIENT_INTENSITY = 0.02f;
	public static final Vector3 LIGHT_POS = new Vector3(0f, 0f, DEFAULT_LIGHT_Z);
	public static final Vector3 LIGHT_COLOR = new Vector3(1f, 0.8f, 0.6f);
	public static final Vector3 AMBIENT_COLOR = new Vector3(0.6f, 0.6f, 1f);
	public static final Vector3 FALLOFF = new Vector3(.4f, 3f, 20f);
	
	Texture rock, rockNormals;
	SpriteBatch batch;
	OrthographicCamera cam;
	ShaderProgram shader;
	Light[] lights = new Light[100];
	int scrolls = 0;

	@Override
	public void init(){
		rock = new Texture(Gdx.files.internal("textures/rock.png"));
		rockNormals = new Texture(Gdx.files.internal("textures/rock_n.png"));
		
		
		ShaderProgram.pedantic = false;
		shader = new ShaderProgram(Gdx.files.internal("shaders/default.vertex").readString(), Gdx.files.internal("shaders/light.fragment").readString());
		if( !shader.isCompiled()) throw new GdxRuntimeException("Could not compile shader: " + shader.getLog());
		if(shader.getLog().length() != 0) System.out.println(shader.getLog());

		shader.begin();

		shader.setUniformi("u_normals", 1);

		shader.setUniformf("AmbientColor", AMBIENT_COLOR.x, AMBIENT_COLOR.y, AMBIENT_COLOR.z, AMBIENT_INTENSITY);
		shader.setUniformf("Falloff", FALLOFF);

		shader.end();

		batch = new SpriteBatch(1000, shader);
		batch.setShader(shader);

		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.setToOrtho(false);

		Gdx.input.setInputProcessor(new InputAdapter(){
			public boolean scrolled(int delta){
				if(delta > 0 && scrolls == 0 || delta < 0 && scrolls == 100) return false;
				
				scrolls -= delta;
				for(int i = 0;i < 100;i ++)
					lights[i] = null;

				for(int i = 0;i < scrolls;i ++){
					Light light = new Light(new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1f), new Vector3(MathUtils.random(), MathUtils.random(), DEFAULT_LIGHT_Z));
					lights[i] = light;

				}
				return true;
			}
		});

		Gdx.graphics.setWindowedMode(300, 225);
	}

	@Override
	public void resize(int width, int height){
		cam.setToOrtho(false, width, height);
		batch.setProjectionMatrix(cam.combined);

		shader.begin();
		shader.setUniformf("Resolution", width, height);
		shader.end();
	}

	@Override
	public void update(){
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		shader.begin();
		for(int i = 0;i < 100;i ++){
			Light light = lights[i];
			if(light == null){
				shader.setUniformf(shader.getUniformLocation("lightarray[" + i + "].color"), Color.CLEAR);
				continue;
			}
			shader.setUniformf(shader.getUniformLocation("lightarray[" + i + "].color"), light.color);
			shader.setUniformf(shader.getUniformLocation("lightarray[" + i + "].pos"), light.pos);

			light.pos.y += Math.sin(light.pos.x * 20f) / 100f;

			light.pos.x += Math.sin(light.pos.y * 20f) / 100f;
		}

		shader.end();
		batch.begin();
		rockNormals.bind(1);
		rock.bind(0);
		batch.draw(rock, 0, 0);
		batch.end();
	}

	public void dispose(){
		batch.dispose();
		rock.dispose();
		rockNormals.dispose();
		shader.dispose();
	}
}
