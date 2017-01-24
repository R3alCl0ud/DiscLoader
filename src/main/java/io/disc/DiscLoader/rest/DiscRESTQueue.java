/**
 * 
 */
package io.disc.DiscLoader.rest;

import java.util.ArrayList;
import java.util.List;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.BaseRequest;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.body.MultipartBody;

import io.disc.DiscLoader.DiscLoader;

/**
 * @author Perry Berman
 *
 */
public class DiscRESTQueue {
	public List<APIRequest> queue;
	
	public DiscREST rest;
	
	public DiscLoader loader;
	
	public boolean waiting;
	
	public DiscRESTQueue(DiscREST discREST) {
		this.rest = discREST;
		this.loader = this.rest.loader;
		
		this.waiting = false;
		
		this.queue = new ArrayList<APIRequest>();
	}
	
	public void handle() {
		if (this.waiting) {
			this.handle();
			return;
		}
		
		final APIRequest apiRequest = queue.get(0);
		
		BaseRequest request = apiRequest.createRequest();
		
		request = this.addHeaders(request, apiRequest.auth);
		request.asStringAsync(new Callback<String>() {

			public void cancelled() {
				apiRequest.future.completeExceptionally(new Throwable());
			}

			public void completed(HttpResponse<String> response) {
				System.out.println("APIResponse: " + response.getBody());
				apiRequest.future.complete(response.getBody());

			}

			public void failed(UnirestException e) {
				apiRequest.future.completeExceptionally(e);
			}
		});
		
	}
	
	public void addToQueue(APIRequest request) {
		queue.add(request);
		this.handle();
	}
	
	public <T extends BaseRequest> T addHeaders(T baseRequest, boolean auth) {
		HttpRequest request = baseRequest.getHttpRequest();
		if (auth && this.loader.token != null)
			request.header("authorization", this.loader.token);
		if (!(request instanceof GetRequest) && !(baseRequest instanceof MultipartBody))
			request.header("Content-Type", "application/json");
		request.header("user-agent", "DiscLoader");
		request.header("Accept-Encoding", "gzip");
		return baseRequest;
	}

	
	
}
