package io.discloader.discloader.entity.user;

public enum UserFlags {
	NONE(-1),
	DISCORD_EMPLOYEE(0),
	DISCORD_PARTNER(1),
	HYPESQUAD_EVENTS(2),
	BUG_HUNTER(3),
	HOUSE_BRAVERY(6),
	HOUSE_BRILLIANCE(7),
	HOUSE_BALANCE(8),
	EARLY_SUPPORTER(9),
	TEAM_USER(10);
	
	private int shift;
	UserFlags(int shift) {
		this.shift = shift;
	}
	
	public int toInt() {
		return 1 << shift;
	}
}
