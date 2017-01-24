package io.disc.DiscLoader;

import com.google.gson.Gson;

import io.disc.DiscLoader.events.eventHandler;
import io.disc.DiscLoader.socket.packets.SocketPacket;

public class testClient {
	public static void main(String... args) {
		DiscLoader client = new DiscLoader();
		client.login("MjcxNjYwMzYxMTMwODM1OTcx.C2gSGw.pCPSCH3b_lIvsrTjMl8QmZ_iPjs");
	}
	
	@eventHandler
	public void raw(String raw) {
		System.out.println(raw);
	}
}
