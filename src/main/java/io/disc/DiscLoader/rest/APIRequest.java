package io.disc.DiscLoader.rest;

import java.util.concurrent.CompletableFuture;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.BaseRequest;
import com.mashape.unirest.request.HttpRequestWithBody;

import io.disc.DiscLoader.util.constants;

public class APIRequest {
	public String url;

	public String route;

	public int method;

	public boolean auth;

	public Object data;

	public CompletableFuture<String> future;

	/**
	 * @param url
	 * @param method
	 * @param auth
	 * @param data
	 */
	public APIRequest(String url, int method, boolean auth, Object data) {
		this.url = url;
		this.method = method;
		this.auth = auth;
		this.data = data;
		this.route = this.getRoute(this.url);
	}

	/**
	 * Converts the {@link APIRequest} URL to the corresponding API Endpoint
	 * 
	 * @param url
	 * @return API Endpoint {@link constants.Endpoints}
	 */
	public String getRoute(String url) {
		String route = url.split("[?]")[0];
		if (route.contains("/channels/") || route.contains("/guilds/")) {
			int startInd = route.contains("/channels/") ? route.indexOf("/channels/") : route.indexOf("/guilds/");
			String majorID = route.substring(startInd).split("/")[2];
			route = route.replaceAll("(\\d{8,})/g", ":id").replaceAll(":id", majorID);
		}
		return route;
	}

	public CompletableFuture<?> setFuture(CompletableFuture<String> future) {
		this.future = future;
		return future;
	}

	public BaseRequest createRequest() {
		BaseRequest request = null;
		switch (this.method) {
		case constants.Methods.GET:
			request = Unirest.get(this.route);
			break;
		case constants.Methods.POST:
			request = Unirest.post(this.route);
			((HttpRequestWithBody)request).body(this.data);
		default:
			request = Unirest.get(this.route);
		}

		return request;
	}

}
