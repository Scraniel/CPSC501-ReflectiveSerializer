package objectInspector;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

public class Inspector implements ReflectiveInspector 
{
	HashSet<Object> inspected = new HashSet<Object>();
	
	// The driver behind the introspection. Gets the class and sends it off to be inspected.
	@Override
	public void inspect(Object obj, boolean recursive) 
	{
		System.out.println("RECURSIVE IS SET TO " + recursive + ". WILL" + (recursive ? " " : " NOT ") + "RECURSIVELY INSPECT FIELDS.");
		
		Object instance = obj;
		Class inspecting;
		
		// Checks if the added object was a Class; if so, this
		// means it's an interface that was added, so there is 
		// no instantiation of the object. Just inspect it as is.
		if(instance.getClass().isInstance(Class.class) )
		{
			inspecting = (Class)instance;
			instance = null;
		}
		else
		{
			inspecting = instance.getClass();
		}

		inspected.add(inspecting);
		
		// These two calls needed to be separated to make sure we don't
		// get duplicates when printing the inheritance hierarchy.
		System.out.println(findClassInfo(inspecting, instance, recursive, 0));
		System.out.println(String.join("\n" + Helpers.prefix(0), traverseInheritanceHierarcy(inspecting, instance, recursive, 0)));

	}
	
	// Returns a list of the declared fields in this class, and if recursive is set to true,
	// inspects all non-primitive fields.
	public ArrayList<String> findDeclaredFieldInfo(Class inspecting, Object instance, boolean recursive, int depth)
	{
		StringBuilder builder = new StringBuilder();
		Field [] fields = inspecting.getDeclaredFields();
		ArrayList<String> strings = new ArrayList<String>();
		
		for(Field field : fields)
		{
			builder.setLength(0);
			// Modifiers
			String modifier = Modifier.toString(field.getModifiers());
			builder.append(modifier);
			builder.append(' ');
			
			//Type 
			builder.append(field.getType().getCanonicalName());
			builder.append(' ');
			
			// Value; if there is no instance, don't bother checking
			builder.append(field.getName());
			if(instance != null)
			{
				field.setAccessible(true);
				builder.append(" = ");
				try {
					Object obj = field.get(instance);
					if(obj != null){
						// Dealing with pesky arrays
						if(obj.getClass().isArray())
							builder.append(Helpers.getInfoFromArray(obj));

						else
							builder.append(obj);
						
						// If the field is an object, inspect it
						if(!field.getType().isPrimitive() && recursive == true)
						{
							strings.add("***********************************************************");
							strings.add("Inspecting Field of " + inspecting.getCanonicalName() +": " + field.getName());
							strings.add("***********************************************************");
							if(!inspected.contains(obj))
							{
								inspected.add(obj);
								builder.append(findClassInfo(obj.getClass(), obj, false, depth + 1));
								builder.append(Helpers.prefix(depth + 1) + String.join("\n" + Helpers.prefix(depth + 1), traverseInheritanceHierarcy(obj.getClass(), obj, recursive, depth + 1)));
							}
							else
							{
								builder.append("This class has already been inspected!");
							}
						}
					}
					else
						builder.append(obj);
					
					
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			
			strings.add(builder.toString());
		}
		
		return strings;
		
	}
	
	// Inspects all superclasses in the inheritance hierarchy.
	public ArrayList<String> traverseInheritanceHierarcy(Class inspecting, Object instance, boolean recursive, int depth)
	{
		
		// Instead of using recursion, we can simply look all the way up the tree
		// and iterate over these superclasses
		StringBuilder builder = new StringBuilder();
		ArrayList<String> strings = new ArrayList<String>();
		ArrayList<Class> superClasses = new ArrayList<Class>();
		Class superClass = inspecting.getSuperclass();
		while(superClass != null)
		{
			superClasses.add(superClass);
			superClass = superClass.getSuperclass();
		}
		
		// Gets information on each class using the findClassInfo() method. This is why
		// we have to call this method directly after calling findClassInfo elsewhere,
		// because otherwise we would potentially look up the inheritance hierarchy of
		// objects multiple times.
		for(Class currentSuper : superClasses)
		{

			strings.add("***********************************************************");
			strings.add("Inspecting Superclass of " + inspecting.getCanonicalName() + ": " + currentSuper.getCanonicalName());
			strings.add("***********************************************************");

			builder.setLength(0);
			
			if(inspected.contains(currentSuper))
			{
				builder.append(Helpers.prefix(depth + 1) + "This class was already inspected!\n");
			}
			else
			{
				builder.append(findClassInfo(currentSuper, instance, recursive, depth + 1));
			}
			strings.add(builder.toString());
		}
		
		return strings;
	}
	
	
	// The recursive part. Calls all of the other methods which in turn recursively call this one.
	public String findClassInfo(Class inspecting, Object instance, boolean recursive, int depth)
	{
		StringBuilder builder = new StringBuilder();
		
		Class superClass = inspecting.getSuperclass();
		String superClassName = superClass == null ? null : superClass.getCanonicalName();
		
		
		builder.append("\n" + Helpers.prefix(depth) + "-----------------------------------------------------------\n" + Helpers.prefix(depth) + "NEW CLASS:\n" + Helpers.prefix(depth) + inspecting.getCanonicalName() + '\n');
		builder.append("\n" + Helpers.prefix(depth) + "Immediate Superclass Name:\n" + Helpers.prefix(depth) + superClassName + '\n');
		builder.append("\n" + Helpers.prefix(depth) + "Fields:\n" + Helpers.prefix(depth) + String.join("\n" + Helpers.prefix(depth), findDeclaredFieldInfo(inspecting, instance, recursive, depth + 1)) + '\n');
		builder.append("\n" + Helpers.prefix(depth) + "-----------------------------------------------------------\n");
		return builder.toString();
	}
	
}
