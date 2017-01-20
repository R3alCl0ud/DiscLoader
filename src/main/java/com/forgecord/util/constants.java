package main.java.com.forgecord.util;

import java.text.MessageFormat;

public class constants {
	public static final String HOST = "https://discordapp.com";
	public static final String CDN = "https://discordapp.com";
	public static final String API = HOST + "/api/v6";
	public static class Endpoints {
		public static String login = API + "/auth/login";
		public static String logout = API + "/auth/login";
		public static String gateway = API + "/gateway";
		public static String botGateway = API + "/gateway/bot";
		public static String CDN = "https://cdn.discordapp.com";
		public static String invite(String id) { return MessageFormat.format("%s/invite/%s", API, id); }
		public static String inviteLink(String id) { return MessageFormat.format("https://discord.gg/%s", API, id); }
		public static String assets(String asset) {
			return HOST + "/assets/" + asset;
		}
		
		public static String user(String userID) {
			return API + "/users/" + userID;
		}
		public static String userChannels(String userID) {
			return Endpoints.user(userID) + "/channels";
		}
		public static String userProfile(String userID) { return MessageFormat.format("%s/profile", Endpoints.user(userID)); }
		public static String avatar(String id, String avatar) {
			return Endpoints.CDN + "/avatars/" + id + "/" + avatar + (avatar.startsWith("a_") ? ".gif" : ".jpg") + "?size=1024";
		}
	}
	
	public static class Status {
		public static int READY = 0;
		public static int CONNECTING = 1;
		public static int RECONNECTING = 2;
		public static int IDLE = 3;
		public static int NEARLY = 4;
		public static int DISCONNECTED = 5;
	}
	
	public static class OPCodes {
		public static int DISPATCH = 0;
		public static int HEARTBEAT = 1;
		public static int IDENTIFY = 2;
		public static int STATUS_UPDATE = 3;
		public static int VOICE_STATE_UPDATE = 4;
		public static int VOICE_SERVER_PING = 5;
		public static int RESUME = 6;
		public static int RECONNECT = 7;
		public static int REQUEST_GUILD_MEMBERS = 8;
		public static int INVALID_SESSION = 9;
		public static int HELLO = 10;
		public static int HEARTBEAT_ACK = 11;
	}
	
	public static class WSEvents {
		public static String READY = "READY";
		public static String GUILD_CREATE = "GUILD_CREATE";
		public static String GUILD_DELETE = "GUILD_DELETE";
		public static String GUILD_UPDATE = "GUILD_UPDATE";
		public static String GUILD_MEMBERS_CHUNK = "GUILD_MEMBERS_CHUNK";
		public static String GUILD_MEMBER_ADD = "GUILD_MEMBER_ADD";
		public static String GUILD_MEMBER_REMOVE = "GUILD_MEMBER_REMOVE";
		public static String GUILD_MEMBER_UPDATE = "GUILD_MEMBER_UPDATE";
		public static String GUILD_BAN_ADD = "GUILD_BAN_ADD";
		public static String GUILD_BAN_REMOVE = "GUILD_BAN_REMOVE";
		public static String GUILD_ROLE_CREATE = "GUILD_ROLE_CREATE";
		public static String GUILD_ROLE_DELETE = "GUILD_ROLE_DELETE";
		public static String GUILD_ROLE_UPDATE = "GUILD_ROLE_UPDATE";
		public static String GUILD_EMOJIS_UPDATE = "GUILD_EMOJIS_UPDATE";
	}

	public static class Events {
		public static String READY = "ready";
		public static String GUILD_CREATE = "GuildCreate";
		public static String GUILD_DELETE = "GuildDelete";
		public static String CHANNEL_CREATE = "ChannelCreate";
	}
}