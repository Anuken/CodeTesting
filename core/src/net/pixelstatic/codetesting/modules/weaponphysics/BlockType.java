package net.pixelstatic.codetesting.modules.weaponphysics;


public enum BlockType{
	solid;

	public boolean inBounds(int x, int y, int size){
		return !(x < 0 || y < 0 || x >= size || y >= size);
	}
}
