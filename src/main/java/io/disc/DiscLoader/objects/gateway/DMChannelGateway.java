package io.disc.DiscLoader.objects.gateway;

/**
 * @author perryberman
 *
 */
public class DMChannelGateway {
	String id;
	int type;
	boolean is_private;
	UserGateway recipient;
	String last_message_id;
}
