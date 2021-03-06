package testClasses;

public class SimpleObject {

	private int intField;
	private double doubleField;
	private char charField;
	private boolean booleanField;
	
	public SimpleObject()
	{
		intField = -1;
		doubleField = -1;
		charField = ' ';
		booleanField = false;
		
	}
	
	public SimpleObject(int intField, double doubleField, char charField, boolean booleanField)
	{
		this.intField = intField;
		this.doubleField = doubleField;
		this.charField = charField;
		this.booleanField = booleanField;
	}
	
	public String toString()
	{
		return "int: " + intField + " double:" + doubleField + " char:" + charField + " boolean:" + booleanField;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof SimpleObject))
			return false;
		
		boolean result = ((SimpleObject)o).intField == this.intField;
		result = result && ((SimpleObject)o).doubleField == this.doubleField;
		result = result && ((SimpleObject)o).charField == this.charField;
		result = result && ((SimpleObject)o).booleanField == this.booleanField;
		
		return result;
	}
}
