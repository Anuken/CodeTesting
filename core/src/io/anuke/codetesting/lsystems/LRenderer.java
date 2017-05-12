package io.anuke.codetesting.lsystems;

import java.util.HashMap;
import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import io.anuke.gif.GifRecorder;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.core.Graphics;
import io.anuke.ucore.core.Inputs;
import io.anuke.ucore.modules.RendererModule;
import io.anuke.ucore.scene.utils.Cursors;
import io.anuke.ucore.util.Mathf;
import io.anuke.ucore.util.Timers;

public class LRenderer extends RendererModule{
	private GifRecorder recorder = new GifRecorder(batch);
	
	final private Color start = Color.PURPLE;//Color.valueOf("37682c");
	final private Color end = Color.CORAL;//Color.valueOf("94ac62");
	final private Stack<Vector3> stack = new Stack<>();
	private float len = 4f;
	private float space = 25;
	
	private boolean moving = false;
	private float lastx, lasty;
	private boolean loading = false;
	
	private Thread thread;
	private float swayscl = 2f;
	private float swayphase = 20f;
	private float swayspace = 1f;
	private int iterations = 6;
	private String axiom = "X";
	private HashMap<Character, String> rules = map(
		'X', "F-[[X]+X]+F[+FX]-X",
		'F', "FF"
	);
	
	private String out;
	private float angle = 90;
	private float x, y;
	
	public LRenderer(){
		cameraScale = 1f;
		
		//setPixelation();
	}
	
	public void init(){
		out = LSystem.get(axiom, iterations, rules);
		
		log(out.length());
		clear();
	}
	
	public void setSwaySpace(float sspace){
		swayspace = sspace;
		generate();
	}
	
	public void setSwayScale(float sscl){
		swayscl = sscl;
		generate();
	}
	
	public void setSwayPhase(float sph){
		swayphase = sph;
		generate();
	}
	
	public void setAxiom(String ax){
		axiom = ax;
		generate();
	}
	
	public void setIterations(int i){
		iterations = i;
		generate();
	}
	
	public void setAngle(float angle){
		space = angle;
		generate();
	}
	
	public void setLength(float length){
		len = length;
		generate();
	}
	
	public HashMap<Character, String> rules(){
		return rules;
	}
	
	public int getCharacters(){
		return out.length();
	}
	
	public boolean isLoading(){
		return loading;
	}
	
	public void generate(){
		clear();
		
		loading = true;
		
		(thread = new Thread(()->{
			out = LSystem.get(axiom, iterations, rules);
			loading = false;
		}){{setDaemon(true);}}).start();
	}
	
	private void clear(){
		angle = 90;
		x = y = 0;
		
		stack.clear();
	}
	
	private void draw(char c){
		if(c == 'F'){
			drawForward();
		}else if(c == '-'){
			angle -= space;
		}else if(c == '+'){
			angle += space;
		}else if(c == '['){
			push();
		}else if(c == ']'){
			pop();
		}
	}
	
	private void drawForward(){
		float sway = swayscl*MathUtils.sin(Timers.time()/swayphase+stack.size()*swayspace);
		
		vector.set(len, 0).rotate(-angle+180 + sway);
		
		Draw.color(start, end, stack.size()/10f);
		Draw.line(x, y, x+vector.x, y+vector.y);
		
		x += vector.x;
		y += vector.y;
	}
	
	private void push(){
		stack.push(new Vector3(x, y, angle));
	}
	
	private void pop(){
		Vector3 vec = stack.pop();
		x = vec.x;
		y = vec.y;
		angle = vec.z;
	}
	
	public void update(){
		if(!((LUI) get(LUI.class)).hasMouse() && !Inputs.keyDown(Keys.CONTROL_LEFT)){
			if(Inputs.buttonUp(Buttons.LEFT)){
				lastx = Graphics.mouse().x;
				lasty = Graphics.mouse().y;
				moving = true;
				Cursors.setHand();
			}
			
			if(Inputs.buttonRelease(Buttons.LEFT)){
				moving = false;
				Cursors.restoreCursor();
			}
			
			if(Inputs.buttonDown(Buttons.LEFT) && moving){
				float dx = Graphics.mouse().x-lastx;
				float dy = Graphics.mouse().y-lasty;
				camera.position.sub(dx*camera.zoom, dy*camera.zoom, 0);
				camera.update();
				
				lastx = Graphics.mouse().x;
				lasty = Graphics.mouse().y;
			}
		}
		
		if(Inputs.scrolled()){
			camera.zoom = Mathf.clamp(camera.zoom-Inputs.scroll()/10f, 0.1f, 10f);
			camera.update();
		}
		
		if(Inputs.keyDown(Keys.ESCAPE))
			Gdx.app.exit();
		
		Timers.update(Gdx.graphics.getDeltaTime());
		
		drawDefault();
		
		clear();
		
		recorder.update();
	}
	
	public void draw(){
		for(char c : out.toCharArray()){
			draw(c);
		}
		Draw.color();
	}
	
	private HashMap<Character, String> map(Object... objects){
		 HashMap<Character, String> map = new HashMap<>();
		 for(int i = 0; i < objects.length; i += 2){
			 map.put((char)objects[i], (String)objects[i+1]);
		 }
		 return map;
	}
	
	public void resize(){
		setCamera(200, 200);
		camera.update();
	}
}
