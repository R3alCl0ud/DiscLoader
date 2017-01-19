package main.java.com.forgecord.client.rest;


import java.lang.reflect.Method;

import org.json.JSONObject;

import main.java.com.forgecord.client.Client;
import main.java.com.forgecord.util.constants;
import main.java.com.forgecord.util.promise.*;

public class RESTMethods {
	
	public Client client;
	public RESTManager rest;
	
	public RESTMethods(RESTManager restManager) {
		this.rest = restManager;
		this.client = restManager.client;
	}
	
	public void login(String token) {
		this.client.manager.connectToWebSocket(token);
	}
	
	public String getGateway() {
		return this.client.ws.gateway = "/?v=6";
	}
}
