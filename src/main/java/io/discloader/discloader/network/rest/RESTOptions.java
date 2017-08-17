package io.discloader.discloader.network.rest;

/**
 * @author Perry Berman
 *
 * @param <T>
 * @param <U>
 */
public class RESTOptions<T> {
	
	private final boolean auth;
	private final String reason;
	private final Object payload;
	
	public RESTOptions() {
		this(true, null, null);
	}
	
	public RESTOptions(boolean auth) {
		this(auth, null, null);
	}
	
	public RESTOptions(boolean auth, String reason, Object payload) {
		this.auth = auth;
		this.reason = reason;
		this.payload = payload;
	}
	
	public RESTOptions(Object payload) {
		this(true, null, payload);
	}
	
	public RESTOptions(String reason) {
		this(true, reason, null);
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
	
}
