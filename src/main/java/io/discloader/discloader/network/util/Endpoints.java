package io.discloader.discloader.network.util;

/**
 * @author Perry Berman
 *
 */
public enum Endpoints {
	DISCORD_BASE_URL("https://discordapp.com", false), API("%s/api/v6", false, DISCORD_BASE_URL), OAUTH2("%s/oauth2", false, API), GATEWAY("%s/gateway", true, API), BOT_GATEWAY("%s/gateway/bot", true, API), GUILDS("%s/guilds", true, API);
	
	private String route;
	private boolean auth;
	
	private Endpoints(String url, boolean auth, Object... objects) {
		for (Object object : objects)
			url = String.format(url, object);
		route = url;
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
		return this.auth;
	}
}
