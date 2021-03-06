package multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerHandler extends Thread {
	Server server;
	Socket socket;
    BufferedReader reader;
	PrintWriter writer;
	int ID;
	
	public ServerHandler(Socket socket, Server server, int ID) {
		this.socket = socket;
		this.server = server; 
		this.ID = ID;
		
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream());
			writer.println(ID);
			writer.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				String receivedMessage= reader.readLine();
				if(receivedMessage.contains(" ")){
					NetworkMessage message = new NetworkMessage(receivedMessage);
					if(message.action == 2 || message.action == 3 || message.action == 5 || message.action == 6){
						server.send(receivedMessage, message.IDreceiver);	
					}
					else{
						server.send(receivedMessage);
						if(message.action == 10 && message.ID == ID)
							server.connected.remove(this);
					}
				}
			} catch (IOException e) {
				server.send(10+ " "+ ID + " "+0+" "+0 +" "+0+";"+"111111"+";");
				server.connected.remove(this);
				break;
			}
		}
		
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
