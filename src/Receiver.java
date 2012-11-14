import java.io.*;
import java.net.*;

import org.jdom2.Document;

public class Receiver {
	public static void main(String[] args) throws IOException {
		int port = 3333;
		ServerSocket serverSocket = null;
		// set the server socket
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + port);
			System.exit(1);
		}
		System.out.println("Waiting for client request");
		while (true) {
			// listen for client
			Socket clientSocket = null;
			try {
				clientSocket = serverSocket.accept();
				System.out.println("Client connected");
			} catch (IOException e) {
				System.err.println("Accept failed.");
				System.exit(1);
			}
			
			//create the socket data streams
            DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromClient = new BufferedReader(
                    new InputStreamReader(
                    clientSocket.getInputStream()));
            //initialize the strings and protocol
            String inputLine;

            //listen for the client response 
            while ((inputLine = inFromClient.readLine()) != null) {
            	System.out.println(inputLine);
            }
		}
	}
}
