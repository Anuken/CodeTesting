package io.anuke.codetesting.modules.ecstesting;

import java.util.HashSet;

import com.badlogic.gdx.utils.Array;

public class PathfindTest{
	Array<Node> rooms = new Array<Node>();

	void test(){
		//TEST DATA i used:
		rooms.add(new Node(0, 0));
		rooms.add(new Node(-1, 0));
		rooms.add(new Node(-2, 0));
		rooms.add(new Node(1, 0));
		rooms.add(new Node(2, 0));
		FindPath(rooms.get(2), rooms.get(4));
	}

	//start at -2,0 target: 2,0

	// path findiner function & heuristic and get nieghbours
	//Node class
	public class Node{
		public boolean walkable;
		public int x, y;
		public int gCost;
		public int hCost;
		public Node parent;

		public Node(int x, int y){
			this.x = x;
			this.y = y;
			walkable = true;
		}

		public int fCost(){
			return gCost + hCost;
		}
		
		public String toString(){
			return x + " " + y;
		}
	}

	public Array<Node> FindPath(Node startPos, Node endPos){
		Array<Node> openSet = new Array<Node>();
		HashSet<Node> closedSet = new HashSet<Node>();
		Array<Node> path = new Array<Node>();

		openSet.add(startPos);

		while(openSet.size > 0){
			Node currentNode = openSet.get(0);
			System.out.println(currentNode);
			
			for(int i = 1;i < openSet.size;i ++){
				if(openSet.get(i).fCost() < currentNode.fCost() || openSet.get(i).fCost() == currentNode.fCost() && openSet.get(i).hCost < currentNode.hCost){
					currentNode = openSet.get(i);
				}
			}

			openSet.removeValue(currentNode, true);
			closedSet.add(currentNode);

			if(currentNode == endPos){ // path found
				Node curNode = endPos;

				while(curNode != startPos){
					path.add(curNode);
					curNode = curNode.parent;
				}
				path.reverse(); //switch it around to get the path in the correct order
				break; // exit main while loop

			}else{
				Array<Node> array = GetNeighbours(currentNode);
				for(Node neighbour : array){
					if( !neighbour.walkable || closedSet.contains(neighbour)){
						continue;
					}

					int newMovementCostToNeighbour = currentNode.gCost + Heuristic(currentNode, neighbour);

					if(newMovementCostToNeighbour < neighbour.gCost || !openSet.contains(neighbour, true)){
						neighbour.gCost = newMovementCostToNeighbour;
						neighbour.hCost = Heuristic(neighbour, endPos);
						neighbour.parent = currentNode;

						if( !openSet.contains(neighbour, true)){
							openSet.add(neighbour);
						}
					}
				}
			}
		}
		return path; // either blank if no path found or will have a Array of a path
	}

	private int Heuristic(Node n, Node end){
		return (Math.abs(n.x - end.x) + Math.abs(n.y - end.y));
	}

	private Array<Node> GetNeighbours(Node node){
		Array<Node> neighbours = new Array<Node>();

		for(int i = 0;i < rooms.size;i ++){
			if(rooms.get(i).x == node.x - 1 || rooms.get(i).x == node.x + 1){
				neighbours.add(rooms.get(i));
			}
		}
		return neighbours;
	}

}
