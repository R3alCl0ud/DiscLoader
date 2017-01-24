package io.disc.DiscLoader.objects.gateway;

public class ChannelGateway {
	public String id;
	public String type;
	public String name;
	public boolean is_private;
	public UserGateway[] recipients;
	public String last_message_id;
}
