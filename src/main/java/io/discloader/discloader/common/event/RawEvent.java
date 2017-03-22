package io.discloader.discloader.common.event;

import com.mashape.unirest.http.HttpResponse;
import com.neovisionaries.ws.client.WebSocketFrame;

import io.discloader.discloader.common.DiscLoader;

public class RawEvent extends DLEvent {

	private WebSocketFrame frame;

	private HttpResponse<String> httpResponse;

	public RawEvent(DiscLoader loader, HttpResponse<String> response) {
		this(loader, null, response);
	}

	public RawEvent(DiscLoader loader, WebSocketFrame frame) {
		this(loader, frame, null);
	}

	private RawEvent(DiscLoader loader, WebSocketFrame frame, HttpResponse<String> response) {
		super(loader);
		setFrame(frame);
		setHttpResponse(response);
	}

	/**
	 * @return the current {@link WebSocketFrame}
	 */
	public WebSocketFrame getFrame() {
		return frame;
	}

	/**
	 * @return the httpResponse
	 */
	public HttpResponse<String> getHttpResponse() {
		return httpResponse;
	}

	/**
	 * Checks if the raw data sent to the client is from the gateway.
	 * 
	 * @return true if {@code getFrame() != null}
	 */
	public boolean isGateway() {
		return frame != null;
	}

	/**
	 * Checks if the raw data sent to the client is from the REST api.
	 * 
	 * @return true if {@code getHttpResponse() != null}
	 */
	public boolean isREST() {
		return httpResponse != null;
	}

	/**
	 * @param frame the frame to set
	 */
	protected void setFrame(WebSocketFrame frame) {
		this.frame = frame;
	}

	/**
	 * @param httpResponse the httpResponse to set
	 */
	protected void setHttpResponse(HttpResponse<String> httpResponse) {
		this.httpResponse = httpResponse;
	}

}
