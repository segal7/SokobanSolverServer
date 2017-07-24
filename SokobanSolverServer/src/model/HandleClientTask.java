package model;

public class HandleClientTask implements Runnable{

	private ClientHandler ch;
	
	public HandleClientTask(ClientHandler ch) {
		this.ch = ch;
	}
	
	@Override
	public void run() {
		ch.handleClient();
	}

}
