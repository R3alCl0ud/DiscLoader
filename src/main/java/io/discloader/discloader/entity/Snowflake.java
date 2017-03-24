package io.discloader.discloader.entity;

import io.discloader.discloader.util.ISnowflake;

public class Snowflake implements ISnowflake {

	private String snowflake;

	public Snowflake(String flake) {
		snowflake = flake;
	}

	@Override
	public double toDouble() {
		return Double.parseDouble(snowflake);
	}

	@Override
	public float toFloat() {
		return Float.parseFloat(snowflake);
	}

	@Override
	public long toLong() {
		return Long.parseLong(snowflake);
	}

	@Override
	public String toString() {
		return snowflake;
	}
}
