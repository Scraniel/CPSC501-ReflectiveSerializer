package reciever;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;

public class Deserializer implements ReflectiveDeserializer {
	HashMap<String, Object> deserialized;
	
	public Deserializer()
	{
		deserialized = new HashMap<String, Object>();
	}
	
	@Override
	public Object deserialize(Document document) 
	{
		Element root = document.getRootElement();
		List<Element> toDeserialize = root.getChildren();
		Class toLoad;
		String id;
		Object instance;
		
		// Need 2 loops, one to load up empty instances, then one to 
		// fill fields
		for(Element loadFrom : toDeserialize)
		{
			id = loadFrom.getAttributeValue("id");
			toLoad = loadClass(loadFrom);
			
			instance = loadEmptyInstance(toLoad, loadFrom);			
			deserialized.put(id, instance);
		}
		
		List<Element> fields, values;
		Class declaringClass;
		Class readingIn = null;
		Field currentField;
		String fieldName, fieldValue;
		Element currentValueOrReference;
		for(Element loadFrom : toDeserialize)
		{
			id = loadFrom.getAttributeValue("id");
			instance = deserialized.get(id);
			fields = loadFrom.getChildren();
			
			try {
				readingIn = Class.forName(loadFrom.getAttributeValue("class"));
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
				return null;
			}
			
			if(readingIn.isArray())
			{
				Class componentType = readingIn.getComponentType();
				int length = Integer.parseInt(loadFrom.getAttributeValue("length"));
				for(int i = 0; i < length; i++)
				{
					currentValueOrReference = fields.get(i);
					fieldValue = currentValueOrReference.getText();
					
					if(currentValueOrReference.getName().compareTo("value") == 0)
						Array.set(instance, i, toObject(componentType, fieldValue));
					else
						Array.set(instance, i, deserialized.get(fieldValue));
				}
			}
			else
			{					
				try 
				{		
					for(Element fieldToLoad : fields)
					{
						declaringClass = Class.forName(fieldToLoad.getAttributeValue("declaringClass")); 			
						fieldName = fieldToLoad.getAttributeValue("name");
						currentField = declaringClass.getDeclaredField(fieldName);
						values = fieldToLoad.getChildren();
						currentValueOrReference = values.get(0);
						currentField.setAccessible(true);
						fieldValue = currentValueOrReference.getText();
						
						if(values.isEmpty())
							return null;
						else if(currentValueOrReference.getName().compareTo("value") == 0)
							currentField.set(instance, toObject(currentField.getType(), fieldValue));
						else
							currentField.set(instance, deserialized.get(fieldValue));
					}
				}
				catch (Exception e) 
				{
					e.printStackTrace();
					return null;
				}
			}
		}
		
		return deserialized.get("0");
	}
	
	private Class loadClass(Element loadFrom)
	{
		Class toLoad = null;
		
		try 
		{
			toLoad = Class.forName(loadFrom.getAttributeValue("class"));
			
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			return null;
		}
		
		return toLoad;
	}
	
	private Object loadEmptyInstance(Class toLoad, Element loadFrom)
	{
		Object instance = null;
		
		if(toLoad.isArray())
		{
			Class arrayType = toLoad.getComponentType();
			int length = Integer.parseInt(loadFrom.getAttributeValue("length"));
			
			instance = Array.newInstance(arrayType, length);
		}
		else
		{
			try 
			{
				Constructor emptyConstructor = toLoad.getDeclaredConstructor(null);
				emptyConstructor.setAccessible(true);
				instance = emptyConstructor.newInstance((Object[])null);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				return null;
			}
		}
		
		return instance;
	}
	
	private static Object toObject( Class clazz, String value ) 
	{
	    if( boolean.class == clazz ) return Boolean.parseBoolean( value );
	    if( byte.class == clazz ) return Byte.parseByte( value );
	    if( short.class == clazz ) return Short.parseShort( value );
	    if( int.class == clazz ) return Integer.parseInt( value );
	    if( long.class == clazz ) return Long.parseLong( value );
	    if( float.class == clazz ) return Float.parseFloat( value );
	    if( double.class == clazz ) return Double.parseDouble( value );
	    if( char.class == clazz) return value.charAt(0);
	    
	    return value;
	}

}
