package io.discloader.discloader.network.rest.actions.guild;

import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;

import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.network.json.GuildJSON;
import io.discloader.discloader.network.rest.actions.RESTAction;
import io.discloader.discloader.util.DLUtil.Endpoints;
import io.discloader.discloader.util.DLUtil.Methods;

public class ModifyGuild extends RESTAction<IGuild> {
	
	private IGuild guild;
	private JSONObject payload;
	
	public ModifyGuild(IGuild guild, JSONObject data) {
		super(guild.getLoader());
		this.guild = guild;
		payload = data;
	}
	
	public CompletableFuture<IGuild> execute() {
		return super.execute(loader.rest.makeRequest(Endpoints.guild(guild.getID()), Methods.PATCH, true, payload));
	}
	
	public void complete(String r, Throwable ex) {
		if (ex != null) {
			future.completeExceptionally(ex);
			return;
		}
		GuildJSON data = gson.fromJson(r, GuildJSON.class);
		guild.setup(data);
		future.complete(guild);
	}
	
}
