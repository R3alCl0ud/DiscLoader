package io.disc.DiscLoader.objects.structures;

import java.util.HashMap;

import io.disc.DiscLoader.DiscLoader;
import io.disc.DiscLoader.objects.gateway.RoleJSON;
import io.disc.DiscLoader.objects.gateway.UserJSON;

public class Mentions {

	public final DiscLoader loader;

	public final Message message;

	public boolean everyone;
	public boolean isMentioned;
	public HashMap<String, User> users;
	public HashMap<String, Role> roles;

	public Mentions(Message message, UserJSON[] mentions, RoleJSON[] mention_roles, boolean mention_everyone) {
		this.message = message;
		this.loader = this.message.loader;
		this.everyone = mention_everyone;
		this.isMentioned = this.everyone ? true : false;
		this.users = new HashMap<String, User>();
		this.roles = new HashMap<String, Role>();
		for (UserJSON data : mentions) {
			this.users.put(data.id, this.loader.addUser(data));
		}
		for (RoleJSON data : mention_roles) {
			
		}
	}

	public void patch(UserJSON[] mentions, RoleJSON[] mention_roles, boolean mention_everyone) {

	}

}
