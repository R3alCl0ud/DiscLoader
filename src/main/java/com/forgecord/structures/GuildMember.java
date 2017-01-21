package main.java.com.forgecord.structures;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONObject;

public class GuildMember {
	
	public Guild guild;
	
	public User user;
	
	public String nickname;
	
	public HashMap<String, Role> roles;
	
	public Date joinedAt;
	
	public boolean deaf;
	public boolean mute;
	
	public GuildMember(Guild guild, JSONObject data) {
		this.guild = guild;
		
		this.user = this.guild.client.dataManager.newUser(data.getJSONObject("user"));
		
		this.roles = new HashMap<String, Role>();
		
		this.nickname = data.getString("nick");
		
		this.joinedAt = Date.from(Instant.parse(data.has("joined_at") ? data.getString("joined_at") : "0"));
		
		this.deaf = data.getBoolean("deaf");
		
		this.mute = data.getBoolean("mute");
		
		data.getJSONArray("roles").forEach(role -> {
			
		});
	}
}
