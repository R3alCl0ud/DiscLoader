/**
 * 
 */
package io.disc.DiscLoader.objects.structures;

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

	private Mentions mentions;
	public final DiscLoader loader;
	public final TextChannel channel;
	public final User author;
	public final Guild guild;

	public Message(TextChannel channel, MessageJSON data) {
		this.id = data.id;

		this.loader = channel.loader;
		
		this.channel = channel;
		
		System.out.println("Why");
		
		this.guild = channel.guild != null ? channel.guild : null;
		
		System.out.println("umm");
		
		this.author = this.loader.addUser(data.author);

		this.editable = this.loader.user.id == this.author.id;

		this.tts = data.tts;

		this.content = data.content;

		this.nonce = data.nonce;
		
	}
	
	public void patch(MessageJSON data) {
		this.content = data.content;
	}

	/**
	 * @return the mentions
	 */
	public Mentions getMentions() {
		return this.mentions;
	}


}
