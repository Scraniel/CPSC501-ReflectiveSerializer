package sender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import testClasses.*;

public class ObjectCreator {
	
	private static Scanner in = new Scanner(System.in);
	
	public static void main(String[] args)
	{
		System.out.println("This program will serialize an object of your choice, then send it to another"
				+ " computer and deserialize it. \n");
		System.out.println("What type of object would you like to serialize?\n"
				+ "1. A simple object with primitive fields\n"
				+ "2. An object with fields that contain object references\n"
				+ "3. An object with fields that contain circular object references (ie. objects connected in a graph)\n"
				+ "4. An objct with a reference to an array of primitives\n"
				+ "5. An object with a references to an array of object references\n"
				+ "Selection:  ");
		
		Object selection = null;
		
		switch(in.nextLine().charAt(0))
		{
		case '1':
			selection = createSimpleObject();
			break;
		case '2':
			selection = createObjectWithReferences();
			break;
		case '3':
			selection = createCircularNodes();
			break;
		case '4':
			selection = createObjectWithPrimitiveArray();
			break;
		case'5':
			selection = createObjectWithObjectArray();
			break;
		}
		
		
		Document doc = new Serializer().serialize(selection);
		
		// HERE'S WHERE YOU ADD THE NETWORK CONNECTION / OUTPUT
		XMLOutputter outputter = new XMLOutputter();
		outputter.setFormat(Format.getPrettyFormat());
		try {
			outputter.output(doc, System.out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static SimpleObject createSimpleObject()
	{
		SimpleObject newObject;
		int intField;
		double doubleField;
		char charField;
		boolean booleanField;
		
		System.out.println("This simple object has 4 different primitive fields: int, double, char, and boolean.");
		System.out.println("Please enter a value for the int field: ");
		intField = Integer.parseInt(in.nextLine());
		System.out.println("Please enter a value for the double field: ");
		doubleField = Double.parseDouble(in.nextLine());
		System.out.println("Please enter a value for the char field: ");
		charField = in.nextLine().charAt(0);
		System.out.println("Please enter a value for the boolean field (true or false): ");
		booleanField = "true".compareToIgnoreCase(in.nextLine()) == 0;
		
		newObject = new SimpleObject(intField, doubleField, charField, booleanField);
		
		System.out.println("The Simple object looks like: " + newObject);
		
		return newObject;
	}
	
	private static ObjectWithReferences createObjectWithReferences()
	{
		ObjectWithReferences newObject;
		SimpleObject firstField, secondField, thirdField;
		
		System.out.println("This object has 3 references to simple objects.");
		System.out.println("Please enter info for the first simple object.");		
		firstField = createSimpleObject();
		
		System.out.println("Please enter info for the second simple object.");
		secondField = createSimpleObject();
		
		System.out.println("Please enter info for the third simple object.");
		thirdField = createSimpleObject();
		
		newObject = new ObjectWithReferences(firstField, secondField, thirdField);
		
		System.out.println("The object with references looks like:\n" + newObject);
		
		return newObject;
	}
	
	private static CircularNode createCircularNodes()
	{
		CircularNode first, second, third;
		
		System.out.println("This object is 3 nodes connected to one another in a circle (ie. 1 -> 2 -> 3 -> 1 etc.)");
		System.out.println("Please enter an identifier (integer) for the first node: ");
		first = new CircularNode(Integer.parseInt(in.nextLine()));
		System.out.println("Please enter an identifier (integer) for the second node: ");
		second = new CircularNode(Integer.parseInt(in.nextLine()));
		System.out.println("Please enter an identifier (integer) for the third node: ");
		third = new CircularNode(Integer.parseInt(in.nextLine()));
		
		first.next = second;
		second.next = third;
		third.next = first;
		
		System.out.println("Your circular chain looks like: " + first + " -> " + first.next + " -> " + first.next.next + " -> " + first.next.next.next);
		
		return first;
	}	
	
	private static ObjectWithPrimitiveArray createObjectWithPrimitiveArray()
	{
		ObjectWithPrimitiveArray newObject;
		int [] array;
		
		System.out.println("This object contains an array of integers.");
		System.out.println("Please enter how many entries the array should have: ");
		array = new int[Integer.parseInt(in.nextLine())];
		
		for(int i = 0; i < array.length; i++)
		{
			System.out.println("Please enter an integer value for element #" + i + ": ");
			array[i] = Integer.parseInt(in.nextLine());
		}
		
		newObject = new ObjectWithPrimitiveArray(array);
		
		System.out.println("Your object looks like: " + newObject);
		
		return newObject;
	}	
	
	private static ObjectWithObjectArray createObjectWithObjectArray()
	{
		ObjectWithObjectArray newObject;
		SimpleObject [] array;
		
		System.out.println("This object contains an array of simple objects.");
		System.out.println("Please enter how many entries the array should have: ");
		array = new SimpleObject[Integer.parseInt(in.nextLine())];
		
		for(int i = 0; i < array.length; i++)
		{
			System.out.println("Please enter the info for element #" + i + ": ");
			array[i] = createSimpleObject();
		}
		
		newObject = new ObjectWithObjectArray(array);
		
		System.out.println("Your object looks like: " + newObject);
		
		return newObject;
		
	}
	
	
	private static ObjectWithCollection createObjectWithCollection()
	{
		ObjectWithCollection newObject;
		ArrayList<SimpleObject> list = new ArrayList<SimpleObject>();
		int size;
		
		System.out.println("This object contains an ArrayList of simple objects.");
		System.out.println("Please enter how many entries the list should have: ");
		size = Integer.parseInt(in.nextLine());
		
		for(int i = 0; i < size; i++)
		{
			System.out.println("Please enter the info for element #" + i + ": ");
			list.add(createSimpleObject());
		}
		
		newObject = new ObjectWithCollection(list);
		
		System.out.println("Your object looks like: " + newObject);
		
		return newObject;
	}
	
}
