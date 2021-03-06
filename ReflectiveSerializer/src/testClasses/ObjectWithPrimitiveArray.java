package testClasses;

import java.util.Arrays;

public class ObjectWithPrimitiveArray {

	private int[] array;
	
	public ObjectWithPrimitiveArray()
	{
		array = null;
	}
	
	public ObjectWithPrimitiveArray(int[] array)
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
		if(!(o instanceof ObjectWithPrimitiveArray))
			return false;
		
		return Arrays.equals(((ObjectWithPrimitiveArray)o).array, this.array);

	}
}
