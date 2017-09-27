package io.discloader.discloader.common.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private static final Map<Long, IGuild> guilds = new HashMap<>();
	private static final Map<Long, IUser> users = new HashMap<>();
	private static final Map<Long, IWebhook> webhooks = new HashMap<>();
	private static final Map<Long, VoiceConnection> voiceConnections = new HashMap<>();
	private static final Map<Long, IChannel> channels = new HashMap<>();
	private static final Map<Long, IChannelCategory> categories = new HashMap<>();
	private static final Map<Long, ITextChannel> textChannels = new HashMap<>();
	private static final Map<Long, IGroupChannel> groupChannels = new HashMap<>();
	private static final Map<Long, IPrivateChannel> privateChannels = new HashMap<>();
	private static final Map<Long, IVoiceChannel> voiceChannels = new HashMap<>();
	private static final Map<Long, IGuildChannel> guildChannels = new HashMap<>();

	public static IChannel addChannel(ChannelJSON data) {
		return addChannel(data, null);
	}

	public static IChannel addChannel(ChannelJSON data, IGuild guild) {
		IChannel channel = EntityBuilder.getChannelFactory().buildChannel(data, guild);
		if (channel != null) {
			channels.put(channel.getID(), channel);
			if (channel instanceof ITextChannel) textChannels.put(channel.getID(), (ITextChannel) channel);
			if (channel instanceof IPrivateChannel) privateChannels.put(channel.getID(), (IPrivateChannel) channel);
			if (channel instanceof IGroupChannel) groupChannels.put(channel.getID(), (IGroupChannel) channel);
			if (channel instanceof IVoiceChannel) voiceChannels.put(channel.getID(), (IVoiceChannel) channel);
			if (channel instanceof IGuildChannel) guildChannels.put(channel.getID(), (IGuildChannel) channel);
		}
		return channel;
	}

	public static IGuild addGuild(GuildJSON data) {
		IGuild guild = EntityBuilder.getGuildFactory().buildGuild(data);
		guilds.put(guild.getID(), guild);
		return guild;
	}

	public static IUser addUser(UserJSON data) {
		if (userExists(data.id == null ? "0" : data.id)) return getUserByID(data.id == null ? "0" : data.id);
		IUser user = EntityBuilder.getUserFactory().buildUser(data);
		users.put(user.getID(), user);
		return user;
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

	public static IChannel getChannelCategoryByID(long channelID) {
		return categories.get(channelID);
	}

	public static IChannel getChannelCategoryByID(String channelID) {
		return categories.get(SnowflakeUtil.parse(channelID));
	}

	public static Collection<IChannel> getChannels() {
		return channels.values();
	}

	public static Collection<IGroupChannel> getGroupChannels() {
		return groupChannels.values();
	}

	public static IGuild getGuildByID(long guildID) {
		return guilds.get(guildID);
	}

	public static IGuild getGuildByID(String guildID) {

		return guildID == null ? null : guilds.get(SnowflakeUtil.parse(guildID));
	}

	public static Collection<IGuildChannel> getGuildChannels() {
		return guildChannels.values();
	}

	/**
	 * Returns a Collection of all of the client's (or shards') guilds
	 * 
	 * @return A Collection of {@link IGuild} objects
	 * @see IGuild
	 * @see #getGuildByID(long) getGuildByID(guildID)
	 * @author Perry Berman
	 */
	public static Collection<IGuild> getGuilds() {
		return guilds.values();
	}

	public static List<IGuild> getGuildsOnShard(Shard shard) {
		if (shard == null) return new ArrayList<>();
		List<IGuild> sgs = new ArrayList<>();
		for (IGuild guild : getGuilds()) {
			if ((guild.getID() >> 22) % shard.getShardCount() == shard.getShardID()) sgs.add(guild);
		}
		return sgs;
	}

	public static IPrivateChannel getPrivateChannelByID(long channelID) {
		return privateChannels.get(channelID);
	}

	public static IPrivateChannel getPrivateChannelByID(String channelID) {
		return getPrivateChannelByID(SnowflakeUtil.parse(channelID));
	}

	public static IPrivateChannel getPrivateChannelByUser(IUser user) {
		if (user == null) return null;
		return getPrivateChannelByUserID(user.getID());
	}

	public static IPrivateChannel getPrivateChannelByUserID(long userID) {
		for (IPrivateChannel channel : getPrivateChannels())
			if (channel.getRecipient().getID() == userID) return channel;
		return null;
	}

	public static IPrivateChannel getPrivateChannelByUserID(String userID) {
		return getPrivateChannelByUserID(SnowflakeUtil.parse(userID));
	}

	public static Collection<IPrivateChannel> getPrivateChannels() {
		return privateChannels.values();
	}

	public static ITextChannel getTextChannelByID(long channelID) {
		return textChannels.get(channelID);
	}

	public static ITextChannel getTextChannelByID(String channelID) {
		return getTextChannelByID(SnowflakeUtil.parse(channelID));
	}

	public static Collection<ITextChannel> getTextChannels() {
		return textChannels.values();
	}

	public static IUser getUserByID(long userID) {
		return users.get(userID);
	}

	public static IUser getUserByID(String userID) {
		return getUserByID(SnowflakeUtil.parse(userID));
	}

	public static Collection<IUser> getUsers() {
		return users.values();
	}

	public static IVoiceChannel getVoiceChannelByID(long channelID) {
		return voiceChannels.get(channelID);
	}

	public static IVoiceChannel getVoiceChannelByID(String channelID) {
		return getVoiceChannelByID(SnowflakeUtil.parse(channelID));
	}

	public static Collection<IVoiceChannel> getVoiceChannels() {
		return voiceChannels.values();
	}

	public static VoiceConnection getVoiceConnectionByGuild(IGuild guild) {
		if (guild == null) return null;
		return getVoiceConnectionByID(guild.getID());
	}

	public static VoiceConnection getVoiceConnectionByID(long guildID) {
		return voiceConnections.get(guildID);
	}

	public static Collection<VoiceConnection> getVoiceConnections() {
		return voiceConnections.values();
	}

	public static IWebhook getWebhookByID(long webhookID) {
		return webhooks.get(webhookID);
	}

	public static IWebhook getWebhookByID(String webhookID) {
		return getWebhookByID(SnowflakeUtil.parse(webhookID));
	}

	/**
	 * @return {@codewebhooks}
	 */
	public static Map<Long, IWebhook> getWebhooks() {
		return webhooks;
	}

	public static boolean guildExists(IGuild guild) {
		if (guild == null) return false;
		return guilds.containsValue(guild);
	}

	public static boolean guildExists(long guildID) {
		return guilds.containsKey(guildID);
	}

	public static boolean guildExists(String guildID) {
		return guildExists(SnowflakeUtil.parse(guildID));
	}

	public static boolean hasVoiceConnection(IGuild guild) {
		if (guild == null) return false;
		return voiceConnections.containsKey(guild.getID());
	}

	public static boolean hasVoiceConnection(long guildID) {
		return voiceConnections.containsKey(guildID);
	}

	public static void putVoiceConnection(VoiceConnection connection) {
		voiceConnections.put(connection.getGuild().getID(), connection);
	}

	public static void removeChannel(IChannel channel) {
		if (channel == null) return;
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
		if (guild == null) return;
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
