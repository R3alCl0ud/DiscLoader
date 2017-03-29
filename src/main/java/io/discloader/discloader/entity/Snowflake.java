package io.discloader.discloader.entity;

public class Snowflake implements ISnowflake {

	private String snowflake;

	public Snowflake(String flake) {
		snowflake = flake;
	}

	@Override
	public String getID() {
		return snowflake;
	}
}
