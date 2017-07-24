package model;

import java.util.List;
import java.util.Set;

public interface ServerModel {
	public void runServer();
	public void stopServer();
	public void disconnectUser(int idx);
	public List<ClientHandler> getActiveConnections();
}
