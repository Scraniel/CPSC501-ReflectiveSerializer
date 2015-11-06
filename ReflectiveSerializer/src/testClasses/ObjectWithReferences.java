package testClasses;

public class ObjectWithReferences {

	SimpleObject firstField, secondField, thirdField;
	
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
}
