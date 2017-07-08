package multiplayer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class Server extends Thread {
	ArrayList<ServerHandler> connected;
	ServerSocket serverSocket;
	int numPlayer;

	public Server() {
		System.out.println("OIMFAPASMFOPASMOAFSP");
	}

	public Server(int port, int numPlayer) {
		this.numPlayer = 3;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		connected = new ArrayList<ServerHandler>();
		start();
	}

	public void start() {
		for (int i = 0; i < numPlayer; i++) {
			try {
				System.out.println("Connection..");
				Socket tmpSocket = serverSocket.accept();
				System.out.println("New connection..");
				ServerHandler newConnection = new ServerHandler(tmpSocket, this, i);
				connected.add(newConnection);
				newConnection.start();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		for (int i = 0; i < numPlayer; i++) 
			for (ServerHandler serverHandler : connected) {
				serverHandler.writer.println(i);
				serverHandler.writer.flush();
			}

			for (ServerHandler serverHandler : connected) {
				serverHandler.writer.println("ok");
				serverHandler.writer.flush();
			}
		
	}

	public void send(String message) {
		for (ServerHandler serverHandler : connected) {
			serverHandler.writer.println(message);
			serverHandler.writer.flush();
		}
	}

	public void close() {
		for (ServerHandler serverHandler : connected) {
			//serverHandler.close();
		}
		//serverSocket.close();
	}

}
