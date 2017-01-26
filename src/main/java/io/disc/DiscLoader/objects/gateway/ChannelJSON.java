package io.disc.DiscLoader.objects.gateway;

public class ChannelJSON {
	public String id;
	public String type;
	public String name;
	public boolean is_private;
	public UserJSON[] recipients;
	public String last_message_id;
}
