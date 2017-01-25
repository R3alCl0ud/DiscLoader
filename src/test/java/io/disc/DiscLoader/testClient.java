package io.disc.DiscLoader;

import javax.swing.JLabel;

import io.disc.DiscLoader.events.eventHandler;

public class testClient {
	public static ClientFrame frame; 
	
	public static void main(String... args) {
		DiscLoader client = new DiscLoader();
		client.login("MjcxNjYwMzYxMTMwODM1OTcx.C2gSGw.pCPSCH3b_lIvsrTjMl8QmZ_iPjs");
		frame = new ClientFrame();
	}
	
	@eventHandler
	public void raw(String raw) {
		
		frame.panel.add(new JLabel((String)raw));
		frame.panel.validate();
	}
	
	@eventHandler
	public void debug(String debug) {
		System.out.println(debug);
	}
}
