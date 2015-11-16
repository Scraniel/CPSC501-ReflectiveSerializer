package sender;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public static void sendDocument(String IP, int port, byte[] data)
	{
		try {
			Socket socket = new Socket(IP, port);

			OutputStream output = socket.getOutputStream();
			output.write(data, 0, data.length);
			output.flush();
			socket.close();			
			
		} catch (UnknownHostException e) {
			System.out.println("Unknown Host: " + IP + ". Please try again.");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
