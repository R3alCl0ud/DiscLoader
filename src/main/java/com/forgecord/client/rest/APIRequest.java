package main.java.com.forgecord.client.rest;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.BaseRequest;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.body.MultipartBody;

import main.java.com.forgecord.util.constants;

import com.mashape.unirest.http.HttpResponse;

import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;

public class APIRequest {
	public final RESTManager rest;
	public String url;
	public final JSONObject data;
	public final String route; 
	public final int method;
	public final boolean auth;
	public CompletableFuture<String> Future;
    
	/**
	 * The rest request class
	 * @param rest The client's rest manager
	 * @param method The http request type, ex. "GET", "POST", "PUT"
	 * @param url The endpoint of the request
	 * @param auth Whether or not to apply the client's token to the request
	 * @param data The request's payload
	 */
	public APIRequest(RESTManager rest, int method, String url, boolean auth, JSONObject data) {
	    this.rest = rest;
	    this.method = method;
	    this.auth = auth;
	    this.url = url;
	    this.data = data;
	    this.route = this.getRoute(this.url);
	}

	/**
	 * Converts the {@link APIRequest} URL to the corresponding API Endpoint 
	 * @param url
	 * @return API Endpoint {@link constants.Endpoints}
	 */
	public String getRoute(String url) {
		String route = url.split("[?]")[0];
		if (route.contains("/channels/") || route.contains("/guilds/")) {
			int startInd = route.contains("/channels/") ? route.indexOf("/channels/") : route.indexOf("/guilds/");
			String majorID = route.substring(startInd).split("/")[2];
			route = route.replaceAll("(\\d{8,})/g",  ":id").replaceAll(":id", majorID);
		}
		return route;
	}
	
	public CompletableFuture<String> setFuture(CompletableFuture<String> comFuture) {
		return this.Future = comFuture;
	}
	
	public void execute() {
		BaseRequest request = null;
		CompletableFuture<String> future = this.Future;
		switch (this.method) {
		case constants.Methods.GET: 
			request = Unirest.get(this.route);
			break;
		
		default: 
			request = Unirest.get(this.route);
		}
		
		request = this.addHeaders(request);
		request.asStringAsync(new Callback<String>() {

			@Override
			public void cancelled() {
				future.completeExceptionally(new Throwable());
			}

			@Override
			public void completed(HttpResponse<String> response) {
				System.out.println("APIResponse: " + response.getBody());
				future.complete(response.getBody());
				
			}

			@Override
			public void failed(UnirestException e) {
				future.completeExceptionally(e);
			}
			
		});
	}
	
	public <T extends BaseRequest> T addHeaders(T baseRequest) {
		HttpRequest request = baseRequest.getHttpRequest();
		if (this.auth && this.rest.client.token != null) request.header("authorization", this.rest.client.token);
        if (!(request instanceof GetRequest) && !(baseRequest instanceof MultipartBody)) request.header("Content-Type", "application/json");
		request.header("user-agent", "Forgecord");
        request.header("Accept-Encoding", "gzip");
		return baseRequest;
	}
	
}
