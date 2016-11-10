package net.pixelstatic.codetesting.modules.generator;

import java.util.ArrayList;

public class Branch{
	Branch parent;
	ArrayList<Branch> children = new ArrayList<Branch>();
	
	public Branch(Branch parent){
		this.parent = parent;
	}
}
