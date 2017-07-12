package multiplayer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	public static NetworkWorld networkWorld;
	Socket socket;
	public PrintWriter writer;
	public ClientHandler clientHandler;

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
		if (!networkWorld.player.collideWithOtherPlayer) {
			writer.println(0 + " " + networkWorld.player.ID + " " + networkWorld.player.getX() + " "
					+ networkWorld.player.getY() + " " + networkWorld.player.currentState + ";" + "11111" + ";");
			writer.flush();
		} else {
			writer.println(1 + " " + networkWorld.player.ID + " " + 0 + " " + 0 + " " + 0 + ";"
					+ networkWorld.player.IDOtherPlayer + ";");
			writer.flush();
		}
	}

	public void movesLeft(float dt) {
		networkWorld.player.movesLeft(dt);
		if (!networkWorld.player.collideWithOtherPlayer) {
			writer.println(0 + " " + networkWorld.player.ID + " " + networkWorld.player.getX() + " "
					+ networkWorld.player.getY() + " " + networkWorld.player.currentState + ";" + "11111" + ";");
			writer.flush();
		} else {
			writer.println(1 + " " + networkWorld.player.ID + " " + 0 + " " + 0 + " " + 0 + ";"
					+ networkWorld.player.IDOtherPlayer + ";");
			writer.flush();
		}
	}

	public void movesUp(float dt) {
		networkWorld.player.movesUp(dt);
		if (!networkWorld.player.collideWithOtherPlayer) {
			writer.println(0 + " " + networkWorld.player.ID + " " + networkWorld.player.getX() + " "
					+ networkWorld.player.getY() + " " + networkWorld.player.currentState + ";" + "11111" + ";");
			writer.flush();
		} else {
			writer.println(1 + " " + networkWorld.player.ID + " " + 0 + " " + 0 + " " + 0 + ";"
					+ networkWorld.player.IDOtherPlayer + ";");
			writer.flush();
		}

	}

	public void movesDown(float dt) {
		networkWorld.player.movesDown(dt);
		if (!networkWorld.player.collideWithOtherPlayer) {
			writer.println(0 + " " + networkWorld.player.ID + " " + networkWorld.player.getX() + " "
					+ networkWorld.player.getY() + " " + networkWorld.player.currentState + ";" + "11111" + ";");
			writer.flush();
		} else {
			writer.println(1 + " " + networkWorld.player.ID + " " + 0 + " " + 0 + " " + 0 + ";"
					+ networkWorld.player.IDOtherPlayer + ";");
			writer.flush();
		}

	}

}
