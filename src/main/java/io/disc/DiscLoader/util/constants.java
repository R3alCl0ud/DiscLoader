package io.disc.DiscLoader.util;

import java.text.MessageFormat;

public class constants {
	public static final String HOST = "https://discordapp.com";
	public static final String API = MessageFormat.format("{0}/api/v6", new Object[] { HOST });

	public static final class Endpoints {
		public static final String login = API + "/auth/login";
		public static final String logout = API + "/auth/login";
		public static final String gateway = API + "/gateway";
		public static final String botGateway = API + "/gateway/bot";
		public static final String CDN = "https://cdn.discordapp.com";

		public static final String invite(String id) {
			return MessageFormat.format("{0}/invite/{1}", new Object[] { API, id });
		}

		public static final String inviteLink(String id) {
			return MessageFormat.format("https://discord.gg/{0}", new Object[] { id });
		}

		public static final String assets(String asset) {
			return MessageFormat.format("{0}/assets/{1}", new Object[] { HOST, asset });
		}

		public static final String user(String userID) {
			return MessageFormat.format("{0}/users/{1}", new Object[] { API, userID });
		}

		public static final String userChannels(String userID) {
			return MessageFormat.format("{0}/channels", new Object[] { Endpoints.user(userID) });
		}

		public static final String userProfile(String userID) {
			return MessageFormat.format("{0}/profile", new Object[] { Endpoints.user(userID) });
		}

		public static final String avatar(String id, String avatar) {
			return MessageFormat.format("{0}/avatars/{1}/{2}{3}?size=1024",
					new Object[] { Endpoints.CDN, id, avatar, avatar.startsWith("a_") ? ".gif" : ".jpg" });
		}

		public static final String messages(String channelID) {
			return MessageFormat.format("{0}/channels/{1}/messages", new Object[] { API, channelID });
		}

		public static final String message(String channelID, String messageID) {
			return MessageFormat.format("{0}/channels/{1}/messages/{2}", new Object[] { API, channelID, messageID });
		}
	}

	public static final class Methods {
		public static final int GET = 0;
		public static final int POST = 1;
		public static final int DELETE = 2;
		public static final int PATCH = 3;
		public static final int PUT = 4;
	}

	public static final class Status {
		public static final int READY = 0;
		public static final int CONNECTING = 1;
		public static final int RECONNECTING = 2;
		public static final int IDLE = 3;
		public static final int NEARLY = 4;
		public static final int DISCONNECTED = 5;
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
	}

	public static final class WSEvents {
		public static final String HELLO = "HELLO";
		public static final String READY = "READY";
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
		public static final String CHANNE_DELETE = "CHANNEL_DELETE";
	}

	public static final class Events {
		public static final String READY = "ready";
		public static final String GUILD_CREATE = "GuildCreate";
		public static final String GUILD_DELETE = "GuildDelete";
		public static final String GUILD_UPDATE = "GuildUpdate";
		public static final String GUILD_MEMBERS_CHUNK = "GuildMembersChunk";
		public static final String GUILD_MEMBER_ADD = "GuildMemberAdd";
		public static final String GUILD_MEMBER_REMOVE = "GuildMemberRemove";
		public static final String GUILD_MEMBER_UPDATE = "GuildMemberUpdate";
		public static final String GUILD_BAN_ADD = "GuildBanAdd";
		public static final String GUILD_BAN_REMOVE = "GuildBanRemove";
		public static final String GUILD_ROLE_CREATE = "GuildRoleCreate";
		public static final String GUILD_ROLE_DELETE = "GuildRoleDelete";
		public static final String GUILD_ROLE_UPDATE = "GuildRoleUpdate";
		public static final String GUILD_EMOJIS_UPDATE = "GuildEmojisUpdate";
		public static final String CHANNEL_CREATE = "ChannelCreate";
		public static final String CHANNE_DELETE = "ChannelDelete";
	}
}