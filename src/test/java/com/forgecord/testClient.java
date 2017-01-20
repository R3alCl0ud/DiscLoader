package test.java.com.forgecord;

import org.json.JSONObject;

import main.java.com.forgecord.client.Client;


public class testClient {
	
	public static JSONObject auth = new JSONObject();
	
	public static void main(String[] args) {
		
		Client client = new Client();

		client.on("debug", e -> System.out.println(e));
		client.on("raw", e -> System.out.println(e));
		
		client.login("TOKEN");

		client.on("ready", e -> {
			System.out.printf("Username: %s%nChannels: %d%n", client.user.username, client.channels.size());
		});
	
	}

}
