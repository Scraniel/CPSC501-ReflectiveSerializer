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
	
	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof CircularNode))
			return false;
		
		boolean result = true;
		
		CircularNode current = this;
		CircularNode next = this.next;
		
		while(next != this)
		{
			result = result && ((CircularNode)o).identifier == this.identifier;
			current = next;
			next = next.next;		
		}	
		
		return result;
	}
}
