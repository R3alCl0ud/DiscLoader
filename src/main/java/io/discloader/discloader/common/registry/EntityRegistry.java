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
import io.discloader.discloader.entity.voice.VoiceConnection;
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

	public static IGuild addGuild(GuildJSON data) {
		IGuild guild = FactoryManager.instance.getGuildFactory().buildGuild(data);
		guilds.put(guild.getID(), guild);
		return guild;
	}

	public static IUser addUser(UserJSON data) {
		IUser user = FactoryManager.instance.getUserFactory().buildUser(data);
		users.put(user.getID(), user);
		return user;
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
	public static Collection<IGroupChannel> getGroupchannels() {
		return groupChannels.values();
	}

	public static IGuild getGuildByID(long guildID) {
		return guilds.get(guildID);
	}

	/**
	 * @return the guildchannels
	 */
	public static Collection<IGuildChannel> getGuildchannels() {
		return guildChannels.values();
	}

	public static Collection<IGuild> getGuilds() {
		return guilds.values();
	}

	/**
	 * @return the privatechannels
	 */
	public static Collection<IPrivateChannel> getPrivatechannels() {
		return privateChannels.values();
	}

	/**
	 * @return the textchannels
	 */
	public static Collection<ITextChannel> getTextchannels() {
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
	public static Collection<IVoiceChannel> getVoicechannels() {
		return voiceChannels.values();
	}

	/**
	 * @return the voiceconnections
	 */
	public static Collection<VoiceConnection> getVoiceconnections() {
		return voiceConnections.values();
	}

	public static boolean guildExists(IGuild guild) {
		return guilds.containsValue(guild);
	}
}
