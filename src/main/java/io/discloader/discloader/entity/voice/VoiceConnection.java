package io.discloader.discloader.entity.voice;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import io.discloader.discloader.entity.impl.IVoiceChannel;

public class VoiceConnection {

	public AudioPlayer player;
	
	public final IVoiceChannel channel;
	
	public VoiceConnection(IVoiceChannel channel) {
		this.channel = channel;
	}

}
