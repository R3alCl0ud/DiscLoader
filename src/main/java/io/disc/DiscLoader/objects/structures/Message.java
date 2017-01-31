package io.disc.DiscLoader.objects.structures;

import io.disc.DiscLoader.DiscLoader;
import io.disc.DiscLoader.objects.gateway.MessageJSON;

public class Message {
	public final String id;
	public String content;
	public String timestamp;
	public String edited_timestamp;
	public String nonce;
	public String webhook_id;

	public final boolean tts;
	public boolean mention_everyone;
	public boolean pinned;

	public Mentions mentions;

	public final Channel channel;
	public final Guild guild;

	public Message(DiscLoader loader, Channel channel, MessageJSON message) {
		this.id = message.id;
		
		this.content = message.content;
		
		this.timestamp = message.timestamp;
		
		this.edited_timestamp = message.edited_timestamp;

		this.nonce = message.nonce;
		
		this.tts = message.tts;

		this.channel = channel;

		this.guild = channel.guild != null ? channel.guild : null;
	}
}
