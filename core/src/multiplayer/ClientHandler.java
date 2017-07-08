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

	@Override
	public void run() {
		while (true) {
			try {
				String receivedMessage = reader.readLine();
				if (!client.initialize) {
					System.out.println(client.networkWorld.player.name);
					client.networkWorld.player.ID = Integer.parseInt(receivedMessage);
					client.initialize = true;
					client.networkWorld.player.player = true;
					if(client.networkWorld.player.ID%2==0){
						client.networkWorld.player.setX(GameConfig.WIDTH/2 - client.networkWorld.player.ID*40);
						client.networkWorld.player.setY(GameConfig.HEIGHT/2);
					}else{
						client.networkWorld.player.setX(GameConfig.WIDTH/2 + client.networkWorld.player.ID*40);
						client.networkWorld.player.setY(GameConfig.HEIGHT/2 - client.networkWorld.player.ID*40);
					}
				} else if (receivedMessage.equals("ok"))
					System.out.println("OK, PLAY");
				else {
					int ID = Integer.parseInt(receivedMessage);
					if (ID != client.networkWorld.player.ID) {
						NetworkPlayer otherPlayer = new NetworkPlayer();
						otherPlayer.ID = ID;
						if(ID%2==0){
							otherPlayer.setX(GameConfig.WIDTH/2 - ID*40);
							otherPlayer.setY(GameConfig.HEIGHT/2);
						}else{
							otherPlayer.setX(GameConfig.WIDTH/2 + ID*40);
							otherPlayer.setY(GameConfig.HEIGHT/2 - ID*40);
						}
						client.networkWorld.otherPlayers.add(otherPlayer);
						System.out.println("Creo un personaggio");
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
