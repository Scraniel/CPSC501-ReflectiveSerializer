package testClasses;

public class CircularNode {

	int identifier;
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
