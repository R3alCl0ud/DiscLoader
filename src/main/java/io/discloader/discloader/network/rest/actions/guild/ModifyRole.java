package io.discloader.discloader.network.rest.actions.guild;

import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;

import io.discloader.discloader.common.registry.EntityBuilder;
import io.discloader.discloader.common.registry.factory.GuildFactory;
import io.discloader.discloader.entity.guild.IRole;
import io.discloader.discloader.network.json.RoleJSON;
import io.discloader.discloader.network.rest.actions.RESTAction;
import io.discloader.discloader.util.DLUtil.Endpoints;
import io.discloader.discloader.util.DLUtil.Methods;

public class ModifyRole extends RESTAction<IRole> {

	private final IRole role;
	private final JSONObject data;
	private GuildFactory gfac = EntityBuilder.getGuildFactory();

	public ModifyRole(IRole role, JSONObject data) {
		super(role.getLoader());
		this.role = role;
		this.data = data;
	}

	@Override
	public CompletableFuture<IRole> execute() {
		return super.execute(loader.rest.makeRequest(Endpoints.guildRole(role.getGuild().getID(), role.getID()), Methods.PATCH, true, data));
	}

	public void complete(String r, Throwable ex) {
		super.complete(r, ex);
		if (!future.isDone()) future.complete(gfac.buildRole(role.getGuild(), gson.fromJson(r, RoleJSON.class)));
	}

}
