package io.disc.DiscLoader.objects.structures;

import java.util.HashMap;

import io.disc.DiscLoader.DiscLoader;
import io.disc.DiscLoader.objects.gateway.ChannelJSON;

public class Channel {
	
	public String id;
	
	public String name;
	
	public String topic;
	
	public String lastMessageID;
	
	public String type;
	
	public int bitrate;
	
	public int userLimit; 
	
	public boolean is_private;
	

	
	public final DiscLoader loader;
	
	public User user;
	
	public HashMap<String, Message> messages;
	public HashMap<String, User> recipients;

	public Guild guild;

	
	public Channel(DiscLoader loader, ChannelJSON channel) {
		this.id = channel.id;
		this.loader = loader;
		this.guild = null;
	}
	
	public void setup(ChannelJSON data) {
		if (data.name != null) this.name = data.name;
		if (data.topic != null) this.topic = data.topic;
		if (data.type != null) this.type = data.type;
		if (data.is_private == true || data.is_private == false) this.is_private = data.is_private;
		
	}
}
