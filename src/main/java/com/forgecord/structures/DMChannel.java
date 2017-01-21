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
public class DMChannel extends TextChannel {

	/**
	 * @param client
	 * @param data
	 */
	public DMChannel(Client client, JSONObject data) {
		super(client, data);
	}

}
