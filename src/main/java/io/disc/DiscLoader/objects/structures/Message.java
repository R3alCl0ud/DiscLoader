package io.disc.DiscLoader.objects.structures;

import io.disc.DiscLoader.objects.gateway.MessageJSON;

public class Message {
	public String id;
	public String content;
	public String channel_id;
	public String timestamp;
	public String edited_timestamp;
	public String nonce;
	public String webhook_id;

	public boolean tts;
	public boolean mention_everyone;
	public boolean pinned;
	
	public Mentions mentions;
	
	public Message(MessageJSON message) {
		
	}
}
