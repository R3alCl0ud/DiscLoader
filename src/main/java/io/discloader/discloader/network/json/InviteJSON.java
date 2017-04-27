package io.discloader.discloader.network.json;

public class InviteJSON {
	public String code;
	public String created_at;
	
	public int uses;
	public int max_uses;
	public int max_age;

	public boolean temporary;
	public boolean revoked;

	public GuildJSON guild;
	public ChannelJSON channel;
	public UserJSON inviter;
}
