package multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class GameClient extends Thread {
	Socket socket;
	String name;
	public String message;
	private BufferedReader reader;
	private PrintWriter writer;
	private MultiplayerManager gameManager;

	private boolean server;
	private boolean online;
	
	public GameClient(String name, Socket socket, MultiplayerManager gameManager) {
		this.name = name;
		this.socket = socket;
		this.gameManager = gameManager;
		try {
			this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.writer = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	@Override
//	public void run() {
//		try {
//			message = reader.readLine();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
	public void setOnline(boolean online) {
		this.online = online;
	}

	public boolean isOnline() {
		return this.online;
	}
	
	public void dispatch(final String message) {
		if ((this.writer != null) && (message != null)) {
//			Scanner s = new Scanner(System.in);
//			System.out.println("NOME: " + name);			
			this.writer.println(message);
//			this.writer.println(s.next());
		}
		
	}

	public void send() {
		this.gameManager.dispatch(this);
	}

	public String receive() {
		try {
			System.out.println("Provo A Ricevere");
			return this.reader.readLine();
		} catch (IOException e) {
			return "#Error";
		}
	}

	public boolean isServer() {
		return this.server;
	}

	public void setServer(boolean server) {
		this.server = server;
	}
		
}