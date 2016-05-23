package net.pixelstatic.codetesting.modules.shaders;

import net.pixelstatic.codetesting.modules.Module;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class LightTest extends Module{
		Texture rock, rockNormals;
		
		SpriteBatch batch;
		OrthographicCamera cam;
		
		ShaderProgram shader;
		
		Light[] lights = new Light[100];

		//our constants...
		public static final float DEFAULT_LIGHT_Z = 0.075f;
		public static final float AMBIENT_INTENSITY = 0.2f;
		public static final float LIGHT_INTENSITY = 1f;
		
		public static final Vector3 LIGHT_POS = new Vector3(0f,0f,DEFAULT_LIGHT_Z);
		
		//Light RGB and intensity (alpha)
		public static final Vector3 LIGHT_COLOR = new Vector3(1f, 0.8f, 0.6f);

		//Ambient RGB and intensity (alpha)
		public static final Vector3 AMBIENT_COLOR = new Vector3(0.6f, 0.6f, 1f);

		//Attenuation coefficients for light falloff
		public static final Vector3 FALLOFF = new Vector3(.4f, 3f, 20f);
		
		
		final String VERT =  
				"attribute vec4 "+ShaderProgram.POSITION_ATTRIBUTE+";\n" +
				"attribute vec4 "+ShaderProgram.COLOR_ATTRIBUTE+";\n" +
				"attribute vec2 "+ShaderProgram.TEXCOORD_ATTRIBUTE+"0;\n" +
				
				"uniform mat4 u_projTrans;\n" + 
				" \n" + 
				"varying vec4 vColor;\n" +
				"varying vec2 vTexCoord;\n" +
				
				"void main() {\n" +  
				"	vColor = "+ShaderProgram.COLOR_ATTRIBUTE+";\n" +
				"	vTexCoord = "+ShaderProgram.TEXCOORD_ATTRIBUTE+"0;\n" +
				"	gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" +
				"}";
		
		@Override
		public void init() {
			rock = new Texture(Gdx.files.internal("textures/rock.png"));
			rockNormals = new Texture(Gdx.files.internal("textures/rock_n.png"));
			
			//Gdx.files.external("light.fragment").writeString(FRAG, false);
			
			ShaderProgram.pedantic = false;
			shader = new ShaderProgram(VERT, Gdx.files.internal("shaders/light.fragment").readString());
			//ensure it compiled
			if (!shader.isCompiled())
				throw new GdxRuntimeException("Could not compile shader: "+shader.getLog());
			//print any warnings
			if (shader.getLog().length()!=0)
				System.out.println(shader.getLog());
			
			//setup default uniforms
			shader.begin();

			//our normal map
			shader.setUniformi("u_normals", 1); //GL_TEXTURE1
			
	
			
			//light/ambient colors
			//LibGDX doesn't have Vector4 class at the moment, so we pass them individually...
			//shader.setUniformf("LightColor", LIGHT_COLOR.x, LIGHT_COLOR.y, LIGHT_COLOR.z, LIGHT_INTENSITY);
			shader.setUniformf("AmbientColor", AMBIENT_COLOR.x, AMBIENT_COLOR.y, AMBIENT_COLOR.z, AMBIENT_INTENSITY);
			shader.setUniformf("Falloff", FALLOFF);
			
			//LibGDX likes us to end the shader program
			shader.end();
			
			batch = new SpriteBatch(1000, shader);
			batch.setShader(shader);
			
			cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			cam.setToOrtho(false);
			
			//handle mouse wheel
			Gdx.input.setInputProcessor(new InputAdapter() {
				public boolean scrolled(int delta) {
					//LibGDX mouse wheel is inverted compared to lwjgl-basics
					LIGHT_POS.z = Math.max(0f, LIGHT_POS.z - (delta * 0.005f));
					System.out.println("New light Z: "+LIGHT_POS.z);
					return true;
				}
			});
			
			Gdx.graphics.setWindowedMode(300, 225);
			
			Light light = new Light(new Color(1f, 1f, 0.5f, 1f), new Vector3(0.5f,0.5f,DEFAULT_LIGHT_Z));
			lights[0] = light;
			
			Light light2 = new Light(new Color(0.5f, 1f, 0.5f, 1f), new Vector3(0.1f,0.7f,DEFAULT_LIGHT_Z));
			lights[1] = light2;
		}

		@Override
		public void resize(int width, int height) {
			cam.setToOrtho(false, width, height);
			batch.setProjectionMatrix(cam.combined);
			
			shader.begin();
			shader.setUniformf("Resolution", width, height);
			shader.end();
		}

		@Override
		public void update() {
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
			//reset light Z
			if (Gdx.input.isTouched()) {
				LIGHT_POS.z = DEFAULT_LIGHT_Z;
				System.out.println("New light Z: "+LIGHT_POS.z);
			}
			
			shader.begin();
			for(int i = 0; i < 100; i ++){
				Light light = lights[i];
				if(light == null) break;
				shader.setUniformf(shader.getUniformLocation("lightarray[" + i + "].color"), light.color);
				shader.setUniformf(shader.getUniformLocation("lightarray[" + i + "].pos"), light.pos);
				
				light.pos.y += Math.sin(light.pos.x*20f)/ 100f;

				light.pos.x += Math.sin(light.pos.y*20f)/ 100f;
			}
			
			shader.end();
			
			batch.begin();
			
			//shader will now be in use...
			
			//update light position, normalized to screen resolution
			float x = Gdx.input.getX() / (float)Gdx.graphics.getWidth();//Mouse.getX() / (float)Display.getWidth();
			float y = 1f - Gdx.input.getY() / (float)Gdx.graphics.getHeight();//Mouse.getY() / (float)Display.getHeight();
					
			LIGHT_POS.x = x;
			LIGHT_POS.y = y;
			

			
			//bind normal map to texture unit 1
			rockNormals.bind(1);
			
			//bind diffuse color to texture unit 0
			//important that we specify 0 otherwise we'll still be bound to glActiveTexture(GL_TEXTURE1)
			rock.bind(0);
			
			//draw the texture unit 0 with our shader effect applied
			batch.draw(rock, 0, 0);
			
			batch.end();
		}


		public void dispose() {
			batch.dispose();
			rock.dispose();
			rockNormals.dispose();
			shader.dispose();
		}	
}
