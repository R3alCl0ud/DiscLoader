package io.discloader.discloader.common.event.guild.member;

import java.util.HashMap;
import java.util.Map;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;

public class GuildMembersChunkEvent extends DLEvent {
	
	public final IGuild guild;
	
	public final Map<String, IGuildMember> members;
	
	public GuildMembersChunkEvent(IGuild guild, HashMap<String, IGuildMember> members) {
		super(guild.getLoader());
		
		this.guild = guild;
		
		this.members = members;
	}
	
}
