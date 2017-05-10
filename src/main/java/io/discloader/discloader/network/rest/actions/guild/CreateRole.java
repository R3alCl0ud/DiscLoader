package io.discloader.discloader.network.rest.actions.guild;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.registry.EntityBuilder;
import io.discloader.discloader.common.registry.factory.GuildFactory;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IRole;
import io.discloader.discloader.entity.sendable.SendableRole;
import io.discloader.discloader.network.json.RoleJSON;
import io.discloader.discloader.network.rest.actions.RESTAction;
import io.discloader.discloader.util.DLUtil.Endpoints;
import io.discloader.discloader.util.DLUtil.Methods;

public class CreateRole extends RESTAction<IRole> {

	private IGuild guild;
	private GuildFactory gfac = EntityBuilder.getGuildFactory();
	private SendableRole data;

	public CreateRole(IGuild guild, SendableRole data) {
		super(guild.getLoader());
		this.guild = guild;
		this.data = data;
	}

	public CompletableFuture<IRole> execute() {
		return super.execute(loader.rest.makeRequest(Endpoints.guildRoles(guild.getID()), Methods.POST, true, data));
	}

	public void complete(String s, Throwable ex) {
		if (ex != null) {
			future.completeExceptionally(ex.getCause());
			return;
		}
		IRole role = gfac.buildRole(guild, gson.fromJson(s, RoleJSON.class));
		future.complete(role);
		return;
	}

}
