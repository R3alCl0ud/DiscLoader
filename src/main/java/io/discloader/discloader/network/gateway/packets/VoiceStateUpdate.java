/**
 * 
 */
package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.guild.member.GuildMemberEvent.VoiceJoinEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberEvent.VoiceLeaveEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberEvent.VoiceSwitchEvent;
import io.discloader.discloader.common.event.voice.VoiceStateUpdateEvent;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.entity.voice.VoiceConnection;
import io.discloader.discloader.entity.voice.VoiceState;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.VoiceStateJSON;

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
		IGuild guild = EntityRegistry.getGuildByID(data.guild_id);
		if (guild.getMember(data.user_id) == null) return;
		VoiceConnection connection = EntityRegistry.getVoiceConnectionByID(guild.getID());
		if (connection != null && SnowflakeUtil.asString(loader.user).equals(data.user_id)) {
			connection.setSessionID(data.session_id);
			connection.setStateUpdated(true);
		}
		VoiceState currentState = new VoiceState(data, guild);
		VoiceState oldState = guild.getVoiceStates().get(SnowflakeUtil.parse(data.user_id));
		guild.updateVoiceState(currentState);
		if (shouldEmit()) {
			if (currentState.channel != null && (oldState != null && oldState.channel != null)) {
				loader.emit(new VoiceSwitchEvent(currentState.member, oldState.channel));
			} else if (currentState.channel != null) {
				loader.emit(new VoiceJoinEvent(currentState.member));
			} else if (oldState != null && oldState.channel != null) {
				loader.emit(new VoiceLeaveEvent(currentState.member, oldState.channel));
			}

			if (oldState != null) {
				VoiceStateUpdateEvent event = new VoiceStateUpdateEvent(oldState, currentState);
				loader.emit(event);
			}
		}
	}

}
