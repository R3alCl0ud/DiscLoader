package io.disc.DiscLoader.objects.structures;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

import io.disc.DiscLoader.DiscLoader;
import io.disc.DiscLoader.objects.gateway.MessageJSON;

/**
 * @author Perry Berman
 *
 */
public class Message {

	public final String id;
	public String content;
	public String timestamp;
	public String edited_timestamp;
	public String nonce;
	public String webhookID;

	public final boolean tts;
	public final boolean editable;
	public boolean mention_everyone;
	public boolean pinned;

//	public final Date timestamp;
	
	public Mentions mentions;
	public final DiscLoader loader;
	public final TextChannel channel;
	public final User author;
	public final Guild guild;

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

		this.editable = this.loader.user.id == this.author.id;

		this.tts = data.tts;

		this.content = data.content;

		this.nonce = data.nonce;

		this.mentions = new Mentions(this, data.mentions, data.mention_roles, data.mention_everyone);
	}
	
	public void patch(MessageJSON data) {
		this.content = data.content;
	}

	public CompletableFuture<Message> edit(String content) {
		return this.loader.rest.editMessage(this.channel, this, content);
	}
	
	public CompletableFuture<Message> delete() {
		return this.loader.rest.deleteMessage(this.channel, this);
	}

}
