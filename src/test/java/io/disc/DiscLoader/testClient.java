package io.disc.DiscLoader;

import javax.swing.JFrame;

import io.disc.DiscLoader.events.eventHandler;

public class testClient extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String... args) {
		DiscLoader client = new DiscLoader();
		client.login("MjcxNjYwMzYxMTMwODM1OTcx.C2gSGw.pCPSCH3b_lIvsrTjMl8QmZ_iPjs");
		JFrame frame = new JFrame();
		frame.setSize(1280, 720);
		frame.setVisible(true);
		frame.setTitle("Debug");
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
