package io.discloader.discloader.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;

import io.discloader.discloader.client.render.util.Resource;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.language.Language;
import io.discloader.discloader.core.entity.Permission;
import io.discloader.discloader.entity.util.ISnowflake;
import io.discloader.discloader.network.gateway.GatewayListener;

/**
 * A collection of all useful constants for DiscLoader
 * 
 * @author Perry Berman
 * @since v0.0.1
 * @version 17
 */
public final class DLUtil {
	
	public static enum ChannelType {
		TEXT("text"), VOICE("voice"), DM("DM"), GROUPDM("GroupDM"), CHANNEL("channel");
		
		private String name;
		
		private ChannelType(String name) {
			this.name = name;
		}
		
		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}
	}
	
	/**
	 * An object containing {@link Integer} representations of the different types of channels in Discord's API
	 * 
	 * @author Perry Berman
	 */
	public static final class ChannelTypes {
		
		public static final int text = 0;
		
		public static final int DM = 1;
		
		public static final int voice = 2;
		
		public static final int groupDM = 3;
	}
	
	public static final class Endpoints {
		
		public static final String OAuth2 = String.format("%s/oauth2", API);
		
		public static final String login = String.format("%s/auth/login", API);
		
		public static final String logout = String.format("%s/auth/logout", API);
		
		public static final String gateway = String.format("%s/gateway", API);
		
		public static final String botGateway = String.format("%s/gateway/bot", API);
		
		public static final String CDN = "https://cdn.discordapp.com";
		
		public static final String currentUser = String.format("%s/users/@me", API);
		
		public static final String currentUserGuilds = String.format("%s/users/@me/guilds", API);
		
		public static final String currentUserChannels = String.format("%s/channels", currentUser);
		
		public static final String currentOAuthApplication = String.format("%s/applications/@me", OAuth2);
		
		public static final String guilds = String.format("%s/guilds", API);
		
		public static final String webhooks = String.format("%s/webhooks", API);
		
		public static final String assets(String asset) {
			return String.format("%s/assets/%s", HOST, asset);
		}
		
		public static final String avatar(long id, String avatar) {
			return String.format("%s/avatars/%d/%s%s", CDN, id, avatar, avatar.startsWith("a_") ? ".gif" : ".jpg");
		}
		
		public static final String defaultAvatar(int num) {
			return String.format("%s/embed/avatar/%d.png", CDN, num);
		}
		
		public static final String bulkDelete(long channelID) {
			return String.format("%s/bulk-delete", messages(channelID));
		}
		
		public static final String channel(long channelID) {
			return String.format("%s/channels/%d", API, channelID);
		}
		
		public static String channelInvites(long channelID) {
			return String.format("%s/invites", channel(channelID));
		}
		
		public static final String channelOverwrite(long channelID, long overwriteID) {
			return String.format("%s/%d", channelOverwrites(channelID), overwriteID);
		}
		
		public static final String channelOverwrites(long channelID) {
			return String.format("%s/permissions", channel(channelID));
		}
		
		public static final String channelPinnedMessage(long channelID, long messageID) {
			return String.format("%s/%d", channelPins(channelID), messageID);
		}
		
		public static final String channelPins(long channelID) {
			return String.format("%s/pins", channel(channelID));
		}
		
		public static final String channelTyping(long channelID) {
			return String.format("%s/typing", channel(channelID));
		}
		
		public static final String channelWebhooks(long channelID) {
			return String.format("%s/webhooks", channel(channelID));
		}
		
		public static final String currentUserReaction(long channelID, long messageID, String emoji) {
			return String.format("%s/@me", messageReaction(channelID, messageID, emoji));
		}
		
		public static final String customEmoji(long emojiID) {
			return String.format("%s/emojis/%d.png", CDN, emojiID);
		}
		
		public static final String guild(long guildID) {
			return String.format("%s/%d", guilds, guildID);
		}
		
		public static final String guildBanMember(long guildID, long memberID) {
			return String.format("%s/%d", guildBans(guildID), memberID);
		}
		
		public static final String guildBans(long guildID) {
			return String.format("%s/bans", guild(guildID));
		}
		
		public static final String guildChannels(long guildID) {
			return String.format("%s/channels", guild(guildID));
		}
		
		public static final String guildEmoji(long guildID, long emojiID) {
			return String.format("%s/%d", guildEmojis(guildID), emojiID);
		}
		
		public static final String guildEmojis(long guildID) {
			return String.format("%s/guilds/%s/emojis", API, guild(guildID));
		}
		
		public static final String guildIcon(long guildID, String icon) {
			return String.format("%s/icons/%d/%s.jpg", CDN, guildID, icon);
		}
		
		public static final String guildInvites(long guildID) {
			return String.format("%s/invites", guild(guildID));
		}
		
		public static final String guildMember(long guildID, long memberID) {
			return String.format("%s/%d", guildMembers(guildID), memberID);
		}
		
		public static final String guildMemberRole(long guildID, long memberID, long roleID) {
			return String.format("%s/roles/%d", guildMember(guildID, memberID), roleID);
		}
		
		public static final String guildMembers(long guildID) {
			return String.format("%s/members", guild(guildID));
		}
		
		public static final String guildNick(long guildID) {
			return String.format("%s/@me/nick", guildMembers(guildID));
		}
		
		public static final String guildPrune(long guildID) {
			return String.format("%s/prune", guild(guildID));
		}
		
		public static final String guildRole(long guildID, long roleID) {
			return String.format("%s/%d", guildRoles(guildID), roleID);
		}
		
		public static final String guildRoles(long guildID) {
			return String.format("%s/roles", guild(guildID));
		}
		
		public static final String guildSplash(long guildID, String splashHash) {
			return String.format("%s/splashes/%d/%s.jpg", CDN, guildID, splashHash);
		}
		
		public static final String guildWebhooks(long guildID) {
			return String.format("%s/webhooks", guild(guildID));
		}
		
		public static final String invite(String code) {
			return String.format("%s/invite/%s", API, code);
		}
		
		public static final String inviteLink(String code) {
			return String.format("https://discord.gg/%s", code);
		}
		
		public static final String message(long channelID, long messageID) {
			return String.format("%s/%d", Endpoints.messages(channelID), messageID);
		}
		
		public static final String messageReaction(long channelID, long messageID, String emoji) {
			return String.format("%s/%s", messageReactions(channelID, messageID), emoji);
		}
		
		public static final String messageReactions(long channelID, long messageID) {
			return String.format("%s/reactions", message(channelID, messageID));
		}
		
		public static final String messages(long channelID) {
			return String.format("%s/messages", Endpoints.channel(channelID));
		}
		
		public static final String OAuth2Authorize(long clientID, String scope, int permissions) {
			return String.format("%s/authorize?client_id=%d&scope=%s&permissions=%d", OAuth2, clientID, scope, permissions);
		}
		
		public static final String user(long userID) {
			return String.format("%s/users/%d", API, userID);
		}
		
		public static final String userChannels(long userID) {
			return String.format("%s/channels", Endpoints.user(userID));
		}
		
		public static final String userGuild(long guildID) {
			return String.format("%s/%d", currentUserGuilds, guildID);
		}
		
		public static final String userProfile(long userID) {
			return String.format("%s/profile", user(userID));
		}
		
		public static final String userReaction(long channelID, long messageID, long userID, String emoji) {
			return String.format("%s/%d", messageReaction(channelID, messageID, emoji), userID);
		}
		
		public static final String webhook(long webhookID) {
			return String.format("%s/%d", webhooks, webhookID);
		}
		
		public static final String webhookToken(long webhookID, String token) {
			return String.format("%s/%d/%s", webhooks, webhookID, token);
		}
	}
	
	/**
	 * The list of events that could possibly be emitted <br>
	 * Events include: <br>
	 * <ul>
	 * <li>{@value #READY}</li>
	 * <li>{@value #GUILD_CREATE}</li>
	 * <li>{@value #GUILD_DELETE}</li>
	 * <li>{@value #GUILD_UPDATE}</li>
	 * <li>{@value #GUILD_MEMBERS_CHUNK}</li>
	 * <li>{@value #GUILD_MEMBER_ADD}</li>
	 * <li>{@value #GUILD_MEMBER_AVAILABLE}</li>
	 * <li>{@value #GUILD_MEMBER_REMOVE}</li>
	 * <li>{@value #GUILD_MEMBER_UPDATE}</li>
	 * <li>{@value #GUILD_BAN_ADD}</li>
	 * </ul>
	 * 
	 * @author Perry Berman
	 * @since v0.0.1
	 * @version First draft
	 */
	public static final class Events {
		
		public static final String READY = "ready";
		
		public static final String GUILD_CREATE = "GuildCreate";
		
		public static final String GUILD_DELETE = "GuildDelete";
		
		public static final String GUILD_UPDATE = "GuildUpdate";
		
		public static final String GUILD_MEMBERS_CHUNK = "GuildMembersChunk";
		
		public static final String GUILD_MEMBER_ADD = "GuildMemberAdd";
		
		public static final String GUILD_MEMBER_AVAILABLE = "GuildMemberAvailable";
		
		public static final String GUILD_MEMBER_REMOVE = "GuildMemberRemove";
		
		public static final String GUILD_MEMBER_UPDATE = "GuildMemberUpdate";
		
		public static final String GUILD_MESSAGE_CREATE = "GuildMessageCreate";
		
		public static final String GUILD_MESSAGE_DELETE = "GuildMessageDelete";
		
		public static final String GUILD_MESSAGE_UPDATE = "GuildMessageUpdate";
		
		public static final String GUILD_BAN_ADD = "GuildBanAdd";
		
		public static final String GUILD_BAN_REMOVE = "GuildBanRemove";
		
		public static final String GUILD_ROLE_CREATE = "GuildRoleCreate";
		
		public static final String GUILD_ROLE_DELETE = "GuildRoleDelete";
		
		public static final String GUILD_ROLE_UPDATE = "GuildRoleUpdate";
		
		public static final String GUILD_EMOJIS_UPDATE = "GuildEmojisUpdate";
		
		public static final String CHANNEL_CREATE = "ChannelCreate";
		
		public static final String CHANNEL_DELETE = "ChannelDelete";
		
		public static final String CHANNEL_UPDATE = "ChannelUpdate";
		
		public static final String CHANNEL_PINS_UPDATE = "ChannelPinsUpdate";
		
		public static final String MESSAGE_CREATE = "MessageCreate";
		
		public static final String MESSAGE_DELETE = "MessageDelete";
		
		public static final String MESSAGE_UPDATE = "MessageUpdate";
		
		public static final String PRESENCE_UPDATE = "PresenceUpdate";
		
		public static final String PRIVATE_MESSAGE_CREATE = "PrivateMessageCreate";
		
		public static final String PRIVATE_MESSAGE_DELETE = "PrivateMessageDelete";
		
		public static final String PRIVATE_MESSAGE_UPDATE = "PrivateMessageUpdate";
		
		public static final String TYPING_START = "TypingStart";
		
		public static final String USER_UPDATE = "UserUpdate";
		
		public static final String GUILD_EMOJI_UPDATE = "GuildEmojiUpdate";
	}
	
	public static final class Methods {
		
		public static final int GET = 0;
		
		public static final int POST = 1;
		
		public static final int DELETE = 2;
		
		public static final int PATCH = 3;
		
		public static final int PUT = 4;
	}
	
	public static final class OPCodes {
		
		public static final int DISPATCH = 0;
		
		public static final int HEARTBEAT = 1;
		
		public static final int IDENTIFY = 2;
		
		public static final int STATUS_UPDATE = 3;
		
		public static final int VOICE_STATE_UPDATE = 4;
		
		public static final int VOICE_SERVER_PING = 5;
		
		public static final int RESUME = 6;
		
		public static final int RECONNECT = 7;
		
		public static final int REQUEST_GUILD_MEMBERS = 8;
		
		public static final int INVALID_SESSION = 9;
		
		public static final int HELLO = 10;
		
		public static final int HEARTBEAT_ACK = 11;
		
		public static final int GUILD_SYNC = 12;
	}
	
	/**
	 * This object contains the values needed to test a raw 53bit permissions integer against. Visit
	 * <a href= "https://discordapp.com/developers/docs/topics/permissions#bitwise-permission-flags"> here</a> to view the official list of
	 * permissions and their descriptions
	 * 
	 * @author Perry Berman
	 * @since 0.0.1
	 */
	public static final class PermissionFlags {
		
		public static final int CREATE_INSTANT_INVITE = 1 << 0;
		
		public static final int KICK_MEMBERS = 1 << 1;
		
		public static final int BAN_MEMBERS = 1 << 2;
		
		public static final int ADMINISTRATOR = 1 << 3;
		
		public static final int MANAGE_CHANNELS = 1 << 4;
		
		public static final int MANAGE_GUILD = 1 << 5;
		
		public static final int ADD_REACTION = 1 << 6;
		
		public static final int READ_MESSAGES = 1 << 10;
		
		public static final int SEND_MESSAGES = 1 << 11;
		
		public static final int SEND_TTS_MESSAGES = 1 << 12;
		
		public static final int MANAGE_MESSAGES = 1 << 13;
		
		public static final int EMBED_LINKS = 1 << 14;
		
		public static final int ATTACH_FILES = 1 << 15;
		
		public static final int READ_MESSAGE_HISTORY = 1 << 16;
		
		public static final int MENTION_EVERYONE = 1 << 17;
		
		public static final int USE_EXTERNAL_EMOJIS = 1 << 18;
		
		public static final int CONNECT = 1 << 20;
		
		public static final int SPEAK = 1 << 21;
		
		public static final int MUTE_MEMBERS = 1 << 22;
		
		public static final int DEAFEN_MEMBERS = 1 << 23;
		
		public static final int MOVE_MEMBERS = 1 << 24;
		
		public static final int USE_VAD = 1 << 25;
		
		public static final int CHANGE_NICKNAME = 1 << 26;
		
		public static final int MANAGE_NICKNAMES = 1 << 27;
		
		public static final int MANAGE_ROLES = 1 << 28;
		
		public static final int MANAGE_WEBHOOKS = 1 << 29;
		
		public static final int MANAGE_EMOJIS = 1 << 30;
	}
	
	/**
	 * This object contains every 53bit {@link Permission} {@link Integer} as specified in Discord's API. Visit
	 * <a href= "https://discordapp.com/developers/docs/topics/permissions#bitwise-permission-flags"> here</a> to view the official list of
	 * permissions and their descriptions
	 * 
	 * @author Perry Berman
	 * @since 0.0.1
	 */
	public static final class PermissionsNums {
		
		public static final int CREATE_INSTANT_INVITE = 0x00000001;
		
		public static final int KICK_MEMBERS = 0x00000002;
		
		public static final int BAN_MEMBERS = 0x00000004;
		
		public static final int ADMINISTRATOR = 0x00000008;
		
		public static final int MANAGE_CHANNELS = 0x00000010;
		
		public static final int MANAGE_GUILD = 0x00000020;
		
		public static final int ADD_REACTION = 0x00000040;
		
		public static final int READ_MESSAGES = 0x00000400;
		
		public static final int SEND_MESSAGES = 0x00000800;
		
		public static final int SEND_TTS_MESSAGES = 0x00001000;
		
		public static final int MANAGE_MESSAGES = 0x00002000;
		
		public static final int EMBED_LINKS = 0x00004000;
		
		public static final int ATTACH_FILES = 0x00008000;
		
		public static final int READ_MESSAGE_HISTORY = 0x00010000;
		
		public static final int MENTION_EVERYONE = 0x00020000;
		
		public static final int USE_EXTERNAL_EMOJIS = 0x00040000;
		
		public static final int CONNECT = 0x00100000;
		
		public static final int SPEAK = 0x00200000;
		
		public static final int MUTE_MEMBERS = 0x00400000;
		
		public static final int DEAFEN_MEMBERS = 0x00800000;
		
		public static final int MOVE_MEMBERS = 0x01000000;
		
		public static final int USE_VAD = 0x02000000;
		
		public static final int CHANGE_NICKNAME = 0x04000000;
		
		public static final int MANAGE_NICKNAMES = 0x08000000;
		
		public static final int MANAGE_ROLES = 0x10000000;
		
		public static final int MANAGE_WEBHOOKS = 0x20000000;
		
		public static final int MANAGE_EMOJIS = 0x40000000;
	}
	
	public static final class Status {
		
		public static final int READY = 0;
		
		public static final int CONNECTING = 1;
		
		public static final int RECONNECTING = 2;
		
		public static final int IDLE = 3;
		
		public static final int NEARLY = 4;
		
		public static final int DISCONNECTED = 5;
	}
	
	public static final class WSEvents {
		
		public static final String HELLO = "HELLO";
		
		public static final String READY = "READY";
		
		public static final String RESUMED = "RESUMED";
		
		public static final String GUILD_CREATE = "GUILD_CREATE";
		
		public static final String GUILD_DELETE = "GUILD_DELETE";
		
		public static final String GUILD_UPDATE = "GUILD_UPDATE";
		
		public static final String GUILD_MEMBERS_CHUNK = "GUILD_MEMBERS_CHUNK";
		
		public static final String GUILD_MEMBER_ADD = "GUILD_MEMBER_ADD";
		
		public static final String GUILD_MEMBER_REMOVE = "GUILD_MEMBER_REMOVE";
		
		public static final String GUILD_MEMBER_UPDATE = "GUILD_MEMBER_UPDATE";
		
		public static final String GUILD_BAN_ADD = "GUILD_BAN_ADD";
		
		public static final String GUILD_BAN_REMOVE = "GUILD_BAN_REMOVE";
		
		public static final String GUILD_ROLE_CREATE = "GUILD_ROLE_CREATE";
		
		public static final String GUILD_ROLE_DELETE = "GUILD_ROLE_DELETE";
		
		public static final String GUILD_ROLE_UPDATE = "GUILD_ROLE_UPDATE";
		
		public static final String GUILD_EMOJIS_UPDATE = "GUILD_EMOJIS_UPDATE";
		
		public static final String CHANNEL_CREATE = "CHANNEL_CREATE";
		
		public static final String CHANNEL_DELETE = "CHANNEL_DELETE";
		
		public static final String CHANNEL_UPDATE = "CHANNEL_UPDATE";
		
		public static final String CHANNEL_PINS_UPDATE = "CHANNEL_PINS_UPDATE";
		
		public static final String MESSAGE_CREATE = "MESSAGE_CREATE";
		
		public static final String MESSAGE_DELETE = "MESSAGE_DELETE";
		
		public static final String MESSAGE_REACTION_ADD = "MESSAGE_REACTION_ADD";
		
		public static final String MESSAGE_REACTION_REMOVE = "MESSAGE_REACTION_REMOVE";
		
		public static final String MESSAGE_UPDATE = "MESSAGE_UPDATE";
		
		public static final String PRESENCE_UPDATE = "PRESENCE_UPDATE";
		
		public static final String TYPING_START = "TYPING_START";
		
		public static final String VOICE_STATE_UPDATE = "VOICE_STATE_UPDATE";
		
		public static final String VOICE_SERVER_UPDATE = "VOICE_SERVER_UPDATE";
	}
	
	/**
	 * A useful JSON String deserializer.
	 * 
	 * @author Perry Berman
	 * @since 0.0.1
	 */
	public static final Gson gson = new Gson();
	
	public static final int APIVersion = 7;
	
	public static final String PERMISSIONS_DOCS = "https://discordapp.com/developers/docs/topics/permissions#bitwise-permission-flags";
	
	public static final String HOST = "https://discordapp.com";
	
	public static final String API = String.format("%s/api/v%d", HOST, APIVersion);
	
	public static final String GatewaySuffix = String.format("?v=%d&encoding=json", APIVersion);
	
	public static final String LoaderVersion = "0.1.1";
	
	public static final File MissingTexture = new Resource("discloader", "texture/commands/missing-texture.png").getPath() == null ? null : new File(new Resource("discloader", "texture/gui/icons/missing-icon.png").getPath());
	
	public static final Language enUS = new Language(new Resource("discloader", "lang/en-US.lang").getResourceAsStream(), Locale.US);
	
	/**
	 * An {@link ArrayList} of {@link WSEvents} that are handled by the {@link GatewayListener SocketListener} before the
	 * {@link DiscLoader loader} has emitted ready.
	 * 
	 * @author Perry Berman
	 * @since v0.0.1
	 * @version First draft
	 */
	public static final ArrayList<String> EventWhitelist = new ArrayList<String>(
			Arrays.asList(WSEvents.HELLO, WSEvents.READY, WSEvents.GUILD_CREATE, WSEvents.GUILD_DELETE, WSEvents.GUILD_MEMBER_ADD, WSEvents.GUILD_MEMBER_REMOVE, WSEvents.GUILD_MEMBER_UPDATE, WSEvents.GUILD_MEMBERS_CHUNK));
	
	public static OffsetDateTime creationTime(ISnowflake snowflake) {
		long time = snowflake.getID() >> 22;
		long discord_epoch = 1420070400000L;
		time += discord_epoch;
		return OffsetDateTime.from(Instant.ofEpochMilli(time));
	}
	
	/**
	 * Converts a ISO-8601 DateTime string to a {@link Date} object.
	 * 
	 * @param datetime A DateTime string in ISO-8601 format
	 * @return Date from DateTime string
	 */
	public static final Date parseISO8601(String datetime) {
		return Date.from(OffsetDateTime.parse(datetime).toInstant());
	}
	
	/**
	 * A more efficient readAllBytes method
	 * 
	 * @param file the file to read
	 * @return A byte array containing the file's data
	 * @throws IOException thrown if there is an error reading the file
	 */
	// @SuppressWarnings("resource")
	public static final byte[] readAllBytes(File file) throws IOException {
		try (InputStream is = new FileInputStream(file)) {
			
			// Get the size of the file
			long length = file.length();
			
			// You cannot create an array using a long type.
			// It needs to be an int type.
			// Before converting to an int type, check
			// to ensure that file is not larger than Integer.MAX_VALUE.
			if (length > Integer.MAX_VALUE) {
				throw new IOException("Cannot read the file into memory completely due to it being too large!");
				// File is too large
			}
			
			// Create the byte array to hold the data
			byte[] bytes = new byte[(int) length];
			
			// Read in the bytes
			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}
			// is.r
			// Ensure all the bytes have been read in
			if (offset < bytes.length) {
				throw new IOException("Could not completely read file " + file.getName());
			}
			
			// Close the input stream and return bytes
			is.close();
			return bytes;
		}
	}
	
	/**
	 * A more efficient readAllBytes method
	 * 
	 * @param resource
	 * @return A byte array containing the resource's data
	 * @throws IOException
	 */
	public static byte[] readAllBytes(Resource resource) throws IOException {
		return IOUtils.toByteArray(resource.getResourceAsStream());
	}
}