package io.discloader.discloader.entity.voice;

import io.discloader.discloader.entity.channel.IGuildVoiceChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.network.json.VoiceStateJSON;

public class VoiceState {
	public IGuildVoiceChannel channel;

	public final IGuildMember member;
	public final IGuild guild;

	public boolean deaf;
	public boolean mute;
	public boolean suppressed;

	public VoiceState(VoiceStateJSON data, IGuild guild2) {
		this.guild = guild2;
		this.member = guild2.getMembers().get(data.user_id);
		this.deaf = data.deaf || data.self_deaf ? true : false;
		this.mute = data.mute || data.self_mute ? true : false;
		this.suppressed = data.suppress;
		this.channel = guild2.getVoiceChannels().get(data.channel_id);
	}
}
