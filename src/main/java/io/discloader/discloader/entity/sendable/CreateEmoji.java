package io.discloader.discloader.entity.sendable;

public class CreateEmoji {
	public String name;
	public String image;
	public String[] roles;
	
	public CreateEmoji(String name, String image, String...roles) {
		this.name = name;
		this.image = image;
		this.roles = roles;
	}
}