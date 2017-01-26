package io.disc.DiscLoader.objects.structures;

import java.util.HashMap;

import io.disc.DiscLoader.DiscLoader;
import io.disc.DiscLoader.objects.gateway.RoleJSON;
import io.disc.DiscLoader.objects.gateway.UserJSON;

public class Mentions {
	public boolean everyone;
	public boolean isMentioned;
	public HashMap<String, User> users;
	public HashMap<String, Role> roles;
	
	public Mentions(DiscLoader loader, UserJSON[] mentions, RoleJSON[] mention_roles, boolean mention_everyone) {
		this.everyone = mention_everyone;
		this.isMentioned = this.everyone ? true : false;
		for (int i = 0; i < mentions.length; i++) {
			
		}
	}
	
}
