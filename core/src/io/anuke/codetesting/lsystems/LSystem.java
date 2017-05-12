package io.anuke.codetesting.lsystems;

import java.util.HashMap;

public class LSystem{
	
	public static String get(String axiom, int iterations, HashMap<Character, String> rules){
		String current = axiom;
		String out;

		for(int i = 0; i < iterations; i ++){
			out = "";
			
			for(int j = 0; j < current.length(); j ++){
				char c = current.charAt(j);
				
				if(rules.get(c) != null){
					out += rules.get(c);
				}else{
					out += c;
				}
			}
			
			current = out;
		}

		return current;
	}
}
