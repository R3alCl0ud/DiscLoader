package io.discloader.discloader.entity;

import io.discloader.discloader.network.json.OAuthApplicationJSON;
import io.discloader.discloader.util.Constants;

public class OAuth2Application {

	public final String clientID;
	public String name;
	public String icon;
	public String description;
	public String[] rpcOrigins;

	public boolean isPublic;
	public boolean requiresGrant;

	public final User owner;

	public OAuth2Application(OAuthApplicationJSON data, User owner) {
		this.clientID = data.id;

		this.name = data.name;

		this.icon = data.icon;

		this.description = data.description;

		this.rpcOrigins = data.rpc_origins;

		this.isPublic = data.bot_public;

		this.requiresGrant = data.bot_requires_code_grant;

		this.owner = owner;
	}

	public String getOAuthURL(int permissions) {
		return Constants.Endpoints.OAuth2Authorize(this.clientID, "bot", permissions);
	}

	public String getOAuthURL() {
		return this.getOAuthURL(0);
	}
	
}
