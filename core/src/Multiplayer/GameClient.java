package Multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameClient extends Thread {
	Socket socket;
	private BufferedReader reader;
	private PrintWriter writer;
	private MultiplayerManager gameManager;

	private boolean server;
	private boolean online;
	
	public GameClient(Socket socket, MultiplayerManager gameManager) {
		this.socket = socket;
		this.gameManager = gameManager;
		try {
			this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.writer = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setOnline(boolean online) {
		this.online = online;
	}

	public boolean isOnline() {
		return this.online;
	}
	
	public void dispatch(final String message) {
		if ((this.writer != null) && (message != null))
			this.writer.println(message);
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