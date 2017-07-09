package multiplayer;

public class ServerThread extends Thread{
	
	Server server;
	public ServerThread(Server server) {
		this.server=server;
		start();
	}
	@Override
	public void run() {
		server.start();
	}
}
