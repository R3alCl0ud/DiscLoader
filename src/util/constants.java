package util;

import org.json.*;

public class constants {
	public static final String HOST = "https://discordapp.com";
	public static final String CDN = "https://discordapp.com";
	public static final String API = HOST + "/api/v6";
	public static final JSONObject Endpoints = new JSONObject().put("login", API + "/auth/login")
			.put("logout", API + "/auth/logout").put("gateway", API + "/gateway")
			.put("botGateway", API + "/gateway/bot").put("CDN", "https://cdn.discordapp.com");

	public static String assets(String asset) {
		return HOST + "/assets/" + asset;
	}

	public static String avatar(int userID, String avatar) {
		return Endpoints.getString("CDN") + "/avatars/" + userID + "/" + avatar + ".jpg?size=1024";
	}
}
