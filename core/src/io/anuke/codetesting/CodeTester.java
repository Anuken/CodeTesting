package io.anuke.codetesting;

import java.awt.Color;
import java.io.File;

import com.badlogic.gdx.Gdx;

import io.anuke.codetesting.gifshake.GifShaker;
import io.anuke.ucore.modules.ModuleCore;

public class CodeTester extends ModuleCore{
	
	@Override
	public void init(){
		//module(new PolygonGenerator());
		
		try{
			
			//sean
			//GifShaker.shake(new File("gif/in.png"), new File("gif/out.gif"), 30, 40, 15, 
			//		61, 177, 352, 355, new Color(0, 0, 0, 0));
			
			//wednesday
			//GifShaker.shake(new File("gif/wed.jpg"), new File("gif/wed.gif"), 8, 20, 20, 
			//				0, 0, 225, 225, Color.WHITE);
			
			//santa
			GifShaker.shake(new File("gif/santa.jpg"), new File("gif/santa.gif"), 14, 15, 20, 
					0, 0, 255, 255, Color.WHITE);
			
			//arte
			//GifShaker.shake(new File("gif/arte.jpg"), new File("gif/arte.gif"), 30, 20, 30, 
			//		5, 12, 191, 203, Color.WHITE);
			
			//catface
			//GifShaker.shake(new File("gif/thepic.png"), new File("gif/cat.gif"), 10, 20, 30, 
			//		36, 98, 72, 67, Color.decode("0xf4f3f9"));
			
			//froggo
			//GifShaker.shake(new File("gif/fastfroggo.png"), new File("gif/froggo.gif"), 90, 20, 30, 
			//		0, 0, 512, 512, new Color(0, 0, 0, 0));
			
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		Gdx.app.exit();
	}
}
