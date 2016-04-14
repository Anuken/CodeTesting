package net.pixelstatic.codetesting.modules;

import java.util.HashMap;

import net.pixelstatic.codetesting.modules.ecstesting.EntityTester;
import net.pixelstatic.codetesting.modules.generator.*;
import net.pixelstatic.codetesting.modules.generator2.GeneratorRenderer;
import net.pixelstatic.codetesting.modules.vertex.VertexEditor;
import net.pixelstatic.codetesting.modules.vertex.VertexGUI;
import net.pixelstatic.codetesting.modules.weaponphysics.WeaponInput;
import net.pixelstatic.codetesting.modules.weaponphysics.WeaponPhysics;
import net.pixelstatic.codetesting.modules.weaponphysics.WeaponWorld;


public enum ModuleGroup{
	WEAPON_PHYSICS{
		@Override
		public Module[] modules(){
			return new Module[]{
				new WeaponPhysics(),
				new WeaponWorld(),
				new WeaponInput()
			};
		}
	},
	GENERATOR{
		@Override
		public Module[] modules(){
			return new Module[]{
				new Generator(),
				new PlantGenerator(),
				new GeneratorInput(),
				new Renderer()
			};
		}
	},
	GENERATOR2{
		@Override
		public Module[] modules(){
			return new Module[]{
				new GeneratorRenderer(),
			};
		}
	},
	VERTEX_EDTIOR{
		@Override
		public Module[] modules(){
			return new Module[]{
				new VertexEditor(),
				new VertexGUI()
			};
		}
	},	
	ENTITY_TESTER{
		@Override
		public Module[] modules(){
			return new Module[]{
				new EntityTester(),
			};
		}
	};
	
	abstract public Module[] modules();
		
	
	
	public final HashMap<Class<? extends Module>, Module> getModules(){
		Module[] modules = modules();
		HashMap<Class<? extends Module>, Module> map = new HashMap<Class<? extends Module>, Module>();
		for(Module m : modules){
			map.put(m.getClass(), m);
		}
		return map;
	}
}
