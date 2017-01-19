package test.java.com.forgecord;

import java.util.Timer;
import java.util.TimerTask;

import main.java.com.forgecord.client.Client;


public class testClient {

	public static void main(String[] args) {
		Client client = new Client();

		client.on("debug", e -> System.out.println(e));
		client.on("raw", e -> System.out.println(e));
		
		client.login("TOKEN");

		client.on("ready", e -> {
			System.out.printf("Username: %s%nChannels: %d%n", client.user.username, client.channels.size());
		});
		
		Timer test = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
				System.out.println("Test timer");
			}
		};
//		test.scheduleAtFixedRate(task, 100, 100);
	}

}
