package io.discloader.discloader.network.rest;

public class QueryParameter {

	private final String name;
	private final Object value;

	public QueryParameter(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

}
