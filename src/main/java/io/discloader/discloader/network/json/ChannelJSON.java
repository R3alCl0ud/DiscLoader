package io.discloader.discloader.network.json;

public class ChannelJSON {
	
	public String id;
	public String name;
	public String topic;
	public String last_message_id;
	public String guild_id;
	public String parent_id;
	
	public short type;
	public int user_limit;
	public int position;
	public int bitrate;
	
	public boolean is_private;
	public boolean nsfw;
	
	public OverwriteJSON[] permission_overwrites;
	public UserJSON[] recipients;
	
	public UserJSON recipient;
}
