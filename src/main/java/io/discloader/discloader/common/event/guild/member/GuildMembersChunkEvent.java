package io.discloader.discloader.common.event.guild.member;

import java.util.HashMap;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.guild.Guild;
import io.discloader.discloader.entity.guild.GuildMember;

public class GuildMembersChunkEvent extends DLEvent {

	public final Guild guild;

	public final HashMap<String, GuildMember> members;

	public GuildMembersChunkEvent(Guild guild, HashMap<String, GuildMember> members) {
		super(guild.loader);

		this.guild = guild;

		this.members = members;
	}

}
