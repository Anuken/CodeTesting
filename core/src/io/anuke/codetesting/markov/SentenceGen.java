package io.anuke.codetesting.markov;

import java.nio.file.Files;
import java.nio.file.Paths;

public class SentenceGen{
	public static final String directory = "/home/anuke/Documents/OtherDocuments/Tritera/tux";
	
	public static void main(String[] args) throws Exception{
		MarkovGenerator gen = new MarkovGenerator();
		
		Files.readAllLines(Paths.get(directory)).forEach(line->{
			gen.process(line);
		});
		
		for(int i = 0; i < 1; i ++){
			String result = gen.generate(900);
			if(result.length() > 0){
				System.out.println(result);
			}
		}
		
	}
}
