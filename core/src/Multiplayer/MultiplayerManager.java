package Multiplayer;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class MultiplayerManager extends Thread{

	private final Set<GameClient> clients = new HashSet<GameClient>();
	// private int serverConnected = 0;
	private String message = "Ciao";
	private String messageServer = "I'm A Server";
	private String messageClient = "I'm A Client";

	public void add(GameClient client) throws IOException {
		this.clients.add(client);
		if (this.clients.size() == 2)
			setup();
	}

	private void setup() {
		for (GameClient client : this.clients) {
			client.setOnline(true);
			new Thread(client, client.toString()).start();
			String buffer = client.receive();
			System.out.println(buffer);
			if (buffer.contains("#Server"))
				client.setServer(true);
			else
				client.setServer(false);
		}
		this.dispatchStart();
		this.start();
	}

	public void dispatch(final GameClient senderClient) {
		for (final GameClient client : this.clients)
			if (!client.equals(senderClient))
				client.dispatch(this.message);
	}

	public void dispatchStart() {
		for (final GameClient client : this.clients)
			client.dispatch(this.message);
	}

	@Override
	public void run() {
		while (this.message != "#Error") {
			for (final GameClient client : this.clients)
				if (client.isOnline())
					client.send();
			for (final GameClient client : this.clients) {
				System.out.println("Older Message: " + this.message);
				this.message = client.receive();
				System.out.println("Messaggio Ricevuto: " + this.message);
			}
		}
	}
}