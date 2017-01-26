package io.disc.DiscLoader.objects.structures;

import io.disc.DiscLoader.objects.gateway.UserGateway;

public class User {
	public String id;
	public String email;
	public String password;
	public String username;
	public String avatar;
	public String discriminator;
	public boolean bot;
	public boolean verified;
	public boolean mfa;

	public User(UserGateway user) {
		this.id = user.id;

		this.username = user.username;

		this.discriminator = user.discriminator;
		
		this.password = user.password;

		this.avatar = user.avatar;

		this.bot = user.bot;
	}
}
