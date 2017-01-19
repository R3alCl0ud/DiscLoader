package main.java.com.forgecord.client.rest;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.http.HttpResponse;

import java.io.*;
import java.lang.reflect.Method;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import org.json.JSONObject;

public class APIRequest<T> {
	public RESTManager rest;
	public String url;
	public JSONObject data;
	public String route; 
	public APIAction<T> action;
    public static final Consumer DEFAULT_SUCCESS = o -> {};
    public static final Consumer<Throwable> DEFAULT_FAILURE = t -> {};
    
	public APIRequest(RESTManager rest, String url, JSONObject data) {
	    this.rest = rest;
	    this.url = url;
	    this.data = data;
	    this.route = this.getRoute(this.url);
	}
	


	public String getRoute(String url) {
		String route = url.split("?")[0];
		if (route.contains("/channels/") || route.contains("/guilds/")) {
			int startInd = route.contains("/channels/") ? route.indexOf("/channels/") : route.indexOf("/guilds/");
			String majorID = route.substring(startInd).split("/")[2];
			route = route.replaceAll("(\\d{8,})/g",  ":id").replaceAll(":id", majorID);
		}
		return route;
	}
	
	public void queue() {
		queue(null, null);
	}
	
	public void queue(Consumer<T> success) {
		
	}
	
	public void queue(Consumer<T> success, Consumer<Throwable> failure) {
        if (success == null) success = DEFAULT_SUCCESS;
        if (failure == null) failure = DEFAULT_FAILURE;
        
        
	}
	
}
