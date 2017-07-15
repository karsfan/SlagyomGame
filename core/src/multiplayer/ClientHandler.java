package multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import character.Bomb;
import character.DynamicObjects.StateDynamicObject;
import character.Weapon.Level;
import character.Weapon.Type;
import staticObjects.Item;
import staticObjects.StaticObject;
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
						// System.out.println(client.networkWorld.player.name);
						client.networkWorld.player.ID = Integer.parseInt(receivedMessage);
						client.initialize = true;
						client.networkWorld.player.player = true;
						if (client.networkWorld.player.ID % 2 == 0) {
							client.networkWorld.player
									.setX((int) (GameConfig.WIDTH / 2 - client.networkWorld.player.ID * 40));
							client.networkWorld.player.setY((int) (GameConfig.HEIGHT / 2));
						} else {
							client.networkWorld.player
									.setX((int) (GameConfig.WIDTH / 2 + client.networkWorld.player.ID * 40));
							client.networkWorld.player
									.setY((int) (GameConfig.HEIGHT / 2 - client.networkWorld.player.ID * 40));
						}
					} else if (receivedMessage.equals("ok")) {
						client.go = true;
						System.out.println("OK, PLAY");
					} else {
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

					NetworkMessage message = new NetworkMessage(receivedMessage);
					for (NetworkPlayer player : client.networkWorld.otherPlayers) {
						if (message.action == 0 ) {
							if (player.ID == message.ID) {
								player.x = message.x;
								player.y = message.y;
								player.setState(message.currentState);
								break;
							}
						} else if (message.action == 10) {
							if (player.ID == message.ID) {
								if (message.ID == 0)
									client.gameSlagyom.screenManager.swapScreen(gameManager.ScreenManager.State.MENU);
								client.networkWorld.otherPlayers.remove(player);
								break;
							}
						}else if(message.action>=21 && message.action<=26){
							if (player.ID == message.ID) {
								player.setState(message.currentState);
								
							}
						}
					}
					if (message.action >= 15 && message.action <= 20) {
						Item item = null;
						if (message.action == 15) {
							item = new Item(message.x, message.y, StaticObject.Element.POTION, Item.Level.FIRST);
							client.networkWorld.getListItems().add(item);
						} else if (message.action == 16) {
							item = new Item(message.x, message.y, StaticObject.Element.POTION, Item.Level.SECOND);
							client.networkWorld.getListItems().add(item);
						} else if (message.action == 17) {
							item = new Item(message.x, message.y, StaticObject.Element.POTION, Item.Level.THIRD);
							client.networkWorld.getListItems().add(item);
						} else if (message.action == 18) {
							item = new Item(message.x, message.y, StaticObject.Element.PARCHMENT, Item.Level.FIRST);
							client.networkWorld.getListItems().add(item);
						} else if (message.action == 19) {
							item = new Item(message.x, message.y, StaticObject.Element.PARCHMENT, Item.Level.SECOND);
							client.networkWorld.getListItems().add(item);
						} else if (message.action == 20) {
							item = new Item(message.x, message.y, StaticObject.Element.COIN, Item.Level.FIRST);
							client.networkWorld.getListItems().add(item);
						}
					} else if (message.action >= 21 && message.action <= 26) {
						Item item = null;
						if (message.action == 21) {
							item = new Item(message.x, message.y, StaticObject.Element.POTION, Item.Level.FIRST);
						} else if (message.action == 22) {
							item = new Item(message.x, message.y, StaticObject.Element.POTION, Item.Level.SECOND);
						} else if (message.action == 23) {
							item = new Item(message.x, message.y, StaticObject.Element.POTION, Item.Level.THIRD);
						} else if (message.action == 24) {
							item = new Item(message.x, message.y, StaticObject.Element.PARCHMENT, Item.Level.FIRST);
						} else if (message.action == 25) {
							item = new Item(message.x, message.y, StaticObject.Element.PARCHMENT, Item.Level.SECOND);
						} else if (message.action == 26) {
							item = new Item(message.x, message.y, StaticObject.Element.COIN, Item.Level.FIRST);
						}
						for (Item itemeliminare : client.networkWorld.getListItems()) {
							if (itemeliminare.getX() == item.getX() && itemeliminare.getY() == item.getY()
									&& itemeliminare.getElement() == item.getElement()
									&& itemeliminare.getLevel() == item.getLevel()) {
//								while (!client.canModify) {
//									System.out.println("Fermo");
//								}
								itemeliminare.picked = true;
								//client.networkWorld.getListItems().remove(itemeliminare);
							}
						}
					}
					if (message.action == 1) {
						if (client.networkWorld.player.ID == message.ID) {
							client.networkWorld.player.readyToFight = true;
						} else if (client.networkWorld.player.ID == message.IDreceiver) {
							client.networkWorld.player.IDOtherPlayer = message.ID;
							client.networkWorld.player.readyToFight = true;
						}
					} else if (message.action == 2) {
						if (client.networkWorld.player.ID == message.IDreceiver) {
							client.networkWorld.battle.enemy.y = message.y;
							client.networkWorld.battle.enemy.setState(message.currentState);
							if (message.currentState == StateDynamicObject.RUNNINGLEFT) {
								client.networkWorld.battle.enemy.movesLeft(message.x);
							} else if (message.currentState == StateDynamicObject.RUNNINGRIGHT) {
								client.networkWorld.battle.enemy.movesRight(message.x);
							} else if (message.currentState == StateDynamicObject.FIGHTINGLEFT) {
								System.out.println("fightLeft");
								((NetworkCharacterBattle) client.networkWorld.battle.enemy).fightLeft(message.x);
							} else if (message.currentState == StateDynamicObject.FIGHTINGRIGHT) {
								((NetworkCharacterBattle) client.networkWorld.battle.enemy).fightRight(message.x);
							} else if (message.currentState == StateDynamicObject.JUMPING) {
								((NetworkCharacterBattle) client.networkWorld.battle.enemy).jump(message.x);
							}
						}

					} else if(message.action == 3){
						if(client.networkWorld.player.ID == message.IDreceiver){
							Bomb bomb = null;
							if(message.x == 1)
								bomb = new Bomb(Level.lev1, Type.Bomba);
							else if(message.x == 2)
								bomb = new Bomb(Level.lev1, Type.Bomba);
							else if(message.x == 3)
								bomb = new Bomb(Level.lev1, Type.Bomba);
							((NetworkCharacterBattle) client.networkWorld.battle.enemy).bag.add(bomb);
							System.out.println(message.y);
							System.out.println(client.networkWorld.battle.enemy.x - client.networkWorld.battle.character.x);
							((NetworkCharacterBattle) client.networkWorld.battle.enemy).forza = (int) message.y;
							((NetworkCharacterBattle) client.networkWorld.battle.enemy).lancia();
						}
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
