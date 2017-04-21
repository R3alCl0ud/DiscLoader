package io.discloader.discloader.common.registry;

import io.discloader.discloader.common.registry.factory.ChannelFactory;
import io.discloader.discloader.common.registry.factory.GuildFactory;
import io.discloader.discloader.common.registry.factory.InviteFactory;
import io.discloader.discloader.common.registry.factory.UserFactory;

/**
 * This class holder objects that instantiate the Objects that implement any of
 * the interfaces from DiscLoader.
 * 
 * <pre>
 * // if you want to set a custom factory for building channels
 * EntityBuilder.setChannelFactory(new CustomChannelFactory());
 * /* 
 *  * the CustomChannelFactory class should extend the {@link ChannelFactory} class
 *  *{@literal /}
 * </pre>
 * 
 * @author Perry Berman
 */
public class EntityBuilder {

	public static final EntityBuilder instance = new EntityBuilder();

	private GuildFactory guildFactory = new GuildFactory();
	private UserFactory userFactory = new UserFactory();
	private ChannelFactory channelFactory = new ChannelFactory();
	private InviteFactory inviteFactory = new InviteFactory();

	public static ChannelFactory getChannelFactory() {
		return instance.channelFactory;
	}

	public static GuildFactory getGuildFactory() {
		return instance.guildFactory;
	}

	/**
	 * @return the inviteFactory
	 */
	public static InviteFactory getInviteFactory() {
		return instance.inviteFactory;
	}

	/**
	 * @return the userFactory
	 */
	public static UserFactory getUserFactory() {
		return instance.userFactory;
	}

	/**
	 * @param channelFactory the channelFactory to set
	 */
	public static void setChannelFactory(ChannelFactory channelFactory) {
		instance.channelFactory = channelFactory;
	}

	public static void setGuildFactory(GuildFactory factory) {
		instance.guildFactory = factory;
	}

	/**
	 * @param inviteFactory the inviteFactory to set
	 */
	public static void setInviteFactory(InviteFactory inviteFactory) {
		instance.inviteFactory = inviteFactory;
	}

	/**
	 * @param userFactory the userFactory to set
	 */
	public static void setUserFactory(UserFactory userFactory) {
		instance.userFactory = userFactory;
	}

}
