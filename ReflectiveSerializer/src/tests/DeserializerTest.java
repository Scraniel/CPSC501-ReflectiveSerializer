package tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.junit.BeforeClass;
import org.junit.Test;

import reciever.Deserializer;
import sender.Serializer;
import testClasses.CircularNode;
import testClasses.ObjectWithCollection;
import testClasses.ObjectWithObjectArray;
import testClasses.ObjectWithPrimitiveArray;
import testClasses.ObjectWithReferences;
import testClasses.SimpleObject;

public class DeserializerTest {

	static Deserializer deserializer;
	static XMLOutputter outputter;
	static SAXBuilder builder;
	
	@BeforeClass
	public static void setUp()
	{
		deserializer = new Deserializer();
		outputter = new XMLOutputter();
		builder = new SAXBuilder();
	}
	
	@Test
	public void testSimpleObject() 
	{
		// Simple Object
		SimpleObject first;
		first = new SimpleObject(0,0.0,'c', true);
		
		String XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<serialized>"
				+ "<object class=\"testClasses.SimpleObject\" id=\"0\">"
				+ "<field name=\"intField\" declaringClass=\"testClasses.SimpleObject\">"
				+ "<value>0</value></field><field name=\"doubleField\" declaringClass=\"testClasses.SimpleObject\">"
				+ "<value>0.0</value></field><field name=\"charField\" declaringClass=\"testClasses.SimpleObject\">"
				+ "<value>c</value></field><field name=\"booleanField\" declaringClass=\"testClasses.SimpleObject\">"
				+ "<value>true</value></field></object></serialized>\r\n";
		
		Object result = null;
		try {
			result = deserializer.deserialize(builder.build(new StringReader(XML)));
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(first, result);	
	}
	
	@Test
	public void testObjectWithReferences()
	{
		// Simple Objects
		SimpleObject first, second, third;
		first = new SimpleObject(0,0.0,'c', true);
		second = new SimpleObject(1,1.1,'h', false);
		third = new SimpleObject(2,2.2,'a', true);
				
		// Object with references
		ObjectWithReferences instance = new ObjectWithReferences(first, second, third);
		
		String XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<serialized><object class=\"testClasses.ObjectWithReferences\" id=\"0\">"
				+ "<field name=\"firstField\" declaringClass=\"testClasses.ObjectWithReferences\"><reference>1</reference></field>"
				+ "<field name=\"secondField\" declaringClass=\"testClasses.ObjectWithReferences\"><reference>2</reference></field>"
				+ "<field name=\"thirdField\" declaringClass=\"testClasses.ObjectWithReferences\"><reference>3</reference></field>"
				+ "</object><object class=\"testClasses.SimpleObject\" id=\"1\"><field name=\"intField\" declaringClass=\"testClasses.SimpleObject\">"
				+ "<value>0</value></field><field name=\"doubleField\" declaringClass=\"testClasses.SimpleObject\"><value>0.0</value>"
				+ "</field><field name=\"charField\" declaringClass=\"testClasses.SimpleObject\"><value>c</value></field>"
				+ "<field name=\"booleanField\" declaringClass=\"testClasses.SimpleObject\"><value>true</value></field></object>"
				+ "<object class=\"testClasses.SimpleObject\" id=\"2\"><field name=\"intField\" declaringClass=\"testClasses.SimpleObject\">"
				+ "<value>1</value></field><field name=\"doubleField\" declaringClass=\"testClasses.SimpleObject\"><value>1.1</value></field>"
				+ "<field name=\"charField\" declaringClass=\"testClasses.SimpleObject\"><value>h</value></field>"
				+ "<field name=\"booleanField\" declaringClass=\"testClasses.SimpleObject\"><value>false</value></field></object>"
				+ "<object class=\"testClasses.SimpleObject\" id=\"3\"><field name=\"intField\" declaringClass=\"testClasses.SimpleObject\">"
				+ "<value>2</value></field><field name=\"doubleField\" declaringClass=\"testClasses.SimpleObject\"><value>2.2</value></field>"
				+ "<field name=\"charField\" declaringClass=\"testClasses.SimpleObject\"><value>a</value></field>"
				+ "<field name=\"booleanField\" declaringClass=\"testClasses.SimpleObject\"><value>true</value></field></object></serialized>\r\n";
		
		Object result = null;
		try {
			result = deserializer.deserialize(builder.build(new StringReader(XML)));
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(instance, result);
		
		// Circular references (1 -> 2 -> 3 -> 1)
		CircularNode one, two, three;
		one = new CircularNode(1);
		two = new CircularNode(2);
		three = new CircularNode(3);
		one.next = two;
		two.next = three;
		three.next = one;
		
		XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<serialized><object class=\"testClasses.CircularNode\" id=\"0\">"
				+ "<field name=\"identifier\" declaringClass=\"testClasses.CircularNode\"><value>1</value></field>"
				+ "<field name=\"next\" declaringClass=\"testClasses.CircularNode\"><reference>1</reference></field></object>"
				+ "<object class=\"testClasses.CircularNode\" id=\"2\"><field name=\"identifier\" declaringClass=\"testClasses.CircularNode\">"
				+ "<value>3</value></field><field name=\"next\" declaringClass=\"testClasses.CircularNode\"><reference>0</reference></field>"
				+ "</object><object class=\"testClasses.CircularNode\" id=\"1\"><field name=\"identifier\" declaringClass=\"testClasses.CircularNode\">"
				+ "<value>2</value></field><field name=\"next\" declaringClass=\"testClasses.CircularNode\"><reference>2</reference></field></object>"
				+ "</serialized>\r\n";
		
		result = null;
		try {
			result = deserializer.deserialize(builder.build(new StringReader(XML)));
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(one, result);
	}
	
	@Test
	public void testObjectWithPrimitiveArray()
	{
		// Primitive array
		ObjectWithPrimitiveArray pArray = new ObjectWithPrimitiveArray(new int[]{1,2,3,4,5});
		
		String XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<serialized>"
				+ "<object class=\"testClasses.ObjectWithPrimitiveArray\" id=\"0\">"
				+ "<field name=\"array\" declaringClass=\"testClasses.ObjectWithPrimitiveArray\">"
				+ "<reference>1</reference></field></object><object class=\"[I\" id=\"1\" length=\"5\"><value>1</value>"
				+ "<value>2</value><value>3</value><value>4</value><value>5</value></object></serialized>\r\n";
		
		Object result = null;
		try {
			result = deserializer.deserialize(builder.build(new StringReader(XML)));
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(pArray, result);
	}
	
	@Test
	public void testObjectWithObjectArray()
	{
		// Simple Objects
		SimpleObject first, second, third;
		first = new SimpleObject(0,0.0,'c', true);
		second = new SimpleObject(1,1.1,'h', false);
		third = new SimpleObject(2,2.2,'a', true);
		
		// object array
		ObjectWithObjectArray oArray = new ObjectWithObjectArray(new SimpleObject[]{first, second, third});
		
		String XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<serialized>"
				+ "<object class=\"testClasses.ObjectWithObjectArray\" id=\"0\">"
				+ "<field name=\"array\" declaringClass=\"testClasses.ObjectWithObjectArray\">"
				+ "<reference>1</reference></field></object><object class=\"testClasses.SimpleObject\" id=\"2\">"
				+ "<field name=\"intField\" declaringClass=\"testClasses.SimpleObject\"><value>0</value></field>"
				+ "<field name=\"doubleField\" declaringClass=\"testClasses.SimpleObject\"><value>0.0</value>"
				+ "</field><field name=\"charField\" declaringClass=\"testClasses.SimpleObject\"><value>c</value>"
				+ "</field><field name=\"booleanField\" declaringClass=\"testClasses.SimpleObject\"><value>true</value>"
				+ "</field></object><object class=\"testClasses.SimpleObject\" id=\"3\">"
				+ "<field name=\"intField\" declaringClass=\"testClasses.SimpleObject\"><value>1</value></field>"
				+ "<field name=\"doubleField\" declaringClass=\"testClasses.SimpleObject\"><value>1.1</value>"
				+ "</field><field name=\"charField\" declaringClass=\"testClasses.SimpleObject\"><value>h</value>"
				+ "</field><field name=\"booleanField\" declaringClass=\"testClasses.SimpleObject\"><value>false</value>"
				+ "</field></object><object class=\"testClasses.SimpleObject\" id=\"4\">"
				+ "<field name=\"intField\" declaringClass=\"testClasses.SimpleObject\"><value>2</value></field>"
				+ "<field name=\"doubleField\" declaringClass=\"testClasses.SimpleObject\"><value>2.2</value></field>"
				+ "<field name=\"charField\" declaringClass=\"testClasses.SimpleObject\"><value>a</value></field>"
				+ "<field name=\"booleanField\" declaringClass=\"testClasses.SimpleObject\"><value>true</value></field>"
				+ "</object><object class=\"[LtestClasses.SimpleObject;\" id=\"1\" length=\"3\"><reference>2</reference>"
				+ "<reference>3</reference><reference>4</reference></object></serialized>\r\n";
		
		Object result = null;
		try {
			result = deserializer.deserialize(builder.build(new StringReader(XML)));
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(oArray, result);
	}
	
	@Test
	public void testObjectWithCollection()
	{
		// Simple Objects
		SimpleObject first, second, third;
		first = new SimpleObject(0,0.0,'c', true);
		second = new SimpleObject(1,1.1,'h', false);
		third = new SimpleObject(2,2.2,'a', true);
		
		// Collection holding things
		ArrayList<SimpleObject> list = new ArrayList<SimpleObject>();
		list.add(first);
		list.add(second);
		list.add(third);
		
		// with collection
		ObjectWithCollection col = new ObjectWithCollection(list);
		
		String XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<serialized>"
				+ "<object class=\"testClasses.ObjectWithCollection\" id=\"0\">"
				+ "<field name=\"list\" declaringClass=\"testClasses.ObjectWithCollection\">"
				+ "<reference>1</reference></field></object><object class=\"testClasses.SimpleObject\" id=\"3\">"
				+ "<field name=\"intField\" declaringClass=\"testClasses.SimpleObject\"><value>0</value></field>"
				+ "<field name=\"doubleField\" declaringClass=\"testClasses.SimpleObject\"><value>0.0</value>"
				+ "</field><field name=\"charField\" declaringClass=\"testClasses.SimpleObject\"><value>c</value>"
				+ "</field><field name=\"booleanField\" declaringClass=\"testClasses.SimpleObject\"><value>true</value>"
				+ "</field></object><object class=\"testClasses.SimpleObject\" id=\"4\">"
				+ "<field name=\"intField\" declaringClass=\"testClasses.SimpleObject\"><value>1</value></field>"
				+ "<field name=\"doubleField\" declaringClass=\"testClasses.SimpleObject\"><value>1.1</value></field>"
				+ "<field name=\"charField\" declaringClass=\"testClasses.SimpleObject\"><value>h</value></field>"
				+ "<field name=\"booleanField\" declaringClass=\"testClasses.SimpleObject\"><value>false</value></field>"
				+ "</object><object class=\"testClasses.SimpleObject\" id=\"5\">"
				+ "<field name=\"intField\" declaringClass=\"testClasses.SimpleObject\"><value>2</value></field>"
				+ "<field name=\"doubleField\" declaringClass=\"testClasses.SimpleObject\"><value>2.2</value></field>"
				+ "<field name=\"charField\" declaringClass=\"testClasses.SimpleObject\"><value>a</value></field>"
				+ "<field name=\"booleanField\" declaringClass=\"testClasses.SimpleObject\"><value>true</value>"
				+ "</field></object><object class=\"[Ljava.lang.Object;\" id=\"2\" length=\"10\"><reference>3</reference>"
				+ "<reference>4</reference><reference>5</reference><reference>null</reference><reference>null</reference>"
				+ "<reference>null</reference><reference>null</reference><reference>null</reference>"
				+ "<reference>null</reference><reference>null</reference></object>"
				+ "<object class=\"java.util.ArrayList\" id=\"1\">"
				+ "<field name=\"elementData\" declaringClass=\"java.util.ArrayList\"><reference>2</reference></field>"
				+ "<field name=\"size\" declaringClass=\"java.util.ArrayList\"><value>3</value></field>"
				+ "<field name=\"modCount\" declaringClass=\"java.util.AbstractList\"><value>3</value></field></object>"
				+ "</serialized>\r\n";
		
		Object result = null;
		try {
			result = deserializer.deserialize(builder.build(new StringReader(XML)));
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(col, result);
	}

}
