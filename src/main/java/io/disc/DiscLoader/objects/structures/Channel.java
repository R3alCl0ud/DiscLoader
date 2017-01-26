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
	
	public Guild guild;
	
	public DiscLoader loader;
	
	public User user;
	
	public HashMap<String, Message> messages;
	public HashMap<String, User> recipients;
	public HashMap<String, GuildMember> members;
	
	public Channel(ChannelJSON channel) {
		this.id = channel.id;
	}
}
