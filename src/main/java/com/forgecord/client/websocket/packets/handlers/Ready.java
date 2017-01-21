package main.java.com.forgecord.client.websocket.packets.handlers;

import java.util.Date;

import org.json.JSONObject;

import main.java.com.forgecord.client.Client;
import main.java.com.forgecord.client.websocket.packets.WebSocketPacketManager;
import main.java.com.forgecord.structures.ClientUser;

public class Ready extends AbstractHandler {

	public Ready(WebSocketPacketManager packetManager) {
		super(packetManager);
	}
	
	public void handle(JSONObject packet) {
		Client client = this.packetManager.ws.client;
		JSONObject data = (JSONObject) packet.get("d");
	
		client.ws.heartbeat(false);

		ClientUser clientUser = new ClientUser(client,  data.getJSONObject("user"));
		clientUser.setSettings(data.getJSONObject("user_settings"));
		client.user = clientUser;
		client.readyAt = new Date();
		client.users.put(clientUser.id, clientUser);
		data.getJSONArray("guilds").forEach(guild -> client.dataManager.newGuild((JSONObject) guild));
		System.out.printf("Username: %s%nGuilds: %d%nChannels: %d%n", client.user.username, client.guilds.size(), client.channels.size());
	}
}
