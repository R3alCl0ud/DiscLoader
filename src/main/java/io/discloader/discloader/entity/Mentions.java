package io.discloader.discloader.entity;

import java.util.HashMap;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.network.gateway.json.RoleJSON;
import io.discloader.discloader.network.gateway.json.UserJSON;

public class Mentions {

	public final DiscLoader loader;

	public final Message message;

	public final Guild guild;

	public boolean everyone;
	public boolean isMentioned;
	public HashMap<String, User> users;
	public HashMap<String, Role> roles;

	public Mentions(Message message, UserJSON[] mentions, RoleJSON[] mention_roles, boolean mention_everyone) {
		this.message = message;
		this.loader = this.message.loader;
		this.everyone = mention_everyone;
		this.isMentioned = this.everyone ? true : false;
		this.guild = this.message.guild != null ? this.message.guild : null;
		this.users = new HashMap<String, User>();
		this.roles = new HashMap<String, Role>();
		for (UserJSON data : mentions) {
			this.users.put(data.id, this.loader.addUser(data));
		}
		if (this.guild != null) {
			for (RoleJSON data : mention_roles) {
				this.roles.put(data.id, this.guild.roles.get(data.id));
			}
		}
	}

	public void patch(UserJSON[] mentions, RoleJSON[] mention_roles, boolean mention_everyone) {

	}

}
