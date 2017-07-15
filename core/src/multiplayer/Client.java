package multiplayer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import gameManager.GameSlagyom;
import staticObjects.Item;
import staticObjects.Item.Level;
import staticObjects.StaticObject.Element;

public class Client {
	public static NetworkWorld networkWorld;
	Socket socket;
	public PrintWriter writer;
	public ClientHandler clientHandler;
	GameSlagyom gameSlagyom;
	boolean initialize = false;
	public boolean go = false;
	//public boolean canModify = true;

	public Client(String name, GameSlagyom gameSlagyom) {
		try {
			socket = new Socket("localhost", 5555);
			writer = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.gameSlagyom = gameSlagyom;
		networkWorld = new NetworkWorld(name);
		clientHandler = new ClientHandler(socket, this);
		clientHandler.start();
	}

	public void movesRight(float dt) {
		networkWorld.player.movesRight(dt);
		if (networkWorld.player.collisionWithObject) {
			sendCollisionItem();
		} else if (!networkWorld.player.collideWithOtherPlayer) {
			writer.println(0 + " " + networkWorld.player.ID + " " + networkWorld.player.getX() + " "
					+ networkWorld.player.getY() + " " + networkWorld.player.currentState + ";" + "11111" + ";");
			writer.flush();
		} else if (networkWorld.player.collideWithOtherPlayer) {// sto
			// collidendo
			// con un
			// giocatore
			writer.println(1 + " " + networkWorld.player.ID + " " + 0 + " " + 0 + " " + 0 + ";"
					+ networkWorld.player.IDOtherPlayer + ";");
			writer.flush();
		}

	}

	public void sendCollisionItem() {
		Item item = networkWorld.player.itemPicked;
		if (item.getElement() == Element.POTION && item.getLevel() == Level.FIRST)
			writer.println(21 + " " + networkWorld.player.ID + " " + item.getX() + " " + item.getY() + " "
					+ networkWorld.player.currentState + ";" + 0 + ";");
		else if (item.getElement() == Element.POTION && item.getLevel() == Level.SECOND)
			writer.println(22 + " " + networkWorld.player.ID + " " + item.getX() + " " + item.getY() + " "
					+ networkWorld.player.currentState + ";" + 0 + ";");
		else if (item.getElement() == Element.POTION && item.getLevel() == Level.THIRD)
			writer.println(23 + " " + networkWorld.player.ID + " " + item.getX() + " " + item.getY() + " "
					+ networkWorld.player.currentState + ";" + 0 + ";");
		else if (item.getElement() == Element.PARCHMENT && item.getLevel() == Level.FIRST)
			writer.println(24 + " " + networkWorld.player.ID + " " + item.getX() + " " + item.getY() + " "
					+ networkWorld.player.currentState + ";" + 0 + ";");
		else if (item.getElement() == Element.PARCHMENT && item.getLevel() == Level.SECOND)
			writer.println(25 + " " + networkWorld.player.ID + " " + item.getX() + " " + item.getY() + " "
					+ networkWorld.player.currentState + ";" + 0 + ";");
		else if (item.getElement() == Element.COIN)
			writer.println(26 + " " + networkWorld.player.ID + " " + item.getX() + " " + item.getY() + " "
					+ networkWorld.player.currentState + ";" + 0 + ";");
		writer.flush();
	}

	public void movesLeft(float dt) {
		networkWorld.player.movesLeft(dt);
		if (networkWorld.player.collisionWithObject) {
			sendCollisionItem();
		} else if (!networkWorld.player.collideWithOtherPlayer) {
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
		if (networkWorld.player.collisionWithObject) {
			sendCollisionItem();
		} else if (!networkWorld.player.collideWithOtherPlayer) {
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
		if (networkWorld.player.collisionWithObject) {
			sendCollisionItem();
		} else if (!networkWorld.player.collideWithOtherPlayer) {
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
