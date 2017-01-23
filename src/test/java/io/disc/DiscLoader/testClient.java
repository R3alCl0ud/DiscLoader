package io.disc.DiscLoader;

import com.google.gson.Gson;

import io.disc.DiscLoader.events.eventHandler;
import io.disc.DiscLoader.socket.packets.SocketPacket;

public class testClient {
	public static void main(String... args) {
		DiscLoader client = new DiscLoader();
		Gson gson = new Gson();
		SocketPacket packet = gson.fromJson("{\"op\":0, \"s\": null, \"d\": {\"name\": \"Test\"}}", SocketPacket.class);
		System.out.println(gson.toJson(packet.d.v));
		
	}
	
	@eventHandler
	public void Ready() {
		
	}
	
}
