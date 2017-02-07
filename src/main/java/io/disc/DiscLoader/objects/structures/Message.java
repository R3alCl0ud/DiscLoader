package io.disc.DiscLoader.objects.structures;

import java.util.concurrent.CompletableFuture;

import io.disc.DiscLoader.DiscLoader;
import io.disc.DiscLoader.objects.gateway.MessageJSON;

public class Message {
	public String id = null;
	public String content;
	public String timestamp;
	public String edited_timestamp;
	public String nonce;
	public String webhook_id;

	public final boolean tts;
	public boolean mention_everyone;
	public boolean pinned;

	public Mentions mentions;

	public final DiscLoader loader;
	
	public final Channel channel;
	public final Guild guild;
	
	
	public final User author;
	public GuildMember member;

	public Message(DiscLoader loader, Channel channel, MessageJSON data) {
		if (data.id != null) this.id = data.id;
		
		if (data.content != null) this.content = data.content;
		
		if (data.timestamp != null) this.timestamp = data.timestamp;
		
		if (data.edited_timestamp != null) this.edited_timestamp = data.edited_timestamp;

		if (data.nonce != null) this.nonce = data.nonce;
		
		this.tts = data.tts;
		
		this.loader = loader;
		if (data.webhook_id == null) {			
			this.author = this.loader.addUser(data.author);
		} else {
			this.author = null;
		}
		
		this.channel = channel;

		this.guild = channel.guild != null ? channel.guild : null;
	}
	
	public CompletableFuture<Message> reply(String content) {
		return ((TextChannel)this.channel).sendMessage(this.author.toString() + ", " + content);
	}
	
	public CompletableFuture<Message> edit(String content) {
		return this.loader.rest.editMessage(this.channel, this, content);
	}
	
	public CompletableFuture<Message> delete() {
		return this.loader.rest.deleteMessage(this.channel, this);
	}
	
	public boolean isDeletable() {
		return false;
	}
}
