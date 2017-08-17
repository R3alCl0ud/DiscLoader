package io.discloader.discloader.entity.message;

import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.guild.IRole;
import io.discloader.discloader.entity.user.IUser;

/**
 * @author Perry Berman
 *
 */
public class MessageBuilder {
	
	private String content = "";
	private ITextChannel channel;
	
	public MessageBuilder(ITextChannel channel) {
		// content = "";
	}
	
	public MessageBuilder mention(IUser user) {
		return this;
	}
	
	public MessageBuilder mention(IRole role) {
		return this;
	}
	
	public MessageBuilder mention(IGuildMember member) {
		return this;
	}
	
	public MessageBuilder mention(ITextChannel channel) {
		return this;
	}
	
	/**
	 * @return the content
	 */
	public String getContent() {
		return this.content;
	}
	
}
