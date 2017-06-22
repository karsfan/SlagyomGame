package Multiplayer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class GameServer extends Thread {
	private ServerSocket server;

	/*public static void main(String[] args) throws Exception {
		new GameServer();
	}*/

	public GameServer(int port) throws Exception {
		server = new ServerSocket(port);
		System.out.println("Il server è in attesa sulla porta " + port);
		this.start();
	}

	public void run() {
		while (true) {
			try {
				System.out.println("SONO IL SERVER");

				System.out.println("In attesa di connessione...");
				Socket client = server.accept(); // quando il client si connette
													// al server
				System.out.println("Connessione accettata da: " + client.getInetAddress());
				Connect c = new Connect(client);
			} catch (Exception e) {
			}

		}
	}
}

class Connect extends Thread {
	private Socket client = null;
	BufferedReader in = null;
	BufferedWriter outRo = null;
	PrintStream out = null;
	
	
	public Connect() {
	}

	public Connect(Socket clientSocket) {
		client = clientSocket;
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintStream(client.getOutputStream(), true);
			outRo = new BufferedWriter (new OutputStreamWriter(client.getOutputStream()));
		} catch (Exception e1) {
			try {
				client.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			return;
		}
		this.start();
	}
	Scanner scanner = new Scanner(System.in);
	public void run() {
		try {
			//out.println("Ciao " + MultiplayerScreen.multiplayerCharName + "!");
			String message = scanner.next();
			outRo.write(message);
			
			
			//out.flush();
			outRo.flush();
			
			// chiude gli stream e le connessioni
			//out.close();
			outRo.close();
			in.close();
			client.close();
		} catch (Exception e) {
		}
	}
}