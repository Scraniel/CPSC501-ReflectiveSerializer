package testClasses;

import java.util.Arrays;

public class ObjectWithObjectArray {
	private SimpleObject[] array;
	
	public ObjectWithObjectArray()
	{
		array = null;
	}
	
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
	
	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof ObjectWithObjectArray))
			return false;
		
		return Arrays.equals(((ObjectWithObjectArray)o).array, this.array);

	}
}
