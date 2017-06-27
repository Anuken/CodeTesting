package io.anuke.codetesting.fluidsim;

public class Cell{
	public boolean settled;
	public float liquid;
	public CellType type;
	public int settletime;
	public Cell bottom, top, left, right;
	
	public Cell(){
		type = CellType.liquid;
	}
	
	public void UnsettleNeighbors() {
		if (top != null)
			top.settled = false;
		if (bottom != null)
			bottom.settled = false;
		if (left != null)
			left.settled = false;
		if (right != null)
			right.settled = false;
	}
	
	public static enum CellType{
		solid, liquid
	}
}
