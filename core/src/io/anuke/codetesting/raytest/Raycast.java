package io.anuke.codetesting.raytest;

public class Raycast implements Comparable<Raycast>{
	public float angle;
	public float hitx, hity;
	public boolean hit;
	public boolean use = true;
	
	@Override
	public int compareTo(Raycast other){
		if(other.angle == angle) return 0;
		return other.angle < angle ? 1 : -1;
	}
}
