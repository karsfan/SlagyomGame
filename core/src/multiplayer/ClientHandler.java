package multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import world.GameConfig;

public class ClientHandler extends Thread {
	Socket socket;
	Client client;
	BufferedReader reader;

	public ClientHandler(Socket socket, Client client) {
		this.socket = socket;
		this.client = client;
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public void run() {
		while (true) {
			try {
				String receivedMessage = reader.readLine();
				if (!receivedMessage.contains(" ")) {
					if (!client.initialize) {
						System.out.println(client.networkWorld.player.name);
						client.networkWorld.player.ID = Integer.parseInt(receivedMessage);
						client.initialize = true;
						client.networkWorld.player.player = true;
						if (client.networkWorld.player.ID % 2 == 0) {
							client.networkWorld.player.setX((int) (GameConfig.WIDTH / 2 - client.networkWorld.player.ID * 40));
							client.networkWorld.player.setY((int) (GameConfig.HEIGHT / 2));
						} else {
							client.networkWorld.player.setX((int) (GameConfig.WIDTH / 2 + client.networkWorld.player.ID * 40));
							client.networkWorld.player.setY((int) (GameConfig.HEIGHT / 2 - client.networkWorld.player.ID * 40));
						}
					} else if (receivedMessage.equals("ok"))
						System.out.println("OK, PLAY");
					else {
						int ID = Integer.parseInt(receivedMessage);
						if (ID != client.networkWorld.player.ID) {
							NetworkPlayer otherPlayer = new NetworkPlayer();
							otherPlayer.ID = ID;
							if (ID % 2 == 0) {
								otherPlayer.setX((int) (GameConfig.WIDTH / 2 - ID * 40));
								otherPlayer.setY((int) (GameConfig.HEIGHT / 2));
							} else {
								otherPlayer.setX((int) (GameConfig.WIDTH / 2 + ID * 40));
								otherPlayer.setY((int) (GameConfig.HEIGHT / 2 - ID * 40));
							}
							client.networkWorld.otherPlayers.add(otherPlayer);
							System.out.println("Creo un personaggio");
						}
					}
				} else {
					// System.out.println(receivedMessage);
					NetworkMessage message = new NetworkMessage(receivedMessage);
					//System.out.println("Leggo "+receivedMessage+"sono "+client.networkWorld.player.ID);
					for (NetworkPlayer player : client.networkWorld.otherPlayers) {
						if (message.action == 0) {
							if (player.ID == message.ID) {
								player.x = message.x;
								player.y = message.y;
								player.setState(message.currentState);
								break;
							}
						}
					}
					if (message.action == 1) {
						if (client.networkWorld.player.ID == message.ID) {
							System.out.println("Sono "+client.networkWorld.player.ID+"Ricevo che devo combattere con "+message.IDreceiver);
							client.networkWorld.player.readyToFight = true;
						} else if (client.networkWorld.player.ID == message.IDreceiver) {
							System.out.println("Sono "+client.networkWorld.player.ID+"   Ricevo che devo combattere con "+message.ID);
							client.networkWorld.player.IDOtherPlayer = message.ID;
							client.networkWorld.player.readyToFight = true;
						}
					}
					else if(message.action == 2){
						if(client.networkWorld.player.ID == message.IDreceiver){
							System.out.println(message.x +" "+message.y+" "+message.currentState);
							client.networkWorld.battle.enemy.x= message.x;
							client.networkWorld.battle.enemy.x= message.y;
							client.networkWorld.battle.enemy.setState(message.currentState);
						}
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
