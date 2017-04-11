package io.discloader.discloader.entity.voice;

import io.discloader.discloader.entity.channel.IGuildVoiceChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.json.VoiceStateJSON;

public class VoiceState {

	public IGuildVoiceChannel channel;

	public final IGuildMember member;
	public final IGuild guild;

	public boolean deaf;
	public boolean mute;
	public boolean suppressed;

	public VoiceState(VoiceStateJSON data, IGuild guild) {
		this.guild = guild;
		member = guild.getMember(data.user_id);
		deaf = data.deaf || data.self_deaf ? true : false;
		mute = data.mute || data.self_mute ? true : false;
		suppressed = data.suppress;
		channel = data.channel_id == null ? null : guild.getVoiceChannels().get(SnowflakeUtil.parse(data.channel_id));
	}
}
