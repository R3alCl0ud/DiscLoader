package main.java.com.forgecord.client.websocket.packets.handlers;

import org.json.JSONObject;

import main.java.com.forgecord.client.Client;
import main.java.com.forgecord.client.websocket.packets.WebSocketPacketManager;
import main.java.com.forgecord.structures.Guild;

public class GuildCreate extends AbstractHandler {
	public GuildCreate(WebSocketPacketManager packetManager) {
		super(packetManager);
	}
	
	public void handle(JSONObject packet) {
		Client client = this.packetManager.ws.client;
		JSONObject data = packet.getJSONObject("d");
		Guild guild = client.guilds.get(data.getString("id"));
		if (guild != null) {
			if (guild.unavailable && !data.getBoolean("unavailable")) {
				guild.setup(data);
				this.packetManager.ws.checkIfReady();
			}
		} else {
			client.dataManager.newGuild(data);
		}
	}
}
