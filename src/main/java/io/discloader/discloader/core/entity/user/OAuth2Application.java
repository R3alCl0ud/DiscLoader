package io.discloader.discloader.core.entity.user;

import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.json.OAuthApplicationJSON;
import io.discloader.discloader.network.util.Endpoints;

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

	/**
	 * The applications rpcOrigins
	 */
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
	public final IUser owner;

	/**
	 * Creates a new OAuth2Application
	 * 
	 * @param data
	 *            The application's data
	 * @param owner
	 *            The owner of the application
	 */
	public OAuth2Application(OAuthApplicationJSON data, IUser owner) {
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
	 * Generates a link to the applications authorization page
	 * 
	 * @param permissions
	 *            The 53bit permissions integer
	 * @return The Application's OAuth2 authorization link
	 */
	public String getOAuthURL(int permissions) {
		return Endpoints.OAuth2Authorize(SnowflakeUtil.parse(clientID), "bot", permissions);
	}

	/**
	 * Generates a link to the applications authorization page
	 * 
	 * @return The Application's OAuth2 authorization link
	 */
	public String getOAuthURL() {
		return getOAuthURL(0);
	}

}
