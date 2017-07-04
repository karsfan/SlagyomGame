package Multiplayer;

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
		//this.run();
	}
	@Override
	public void run() {
			System.out.println("Starting server");
		//while (this.running) {
			final MultiplayerManager gameManager = new MultiplayerManager();
			
			/*final Socket socket1 = this.serverSocket.accept();

			System.out.println("CLIENT 1 CONNESSO");
			final GameClient cm1 = new GameClient(socket1, gameManager);
			gameManager.add(cm1);

			final Socket socket2 = this.serverSocket.accept();
			System.out.println("CLIENT 2 CONNESSO");

			final GameClient cm2 = new GameClient(socket2, gameManager);
			gameManager.add(cm2);*/
			
			ArrayList <Socket> sockets = new ArrayList <Socket>();
			ArrayList <GameClient> players = new ArrayList <GameClient>();
			
			for (int i = 0; i < 2; i++)
			{
				Socket tmpSocket = null;
				try {
					tmpSocket = this.serverSocket.accept();
				} catch (IOException e) {
					e.printStackTrace();
				}
				sockets.add(tmpSocket);
				System.out.println("CLIENT" + (i+1) + "CONNESSO");

				GameClient tmpPlayer = new GameClient("", tmpSocket, gameManager);
				players.add(tmpPlayer);
		
				try {
					gameManager.add(tmpPlayer);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			System.out.println("STARTING...");
		//}
	}
	
//	private void runGameServer() throws IOException {
//		this.serverSocket = new ServerSocket(this.port);
//		while (this.running) {
//			final MultiplayerManager gameManager = new MultiplayerManager();
//			
//			/*final Socket socket1 = this.serverSocket.accept();
//
//			System.out.println("CLIENT 1 CONNESSO");
//			final GameClient cm1 = new GameClient(socket1, gameManager);
//			gameManager.add(cm1);
//
//			final Socket socket2 = this.serverSocket.accept();
//			System.out.println("CLIENT 2 CONNESSO");
//
//			final GameClient cm2 = new GameClient(socket2, gameManager);
//			gameManager.add(cm2);*/
//			
//			ArrayList <Socket> sockets = new ArrayList <Socket>();
//			ArrayList <GameClient> players = new ArrayList <GameClient>();
//			
//			for (int i = 0; i < 3; i++)
//			{
//				Socket tmpSocket = this.serverSocket.accept();
//				sockets.add(tmpSocket);
//				System.out.println("CLIENT" + (i+1) + "CONNESSO");
//
//				GameClient tmpPlayer = new GameClient("", tmpSocket, gameManager);
//				players.add(tmpPlayer);
//		
//				gameManager.add(tmpPlayer);
//			}
//			
//			System.out.println("STARTING...");
//		}
//	}
	
}