package reciever;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class Server {
	
	public static void main(String[] args)
	{
		if(args.length != 1)
		{
			System.out.println("Usage: java Server <listening port #>");
			return;
		}
		
		boolean listen = true;
		SAXBuilder builder = new SAXBuilder();
		Scanner in = new Scanner(System.in);
		int port = Integer.parseInt(args[0]);
		Deserializer deserializer = new Deserializer();
		
		try{
			
			ServerSocket servsock = new ServerSocket(port);
		    
		    while (listen) {
		    	
		    	System.out.println("Listening on port " + port + "...");
		    	Socket socket = servsock.accept();
		    	System.out.println("Connection established! Reading XML now...");
		      
		    	InputStream inputStream = socket.getInputStream();

		    	Document document = builder.build(inputStream);
		    	System.out.println("Deserializing object...");
		    	Object obj = deserializer.deserialize(document);
		    	System.out.println("Object deserialized! It looks like this:\n\n" + obj + "\n");
		      
		    	socket.close();
		      
		    	System.out.print("Do you want to attempt another connection? (Y/N): ");
		    	if(in.nextLine().compareToIgnoreCase("N") == 0)
		    		listen = false;
		    }
		    
		    in.close();
		    servsock.close();
		}
		catch(IOException e)
		{
			System.err.println("Could not connect on port " + port + ". Please try again.");
			e.printStackTrace();
		}
		catch(JDOMException e)
		{
			System.err.println("Something went wrong when building the XML document. Please try again.");
			e.printStackTrace();
		}
	}
}
