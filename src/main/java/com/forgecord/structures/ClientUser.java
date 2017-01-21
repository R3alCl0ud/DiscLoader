package main.java.com.forgecord.structures;

import org.json.JSONObject;

import main.java.com.forgecord.client.Client;

/**
 * @author Perry Berman
 *
 */
public class ClientUser extends User {
	
	private JSONObject settings;
	
	/**
	 * @return the settings
	 */
	public JSONObject getSettings() {
		return settings;
	}

	/**
	 * @param settings the settings to set
	 */
	public void setSettings(JSONObject settings) {
		this.settings = settings;
	}


	/**
	 * @param client the {@link Client} that instantiated this {@link ClientUser} 
	 * @param data the current user's information
	 */
	public ClientUser(Client client, JSONObject data) {
		super(client, data);
	}

	/**
	 * @param game the game to set as playing
	 */
	public void setGame(String game) {
		
	}
	
	/**
	 * @param status 
	 */
	public void setStatus(String status) {
		
	}
	
}
