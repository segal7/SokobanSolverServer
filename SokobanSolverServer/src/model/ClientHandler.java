package model;

import java.io.IOException;
import java.net.Socket;
import java.util.Observable;

public abstract class ClientHandler extends Observable{
	
	protected Socket client_connection;
	
	public ClientHandler(Socket connection_socket){
		this.client_connection = connection_socket;
	}
	
	abstract void handleClient();
	abstract void disconnectUser();
}
