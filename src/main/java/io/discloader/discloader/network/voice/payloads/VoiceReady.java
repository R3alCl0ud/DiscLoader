/**
 * 
 */
package io.discloader.discloader.network.voice.payloads;

/**
 * @author Perry Berman
 *
 */
public class VoiceReady {
	public int ssrc;
	
	public int port;
	
	public String[] modes;
	
	public int heartbeat_interval; 
	
	public String ip;
}
