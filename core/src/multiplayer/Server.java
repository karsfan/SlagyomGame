package multiplayer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import staticObjects.Item;
import staticObjects.Item.Level;
import staticObjects.StaticObject.Element;

public class Server {
	public ArrayList<ServerHandler> connected;
	ServerSocket serverSocket;
	int numPlayer;

	public Server(int port, int numPlayer) {
		this.numPlayer = 2;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		connected = new ArrayList<ServerHandler>();
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
		System.out.println("all player are connected");
		for (int i = 0; i < numPlayer; i++)
			for (ServerHandler serverHandler : connected) {
				serverHandler.writer.println(i);
				serverHandler.writer.flush();
			}
		for (int i = 0; i < 50; i++) {
			Item item = new Item();
			for (ServerHandler serverHandler : connected) {
				if (item.getElement() == Element.POTION && item.getLevel() == Level.FIRST)
					serverHandler.writer
							.println(15 + " " + 0 + " " + item.getX() + " " + item.getY() + " " + 0 + ";" + 0 + ";");
				else if (item.getElement() == Element.POTION && item.getLevel() == Level.SECOND)
					serverHandler.writer
							.println(16 + " " + 0 + " " + item.getX() + " " + item.getY() + " " + 0 + ";" + 0 + ";");
				else if (item.getElement() == Element.POTION && item.getLevel() == Level.THIRD)
					serverHandler.writer
							.println(17 + " " + 0 + " " + item.getX() + " " + item.getY() + " " + 0 + ";" + 0 + ";");
				else if (item.getElement() == Element.PARCHMENT && item.getLevel() == Level.FIRST)
					serverHandler.writer
							.println(18 + " " + 0 + " " + item.getX() + " " + item.getY() + " " + 0 + ";" + 0 + ";");
				else if (item.getElement() == Element.PARCHMENT && item.getLevel() == Level.SECOND)
					serverHandler.writer
							.println(19 + " " + 0 + " " + item.getX() + " " + item.getY() + " " + 0 + ";" + 0 + ";");
				else if (item.getElement() == Element.COIN)
					serverHandler.writer
							.println(20 + " " + 0 + " " + item.getX() + " " + item.getY() + " " + 0 + ";" + 0 + ";");
				serverHandler.writer.flush();
			}
		}
		for (int i = 0; i < numPlayer; i++)
			for (ServerHandler serverHandler : connected) {
				serverHandler.writer.println("ok");
				serverHandler.writer.flush();
			}
	}

	public void send(String message) {
		//System.out.println(message);
		for (ServerHandler serverHandler : connected) {
			serverHandler.writer.println(message);
			serverHandler.writer.flush();
		}
	}

//	public void close() {
//		for (ServerHandler serverHandler : connected) {
//			// serverHandler.close();
//		}
//		// serverSocket.close();
//	}

	public void send(String message, int iDreceiver) {
		for (ServerHandler serverHandler : connected) {
			if (serverHandler.ID == iDreceiver) {
				serverHandler.writer.println(message);
				serverHandler.writer.flush();
				break;
			}
		}
	}

}
