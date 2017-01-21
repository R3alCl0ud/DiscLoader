package main.java.com.forgecord.structures;

import org.json.JSONObject;

import main.java.com.forgecord.client.Client;

/**
 * Channel base class, All channel types extend this class
 * 
 * @author Perry Berman
 *
 */
public class Channel {

	/**
	 * 
	 */
	public final String id;
	
	/**
	 * the channel's name
	 */
	public String name;
	
	/**
	 * The channel's type 
	 */
	public String type;

	
	/**
	 * the {@link Client client} that cached the channel 
	 */
	public Client client;

	/**
	 * @param client
	 * @param data
	 */
	public Channel(Client client, JSONObject data) {
		this.client = client;

		this.id = data.getString("id");
		
		this.name = null;
	}
}
