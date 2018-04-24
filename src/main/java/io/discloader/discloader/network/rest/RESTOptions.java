package io.discloader.discloader.network.rest;

/**
 * @author Perry Berman
 */
public class RESTOptions {

	private final boolean auth;
	private final String reason;
	private final Object payload;
	private final QueryParameter[] parameters;

	public RESTOptions() {
		this(true, (Object) null, (String) null);
	}

	public RESTOptions(boolean auth) {
		this(auth, (Object) null, (String) null);
	}

	public RESTOptions(boolean auth, Object payload, String reason) {
		this.auth = auth;
		this.reason = reason;
		this.payload = payload;
		this.parameters = null;
	}

	public RESTOptions(boolean auth, Object payload) {
		this(auth, payload, (String) null);
	}

	public RESTOptions(boolean auth, Object payload, String reason, QueryParameter... parameters) {
		this.auth = auth;
		this.reason = reason;
		this.payload = payload;
		this.parameters = parameters;
	}

	public RESTOptions(Object payload) {
		this(true, payload, null);
	}

	public RESTOptions(String reason) {
		this(true, null, reason);
	}

	public RESTOptions(QueryParameter... parameters) {
		this(true, null, null, parameters);
	}

	/**
	 * @return the auth
	 */
	public boolean isAuth() {
		return this.auth;
	}

	/**
	 * @return the reason
	 */
	public String getReason() {
		return this.reason;
	}

	/**
	 * @return the payload
	 */
	public Object getPayload() {
		return this.payload;
	}

	/**
	 * @return the parameters
	 */
	public QueryParameter[] getParameters() {
		return parameters;
	}

}
