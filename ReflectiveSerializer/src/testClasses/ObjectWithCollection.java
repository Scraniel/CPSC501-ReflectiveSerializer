package testClasses;

import java.util.ArrayList;

public class ObjectWithCollection {
	private ArrayList<SimpleObject> list;
	
	public ObjectWithCollection()
	{
		list = null;
	}
	
	public ObjectWithCollection(ArrayList<SimpleObject> list)
	{
		this.list = list;
	}
	
	public String toString()
	{
		String value = "{";
		
		for(int i = 0; i < list.size(); i++)
		{
			value += list.get(i);
			
			if(i < list.size() -1)
				value += ", ";
			
		}
		value += "}";
		
		return value;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof ObjectWithCollection))
			return false;
		
		return ((ObjectWithCollection)o).list.equals(this.list);

	}
}
