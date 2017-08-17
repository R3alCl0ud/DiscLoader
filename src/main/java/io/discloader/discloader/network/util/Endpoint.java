package io.discloader.discloader.network.util;

import io.discloader.discloader.util.DLUtil;

/**
 * @author Perry Berman
 *
 */
public enum Endpoint {
	DISCORD_BASE_URL("https://discordapp.com", false),
	DISCORD_API("%s/api/v%d", false, DISCORD_BASE_URL, DLUtil.APIVersion),
	CDN("https://cdn.discordapp.com", false),
	
	OAUTH2("%s/oauth2", false, DISCORD_API),
	
	GATEWAY("%s/gateway", true, DISCORD_API),
	GATEWAY_BOT("%s/gateway/bot", true, DISCORD_API),
	

	GUILDS("%s/guilds", true, DISCORD_API);
	
	private String route;
	private boolean auth;
	
	private Endpoint(String url, boolean auth, Object... objects) {
		for (Object object : objects)
			url = String.format(url, object);
		route = url;
		this.auth = auth;
	}
	
	public String compile(Object... objects) {
		for (Object object : objects)
			route = String.format(route, object);
		return route;
	}
	
	@Override
	public String toString() {
		return route;
	}
	
	/**
	 * @return the auth
	 */
	public boolean requiresAuth() {
		return auth;
	}
}
