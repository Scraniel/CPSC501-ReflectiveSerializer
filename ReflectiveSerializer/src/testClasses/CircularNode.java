package testClasses;

public class CircularNode {

	public int identifier;
	public CircularNode next;
	
	public CircularNode()
	{
		identifier = -1;
		next = null;
	}
	
	public CircularNode(int value)
	{
		identifier = value;
	}
	
	public String toString()
	{
		return "Chain:" + identifier + "->" + next.identifier + "->" + next.next.identifier + "->" + next.next.next.identifier; 
	}
}
