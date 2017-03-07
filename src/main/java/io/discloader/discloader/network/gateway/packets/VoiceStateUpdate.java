/**
 * 
 */
package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.entity.voice.VoiceConnection;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.VoiceStateJSON;

/**
 * @author Perry Berman
 *
 */
public class VoiceStateUpdate extends DLPacket {


	public VoiceStateUpdate(DiscSocket socket) {
		super(socket);
	}
	
	public void handle(SocketPacket packet) {
		String d = this.gson.toJson(packet.d);
		VoiceStateJSON data = this.gson.fromJson(d, VoiceStateJSON.class);
		VoiceConnection connection = this.loader.voiceConnections.get(data.guild_id);
		if (connection.getUserID().equals(data.user_id)) {
		    connection.setSessionID(data.session_id);
		    connection.setStateUpdated(true);
		}
	}
	
}
