package io.discloader.discloader.network.json;

public class MemberJSON {

	public UserJSON user;

	public String guild_id;
	public String nick;
	public String[] roles;
	public String joined_at;
	public boolean deaf;
	public boolean mute;

	public MemberJSON() {
		this(null);
	}

	public MemberJSON(UserJSON user) {
		this.user = user;
		this.nick = null;
		roles = new String[] {};
		
	}

}
