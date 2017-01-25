package io.disc.DiscLoader;

import io.disc.DiscLoader.events.eventHandler;

public class testClient {
	public static void main(String... args) {
		DiscLoader client = new DiscLoader();
		client.login("MjcxNjYwMzYxMTMwODM1OTcx.C2gSGw.pCPSCH3b_lIvsrTjMl8QmZ_iPjs");
		ClientFrame frame = new ClientFrame();
	}
	
	@eventHandler
	public void raw(String raw) {
		System.out.println(raw);
	}
	
	@eventHandler
	public void debug(String debug) {
		System.out.println(debug);
	}
}
