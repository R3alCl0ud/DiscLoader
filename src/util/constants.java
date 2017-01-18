package util;

import org.json.*;

public class constants {
	public static final String HOST = "https://discordapp.com";
	public static final String CDN = "https://discordapp.com";
	public static final String API = HOST + "/api/v6";
//	public static final JSONObject Endpoints = new JSONObject().put("login", API + "/auth/login")
//			.put("logout", API + "/auth/logout").put("gateway", API + "/gateway")
//			.put("botGateway", API + "/gateway/bot").put("CDN", "https://cdn.discordapp.com")
//			.put("me", API + "/users/@me");

	public static class Endpoints {
		public static String login = API + "/auth/login";
		public static String logout = API + "/auth/login";
		public static String gateway = API + "/auth/login";
		public static String botGateway = API + "/auth/login";
		public static String CDN = "https://cdn.discordapp.com";
		public static String assets(String asset) {
			return HOST + "/assets/" + asset;
		}
		
		public static String user(String userID) {
			return API + "/users/" + userID;
		}
		public static String userChannels(String userID) {
			return Endpoints.user(userID) + "/channels";
		}
		public static String avatar(int userID, String avatar) {
			return Endpoints.CDN + "/avatars/" + userID + "/" + avatar + (avatar.startsWith("a_") ? ".gif" : ".jpg") + "?size=1024";
		}

		
	}
	



}
