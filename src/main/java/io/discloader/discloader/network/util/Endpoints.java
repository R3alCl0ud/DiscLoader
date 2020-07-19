package io.discloader.discloader.network.util;

import io.discloader.discloader.util.DLUtil;

/**
 * @author Perry Berman
 */
public class Endpoints {
	
	public static final String Discord = "https://discord.com";
	public static final String API = String.format("%s/api/v%d", Discord, DLUtil.APIVersion);
	public static final String CDN = "https://cdn.discordapp.com";
	
	public static final String GatewaySuffix = String.format("?v=%d&encoding=json", DLUtil.APIVersion);
	
	public static final String EmojiJSON = "https://raw.githubusercontent.com/emojione/emojione/master/emoji.json";
	
	public static final String OAuth2 = String.format("%s/oauth2", API);
	
	public static final String login = String.format("%s/auth/login", API);
	public static final String logout = String.format("%s/auth/logout", API);
	
	public static final String gateway = String.format("%s/gateway", API);
	public static final String botGateway = String.format("%s/gateway/bot", API);
	
	public static final String currentUser = String.format("%s/users/@me", API);
	public static final String currentUserGuilds = String.format("%s/users/@me/guilds", API);
	public static final String currentUserChannels = String.format("%s/channels", currentUser);
	public static final String currentOAuth2Application = String.format("%s/applications/@me", OAuth2);
	
	public static final String channels = String.format("%s/channels", API);
	public static final String guilds = String.format("%s/guilds", API);
	public static final String webhooks = String.format("%s/webhooks", API);
	public static final String voiceRegions = String.format("%s/voice/regions", API);
	
	public static final String defaultGroupIcon = String.format("%s/assets/f046e2247d730629309457e902d5c5b3.svg", Discord);
	
	public static final String appAssets(long appID, String asset) {
		return String.format("%s/app-assets/%d/%s", CDN, appID, asset);
	}
	
	public static final String appAssets(String appID, String asset) {
		return String.format("%s/app-assets/%s/%s", CDN, appID, asset);
	}
	
	public static final String assets(String asset) {
		return String.format("%s/assets/%s", Discord, asset);
	}
	
	public static final String auditLogs(long guildID) {
		return String.format("%s/audit-logs", guild(guildID));
	}
	
	public static final String avatar(long id, String avatar) {
		return String.format("%s/avatars/%d/%s%s", CDN, id, avatar, avatar.startsWith("a_") ? ".gif" : ".png");
	}
	
	public static final String bulkDelete(long channelID) {
		return String.format("%s/bulk-delete", messages(channelID));
	}
	
	public static final String channel(long channelID) {
		return String.format("%s/channels/%d", API, channelID);
	}
	
	public static final String channelIcon(long channelID, String iconHash) {
		return String.format("%s/channel-icons/%d/%s.png", CDN, channelID, iconHash);
	}
	
	public static final String channelRecipient(long channelID, long userID) {
		return String.format("%s/%d", channelRecipients(channelID), userID);
	}
	
	public static final String channelRecipients(long channelID) {
		return String.format("%s/%d/recipients", channels, channelID);
	}
	
	public static final String channelInvites(long channelID) {
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
	
	public static final String defaultAvatar(int num) {
		return String.format("%s/embed/avatars/%d.png", CDN, num);
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
	
	public static final String guildRegions(long guildID) {
		return String.format("%s/regions", guild(guildID));
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
	
	public static final String voiceGateway(String url) {
	    return String.format("wss://%s?v=%d", url,  DLUtil.VoiceAPIVersion);
	}
	
}
