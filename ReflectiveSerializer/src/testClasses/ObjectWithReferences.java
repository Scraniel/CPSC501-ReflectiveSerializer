package testClasses;

public class ObjectWithReferences {

	private SimpleObject firstField, secondField, thirdField;
	
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
