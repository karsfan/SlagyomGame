package multiplayer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class GameServer extends Thread {

//	public static void main(final String[] args) throws IOException {
//		final GameServer server;
//		server = new GameServer(2727);
//		server.runGameServer();
//	}
	ArrayList <Socket> sockets = new ArrayList <Socket>();
	ArrayList <GameClient> clients = new ArrayList <GameClient>();
	
	private final int port;
	private final boolean running = true;
	private ServerSocket serverSocket;

	public GameServer(final int port) {
		this.port = port;
		try {
			this.serverSocket = new ServerSocket(this.port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Initializing server");
	}
	@Override
	public void run() {
			System.out.println("Starting server");
		while (this.running) {
			final MultiplayerManager gameManager = new MultiplayerManager();
			
			for (int i = 0; i < 2; i++)
			{
				Socket tmpSocket = null;
				try {
					tmpSocket = this.serverSocket.accept();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.out.println("CLIENT" + (i+1) + "CONNESSO");

				GameClient tmpPlayer = new GameClient("", tmpSocket, gameManager);
				clients.add(tmpPlayer);
				tmpPlayer.start();
		
				try {
					gameManager.add(tmpPlayer);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			System.out.println("STARTING...");
		}
	}

	
}