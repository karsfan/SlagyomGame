package multiplayer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	public static NetworkWorld networkWorld;
	Socket socket;
	PrintWriter writer;
	ClientHandler clientHandler;

	boolean initialize = false;

	public Client(String name) {
		try {
			socket = new Socket("localhost", 5555);
			writer = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

		networkWorld = new NetworkWorld(name);
		clientHandler = new ClientHandler(socket, this);
		clientHandler.start();
	}

	public void movesRight(float dt) {
		networkWorld.player.movesRight(dt);
		writer.println(networkWorld.player.ID + " "+ networkWorld.player.getX()+ " "+ networkWorld.player.getY()+" "+networkWorld.player.currentState+";");
		writer.flush();
	}

	public void movesLeft(float dt) {
		networkWorld.player.movesLeft(dt);
		writer.println(networkWorld.player.ID + " "+ networkWorld.player.getX()+ " "+ networkWorld.player.getY()+" "+networkWorld.player.currentState+";");
		writer.flush();
	}

	public void movesUp(float dt) {
		networkWorld.player.movesUp(dt);
		writer.println(networkWorld.player.ID + " "+ networkWorld.player.getX()+ " "+ networkWorld.player.getY()+" "+networkWorld.player.currentState+";");
		writer.flush();
	}

	public void movesDown(float dt) {
		networkWorld.player.movesDown(dt);
		writer.println(networkWorld.player.ID + " "+ networkWorld.player.getX()+ " "+ networkWorld.player.getY()+" "+networkWorld.player.currentState+";");
		writer.flush();
	}

}
