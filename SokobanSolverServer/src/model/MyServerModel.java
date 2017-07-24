package model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

import common.CommandType;

public class MyServerModel extends Observable implements ServerModel , Observer{
	
	private int port;

	private volatile boolean stop = true;
	private ServerSocket server = null;
	private List<ClientHandler> active_connections;
	private Thread clientAcceptingThread;
	
	private ForkJoinPool threadPool;
	
	public MyServerModel(int port,int maxActiveConnections){
		this.port = port;
		this.threadPool = new ForkJoinPool(maxActiveConnections);
		this.active_connections = new LinkedList<>();
	}
	
	@Override
	public void runServer(){
		if(this.stop == true){
			this.stop = false;
			System.out.println(">>starting the server.");
			try{ server = new ServerSocket(port); }
			catch( IOException e ) { e.printStackTrace(); }

			MyServerModel model = (MyServerModel) this;
			
			if(server != null){
				System.out.println(">>server is running and listening on port " + this.port +".");
				clientAcceptingThread = new Thread(new Runnable() {
					@Override
					public void run() {
						Socket aClient = null;
						while(!stop){ //server is still accepting users and running
								if(server.isClosed() == false){
									try{
										aClient = server.accept();
										ClientHandler ch = new SokobanClientHandler(aClient);
										active_connections.add(ch);
										System.out.println(">> client " + aClient.getInetAddress().toString() +":" + aClient.getPort() + " connected to the server.");
										ch.addObserver(model);
										threadPool.execute(new HandleClientTask(ch));
										setChanged();
										notifyObservers();
									}
									catch(SocketException e) {} 
									catch(IOException e) { e.printStackTrace(); }
								}
						}
						
						threadPool.shutdown();
						disconnectUsers();
						try { server.close(); }
						catch( IOException e) { e.printStackTrace(); }
					}
				},"client accepting thread");

				try{ clientAcceptingThread.start();} 
				catch (Exception e) {}

			}
			else{
				System.out.println(">>failed to start server.");
			}
		}
	}
	
	@Override
	public void stopServer(){
		if(this.stop != true){
			this.stop = true;
			System.out.println(">>closing the server.");
			try { server.close(); }
			catch (IOException e) { e.printStackTrace(); }
		}		
	}
	
	@Override
	public void disconnectUser(int idx){
		ClientHandler should_disconnect = active_connections.get(idx);
		if(should_disconnect != null)
			should_disconnect.disconnectUser();
		setChanged();
		notifyObservers();
	}
	public void disconnectUsers(){
		for(ClientHandler c : active_connections)
			c.disconnectUser();
		setChanged();
		notifyObservers();
	}

	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof ClientHandler){
			if(arg instanceof CommandType)
				if((CommandType)arg == CommandType.USER_DISCONNECT){
					active_connections.remove(o);
					setChanged();
					notifyObservers();
				}
		}	
	}

	@Override
	public List<ClientHandler> getActiveConnections() {
		return active_connections;
	}
	 
}
