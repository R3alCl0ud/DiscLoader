package io.discloader.discloader.entity;

import io.discloader.discloader.network.json.OAuthApplicationJSON;
import io.discloader.discloader.util.Constants;

public class OAuth2Application {

	/**
	 * The clientID of the application
	 */
	public final String clientID;
	
	/**
	 * The name of the application 
	 */
	public String name;
	
	/**
	 * The applications icon hash
	 */
	public String icon;
	
	/**
	 * The applications description
	 */
	public String description;
	
	public String[] rpcOrigins;

	/**
	 * Whether or not the application is public
	 */
	public boolean isPublic;
	
	/**
	 * requires a code grant to join
	 */
	public boolean requiresGrant;

	/**
	 * The {@link User} that owns this OAuth2 Application
	 */
	public final User owner;

	/**
	 * Creates a new OAuth2Application
	 * @param data The application's data
	 * @param owner The owner of the application
	 */
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

	/**
	 * 
	 * @param permissions The 53bit permissions integer
	 * @return The Application's OAuth2 authorization link
	 */
	public String getOAuthURL(int permissions) {
		return Constants.Endpoints.OAuth2Authorize(this.clientID, "bot", permissions);
	}

	/**
	 * 
	 * @return The Application's OAuth2 authorization link
	 */
	public String getOAuthURL() {
		return this.getOAuthURL(0);
	}
	
}
