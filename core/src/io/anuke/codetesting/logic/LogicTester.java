package io.anuke.codetesting.logic;

import io.anuke.ucore.UCore;

public class LogicTester{
	
	public static void main(String[] args){
		/*
		String[] problems = {
			"AAA-3",
			"IAI-2",
			"EIO-1",
			"AAI-2",
			"IEO-1",
			"EOO-4",
			"EAA-1",
			"AII-3",
			"AAI-4",
			"IAO-3",
			"AII-2",
			"AIO-3",
			"AEE-4",
			"EAE-4",
			"EAO-3",
			"EEE-1",
			"EAE-1",
			"OAI-3",
			"AOO-2",
			"EAO-1"
		};
		*/
		
		String[] problems = {
			"III-4",
			"EAE-2",
			"EAO-1",
			"OEO-3",
			"AAI-3",
			"AAA-3",
			"EIA-2",
			"AII-4",
			"AOI-2",
			"AAI-4"
		};
		
		for(int i = 0; i < problems.length; i ++){
			Syllogism s = Syllogism.fromString(problems[i]);
			UCore.log(s.toString());
			UCore.log("#" + (i + 1) + ": " + s.getFallacy());
			UCore.log();
		}
		
		//Syllogism s = Syllogism.fromString("IEO-1");
		
		//UCore.log(s.toString());
		//UCore.log("\nfallacies: " + s.getFallacy());
	}
}
