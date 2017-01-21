package main.java.com.forgecord.client.rest.RequestHandlers;

import java.util.ArrayList;
import java.util.List;

import main.java.com.forgecord.client.rest.APIRequest;
import main.java.com.forgecord.client.rest.RESTManager;

public class RequestHandler {
	
	public RESTManager restManager;
	public List<APIRequest> queue = new ArrayList<APIRequest>();
	
	public RequestHandler(RESTManager restManager) {
		this.restManager = restManager;
	}
	
	public void push(APIRequest request) {
		this.queue.add(request);
	}
	
	public void handle() {
		return;
	}
}
