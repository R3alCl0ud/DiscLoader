package test.java.com.forgecord;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;

import main.java.com.forgecord.client.Client;


public class testClient {
	
	public static JSONObject auth;
	
	public static void main(String[] args) {
		
		Client client = new Client();
		
		try {
			String content = ""; 
			Object[] lines = Files.readAllLines(Paths.get("./auth.json")).toArray();
			for(Object line : lines) content += line;
			auth = new JSONObject(content);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		client.on("debug", e -> System.out.println(e));
		client.on("raw", e -> System.out.println(e));
		
		client.login(auth.getString("token"));

		client.on("ready", e -> {
			System.out.printf("Username: %s%nChannels: %d%n", client.user.username, client.channels.size());
		});
	

	}

}
