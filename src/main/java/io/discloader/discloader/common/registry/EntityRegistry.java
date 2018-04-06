package io.discloader.discloader.common.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.Shard;
import io.discloader.discloader.core.entity.Webhook;
import io.discloader.discloader.entity.IWebhook;
import io.discloader.discloader.entity.channel.ChannelTypes;
import io.discloader.discloader.entity.channel.IChannel;
import io.discloader.discloader.entity.channel.IChannelCategory;
import io.discloader.discloader.entity.channel.IGroupChannel;
import io.discloader.discloader.entity.channel.IGuildChannel;
import io.discloader.discloader.entity.channel.IPrivateChannel;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.channel.IVoiceChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.entity.voice.VoiceConnection;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.json.GuildJSON;
import io.discloader.discloader.network.json.UserJSON;
import io.discloader.discloader.network.json.WebhookJSON;

public class EntityRegistry {
	/**
	 * A Map of the client's cached guilds. Indexed by {@link IGuild#getID()}.
	 * 
	 *
	 * @see IGuild
	 * @see Map
	 */
	private static final Map<Long, IGuild> guilds = new HashMap<>();
	/**
	 * A Map of the client's cached users. Indexed by {@link IUser#getID()}.
	 * 
	 * @see IUser
	 * @see Map
	 */
	private static final Map<Long, IUser> users = new HashMap<>();
	/**
	 * A Map of the client's cached webhooks. Indexed by {@link IWebhook#getID()}.
	 * 
	 * @see IWebhook
	 * @see Map
	 */
	private static final Map<Long, IWebhook> webhooks = new HashMap<>();
	/**
	 * A Map of the client's cached VoiceConnections. Indexed by
	 * {@link IGuild#getID()}.
	 * 
	 *
	 * @see VoiceConnection
	 * @see Map
	 */
	private static final Map<Long, VoiceConnection> voiceConnections = new HashMap<>();
	/**
	 * A Map of the client's cached channels. Indexed by {@link IChannel#getID()}.
	 * 
	 * @see IChannel
	 * @see Map
	 */
	private static final Map<Long, IChannel> channels = new HashMap<>();
	/**
	 * A Map of the client's cached category channels. Indexed by
	 * {@link IChannel#getID()}.
	 * 
	 * @see IChannel
	 * @see IChannelCategory
	 * @see Map
	 */
	private static final Map<Long, IChannelCategory> categories = new HashMap<>();
	/**
	 * A Map of the client's cached TextChannels. Indexed by
	 * {@link IChannel#getID()}.
	 * 
	 * @see IChannel
	 * @see ITextChannel
	 * @see Map
	 */
	private static final Map<Long, ITextChannel> textChannels = new HashMap<>();

	/**
	 * A HashMap of the client's cached GroupDM channels. Indexed by
	 * {@link IChannel#getID()}
	 * 
	 * @see IChannel
	 * @see IGroupChannel
	 * @see Map
	 */
	private static final Map<Long, IGroupChannel> groupChannels = new HashMap<>();
	/**
	 * A HashMap of the client's cached PrivateChannels. Indexed by
	 * {@link IChannel#getID()}.
	 * 
	 * @see IChannel
	 * @see IPrivateChannel
	 * @see Map
	 */
	private static final Map<Long, IPrivateChannel> privateChannels = new HashMap<>();
	/**
	 * A Map of the client's cached VoiceChannels. Indexed by
	 * {@link IChannel#getID()}.
	 * 
	 * @see IChannel
	 * @see IVoiceChannel
	 * @see Map
	 */
	private static final Map<Long, IVoiceChannel> voiceChannels = new HashMap<>();
	private static final Map<Long, IGuildChannel> guildChannels = new HashMap<>();
	private static final Map<Integer, Shard> shards = new HashMap<>();

	public static IChannel addChannel(ChannelJSON data, DiscLoader loader) {
		return addChannel(data, loader, null);
	}

	public static IChannel addChannel(ChannelJSON data, DiscLoader loader, IGuild guild) {
		IChannel channel = EntityBuilder.getChannelFactory().buildChannel(data, loader, guild);
		if (channel != null) {
			channels.put(channel.getID(), channel);
			if (channel instanceof ITextChannel)
				textChannels.put(channel.getID(), (ITextChannel) channel);
			if (channel instanceof IPrivateChannel)
				privateChannels.put(channel.getID(), (IPrivateChannel) channel);
			if (channel instanceof IGroupChannel)
				groupChannels.put(channel.getID(), (IGroupChannel) channel);
			if (channel instanceof IVoiceChannel)
				voiceChannels.put(channel.getID(), (IVoiceChannel) channel);
			if (channel instanceof IGuildChannel)
				guildChannels.put(channel.getID(), (IGuildChannel) channel);
			if (channel instanceof IChannelCategory)
				categories.put(channel.getID(), (IChannelCategory) channel);
		}
		return channel;
	}

	public static IGuild addGuild(GuildJSON data) {
		IGuild guild = EntityBuilder.getGuildFactory().buildGuild(data);
		guilds.put(guild.getID(), guild);
		return guild;
	}

	public static Shard addShard(Shard shard) {
		return shards.put(shard.getShardID(), shard);
	}

	public static IUser addUser(UserJSON data) {
		if (data == null)
			return null;
		if (userExists(data.id == null ? "0" : data.id)) {
			IUser user = getUserByID(data.id == null ? "0" : data.id);
			if (user.getUsername() == null && data.username != null)
				user.setup(data);
			return user;
		}
		IUser user = EntityBuilder.getUserFactory().buildUser(data);
		users.put(user.getID(), user);
		return user;
	}

	public static void addVoiceConnection(VoiceConnection connection) {
		voiceConnections.put(connection.getGuild().getID(), connection);
	}

	public static IWebhook addWebhook(WebhookJSON data) {
		if (webhookExists(data.id == null ? "0" : data.id)) {
			return getWebhookByID(data.id == null ? "0" : data.id);
		}
		IWebhook webhook = new Webhook(data);
		webhooks.put(webhook.getID(), webhook);
		return webhook;
	}

	public static IChannel getChannelByID(long channelID) {
		return channels.get(channelID);
	}

	public static IChannel getChannelByID(String channelID) {
		return channels.get(SnowflakeUtil.parse(channelID));
	}

	/**
	 * @return {@code categories}
	 */
	public static Map<Long, IChannelCategory> getChannelCategories() {
		return categories;
	}

	/**
	 * @return {@code categories}
	 */
	public static Collection<IChannelCategory> getChannelCategoriesCollection() {
		return categories.values();
	}

	public static IChannelCategory getChannelCategoryByID(long channelID) {
		return categories.get(channelID);
	}

	public static IChannelCategory getChannelCategoryByID(String channelID) {
		return categories.get(SnowflakeUtil.parse(channelID));
	}

	public static Map<Long, IChannel> getChannels() {
		return channels;
	}

	public static Collection<IChannel> getChannelsCollection() {
		return channels.values();
	}

	public static Map<Long, IGroupChannel> getGroupChannels() {
		return groupChannels;
	}

	public static Collection<IGroupChannel> getGroupChannelsCollection() {
		return groupChannels.values();
	}

	public static IGuild getGuildByID(long guildID) {
		return guilds.get(guildID);
	}

	public static IGuild getGuildByID(String guildID) {

		return guildID == null ? null : guilds.get(SnowflakeUtil.parse(guildID));
	}

	public static Map<Long, IGuildChannel> getGuildChannels() {
		return guildChannels;
	}

	public static Collection<IGuildChannel> getGuildChannelsCollection() {
		return guildChannels.values();
	}

	/**
	 * Returns a {@link Map}{@literal <}{@link Long}, {@link IGuild}{@literal >} of
	 * all of the client's guilds. The {@link Map} is indexed by
	 * {@link IGuild#getID()}
	 * 
	 * @return A {@link Map} of {@link IGuild} objects indexed by
	 *         {@link IGuild#getID()}
	 * @see IGuild
	 * @see #getGuildByID(long) getGuildByID(guildID)
	 *
	 */
	public static Map<Long, IGuild> getGuilds() {
		return guilds;
	}

	/**
	 * Returns a Collection of all of the client's guilds
	 * 
	 * @return A Collection of {@link IGuild} objects
	 * @see IGuild
	 * @see #getGuildByID(long) getGuildByID(guildID)
	 *
	 */
	public static Collection<IGuild> getGuildsCollection() {
		return guilds.values();
	}

	public static List<IGuild> getGuildsOnShard(int shardid, int shards) {
		List<IGuild> sgs = new ArrayList<>();
		for (IGuild guild : getGuildsCollection()) {
			if ((guild.getID() >> 22) % shards == shardid)
				sgs.add(guild);
		}
		return sgs;
	}

	public static List<IGuild> getGuildsOnShard(Shard shard) {
		if (shard == null)
			return getGuildsOnShard(0, 1);
		return getGuildsOnShard(shard.getShardID(), shard.getShardCount());
	}

	public static IPrivateChannel getPrivateChannelByID(long channelID) {
		return privateChannels.get(channelID);
	}

	public static IPrivateChannel getPrivateChannelByID(String channelID) {
		return getPrivateChannelByID(SnowflakeUtil.parse(channelID));
	}

	public static IPrivateChannel getPrivateChannelByUser(IUser user) {
		if (user == null)
			return null;
		return getPrivateChannelByUserID(user.getID());
	}

	public static IPrivateChannel getPrivateChannelByUserID(long userID) {
		for (IPrivateChannel channel : getPrivateChannelsCollection())
			if (channel.getRecipient().getID() == userID)
				return channel;
		return null;
	}

	public static IPrivateChannel getPrivateChannelByUserID(String userID) {
		return getPrivateChannelByUserID(SnowflakeUtil.parse(userID));
	}

	public static Map<Long, IPrivateChannel> getPrivateChannels() {
		return privateChannels;
	}

	public static Collection<IPrivateChannel> getPrivateChannelsCollection() {
		return privateChannels.values();
	}

	public static Shard getShardByID(int shardID) {
		return shards.get(shardID);
	}

	public static Map<Integer, Shard> getShards() {
		return shards;
	}

	public static Collection<Shard> getShardsCollection() {
		return shards.values();
	}

	public static ITextChannel getTextChannelByID(long channelID) {
		return textChannels.get(channelID);
	}

	public static ITextChannel getTextChannelByID(String channelID) {
		return getTextChannelByID(SnowflakeUtil.parse(channelID));
	}

	public static Map<Long, ITextChannel> getTextChannels() {
		return textChannels;
	}

	public static Collection<ITextChannel> getTextChannelsCollection() {
		return textChannels.values();
	}

	public static IUser getUserByID(long userID) {
		return users.get(userID);
	}

	public static IUser getUserByID(String userID) {
		return getUserByID(SnowflakeUtil.parse(userID));
	}

	public static Map<Long, IUser> getUsers() {
		return users;
	}

	public static Collection<IUser> getUsersCollection() {
		return users.values();
	}

	public static IVoiceChannel getVoiceChannelByID(long channelID) {
		return voiceChannels.get(channelID);
	}

	public static IVoiceChannel getVoiceChannelByID(String channelID) {
		return getVoiceChannelByID(SnowflakeUtil.parse(channelID));
	}

	public static Map<Long, IVoiceChannel> getVoiceChannels() {
		return voiceChannels;
	}

	public static Collection<IVoiceChannel> getVoiceChannelsCollection() {
		return voiceChannels.values();
	}

	public static VoiceConnection getVoiceConnectionByGuild(IGuild guild) {
		if (guild == null)
			return null;
		return getVoiceConnectionByID(guild.getID());
	}

	public static VoiceConnection getVoiceConnectionByID(long guildID) {
		return voiceConnections.get(guildID);
	}

	public static Map<Long, VoiceConnection> getVoiceConnections() {
		return voiceConnections;
	}

	public static Collection<VoiceConnection> getVoiceConnectionsCollection() {
		return voiceConnections.values();
	}

	public static IWebhook getWebhookByID(long webhookID) {
		return webhooks.get(webhookID);
	}

	public static IWebhook getWebhookByID(String webhookID) {
		return getWebhookByID(SnowflakeUtil.parse(webhookID));
	}

	/**
	 * @return {@code webhooks}
	 */
	public static Map<Long, IWebhook> getWebhooks() {
		return webhooks;
	}

	public static Collection<IWebhook> getWebhooksCollection() {
		return webhooks.values();
	}

	public static boolean guildExists(IGuild guild) {
		if (guild == null)
			return false;
		return guilds.containsValue(guild);
	}

	public static boolean guildExists(long guildID) {
		return guilds.containsKey(guildID);
	}

	public static boolean guildExists(String guildID) {
		return guildExists(SnowflakeUtil.parse(guildID));
	}

	public static boolean hasVoiceConnection(IGuild guild) {
		if (guild == null)
			return false;
		return voiceConnections.containsKey(guild.getID());
	}

	public static boolean hasVoiceConnection(long guildID) {
		return voiceConnections.containsKey(guildID);
	}

	/**
	 * @deprecated Use {@link #addVoiceConnection(VoiceConnection)} instead
	 */
	public static void putVoiceConnection(VoiceConnection connection) {
		addVoiceConnection(connection);
	}

	public static void removeChannel(IChannel channel) {
		if (channel == null)
			return;
		channels.remove(channel.getID());
		if (channel.getType() == ChannelTypes.TEXT) {
			textChannels.remove(channel.getID());
		} else if (channel.getType() == ChannelTypes.VOICE) {
			voiceChannels.remove(channel.getID());
		} else if (channel.getType() == ChannelTypes.GROUP) {
			groupChannels.remove(channel.getID());
		} else if (channel.getType() == ChannelTypes.DM) {
			privateChannels.remove(channel.getID());
		}
	}

	public static void removeGuild(IGuild guild) {
		if (guild == null)
			return;
		guilds.remove(guild.getID());
	}

	public static VoiceConnection removeVoiceConnection(long guildID) {
		return voiceConnections.remove(guildID);
	}

	public static boolean userExists(long userID) {
		return users.containsKey(userID);
	}

	public static boolean userExists(String userID) {
		return userExists(SnowflakeUtil.parse(userID));
	}

	public static boolean webhookExists(long webhookID) {
		return webhooks.containsKey(webhookID);
	}

	public static boolean webhookExists(String webhookID) {
		return webhookExists(SnowflakeUtil.parse(webhookID));
	}
}
