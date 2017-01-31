package io.disc.DiscLoader.objects.structures;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.disc.DiscLoader.DiscLoader;
import io.disc.DiscLoader.objects.gateway.ChannelJSON;
import io.disc.DiscLoader.util.constants.Permissions;

public class GuildChannel extends Channel {
	public final Guild guild;
	
	public HashMap<String, GuildMember> members;
	public Permissions permission;
	
	
	public GuildChannel(DiscLoader loader, Guild guild, ChannelJSON channel) {
		super(loader, channel);
		this.name = channel.name;
		this.guild = guild;
		this.members = new HashMap<String, GuildMember>();
	}
	
	public HashMap<String, GuildMember> getMembers() {
		if (this.members.size() == 0) {
			
		}
		return this.members;
	}
	
	public CompletableFuture<Message> sendMessage(String content) {
		return this.loader.rest.sendMessage(this, content);
	}
}
