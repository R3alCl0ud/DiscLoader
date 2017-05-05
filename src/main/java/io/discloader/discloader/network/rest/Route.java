package io.discloader.discloader.network.rest;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.Endpoint;

import com.mashape.unirest.request.BaseRequest;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.body.MultipartBody;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.network.util.Endpoints;

public enum Route {

	CURRENT_USER("%s/users/@me");

	private List<APIRequest> queue;
	private String route;
	private Endpoints endpoint;
	private RateLimiter rateLimiter;
	private DiscLoader loader;

	Route(String route) {
		queue = new ArrayList<>();
		this.route = route;
		rateLimiter = new RateLimiter();
	}

	public void makeRequest(APIRequest request) {
		queue.add(request);
	}

	public void handle() {

	}

	public void setLoader(DiscLoader loader) {
		this.loader = loader;
	}

	// public <T extends BaseRequest> T addHeaders(T baseRequest, boolean auth,
	// boolean multi) {
	// HttpRequest request = baseRequest.getHttpRequest();
	//
	// if (auth && DiscLoader.token != null) request.header("authorization",
	// DiscLoader.token);
	// if (!(request instanceof GetRequest) && !(baseRequest instanceof
	// MultipartBody) && !multi) {
	// request.header("Content-Type", "application/json");
	// }
	// request.header("user-agent", "DiscordBot (http://discloader.io,
	// v0.1.1)");
	// request.header("Accept-Encoding", "gzip");
	// return baseRequest;
	// }

	/**
	 * @return the route
	 */
	public String getRoute() {
		return route;
	}

	/**
	 * @return the endpoint
	 */
	public Endpoints getEndpoint() {
		return endpoint;
	}

}
