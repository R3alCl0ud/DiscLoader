package io.disc.DiscLoader;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import javax.swing.*;
import javax.swing.event.*;

import io.disc.DiscLoader.events.eventHandler;
import io.disc.DiscLoader.objects.loader.Mod;

@Mod(modid = "Test Client", version = "0.0.1_a", desc = "A test client for DiscLoader API")
public class testClient {
	public static ClientFrame frame;

	public static void main(String... args) {
		DiscLoader client = new DiscLoader();
//		 frame = new ClientFrame(client);
		 client.login("MjcxNjYwMzYxMTMwODM1OTcx.C2gSGw.pCPSCH3b_lIvsrTjMl8QmZ_iPjs");
	}

	@eventHandler
	public void raw(String raw) {
		System.out.println(raw);
		// frame.updateContents();
	}

	@eventHandler
	public void debug(String debug) {
		System.out.println(debug);
	}
}
