package test.java.com.forgecord;

import main.java.com.forgecord.client.Client;

public class testClient {

	public static void main(String[] args) {
		Client client = new Client();

		
		client.login("");
		System.out.println(client.token);
		
	}

}
