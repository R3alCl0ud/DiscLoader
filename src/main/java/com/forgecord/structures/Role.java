package main.java.com.forgecord.structures;

import org.json.JSONObject;

public class Role {
	
	public final Guild guild;
	public final String id;
	public String name;
	
	public int color;
	public int position;
	public int permissions;
	
	public boolean hoist;
	public final boolean managed;
	public boolean mentionable;
	
	public Role(Guild guild, JSONObject data) {
		this.guild = guild;
		
		this.id = data.getString("id");
		
		this.name = data.getString("name");
		
		this.color = data.getInt("color");
		
		this.hoist = data.getBoolean("hoist");
		
		this.position = data.getInt("position");
		
		this.permissions = data.getInt("permissions");
		
		this.managed = data.getBoolean("managed");
		
		this.mentionable = data.getBoolean("mentionable");
	}
}
