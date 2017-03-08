package io.discloader.discloader.entity.voice;

import io.discloader.discloader.entity.guild.Guild;
import io.discloader.discloader.entity.guild.GuildMember;
import io.discloader.discloader.entity.impl.IVoiceChannel;
import io.discloader.discloader.network.json.VoiceStateJSON;

public class VoiceState {
	public IVoiceChannel channel;

	public final GuildMember member;
	public final Guild guild;

	public boolean deaf;
	public boolean mute;
	public boolean suppressed;

	public VoiceState(VoiceStateJSON data, Guild guild) {
		this.guild = guild;
		this.member = guild.members.get(data.user_id);
		this.deaf = data.deaf || data.self_deaf ? true : false;
		this.mute = data.mute || data.self_mute ? true : false;
		this.suppressed = data.suppress;

		this.channel = guild.voiceChannels.get(data.channel_id);
	}
}
