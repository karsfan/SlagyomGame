package multiplayer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	NetworkWorld networkWorld;
	Socket socket;
	PrintWriter writer;
	ClientHandler clientHandler;
	int ID; 
	
	public Client(int ID) {
		try {
			socket = new Socket("localhost", 5555);
			writer = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.ID = ID;
		
		clientHandler = new ClientHandler (socket, this); 
		clientHandler.start();
		networkWorld = new NetworkWorld("Ciccio");
	}
}
