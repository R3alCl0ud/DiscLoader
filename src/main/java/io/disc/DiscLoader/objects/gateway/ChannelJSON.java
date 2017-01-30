package io.disc.DiscLoader.objects.gateway;

public class ChannelJSON {
	public String id;
	public String type;
	public String name;
	public String topic;
	public String last_message_id;
	
	public int user_limit;
	public int position;
	public int bitrate;
	
	public boolean is_private;
	
	public OverwriteJSON[] permission_overwrites;
	public UserJSON[] recipients;
}
