package view;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.ClientHandler;

public class AdminLogic implements Observer , View{
	ViewModel vm;
	
	@FXML
	TextField server_status;
	@FXML
	ListView<ClientHandler> connections;
	int size = 0;
	
	public AdminLogic(){
	}
	
	public void setViewModel(ViewModel vm){
		this.vm = vm;
		server_status.textProperty().bind(vm.status);
		ObservableList<ClientHandler> items = FXCollections.observableList(vm.getConnections());
		connections.setItems(items);
		
	}
	@Override
	public void startButton(){
		vm.startServer();
	}
	@Override
	public void stopButton(){
		vm.stopServer();
	}
	@Override
	public void clickConnection(){
		int i = connections.getFocusModel().getFocusedIndex();
		if(vm.getConnections().isEmpty())
			return;
		vm.disconnectUser(i);
	}
	@Override
	public void setConnections(List<ClientHandler> list){
		ObservableList<ClientHandler> items = FXCollections.observableList(list);
		connections.setItems(items);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		List<ClientHandler> list = vm.getConnections();
		this.setConnections(list);
	}
}
