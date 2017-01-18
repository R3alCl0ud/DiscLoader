package structures;

import org.json.JSONObject;

import client.Client;
import util.constants;

public class User {
	
	public int id;
	public String username;
	public String discriminator;
	public String avatar;
	public boolean bot;
	public int lastMessageID;
	public Client client;
	
	public User(Client client, JSONObject data) {
		this.client = client;
	}
	
	public void setup(JSONObject data) {
		this.id = data.getInt("id");
		
		
		this.username = data.getString("avatar");
		
		
		this.discriminator = data.getString("discriminator");
		
		
		this.avatar = data.getString("avatar");
		
		
		this.bot = data.getBoolean("bot");
	}
	
	public String avatarURL() {
		return constants.Endpoints.avatar(this.id, this.avatar);
	}
}
