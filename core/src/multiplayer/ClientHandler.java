package multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class ClientHandler extends Thread {
	Socket socket;
	Client client;
	BufferedReader reader;
	public ClientHandler(Socket socket, Client client) {
		this.socket = socket;
		this.client = client;
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				String receivedMessage = reader.readLine();
				if (receivedMessage.equals("ok"))
					System.out.println("OK, PLAY");
				else {
					int ID = Integer.parseInt(receivedMessage);
					if (ID != client.ID) {
						System.out.println("Creo un personaggio");
					}
				}
			
			
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
}
