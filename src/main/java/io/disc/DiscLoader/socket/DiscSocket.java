package io.disc.DiscLoader.socket;

import com.neovisionaries.ws.client.WebSocket;

import io.disc.DiscLoader.DiscLoader;

/**
 * @author Perry Berman
 *
 */
public class DiscSocket {
	
	/**
	 * 
	 */
	public DiscLoader loader;
	
	
	/**
	 * 
	 */
	public WebSocket ws;
	
	/**
	 * @param loader
	 */
	public DiscSocket(DiscLoader loader) {
		this.loader = loader;
	}
	
	/**
	 * 
	 */
	public void connectSocket() {
		
	}
	
}
