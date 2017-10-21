package io.anuke.codetesting.raytest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import io.anuke.ucore.core.*;
import io.anuke.ucore.graphics.Drawv;
import io.anuke.ucore.graphics.Hue;
import io.anuke.ucore.modules.RendererModule;
import io.anuke.ucore.util.*;

public class RayTest2 extends RendererModule{
	Array<Rectangle> rects = new Array<>();
	Array<Light> lights = new Array<>();
	Vector2[] points = { new Vector2(), new Vector2(), new Vector2(), new Vector2() };

	public RayTest2() {
		for(int i = 0; i < 10; i++){
			rects.add(new Rectangle(Mathf.range(120), Mathf.range(120), Mathf.random(5, 20), Mathf.random(5, 20)));
		}
		
		/*
		float s = 300;
		
		rects.add(new Rectangle(s/2, -s/2, 1, s));
		rects.add(new Rectangle(-s/2 - 1, -s/2, 1, s));
		
		rects.add(new Rectangle(-s/2, s/2, s, 1));
		rects.add(new Rectangle(-s/2, -s/2 - 1, s, 1));
		*/
		lights.add(new Light());

		Core.cameraScale = 2;
	}

	@Override
	public void update(){
		if(Inputs.keyDown(Keys.ESCAPE)){
			Gdx.app.exit();
		}

		Core.camera.position.set(0, 0, 0);
		lights.first().set(Graphics.mouseWorld().x, Graphics.mouseWorld().y);

		drawDefault();

		Drawv.begin();
		Drawv.color(Color.ORANGE);

		for(Light light : lights){
			
			
			for(int i = 0; i < light.raycasts.length; i++){
				Raycast cast = light.raycasts[i];
				Raycast next = (i == light.raycasts.length - 1 ? light.raycasts[(0)] : light.raycasts[(i + 1)]);
				
				//if(Angles.angleDist(cast.angle, next.angle) > 160 || next.angle - cast.angle >= 180 || i == light.raycasts.size - 1) continue;

				//Draw.color(Hue.lightness(i++/(float)light.raycasts.size));
				//Draw.line(light.x, light.y, light.x + cast.hitx, light.y + cast.hity);
				
				float l1 = Vector2.dst(cast.hitx, cast.hity, 0, 0) / light.radius;
				float l2 = Vector2.dst(next.hitx, next.hity, 0, 0) / light.radius;
				
				Color to = Tmp.c2.set(light.color);
				to.a = 0f;
				
				float c1 = Color.YELLOW.toFloatBits(), 
					  c2 = Hue.mix(light.color, Tmp.c2, l1, Tmp.c1).toFloatBits(),
				      c3 = Hue.mix(light.color, Tmp.c2, l2, Tmp.c1).toFloatBits(); 
				
				Drawv.tri(light.x, light.y, light.x + cast.hitx, light.y + cast.hity, light.x + next.hitx, light.y + next.hity,
						c1, c2, c3);
			}
			
		}

		Drawv.end();

		Graphics.begin();
		for(Light light : lights){
			for(Raycast cast : light.raycasts){
				Draw.color(Color.RED);
				Draw.line(light.x, light.y, light.x + cast.hitx, light.y + cast.hity);
			}
		}
		Graphics.end();
	}

	@Override
	public void draw(){
		Draw.color(Color.GREEN);

		for(Rectangle rect : rects){
			Draw.linerect(rect);
		}

		Draw.color(Color.RED);

		for(Light light : lights){
			
			for(int i = 0; i < light.raycasts.length; i ++){
				Angles.translation(360f/light.raycasts.length * i, light.radius);
				light.raycasts[i].hit = false;
				light.raycasts[i].hitx = Angles.x();
				light.raycasts[i].hity = Angles.y();
				
				Raycast cast = light.raycasts[i];
				
				Vector2 to = Tmp.v2.set(cast.hitx, cast.hity);
				Vector2 current = Tmp.v3.set(cast.hitx + light.x, cast.hity + light.y);
				float clen = light.radius;
				
				for(int j = 0; j < rects.size; j++){
					Rectangle other = rects.get(j);
					Vector2 out = Physics.raycastRect(light.x, light.y, light.x + to.x, light.y + to.y, other);

					if(out == null){
						continue;
					}

					float len = out.dst(light.x, light.y);

					if(len < clen){
						current.set(out);
						clen = len;
					}
				}
				
				cast.hitx = current.x - light.x;
				cast.hity = current.y - light.y;
			}
			
			/*
			for(Rectangle rect : rects){
				points(rect);

				for(Vector2 to : points){
					to.sub(light.x, light.y);

					if(to.len() < light.radius){
						Vector2 current = new Vector2();
						float clen = Float.MAX_VALUE;

						for(int i = 0; i < rects.size; i++){
							Rectangle other = rects.get(i);
							Vector2 out = Physics.raycastRect(light.x, light.y, light.x + to.x, light.y + to.y, other);

							if(out == null){
								out = Tmp.v2.set(light.x + to.x, light.y + to.y);
								//continue;
							}

							float len = out.dst(light.x, light.y);

							if(len < clen){
								current.set(out);
								clen = len;
							}
						}

						if(clen > 0f){
							current.sub(light.x, light.y);
							
							float angle = current.angle();

							Raycast cast = light.raycasts[(int)(angle/361f*light.raycasts.length)];
							cast.angle = angle;
							cast.hitx = current.x;
							cast.hity = current.y;
							cast.hit = true;
						}
					}

				}
			}
			
			//forward correction?
			
			/*
			for(int i = 0; i < light.raycasts.length; i ++){
				
				
				if(light.raycasts[i].hit){
					for(int j = i + 1; j < light.raycasts.length; j ++){
						if(light.raycasts[j].hit){
							i = j;
							break;
						}
						
						light.raycasts[j].use = false;
					}
				}
			}
			*/
		}
	}

	void points(Rectangle rect){
		points[0].set(rect.x, rect.y);
		points[1].set(rect.x + rect.width, rect.y);
		points[2].set(rect.x, rect.y + rect.height);
		points[3].set(rect.x + rect.width, rect.y + rect.height);
	}
}
