package testClasses;

public class CircularNode {

	private int identifier;
	public CircularNode next;
	
	public CircularNode(int value)
	{
		identifier = value;
	}
	
	public String toString()
	{
		return "Identifier:" + identifier; 
	}
}
