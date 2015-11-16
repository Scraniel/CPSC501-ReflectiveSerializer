package sender;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import testClasses.*;
import reciever.*;

public class Serializer implements ReflectiveSerializer {

	private HashMap<Object, Integer> serialized;
	private LinkedList<Element> elementsToAdd;
	private Document doc;
	int currentID;
	
	public Serializer()
	{
		serialized = new HashMap();
		elementsToAdd = new LinkedList<Element>();
		currentID = 0;
	}
	
	@Override
	public Document serialize(Object obj) {
		
		Element root = new Element("serialized");
		doc = new Document(root);		
	
		elementsToAdd.push(serializeObject(obj, getID()));
		
		root.addContent(elementsToAdd);
		
		currentID = 0;
		serialized.clear();
		elementsToAdd.clear();
		return doc;
	}
	
	private int getID()
	{
		return currentID++;
	}
	
	private Element serializeField(Field field, Object obj)
	{
		field.setAccessible(true);
		
		String declaringClass = field.getDeclaringClass().getName();
		
		Element newField = new Element("field");
		newField.setAttribute("name", field.getName());
		newField.setAttribute("declaringClass", declaringClass);
		Element value;			
		
		if(field.getType().isPrimitive())
		{
			value = new Element("value");
			
			try {
				value.addContent(field.get(obj).toString());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		else
		{
			value = new Element("reference");
			
			Object instance = null;
			try {
				instance = field.get(obj);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
			int id;
			
			if(instance != null){
				if(serialized.containsKey(instance))
					id = serialized.get(instance);
				else
				{
					id = getID();
					Element toAdd = serializeObject(instance, id);
					if(toAdd != null)
						elementsToAdd.add(toAdd); 
				}
				
				value.addContent(Integer.toString(id));
			}
			else
				value.addContent("null");
			
		}
		
		newField.addContent(value);
		
		return newField;
	}
	
	private Element serializeObject(Object obj, int id)
	{
		if(serialized.containsKey(obj))
			return null;
		
		serialized.put(obj, id);
		Class metaObj = obj.getClass();		
		Element newObj = new Element("object");
		newObj.setAttribute("class", metaObj.getName());
		newObj.setAttribute("id", Integer.toString(id));
		
		if(metaObj.isArray())
			return serializeArray(obj, id, newObj);
		
		ArrayList<Field> fields = new ArrayList<Field>(Arrays.asList(metaObj.getDeclaredFields()));
		Class superClass = metaObj.getSuperclass();
		
		while(superClass != null)
		{
			fields.addAll(Arrays.asList(superClass.getDeclaredFields()));
			superClass = superClass.getSuperclass();
		}
		
		fields.removeIf(x -> Modifier.isStatic(x.getModifiers()));
		
		for(Field field : fields)
		{		
			newObj.addContent(serializeField(field, obj));		
		}
		
		return newObj;
	}
	
	private Element serializeArray(Object instance, int id, Element base)
	{
		Class arrayClass = instance.getClass();
		
		if(!arrayClass.isArray())
			return null;

		
		int length = Array.getLength(instance);
		base.setAttribute("length", Integer.toString(length));
		
		if(arrayClass.getComponentType().isPrimitive())
		{
			for(int i = 0; i < length; i++)
			{
				Element value = new Element("value");
				value.addContent(Array.get(instance, i).toString());
				base.addContent(value);
			}
		}
		else
		{
			for(int i = 0; i < length; i++)
			{
				Element reference = new Element("reference");
				Object elementInstance = Array.get(instance, i);
				int elementRefID;
				
				if(elementInstance != null)
				{
					if(serialized.containsKey(elementInstance))
						elementRefID = serialized.get(elementInstance);
					else
					{
						elementRefID = getID();
						Element toAdd = serializeObject(elementInstance, elementRefID);
						if(toAdd != null)
							elementsToAdd.add(toAdd);
					}
					
					reference.addContent(Integer.toString(elementRefID));
				}
				else
					reference.addContent("null");
				
				base.addContent(reference);
			}
		}
		
		return base;
	}

}
