package io.discloader.discloader.entity.voice;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.Guild;
import io.discloader.discloader.entity.channels.VoiceChannel;
import io.discloader.discloader.entity.sendable.Packet;
import io.discloader.discloader.entity.sendable.VoiceUpdate;
import io.discloader.discloader.util.Constants;
public class VoiceConnection {

	public AudioPlayer player;
	
	public final Guild guild;
	public final VoiceChannel channel;
	public final DiscLoader loader;
	
	
	public VoiceConnection(VoiceChannel channel) {
		this.channel = channel;
		this.guild = channel.guild;
		this.loader = channel.loader;
	}

	public void sendStateUpdate() {
		VoiceUpdate d = new VoiceUpdate(this.guild, this.channel, false, false);
		this.loader.socket.send(new Packet(Constants.OPCodes.VOICE_STATE_UPDATE, d));
	}
	
}
