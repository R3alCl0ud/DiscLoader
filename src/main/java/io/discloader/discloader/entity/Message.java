package io.discloader.discloader.entity;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.channels.TextChannel;
import io.discloader.discloader.entity.sendable.RichEmbed;
import io.discloader.discloader.network.gateway.json.MessageJSON;
import io.discloader.discloader.util.Constants;

/**
 * The actual fuck
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

	/**
	 * fdn
	 * @param channel
	 * @param data
	 */
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
		
		this.timestamp = Constants.parseISO8601(data.timestamp);
		
		this.editedAt = data.edited_timestamp != null ? Constants.parseISO8601(data.edited_timestamp) : null;
	}

	/**
	 * @param data
	 * @return this
	 */
	public Message patch(MessageJSON data) {
		this.content = data.content;

		this.mentions.patch(data.mentions, data.mention_roles, data.mention_everyone);

		this.editedAt = Constants.parseISO8601(data.edited_timestamp);
		
		return this;
	}

	/**
	 * Edit's the messages content. Only possible if the {@link DiscLoader loader} is the message's {@link #author}
	 * @param content The new content of the message
	 * @param embed The new embed for the message
	 * @return A Future that completes with {@literal this} when sucessfull
	 */
	public CompletableFuture<Message> edit(String content, RichEmbed embed) {
		return this.loader.rest.editMessage(this.channel, this, content, embed, null, null);
	}
	
	/**
	 * Edit's the messages content. Only possible if the {@link DiscLoader loader} is the message's {@link #author}
	 * @param content The new content of the message
	 * @return A Future that completes with {@literal this} when sucessfull
	 */
	public CompletableFuture<Message> edit(String content) {
		return this.edit(content, null);
	}
	
	/**
	 * Edit's the messages content. Only possible if the {@link DiscLoader loader} is the message's {@link #author}
	 * @param embed The new embed for the message
	 * @return A Future that completes with {@literal this} when sucessfull
	 */
	public CompletableFuture<Message> edit(RichEmbed embed) {
		return this.edit(null, embed);
	}

	/**
	 * Deletes the message if the loader has suficient permissions
	 * @see Constants.PermissionFlags
	 * @return A Future that completes with {@literal this} when sucessfull
	 */
	public CompletableFuture<Message> delete() {
		return this.loader.rest.deleteMessage(this.channel, this);
	}

}
