package io.anuke.codetesting.markov;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class MarkovGenerator{
	ObjectMap<String, Array<String>> chain = new ObjectMap<>();
	
	private Array<String> get(String word){
		Array<String> result = null;
		if(chain.containsKey(word)){
			result = chain.get(word);
		}else{
			result = new Array<String>();
			chain.put(word, result);
		}
		return result;
	}
	
	void process(String message){
		String[] split = message.split(" ");
		if(split.length == 0){
			return;
		}
		
		get("__start__").add(split[0]);
		for(int i = 0; i < split.length - 1; i ++){
			get(split[i]).add(split[i+1]);
		}
		get(split[split.length-1]).add("__end__");
	}
	
	String generate(int maxLength){
		Array<String> begin = get("__start__");
		
		int length = 0;
		StringBuilder output = new StringBuilder();
		String start = get("__start__").random();
		if(start == null) return null;
		
		while(length < maxLength){
			Array<String> next = chain.get(start);
			if(next == null || next.size == 0){
				break;
			}
			
			if(length > 0)
				output.append(" ");
			
			String select = next.random();
			if(select.equals("__end__")){
				//output.append("\n");
				select = begin.random();
				//break;
			}
			
			output.append(select);
			start = select;
			
			length ++;
		}
		
		return output.toString();
	}
}
