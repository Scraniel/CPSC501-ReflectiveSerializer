package testClasses;

import java.util.Arrays;

public class ObjectWithReferences {

	private SimpleObject firstField, secondField, thirdField;
	
	public ObjectWithReferences()
	{
		firstField = null;
		secondField = null;
		thirdField = null;
	}
	
	public ObjectWithReferences(SimpleObject firstField, SimpleObject secondField, SimpleObject thirdField)
	{
		this.firstField = firstField;
		this.secondField = secondField;
		this.thirdField = thirdField;
	}
	
	public String toString()
	{
		return "First Field:" + firstField + "\nSecond Field:" + secondField + "\nThird Field:" + thirdField;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof ObjectWithReferences))
			return false;
		
		boolean result = ((ObjectWithReferences)o).firstField.equals(this.firstField);
		result = result && ((ObjectWithReferences)o).secondField.equals(this.secondField);
		result = result && ((ObjectWithReferences)o).thirdField.equals(this.thirdField);
		
		return result;
	}
}
