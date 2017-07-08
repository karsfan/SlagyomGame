package multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionManager implements Runnable {
	private String name = "Prova1";
	private PrintWriter writer;
	private BufferedReader reader;
	private Socket socket;

	public ConnectionManager() {
		try {
			this.socket = new Socket("localhost", 2727);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.writer = new PrintWriter(this.socket.getOutputStream(), true);
			while (true) {
				System.out.println("invio");
				String toSend = "#Server " + this.name + " Ciao";
				this.writer.println(toSend);
				System.out.println(this.reader.readLine());
				System.out.println("ricevuto");
				System.out.println(this.reader.readLine());
			}
		} catch (IOException e) {
			this.writer.println("#Error");
		}
		close();
	}

	private void close() {
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new ConnectionManager().run();
	}
}
