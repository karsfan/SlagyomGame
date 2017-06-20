package Multiplayer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;



public class GameClient {

	public GameClient(int port) {
		BufferedReader in = null;
		BufferedWriter outRo = null;
	//	PrintStream out = null;
		Socket socket = null;
		String message;
		
		System.out.println("SONO IL CLIENT");
		try {
			
			// open a socket connection
			socket = new Socket("localhost", port);
			// Apre i canali I/O
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//out = new PrintStream(socket.getOutputStream(), true);
			outRo = new BufferedWriter (new OutputStreamWriter(socket.getOutputStream()));
			
			// Legge dal server
			message = in.readLine();
			System.out.print("Messaggio Ricevuto : " + message);
			outRo.close();
			//out.close();
			in.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
}