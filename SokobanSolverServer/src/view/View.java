package view;

import java.util.List;

import model.ClientHandler;

public interface View {
	public void setViewModel(ViewModel vm);
	void setConnections(List<ClientHandler> list);
	void startButton();
	void stopButton();
	void clickConnection();
}
