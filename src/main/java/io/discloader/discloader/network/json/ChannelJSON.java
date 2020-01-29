package io.discloader.discloader.network.json;

public class ChannelJSON {
	
	public String application_id;
	public String guild_id;
	public String icon;
	public String id;
	public String last_message_id;
	public String last_pin_timestamp;
	public String name;
	public String owner_id;
	public String parent_id;
	public String topic;
	
	public short type;
	public int user_limit;
	public int rate_limit_per_user;
	public int position;
	public int bitrate;
	
	public boolean is_private;
	public boolean nsfw;
	
	public OverwriteJSON[] permission_overwrites;
	public UserJSON[] recipients;
	
	public UserJSON recipient;
}
