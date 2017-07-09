package multiplayer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	public NetworkWorld networkWorld;
	Socket socket;
	PrintWriter writer;
	ClientHandler clientHandler;
	
	boolean initialize = false;

	public Client(String name) {
		try {
			socket = new Socket("localhost", 5555);
			writer = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

		networkWorld = new NetworkWorld(name);
		clientHandler = new ClientHandler(socket, this);
		clientHandler.start();
	}
}
