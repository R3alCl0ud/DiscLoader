/**
 * 
 */
package io.discloader.discloader.network.voice.payloads;

/**
 * @author Perry Berman
 * @since 0.0.3
 */
public class VoiceIdentify {
	public String server_id;

	public String user_id;

	public String session_id;

	public String token;

	public VoiceIdentify(String serverID, String userID, String sessionID, String token) {
		this.server_id = serverID;

		this.user_id = userID;

		this.session_id = sessionID;

		this.token = token;
	}
}
