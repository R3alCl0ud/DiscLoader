package test.java.com.forgecord;

import main.java.com.forgecord.client.Client;

public class testClient {

	public static void main(String[] args) {
		Client client = new Client();

		
		client.login("mfa.qaPsDgy3JjktGISe4QH-MbD7Em0VCc7XK71yFCTq0wjcPZqKp0YNuVRbkeNG2yF23bqGM7sN7AepRJKqB2Ub");
		System.out.println(client.token);
		
	}

}
