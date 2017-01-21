/**
 * 
 */
package main.java.com.forgecord.structures;

import org.json.JSONObject;

import main.java.com.forgecord.client.Client;

/**
 * @author Perry Berman
 *
 */
public class GroupChannel extends DMChannel {

	/**
	 * @param client
	 * @param data
	 */
	public GroupChannel(Client client, JSONObject data) {
		super(client, data);
	}

}
