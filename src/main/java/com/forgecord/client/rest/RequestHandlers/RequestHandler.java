package main.java.com.forgecord.client.rest.RequestHandlers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;


import main.java.com.forgecord.client.rest.RESTManager;

public class RequestHandler {
	
	public RESTManager restManager;
	public List<JSONObject> queue = new ArrayList<JSONObject>();
	
	public RequestHandler(RESTManager restManager) {
		this.restManager = restManager;
	}
	
	public void push(JSONObject request) {
		this.queue.add(request);
	}
	
	public void handle() {
		return;
	}
}
