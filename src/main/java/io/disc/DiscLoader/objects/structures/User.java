package io.disc.DiscLoader.objects.structures;

import java.text.MessageFormat;

import io.disc.DiscLoader.DiscLoader;
import io.disc.DiscLoader.objects.gateway.UserJSON;

public class User {
	public final DiscLoader loader;
	public String id;
	public String email;
	public String password;
	public String username;
	public String avatar;
	public String discriminator;
	public boolean bot;
	public boolean verified;
	public boolean mfa;

	public User(DiscLoader loader, UserJSON user) {
		this.loader = loader;
		
		this.id = user.id;

		this.username = user.username;

		this.discriminator = user.discriminator;
		
		this.password = user.password;

		this.avatar = user.avatar;

		this.bot = user.bot;
	}
	
	public String toString() {
		return MessageFormat.format("<@%d>", new Object[] {this.id});
	}
}
