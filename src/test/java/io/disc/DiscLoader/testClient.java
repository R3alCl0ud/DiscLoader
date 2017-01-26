package io.disc.DiscLoader;

import javax.swing.JLabel;

import io.disc.DiscLoader.events.eventHandler;
import io.disc.DiscLoader.objects.loader.Mod;

@Mod(
		modid = "Test Client",
		version = "0.0.1_a",
		desc = "A test client for DiscLoader API"
	)
public class testClient {
	public static ClientFrame frame;

	public static void main(String... args) {
		DiscLoader client = new DiscLoader();
		frame = new ClientFrame(client);
		client.login("MjcxNjYwMzYxMTMwODM1OTcx.C2gSGw.pCPSCH3b_lIvsrTjMl8QmZ_iPjs");
	}

	@eventHandler
	public void raw(String raw) {
		System.out.println(raw);
		JLabel nextLine = new JLabel((String) raw);
		nextLine.setLocation(0, (110 + frame.panel.getHeight()));
		frame.panel.add(nextLine);
		frame.panel.revalidate();
		frame.panel.repaint();
	}

	@eventHandler
	public void debug(String debug) {
		System.out.println(debug);
	}
}
