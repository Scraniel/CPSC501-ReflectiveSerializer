package testClasses;

public class ObjectWithObjectArray {
	private SimpleObject[] array;
	
	public ObjectWithObjectArray(SimpleObject[] array)
	{
		this.array = array;
	}
	
	public String toString()
	{
		String value = "{";
		
		for(int i = 0; i < array.length; i++)
		{
			value += array[i];
			
			if(i < array.length -1)
				value += ", ";
		}
		value += '}';
		
		return value;
		
	}
}
