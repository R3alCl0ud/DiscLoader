package main.java.com.forgecord.client.rest.RequestHandlers;

import org.json.JSONObject;

import com.mashape.unirest.http.*;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.*;
import com.mashape.unirest.request.body.*;

import main.java.com.forgecord.client.rest.APIRequest;
import main.java.com.forgecord.client.rest.RESTManager;

import java.util.concurrent.*;
import java.util.function.Consumer;

public class SequentialRequestHandler extends RequestHandler {

	public boolean waiting;
	
	public SequentialRequestHandler(RESTManager restManager, String endpoint) {
		super(restManager);
		this.waiting = false;
	}
	
	public void push(APIRequest request) {
	    super.push(request);
	    System.out.println(request.toString());
	    this.handle();
	}
	
	public void completedRequest(Object object) {
		this.queue.remove(0);
		this.waiting = false;
	}
	
	public JSONObject execute(APIRequest item) {
		System.out.println(item.route);
		if (item.Future != null) item.Future.thenAccept(this::completedRequest);
		item.execute();
		return null;
	}

	public void handle() {
		super.handle();
		
		if (this.waiting || this.queue.size() == 0) return;
		this.waiting = true;
		
		APIRequest item = this.queue.get(0);
		
		this.execute(item);
		this.handle();
	}
}
