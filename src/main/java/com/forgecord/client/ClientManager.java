package main.java.com.forgecord.client;

public class ClientManager {
	public Client client;
	
	public ClientManager(Client client) {
		this.client = client;
	}
	
	public void connectToWebSocket(String token) {
		this.client.token = token;
	}
}
