package io.disc.DiscLoader.objects.structures;

import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;

import io.disc.DiscLoader.DiscLoader;
import io.disc.DiscLoader.objects.gateway.UserJSON;

public class User {
	public final DiscLoader loader;
	public final String id;
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

		if (user.username != null) {
			this.setup(user);
		}
	}
	
	
	/**
	 * @param user
	 */
	public User(DiscLoader loader, User user) {
		this.loader = loader;
		
		this.id = user.id;
		
		this.username = user.username;

		this.discriminator = user.discriminator;
		
		this.password = user.password;

		this.avatar = user.avatar;

		this.bot = user.bot;
	}


	public void setup(UserJSON data) {
		this.username = data.username;

		this.discriminator = data.discriminator;
		
		this.password = data.password;

		this.avatar = data.avatar;

		this.bot = data.bot;
	}
	
	public String toString() {
		return MessageFormat.format("<@{0}>", new Object[] {this.id});
	}

	
	public CompletableFuture<User> setUsername(String username) {
		return this.loader.rest.setUsername(username);
	}
	

	/**
	 * @param data
	 * @return this 
	 */
	public User patch(UserJSON data) {
		if (data.username != null) this.username = data.username;

		if (data.discriminator != null) this.discriminator = data.discriminator;
		
		if (data.password != null)this.password = data.password;

		if (data.password != null) this.avatar = data.avatar;

		if (data.bot == true || data.bot == false) this.bot = data.bot;
		
		return this;
	}
}
