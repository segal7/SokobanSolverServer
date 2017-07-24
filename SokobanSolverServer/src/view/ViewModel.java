package view;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.ClientHandler;
import model.ServerModel;

public class ViewModel extends Observable implements Observer{
	ServerModel model;
	
	StringProperty status;
	
	public ViewModel(ServerModel model){
		this.model = model;
		this.status = new SimpleStringProperty();
		this.status.set("Off");
	}
	
	public void startServer(){
		this.status.set("On");
		model.runServer();
	}
	
	public void stopServer(){
		this.status.set("Off");
		model.stopServer();
	}

	public List<ClientHandler> getConnections(){
		return model.getActiveConnections();
	}
	
	public void disconnectUser(int idx){
		model.disconnectUser(idx);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		setChanged();
		notifyObservers();
	}
}
