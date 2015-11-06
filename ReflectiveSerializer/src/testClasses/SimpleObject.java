package testClasses;

public class SimpleObject {

	int intField;
	double doubleField;
	char charField;
	boolean booleanField;
	
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
}
