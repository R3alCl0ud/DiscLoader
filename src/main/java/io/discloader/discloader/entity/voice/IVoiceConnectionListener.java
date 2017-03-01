/**
 * 
 */
package io.discloader.discloader.entity.voice;

/**
 * @author Perry Berman
 *
 */
public interface IVoiceConnectionListener {
	
	/**
	 * Executed when the voice connection gets disconnected
	 * @param reason The reason the voice connection was disconnected
	 */
	void disconnected(String reason);
	
	void ready();
}
