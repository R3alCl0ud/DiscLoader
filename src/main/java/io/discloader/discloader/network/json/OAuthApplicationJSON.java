package io.discloader.discloader.network.json;

public class OAuthApplicationJSON {

	public String id;
	public String name;
	public String icon;
	public String description;
	public String[] rpc_origins;
	
	public boolean bot_public;
	public boolean bot_requires_code_grant;
	
	public UserJSON owner;

}
