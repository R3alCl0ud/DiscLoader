/**
 * 
 */
package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.common.event.voice.VoiceStateUpdateEvent;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.voice.VoiceConnection;
import io.discloader.discloader.entity.voice.VoiceState;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.VoiceStateJSON;
import io.discloader.discloader.util.DLUtil.Status;

/**
 * @author Perry Berman
 */
public class VoiceStateUpdate extends AbstractHandler {

	public VoiceStateUpdate(DiscSocket socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		String d = this.gson.toJson(packet.d);
		VoiceStateJSON data = this.gson.fromJson(d, VoiceStateJSON.class);
		IGuild guild = loader.guilds.get(data.guild_id);
		VoiceConnection connection = this.loader.voiceConnections.get(guild.getID());
		if (connection != null && connection.getUserID().equals(data.user_id)) {
			connection.setSessionID(data.session_id);
			connection.setStateUpdated(true);
		}
		VoiceState currentState = new VoiceState(data, guild);
		guild.updateVoiceState(currentState);
		VoiceState oldState = guild.getVoiceStates().get(data.user_id);
		if (loader.ready && loader.socket.status == Status.READY && oldState != null) {
			VoiceStateUpdateEvent event = new VoiceStateUpdateEvent(oldState, currentState);
			for (IEventListener e : loader.handlers) {
				e.VoiceStateUpdate(event);
			}
		}
	}

}
