package io.discloader.discloader.common.registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import io.discloader.discloader.entity.channel.IChannel;
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

public class EntityRegistry {

	private static final Map<Long, IGuild> guilds = new HashMap<>();
	private static final Map<Long, IUser> users = new HashMap<>();
	private static final Map<Long, VoiceConnection> voiceConnections = new HashMap<>();
	private static final Map<Long, IChannel> channels = new HashMap<>();
	private static final Map<Long, ITextChannel> textChannels = new HashMap<>();
	private static final Map<Long, IVoiceChannel> voiceChannels = new HashMap<>();
	private static final Map<Long, IGroupChannel> groupChannels = new HashMap<>();
	private static final Map<Long, IPrivateChannel> privateChannels = new HashMap<>();
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

	public static IChannel getChannelByID(long channelID) {
		return channels.get(channelID);
	}

	public static IChannel getChannelByID(String channelID) {
		return channels.get(SnowflakeUtil.parse(channelID));
	}

	/**
	 * @return the channels
	 */
	public static Collection<IChannel> getChannels() {
		return channels.values();
	}

	/**
	 * @return the groupchannels
	 */
	public static Collection<IGroupChannel> getGroupChannels() {
		return groupChannels.values();
	}

	public static IGuild getGuildByID(long guildID) {
		return guilds.get(guildID);
	}

	public static IGuild getGuildByID(String guildID) {
		return guilds.get(SnowflakeUtil.parse(guildID));
	}

	/**
	 * @return the guildchannels
	 */
	public static Collection<IGuildChannel> getGuildChannels() {
		return guildChannels.values();
	}

	public static Collection<IGuild> getGuilds() {
		return guilds.values();
	}

	public static ITextChannel getPrivateChannelByID(long channelID) {
		return privateChannels.get(channelID);
	}

	public static ITextChannel getPrivateChannelByID(String channelID) {
		return getPrivateChannelByID(SnowflakeUtil.parse(channelID));
	}

	/**
	 * @return the privatechannels
	 */
	public static Collection<IPrivateChannel> getPrivateChannels() {
		return privateChannels.values();
	}

	public static ITextChannel getTextChannelByID(long channelID) {
		return textChannels.get(channelID);
	}

	public static ITextChannel getTextChannelByID(String channelID) {
		return getTextChannelByID(SnowflakeUtil.parse(channelID));
	}

	/**
	 * @return the textchannels
	 */
	public static Collection<ITextChannel> getTextChannels() {
		return textChannels.values();
	}

	public static IUser getUserByID(long userID) {
		return users.get(userID);
	}

	/**
	 * @return the users
	 */
	public static Collection<IUser> getUsers() {
		return users.values();
	}

	/**
	 * @return the voicechannels
	 */
	public static Collection<IVoiceChannel> getVoiceChannels() {
		return voiceChannels.values();
	}

	/**
	 * @return the voiceconnections
	 */
	public static Collection<VoiceConnection> getVoiceConnections() {
		return voiceConnections.values();
	}

	public static VoiceConnection getVoiceConnectionByGuild(IGuild guild) {
		return getVoiceConnectionByID(guild.getID());
	}

	public static VoiceConnection getVoiceConnectionByID(long guildID) {
		return voiceConnections.get(guildID);
	}

	public static boolean guildExists(IGuild guild) {
		return guilds.containsValue(guild);
	}

	public static boolean guildExists(long guildID) {
		return guilds.containsKey(guildID);
	}

	public static boolean guildExists(String guildID) {
		return guildExists(SnowflakeUtil.parse(guildID));
	}

	public static void putVoiceConnection(VoiceConnection connection) {
		voiceConnections.put(connection.guild.getID(), connection);
	}

	public static void removeGuild(IGuild guild) {
		guilds.remove(guild.getID());
	}

	public static void removeChannel(IChannel channel) {

	}

	public static boolean hasVoiceConnection(long guildID) {
		return voiceConnections.containsKey(guildID);
	}

	public static boolean userExists(long userID) {
		return users.containsKey(userID);
	}

	public static boolean userExists(String userID) {
		return users.containsKey(SnowflakeUtil.parse(userID));
	}

	public static IUser getUserByID(String userID) {
		return getUserByID(SnowflakeUtil.parse(userID));
	}
}
