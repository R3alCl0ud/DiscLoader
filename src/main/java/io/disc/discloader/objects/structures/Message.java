package io.disc.discloader.objects.structures;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

import io.disc.discloader.DiscLoader;
import io.disc.discloader.objects.gateway.MessageJSON;
import io.disc.discloader.util.constants;

/**
 * @author Perry Berman
 *
 */
public class Message {

	public final String id;
	public String content;
	// public String timestamp;
	public String edited_timestamp;
	public String nonce;
	public String webhookID;

	public final boolean tts;
	public final boolean editable;
	public boolean pinned;

	public final Date timestamp;
	public Date editedAt;
	
	
	public Mentions mentions;
	public final DiscLoader loader;
	public final TextChannel channel;
	public final User author;
	public final Guild guild;
	public final GuildMember member;

	public Message(TextChannel channel, MessageJSON data) {
		this.id = data.id;

		this.loader = channel.loader;

		this.channel = channel;

		this.guild = channel.guild != null ? channel.guild : null;

		if (!this.loader.users.containsKey(data.author.id)) {
			this.author = this.loader.addUser(data.author);
		} else {
			this.author = this.loader.users.get(data.author.id);
		}

		this.member = this.guild != null ? this.guild.members.get(this.author.id) : null;

		this.editable = this.loader.user.id == this.author.id;

		this.tts = data.tts;

		this.content = data.content;

		this.nonce = data.nonce;

		this.mentions = new Mentions(this, data.mentions, data.mention_roles, data.mention_everyone);
		
		this.timestamp = constants.parseISO8601(data.timestamp);
		
		this.editedAt = data.edited_timestamp != null ? constants.parseISO8601(data.edited_timestamp) : null;
	}

	/**
	 * @param data
	 * @return this
	 */
	public Message patch(MessageJSON data) {
		this.content = data.content;

		this.mentions.patch(data.mentions, data.mention_roles, data.mention_everyone);

		this.editedAt = constants.parseISO8601(data.edited_timestamp);
		
		return this;
	}

	public CompletableFuture<Message> edit(String content) {
		return this.loader.rest.editMessage(this.channel, this, content);
	}

	public CompletableFuture<Message> delete() {
		return this.loader.rest.deleteMessage(this.channel, this);
	}

}
